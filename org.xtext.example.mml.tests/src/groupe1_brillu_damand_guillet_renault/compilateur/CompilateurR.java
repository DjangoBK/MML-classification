package groupe1_brillu_damand_guillet_renault.compilateur;

import javax.rmi.CORBA.Util;

import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.LogisticRegression;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SVM;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.ValidationMetric;

public class CompilateurR {
	MMLModel result;
	MLChoiceAlgorithm algorithm;
	
	int nbVar;
	String predVar;
	String algoName;
	String dataSet;

	public CompilateurR(MMLModel result, MLChoiceAlgorithm algorithm) {
		this.result = result;
		this.algorithm = algorithm;
	}

	public String traitement() {
		String res = "library(rpart,quietly = TRUE)\r\n" + //DT
				"library(caret,quietly = TRUE)\r\n" +  //DT
				"library(rpart.plot,quietly = TRUE)\r\n" + //DT
				"library(rattle) \n" +//DT
				"library(randomForest) \n" + //RF
				"library(e1071) \n"; //all
		DataInput dataInput = result.getInput();
		String fileLocation = dataInput.getFilelocation();
		String fileName = fileLocation.split("/")[fileLocation.split("/").length-1].replace(".csv", "");
		setDataSet(fileName);
		String csvReading = "mml_data = read.table(" + mkValueInSingleQuote(fileLocation) + ", header=  T, sep=',')";
		res+= csvReading + "\n";
		setPredVar(fileLocation, ",");
		setNbVar(fileLocation, ",");
		System.err.println(this.predVar + " ------ " + this.nbVar);
		
		//traitementStratificationMethod();
		if(this.algorithm.getAlgorithm() instanceof DT) {
			res += traitementDT();
			res+=traitementMetrics();
			setAlgoName("Decision Tree");
		}
		else if (this.algorithm.getAlgorithm() instanceof SVM) {
			res += traitementSVM();
			res += traitementMetrics();
			setAlgoName("SVM");
		}
		else if (this.algorithm.getAlgorithm() instanceof RandomForest) {
			res += traitementRandomForest();
			res += traitementMetrics();
			setAlgoName("Random Forest");
		}
		else if (this.algorithm.getAlgorithm() instanceof LogisticRegression) {
			res += traitementLogisticRegression();
			res += traitementMetrics();
			setAlgoName("Logistic Regression");
		}
		//res =  traitementImport() + res;
		return res;
	}

	private String traitementDT() {
		setAlgoName("DT");
		double test_size = result.getValidation().getStratification().getNumber()/100.0;
		String size = "test_size = " + test_size +"\n";
		String nbVar = "nbVar <- dim(mml_data)[2]\n";
		String sample = "train <- sample(1:nrow(mml_data),size = ceiling(test_size*nrow(mml_data)),replace = FALSE) \n";
		String train = "train_set = mml_data[train,] \n";
		String test = "test_set = mml_data[-train,] \n";
		String x_test = "x_test <- mml_data[,1:(nbVar-1)] \n";
		String y_test = "y_test <- mml_data[,nbVar] \n";
		
		String algoSet = "tree <- rpart(variety~.,data=train_set,method = \"class\") \n";
		algoSet += "pred <- predict(object=tree,x_test,type=\"class\") \n";
		String cm = "cm <- confusionMatrix(pred, y_test)\r\n";
		
		return size + nbVar + sample + train + test + x_test + y_test + algoSet + cm;
	}

	private String traitementLogisticRegression() {
		setAlgoName("LR");
		// TODO Auto-generated method stub
		return null;
	}

	private String traitementRandomForest() {
		setAlgoName("RF");
		double test_size = result.getValidation().getStratification().getNumber()/100.0;
		String size = "test_size = " + test_size +"\n";
		String nbVar = "nbVar <- dim(mml_data)[2]\n";
		String sample = "ind <- sample(2,nrow(mml_data),replace=TRUE,prob=c(test_size,(1-test_size))) \n";
		String train = "trainData <- mml_data[ind==1,] \n";
		String test = "testData <- mml_data[ind==2,] \n";

		String algoSet = "mml_data_rf <- randomForest(variety~.,data=trainData,ntree=100,proximity=TRUE) \n";
		String predict = "pred <- predict(mml_data_rf) \n";
		String cm = "cm <- confusionMatrix(predict(mml_data_rf),trainData$variety) \n";


		return size + nbVar + sample + train + test + algoSet + predict + cm;
	}

