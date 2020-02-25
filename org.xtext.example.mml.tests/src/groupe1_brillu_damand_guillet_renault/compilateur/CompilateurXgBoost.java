package groupe1_brillu_damand_guillet_renault.compilateur;

import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.LogisticRegression;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SVM;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.ValidationMetric;

public class CompilateurXgBoost {

	MMLModel result;
	String importList;
	MLChoiceAlgorithm algorithm;
	String algo;
	String dataset;
	String predVar;

	public CompilateurXgBoost(MMLModel result, MLChoiceAlgorithm algorithm) {
		this.result = result;
		this.algorithm = algorithm;
	}

	public String getAlgo() {
		return algo;
	}

	public void setAlgo(String algo) {
		this.algo = algo;
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public void setPredVar(String fileLocation, String separator) {
		System.err.println("Formula : " + result.getFormula());
		if (result.getFormula() != null) {
			if (result.getFormula().getPredictive().getColName() != null) {
				this.predVar = result.getFormula().getPredictive().getColName();
			} else if (result.getFormula().getPredictive().getColumn() != 0) {
				this.predVar = Utils.getCol(fileLocation, separator, result.getFormula().getPredictive().getColumn());
			}
		} else {
			this.predVar = Utils.getLastCol(fileLocation, separator);
		}
		System.out.println(predVar);
	}

	public String traitement() {
		String res = "import numpy as np\r\n" + "import xgboost as xgb\r\n" + "from sklearn import datasets\r\n"
				+ "from sklearn.cross_validation import train_test_split\r\n" + "from xgboost import XGBClassifier\r\n"
				+ "from sklearn.datasets import dump_svmlight_file\r\n" + "from sklearn.externals import joblib\r\n"
				+ "from sklearn.metrics import precision_score";

		DataInput dataInput = result.getInput();
		String fileLocation = dataInput.getFilelocation();
		String fileName = fileLocation.split("/")[fileLocation.split("/").length - 1].replace(".csv", "");
		setDataset(fileName);
		setPredVar(fileLocation, ",");
		String csvReading = "mml_data = pd.read_csv(" + mkValueInSingleQuote(fileLocation) + ")";
		res += csvReading + "\n";

		System.out.println(this.algorithm.getAlgorithm());

		if (this.algorithm.getAlgorithm() instanceof DT) {
			res += traitementDT();
			res += traitementMetrics();
		} else if (this.algorithm.getAlgorithm() instanceof SVM) {
			res += "print(\"Non supporte pas XgBoot\")";
		} else if (this.algorithm.getAlgorithm() instanceof RandomForest) {
			res += "print(\"Non supporte pas XgBoot\")";
		} else if (this.algorithm.getAlgorithm() instanceof LogisticRegression) {
			res += "print(\"Non supporte pas XgBoot\")";
			System.out.println(res);
		}
		// res = traitementImport() + res;
		return res;
	}

	private static String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}

	/** Algo SVM **/
	public String traitementSVM() {
		return algo;
	}

	/** Algo DT **/
	public String traitementDT() {
		setAlgo("DT");
		double test_size = result.getValidation().getStratification().getNumber()/100.0;
		String size = "test_size = " + test_size +"\n";
		
		String split = "X = mml_data.drop(columns=[\""+this.predVar+"\"])\n"
				+ "Y = mml_data[\"variety\"]\n";
		
		String TRAIN_TEST_SPLIT = "X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size="+ test_size +") \n";
		String model = "model = model = XGBClassifier()\n" + 
						"model.fit(X_train, y_train)\n";
		
		return test_size + size + split + TRAIN_TEST_SPLIT + model;
	}

	/** Algo Random Forest **/
	public String traitementRandomForest() {
		return algo;
	}

	/** Algo Logistic Regression **/
	public String traitementLogisticRegression() {
		return algo;
	}

	public String traitementMetric(int i) {
		if(this.result.getValidation().getStratification() instanceof TrainingTest) {
			if(this.result.getValidation().getMetric().get(i) == ValidationMetric.ACCURACY) {
				return "accuracy = accuracy_score(y_test, clf.predict(X_test))\r\n" + 
						"\r\n" + 
						"print(accuracy)";			
			}
			else if(this.result.getValidation().getMetric().get(i) == ValidationMetric.RECALL) {
				return "recall = recall_score(y_test, clf.predict(X_test), average='macro')\n"
						+ "print(recall)";
			}
			else if(this.result.getValidation().getMetric().get(i)==ValidationMetric.F1) {
				return "precision = precision_score(y_test, clf.predict(X_test), average='macro')\r\n"
						+ "print(precision)";
			}
			else if(this.result.getValidation().getMetric().get(i)==ValidationMetric.PRECISION) {
				return "f1 = f1_score(y_test, clf.predict(X_test), average='macro')\r\n"
						+ "print(f1)";
			}
			else {return null;}
		}
		else if(this.result.getValidation().getStratification() instanceof CrossValidation) {
			if(this.result.getValidation().getMetric().get(i) == ValidationMetric.ACCURACY) {
				return "accuracy = cross_val_score(y_test, clf.predict(X_test))\r\n" + 
						"\r\n" + 
						"print(accuracy)";			
			}
			else if(this.result.getValidation().getMetric().get(i) == ValidationMetric.RECALL) {
				return "recall = recall_score(y_test, clf.predict(X_test), average='macro')\n"
						+ "print(recall)";
			}
			else if(this.result.getValidation().getMetric().get(i)==ValidationMetric.F1) {
				return "precision = precision_score(y_test, clf.predict(X_test), average='macro')\r\n"
						+ "print(precision)";
			}
			else if(this.result.getValidation().getMetric().get(i)==ValidationMetric.PRECISION) {
				return "f1 = f1_score(y_test, clf.predict(X_test), average='macro')\r\n"
						+ "print(f1)";
			}
			else {return null;}
		}
		else {
			return null;
		}
	}
	
	public String traitementMetrics() {
		String res = "";
		for(int i = 0 ; i <this.result.getValidation().getMetric().size() ; i++) {
			res += traitementMetric(i) + "\n";
		}
		return res;
	}
}
