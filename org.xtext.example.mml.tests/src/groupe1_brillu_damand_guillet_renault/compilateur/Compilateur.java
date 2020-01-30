package groupe1_brillu_damand_guillet_renault.compilateur;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.LogisticRegression;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SVM;
import org.xtext.example.mydsl.tests.MmlInjectorProvider;

import com.google.inject.Inject;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class Compilateur {
	@Inject
	static ParseHelper<MMLModel> parseHelper;
	
	static String DEFAULT_COLUMN_SEPARATOR = ","; // by default	
	
	String algo;
	String framework;
	
	public static String traitementAlgo(MMLModel result) {
		EList<MLChoiceAlgorithm> algorithms = result.getAlgorithms();
		String res = "";
		
		for(MLChoiceAlgorithm mlcalgo : algorithms) {
			FrameworkLang framworkLang = mlcalgo.getFramework();
			if(framworkLang == FrameworkLang.SCIKIT) {
				CompilateurScikitLearn compilateur = new CompilateurScikitLearn(result, mlcalgo);
				res += compilateur.traitement();
			}
			else if(framworkLang == FrameworkLang.R) {
				CompilateurR compilateur = new CompilateurR(result, mlcalgo);
				return compilateur.traitement();
			}
			else if(framworkLang == FrameworkLang.JAVA_WEKA) {
				CompilateurWeka compilateur = new CompilateurWeka(result, mlcalgo);
				return compilateur.traitement();
			}
		}
		return res;
	}

	public static String getFramework(MMLModel result) {
		EList<MLChoiceAlgorithm> algorithms = result.getAlgorithms();		
		for(MLChoiceAlgorithm mlcalgo : algorithms) {
			FrameworkLang framworkLang = mlcalgo.getFramework();
			if(framworkLang == FrameworkLang.SCIKIT) {
				return "SCIKIT";
			}
			else if(framworkLang == FrameworkLang.R) {
				return "R";
			}
			else if(framworkLang == FrameworkLang.JAVA_WEKA) {
				return "WEKA";
			}
		}
		return "";
	}	
	
	public static String getAlgo(MMLModel result) {
		EList<MLChoiceAlgorithm> algorithms = result.getAlgorithms();		
		for(MLChoiceAlgorithm mlcalgo : algorithms) {
			if(mlcalgo.getAlgorithm() instanceof DT) {
				return "Decision Tree";
			}
			else if (mlcalgo.getAlgorithm() instanceof SVM) {
				return "SVM";
			}
			else if (mlcalgo.getAlgorithm() instanceof RandomForest) {
				return "Random Forest";
			}
			else if (mlcalgo.getAlgorithm() instanceof LogisticRegression) {
				return "Logistic Regression";
			}
		}
		return "";
	}

}
