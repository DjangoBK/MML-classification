package groupe1_brillu_damand_guillet_renault.compilateur;

import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.LogisticRegression;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SVM;

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
				+ "from sklearn.cross_validation import train_test_split\r\n"
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
			res += traitementSVM();
			res += traitementMetrics();
		} else if (this.algorithm.getAlgorithm() instanceof RandomForest) {
			res += traitementRandomForest();
			res += traitementMetrics();
		} else if (this.algorithm.getAlgorithm() instanceof LogisticRegression) {
			res += traitementLogisticRegression();
			res += traitementMetrics();
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
		return algo;
	}

	/** Algo Random Forest **/
	public String traitementRandomForest() {
		return algo;
	}

	/** Algo Logistic Regression **/
	public String traitementLogisticRegression() {
		return algo;
	}

	public String traitementMetrics() {
		return algo;
	}
}
