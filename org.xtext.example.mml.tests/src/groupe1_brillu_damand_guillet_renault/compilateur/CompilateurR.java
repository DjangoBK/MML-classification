package groupe1_brillu_damand_guillet_renault.compilateur;

import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.LogisticRegression;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SVM;

public class CompilateurR {
	MMLModel result;
	MLChoiceAlgorithm mlChoiceAlgorithm;

	public CompilateurR(MMLModel result, MLChoiceAlgorithm mlChoiceAlgorithm) {
		this.result = result;
		this.mlChoiceAlgorithm = mlChoiceAlgorithm;
	}

	public String traitement(MMLModel result) {
		String resultat = "";
		DataInput dataInput = result.getInput();
		String fileLocation = dataInput.getFilelocation();
		String csvReading = "mml_data = read.table(" + mkValueInSingleQuote(fileLocation) + ", header=  T, sep=',')";
		System.out.println(fileLocation);
		resultat += csvReading + "\n";

		MLAlgorithm algo = this.mlChoiceAlgorithm.getAlgorithm();
		
		if (algo instanceof DT) {
			resultat += traitementDT() + traitementMetric();
		} else if (algo instanceof SVM) {
			resultat += traitementSVM() + traitementMetric();
		} else if (algo instanceof RandomForest) {
			resultat += traitementRandomForest();
		} else if (algo instanceof LogisticRegression) {
			resultat += traitementLogisticRegression()+ traitementMetric();
		}
		return resultat;
	}

	private String traitementDT() {
		System.out.println("traitement DT");
		double test_size = (double) result.getValidation().getStratification().getNumber() / 100;
		String size = "test_size = " + test_size + "\n";
		System.out.println(size);
		String TRAIN_TEST_SPLIT = "X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size=" + test_size
				+ ") \n";
		System.out.println(TRAIN_TEST_SPLIT);
		return "";
	}

	private String traitementLogisticRegression() {
		// TODO Auto-generated method stub
		return null;
	}

	private String traitementRandomForest() {
		// TODO Auto-generated method stub
		return null;
	}

	private String traitementSVM() {
		// TODO Auto-generated method stub
		return null;
	}

	private String traitementMetric() {
		// TODO Auto-generated method stub
		return null;
	}

	private static String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}

}
