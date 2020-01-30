package groupe1_brillu_damand_guillet_renault.compilateur;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.CSVParsingConfiguration;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.tests.MmlInjectorProvider;

import com.google.common.io.Files;
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
		System.err.println("traitement algo");
		
		for(MLChoiceAlgorithm mlcalgo : algorithms) {
			FrameworkLang framworkLang = mlcalgo.getFramework();
			if(framworkLang == FrameworkLang.SCIKIT) {
				System.err.println("scikit-learn");
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
	
	private static String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}
	
	

}
