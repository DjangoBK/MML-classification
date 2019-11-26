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

public class CompilateurScikitLearn {
	MMLModel result;
	String importList;
	
	public CompilateurScikitLearn(MMLModel result) {
		this.result = result;
	}
	
	public String traitement() {
		String res = "from sklearn.model_selection import train_test_split\r\n"
				+ "from sklearn import tree\r\n" //DT
				+ "from sklearn.linear_model import LogisticRegression\n" //Logistic Regression
				+ "from sklearn.ensemble import RandomForestClassifier\r\n" //RandomForest
				+ "from sklearn.datasets import make_classification\n"//RandomForest
				+ "from sklearn.metrics import accuracy_score\n"  //metric = accuracy
				+ "from sklearn.svm import SVC\n" //Algo = SVC
				+ "from sklearn.metrics import recall_score\n" //metric = recall
				+ "from sklearn.svm import OneClassSVM\n" //SVC -> OneClassSVM
				+ "from sklearn.svm import NuSVC\n" //SVC -> NuClassSVC
				+ "from sklearn.metrics import f1_score\n" //metric = f1
				+ "from sklearn.metrics import precision_score\n" //metric = precision
				+ "from sklearn import datasets" //Logistic Regression
				+ "\n";
		DataInput dataInput = result.getInput();
		String fileLocation = dataInput.getFilelocation();
		String csvReading = "mml_data = pd.read_csv(" + mkValueInSingleQuote(fileLocation) + ")";
		res+= csvReading + "\n";
		
		//traitementStratificationMethod();
		/*
		if(result.getAlgorithm().getAlgorithm() instanceof DT) {
			res += traitementDT();
			res+=traitementMetric();
		}
		else if (result.getAlgorithm().getAlgorithm() instanceof SVM) {
			res += traitementSVM();
			res += traitementMetric();
		}
		else if (result.getAlgorithm().getAlgorithm() instanceof RandomForest) {
			res += traitementRandomForest();
		}
		else if (result.getAlgorithm().getAlgorithm() instanceof LogisticRegression) {
			res += traitementLogisticRegression();
			res += traitementMetric();
		}*/
		//res =  traitementImport() + res;
		return res;
	}
	
	/** Algo SVM **/
	public String traitementSVM() {
		/*System.out.println("traitement SVM");
		SVMImpl algo = (SVMImpl) result.getAlgorithm().getAlgorithm();
		
		double test_size = result.getValidation().getStratification().getNumber()/100.0;
		String size = "test_size = " + test_size +"\n";
		
		String TRAIN_TEST_SPLIT = "X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size="+ test_size +") \n";
		String split = "X = mml_data.drop(columns=[\"variety\"])\n"
				+ "Y = mml_data[\"variety\"]\n";
		String algoSet = "";
		
		String Cclass = algo.getC();
		String gamma = algo.getGamma();
		String kernel = algo.getKernel().getName();
		
		if(algo.getSvmclassification() == SVMClassification.CCLASS) {
			algoSet += "clf = SVC(";
			if(Cclass != null) {
				algoSet += "C=\""+Cclass+"\",";
			}
			if(gamma != null) {
				algoSet += "gamma = \""+gamma+"\",";
			}
			if(kernel != null) {
				algoSet += "kernel = \""+kernel+"\"";
			}
			
			algoSet += ")\n"
					+ "clf.fit(X_train, y_train) \n";
		}
		else if(algo.getSvmclassification() == SVMClassification.NU_CLASS) {
			algoSet += "clf = NuSVC(";
			if(Cclass != null) {
				algoSet += "C=\""+Cclass+"\",";
			}
			if(gamma != null) {
				algoSet += "gamma = \""+gamma+"\",";
			}
			if(kernel != null) {
				algoSet += "kernel = \""+kernel+"\"";
			}
			
			algoSet += ")\n"
					+ "clf.fit(X_train, y_train) \n";
		}
		else if(algo.getSvmclassification() == SVMClassification.ONE_CLASS) {
			algoSet += "clf = OneClassSVM(gamma=\"auto\")\n"
					+ "clf.fit(X_train, y_train) \n";
		}
		else {
			return null;
		}
		return size + split + TRAIN_TEST_SPLIT +  algoSet; */
		return null;
	}
	
