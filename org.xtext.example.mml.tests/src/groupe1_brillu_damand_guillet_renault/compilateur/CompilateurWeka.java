package groupe1_brillu_damand_guillet_renault.compilateur;

import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.LogisticRegression;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SVM;
import org.xtext.example.mydsl.mml.SVMClassification;
import org.xtext.example.mydsl.mml.SVMKernel;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.ValidationMetric;
import org.xtext.example.mydsl.mml.impl.SVMImpl;

import libsvm.*;

public class CompilateurWeka {
MMLModel result;
MLChoiceAlgorithm algo;
	
	public CompilateurWeka(MMLModel result, MLChoiceAlgorithm mlAlgo) {
		this.result = result;
		this.algo = mlAlgo;
	}
	
	public String traitement() {
		String res = ImportAndMain();
		
		DataInput dataInput = result.getInput();
		String fileLocation = dataInput.getFilelocation();
		double test_size = result.getValidation().getStratification().getNumber()/100.0;
		
		res += "\t\tCSVLoader loader = new CSVLoader();\n"
				+ "\t\tloader.setSource(new File(\""+fileLocation+"\"));\n";
		String csvReading = "\t\tInstances data = loader.getDataSet();\n"
				+ "\t\tdata.randomize(new Random());\n\n";
		String split = "\t\tdata.setClassIndex(data.numAttributes()-1);\n";
		String size = "\t\tint trainSize = (int) Math.round(data.numInstances() * "+test_size+");\n" + 
				"\t\tint testSize = data.numInstances() - trainSize;\n";
		String TRAIN_TEST_SPLIT = "\t\tInstances train = new Instances(data, 0, trainSize);\n" + 
				"\t\tInstances test = new Instances(data, trainSize, testSize);\n";
		res += csvReading + split + size + TRAIN_TEST_SPLIT  + "\n";
		
		if(algo.getAlgorithm() instanceof DT) {
			res += traitementDT();
			res+=traitementMetric();
		}
		else if (algo.getAlgorithm() instanceof SVM) {
			res += traitementSVM();
			res+=traitementMetric();
		}
		else if (algo.getAlgorithm() instanceof RandomForest) {
			res += traitementRandomForest();
			res += traitementMetric();
		}
		else if (algo.getAlgorithm() instanceof LogisticRegression) {
			res += traitementLogisticRegression();
			res += traitementMetric();
		}
		//res =  traitementImport() + res;
		return res;
		
	}
	
	/** SVM **/
	public String traitementSVM() {
		SVMImpl algo = (SVMImpl) result.getAlgorithms().get(0).getAlgorithm();
		
		String Cclass = algo.getC();
		String gamma = algo.getGamma();
		String kernel = algo.getKernel().getName();
		
		String algoSet = "svm_parameter param = new svm_parameter()";
		
		//A supprimer
		svm_parameter test = new svm_parameter();
		test.svm_type = svm_parameter.ONE_CLASS;
		test.kernel_type = svm_parameter.RBF;
		
		
		if(algo.getSvmclassification() == SVMClassification.CCLASS) {
			if(Cclass != null) {
				algoSet += "param.svm_type = svm_parameter.C_SVC;";
			}
			
		} else if(algo.getSvmclassification() == SVMClassification.NU_CLASS) {
			if(Cclass != null) {
				algoSet += "param.svm_type = svm_parameter.NU_SVC;";
			}
			
		} else if(algo.getSvmclassification() == SVMClassification.ONE_CLASS) {
			if(Cclass != null) {
				algoSet += "param.svm_type = svm_parameter.ONE_CLASS;";
			}
			
		} else {
			return null;
		}
		
		if(gamma != null) {
			algoSet += "param.gamma = " + gamma + ";";
		}
		if(algo.isKernelSpecified()) {
			if(algo.getKernel() == SVMKernel.LINEAR)
				algoSet += "param.kernel_type = svm_parameter.LINEAR";
			else if(algo.getKernel() == SVMKernel.POLY)
				algoSet += "param.kernel_type = svm_parameter.POLY";
			else if(algo.getKernel() == SVMKernel.RADIAL)
				algoSet += "param.kernel_type = svm_parameter.RBF";
		}
		
		return algoSet;
	}
	
	/** Algo DT **/
	public String traitementDT() {
		return "\t\tClassifier cls = new J48();\n" + 
				"\t\tcls.buildClassifier(train);\n";
	}
	
	/** Algo Ramdom Forest **/
	public String traitementRandomForest() {
		return "\t\tClassifier cls = new RandomForest();\n" + 
				"\t\tcls.buildClassifier(train);\n";
	}
	
	/** Algo Logistic Regession **/
	public String traitementLogisticRegression() {
		return "\t\tClassifier cls = new Logistic();\n" + 
				"\t\tcls.buildClassifier(train);\n";
	}
	
	/** traitement de la classification metric : accuracy, recall, f1 ou precision**/
	public String traitementMetric() {
		String res = "";
		if(this.result.getValidation().getStratification() instanceof TrainingTest) {
			res += "\t\tEvaluation eval = new Evaluation(train);\n" + 
					"\t\teval.evaluateModel(cls, test);\n";
			
		}
		else if(this.result.getValidation().getStratification() instanceof CrossValidation) {
			res += "\t\tEvaluation eval = new Evaluation(train);\n" + 
					"\t\teval.crossValidateModel(cls, test, 2, new Random());\n";
		}
		else {
			return null;
		}
		
		if(this.result.getValidation().getMetric().get(0) == ValidationMetric.ACCURACY) {
			res += "\t\tdouble accuracy = eval.pctCorrect();\n" + 
					"\t\tSystem.out.println(\"Accuracy = \" + accuracy);\n";			
		}
		else if(this.result.getValidation().getMetric().get(0) == ValidationMetric.RECALL) {
			res += "\t\tSystem.out.println(eval.weightedRecall());\n";
		}
		else if(this.result.getValidation().getMetric().get(0)==ValidationMetric.F1) {
			res += "\t\tSystem.out.println(eval.weightedFMeasure());\n";
		}
		else if(this.result.getValidation().getMetric().get(0)==ValidationMetric.PRECISION) {
			res += "\t\tSystem.out.println(eval.weightedPrecision());\n";
		}
		else {return null;}
		
		return res + "\t}\n" + "}";
	}

	public String ImportAndMain() {
		return "import java.io.File;\n" + 
				"import java.util.Random;\n" + 
				"\n" + 
				"import weka.classifiers.Classifier;\n" + 
				"import weka.classifiers.Evaluation;\n" + 
				"import weka.classifiers.functions.Logistic;\n" + 
				"import weka.classifiers.trees.J48;\n" + 
				"import weka.classifiers.trees.RandomForest;\n" + 
				"import weka.core.Instances;\n" +
				"import libsvm.svm_node;\n" +
				"import weka.core.converters.CSVLoader;\n\n" +
				"public class main {\n" + 
				"\n" + 
				"	public static void main(String[] args) throws Exception {\n";
	}
}
