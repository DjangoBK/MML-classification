package groupe1_brillu_damand_guillet_renault.compilateur;

import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.LogisticRegression;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SVM;
import org.xtext.example.mydsl.mml.SVMClassification;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.ValidationMetric;
import org.xtext.example.mydsl.mml.impl.SVMImpl;

public class CompilateurWeka {
MMLModel result;
	
	public CompilateurWeka(MMLModel result) {
		this.result = result;
	}
	
	public String traitement(MMLModel result) {
		DataInput dataInput = result.getInput();
		String fileLocation = dataInput.getFilelocation();
		double test_size = result.getValidation().getStratification().getNumber()/100.0;
		
		String res = "CSVLoader loader = new CSVLoader();\n"
				+ "loader.setSource(new File(\""+fileLocation+"\"));\n";
		String csvReading = "Instances data = loader.getDataSet();\n";
		String split = "data.setClassIndex(data.numAttributes()-1);\n";
		String size = "int trainSize = (int) Math.round(data.numInstances() * "+test_size+");\n" + 
				"int testSize = data.numInstances() - trainSize;\n";
		String TRAIN_TEST_SPLIT = "Instances train = new Instances(data, 0, trainSize);\n" + 
				"			Instances test = new Instances(data, trainSize, testSize);";
		res += csvReading + split + size + TRAIN_TEST_SPLIT  + "\n";
		
		if(result.getAlgorithms().get(0).getAlgorithm() instanceof DT) {
			res += traitementDT();
			res+=traitementMetric();
		}
		else if (result.getAlgorithms().get(0).getAlgorithm() instanceof SVM) {
			res += traitementSVM();
			res+=traitementMetric();
		}
		/*else if (result.getAlgorithm().getAlgorithm() instanceof RandomForest) {
			res += traitementRandomForest();
		}
		else if (result.getAlgorithm().getAlgorithm() instanceof LogisticRegression) {
			res += traitementLogisticRegression();
		}
		//res =  traitementImport() + res;*/
		return res;
		
	}
	
	/** SVM **/
	public String traitementSVM() {
		System.out.println("traitement SVM");
		SVMImpl algo = (SVMImpl) result.getAlgorithms().get(0).getAlgorithm();
		
		double test_size = result.getValidation().getStratification().getNumber()/100.0;
		String size = "double test_size = " + test_size +"\n";
		
		String TRAIN_TEST_SPLIT = "X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size="+ test_size +") \n";
		String split = "X = mml_data.drop(columns=[\"variety\"])\n"
				+ "Y = mml_data[\"variety\"]\n";
		String algoSet = "";
		
		String Cclass = algo.getC();
		String gamma = algo.getGamma();
		String kernel = algo.getKernel().getName();
		
		if(algo.getSvmclassification() == SVMClassification.CCLASS) {
			
		} else if(algo.getSvmclassification() == SVMClassification.CCLASS) {
			
		} else if(algo.getSvmclassification() == SVMClassification.CCLASS) {
			
		} else if(algo.getSvmclassification() == SVMClassification.CCLASS) {
			
		}
		return null;
	}
	
	/** Algo DT **/
	public String traitementDT() {		
		System.out.println("traitement DT");
		String algoSet = "Classifier cls = new J48();\n" + 
				"			 cls.buildClassifier(train);\n";
		algoSet += "Evaluation eval = new Evaluation(train);\n" + 
				"eval.evaluateModel(cls, test);\n";
		
		return algoSet;
	}
	
	/** traitement de la classification metric : accuracy, recall, f1 ou precision**/
	public String traitementMetric() {
		String res = "";
		if(this.result.getValidation().getStratification() instanceof TrainingTest) {
			res += "Evaluation eval = new Evaluation(train);\n" + 
					"eval.evaluateModel(cls, test);\n";
			
		}
		else if(this.result.getValidation().getStratification() instanceof CrossValidation) {
			
		}
		else {
			return null;
		}
		
		if(this.result.getValidation().getMetric().get(0) == ValidationMetric.ACCURACY) {
			return "double accuracy = eval.pctCorrect();\n" + 
					"System.out.println(\"Accuracy = \" + accuracy)";			
		}
		else if(this.result.getValidation().getMetric().get(0) == ValidationMetric.RECALL) {
			return "System.out.println(eval.weightedRecall());";
		}
		else if(this.result.getValidation().getMetric().get(0)==ValidationMetric.F1) {
			return "precision = precision_score(y_true, y_pred, average='macro')\r\n"
					+ "print(precision)";
		}
		else if(this.result.getValidation().getMetric().get(0)==ValidationMetric.PRECISION) {
			return "f1 = f1_score(y_true, y_pred, average='macro')\r\n"
					+ "print(f1)";
		}
		else {return null;}
	}

}