	/** Algo DT **/
	public String traitementDT() {		
		System.out.println("traitement DT");
		double test_size = result.getValidation().getStratification().getNumber()/100.0;
		String size = "test_size = " + test_size +"\n";
		
		String split = "X = mml_data.drop(columns=[\"variety\"])\n"
				+ "Y = mml_data[\"variety\"]\n";
		
		String TRAIN_TEST_SPLIT = "X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size="+ test_size +") \n";
		
		String algoSet = "clf = tree.DecisionTreeClassifier() \n";
		algoSet += "clf.fit(X_train, y_train) \n";
		
		return size + split + TRAIN_TEST_SPLIT + algoSet;
	}
	
	/** Algo Random Forest**/
	public String traitementRandomForest() {
		System.out.println("traitement Random Forest");
		double test_size = result.getValidation().getStratification().getNumber()/100.0;
		String size = "test_size = " + test_size +"\n";
		
		String split = "X = mml_data.drop(columns=[\"variety\"])\n"
				+ "Y = mml_data[\"variety\"]\n";
		
		String TRAIN_TEST_SPLIT = "X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size="+ test_size +") \n";
		
		String algoSet = "X, Y = make_classification(n_samples=1000, n_features=4,\r\n" + 
				"                           n_informative=2, n_redundant=0,\r\n" + 
				"                           random_state=0, shuffle=False)\n";
		algoSet += "clf = RandomForestClassifier(n_estimators=100, max_depth=2,\r\n" + 
				"                             random_state=0)\n";
		
		algoSet += "clf.fit(X, Y)\n\r\n" + 
				"print(clf.feature_importances_)\r\n" + 
				"\r\n" + 
				"print(clf.predict([[0, 0, 0, 0]]))\n";
		return size + split + TRAIN_TEST_SPLIT + algoSet;
	}
	
	/** Algo Logistic Regression**/
	public String traitementLogisticRegression() {
		System.out.println("traitement Logistic Regression");
		
		double test_size = result.getValidation().getStratification().getNumber()/100.0;
		String size = "test_size = " + test_size +"\n";
		
		String split = "X = mml_data.drop(columns=[\"variety\"])\n"
				+ "Y = mml_data[\"variety\"]\n";
		
		String TRAIN_TEST_SPLIT = "X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size="+ test_size +") \n";
		
		String algoSet = "logreg = LogisticRegression(C=1e5, solver='lbfgs', multi_class='multinomial')"
				+ "logreg.fit(X, Y)";
		
		return size + split + TRAIN_TEST_SPLIT + algoSet;
	}
	
	/** traitement de la stratification method : Cross Validation ou Training Test**/
	//surement inutile
	public String traitementStratificationMethod() {
		if(this.result.getValidation().getStratification() instanceof CrossValidation) {
			System.err.println("Cross Validation");
		}
		else if(this.result.getValidation().getStratification() instanceof TrainingTest) {
			System.err.println("Training Test");
		}
		return null;
	}
	
	/** traitement de la classification metric : accuracy, recall, f1 ou precision**/
	public String traitementMetric() {
		if(this.result.getValidation().getStratification() instanceof TrainingTest) {
			if(this.result.getValidation().getMetric().get(0) == ValidationMetric.ACCURACY) {
				return "accuracy = accuracy_score(y_test, clf.predict(X_test))\r\n" + 
						"\r\n" + 
						"print(accuracy)";			
			}
			else if(this.result.getValidation().getMetric().get(0) == ValidationMetric.RECALL) {
				return "recall = recall_score(y_test, clf.predict(X_test), average='macro')\n"
						+ "print(recall)";
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
		else if(this.result.getValidation().getStratification() instanceof CrossValidation) {
			if(this.result.getValidation().getMetric().get(0) == ValidationMetric.ACCURACY) {
				return "accuracy = cross_val_score(y_test, clf.predict(X_test))\r\n" + 
						"\r\n" + 
						"print(accuracy)";			
			}
			else if(this.result.getValidation().getMetric().get(0) == ValidationMetric.RECALL) {
				return "recall = recall_score(y_test, clf.predict(X_test), average='macro')\n"
						+ "print(recall)";
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
		else {
			return null;
		}		
	}
	
	/** traitement des dependances a importer**/
	public String traitementImport() {
		return this.importList;
	}
	/** ajout d'un dependance a importer **/
	public void addImport(String s) {
		this.importList += s;
	}
	
	private static String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}
}