	private String traitementSVM() {
		setAlgoName("SVM");
		double test_size = result.getValidation().getStratification().getNumber()/100.0;
		String size = "test_size = " + test_size +"\n";
		String nbVar = "nbVar <- dim(mml_data)[2]\n";
		String sample = "train <- sample(1:nrow(mml_data),size = ceiling(test_size*nrow(mml_data)),replace = FALSE) \n";
		String train = "train_set = mml_data[train,] \n";
		String test = "test_set = mml_data[-train,] \n";
		String x_test = "x_test <- mml_data[,1:(nbVar-1)] \n";
		String y_test = "y_test <- mml_data[,nbVar] \n";

		String algoSet = "svm_model <- svm("+predVar+" ~ ., data=train_set, kernel=\"radial\", cost=1, gamma=0.5) \n";
		String predict = "pred <- predict(svm_model, x_test)\n";
		String cm="cm = confusionMatrix(pred, y_test)\n";

		return size+nbVar+sample+train+test+x_test+y_test+algoSet+predict+cm;
	}
	
	private String traitementMetrics() {
		String res = "";
		for(int i = 0 ; i <this.result.getValidation().getMetric().size() ; i++) {
			res += traitementMetric(i) + "\n";
		}
		return res;
	}

	private String traitementMetric(int i) {
		if(this.result.getValidation().getStratification() instanceof TrainingTest) {
			if(this.result.getValidation().getMetric().get(i) == ValidationMetric.ACCURACY) { 
				return	"overall <- cm$overall\r\n" + 
						"overall['Accuracy'] ";			
			}
			else if(this.result.getValidation().getMetric().get(i) == ValidationMetric.RECALL) {
				return "class <- cm$byClass\r\n"
						+ "n <- dim(class)[1]"
						+ "recallCm(cm, n)";
			}
			else if(this.result.getValidation().getMetric().get(i)==ValidationMetric.F1) {
				return "class <- cm$byClass\r\n"
						+ "n <- dim(class)[1]"
						+ "F1Cm(cm, n)";
			}
			else if(this.result.getValidation().getMetric().get(i)==ValidationMetric.PRECISION) {
				return "class <- cm$byClass\r\n"
						+ "n <- dim(class)[1]"
						+ "precisionCm(cm, n)";
			}
			else {return null;}
		}
		else if(this.result.getValidation().getStratification() instanceof CrossValidation) {
			if(this.result.getValidation().getMetric().get(i) == ValidationMetric.ACCURACY) {
				return "";			
			}
			else if(this.result.getValidation().getMetric().get(i) == ValidationMetric.RECALL) {
				return "";
			}
			else if(this.result.getValidation().getMetric().get(i)==ValidationMetric.F1) {
				return "";
			}
			else if(this.result.getValidation().getMetric().get(0)==ValidationMetric.PRECISION) {
				return "";
			}
			else {return null;}
		}
		else {
			return null;
		}		
	}

	private static String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}

	public String getAlgoName() {
		return algoName;
	}

	public void setAlgoName(String algoName) {
		this.algoName = algoName;
	}
	
	public void setPredVar(String fileLocation, String separator) {
		System.err.println("Formula : " + result.getFormula());
		if(result.getFormula() != null) {
			if(result.getFormula().getPredictive().getColName() != null) {
				this.predVar = result.getFormula().getPredictive().getColName();
			}
			else if(result.getFormula().getPredictive().getColumn() != 0) {
				this.predVar = Utils.getCol(fileLocation, separator, result.getFormula().getPredictive().getColumn());
			}
		}
		else {
			this.predVar = Utils.getLastCol(fileLocation, separator);
		}
		System.out.println(predVar);
	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public int getNbVar() {
		return nbVar;
	}

	public void setNbVar(String fileLocation, String separator) {
		this.nbVar = Utils.getNbCol(fileLocation, separator);
	}
	
	
	
	
	
	

}
