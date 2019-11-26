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
	
	public static void createFile() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"C:/CSVFile/iris.csv\" separator ;\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "recall\n"
				+ "");
		
		String pandasCode = loadData(result);
		//pandasCode += traitementAlgo(result);
		
		Files.write(pandasCode.getBytes(), new File("mml_from_compiler.py"));
		// end of Python generation
		
		
		/*
		 * Calling generated Python script (basic solution through systems call)
		 * we assume that "python" is in the path
		 */
		Process p = Runtime.getRuntime().exec("python mml.py");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line; 
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }
	}
	
	
	public static String loadData(MMLModel result) {
		DataInput dataInput = result.getInput();
		String fileLocation = dataInput.getFilelocation();
		
		String pythonImport = "import pandas as pd\n"; 
		String csv_separator = DEFAULT_COLUMN_SEPARATOR;
		CSVParsingConfiguration parsingInstruction = dataInput.getParsingInstruction();
		
		if (parsingInstruction != null) {
			csv_separator = parsingInstruction.getSep().toString();
		}
		return pythonImport + "\n";
	}
	
	public static String traitementAlgo(MMLModel result) {
		//FrameworkLang framworkLang = result.getAlgorithm().getFramework();
		EList<MLChoiceAlgorithm> algorithms = result.getAlgorithms();
		String res = "";
		for(MLChoiceAlgorithm mlcalgo : algorithms) {
			FrameworkLang framworkLang = mlcalgo.getFramework();
			if(framworkLang == FrameworkLang.SCIKIT) {
				System.err.println("scikit-learn");
				CompilateurScikitLearn compilateur = new CompilateurScikitLearn(result);
				return compilateur.traitement();
			}
			else if(framworkLang == FrameworkLang.R) {
				//CompilateurR compilateur = new CompilateurR(result);
				//return compilateur.traitement(result);
			}
			else if(framworkLang == FrameworkLang.JAVA_WEKA) {
				//CompilateurWeka compilateur = new CompilateurWeka(result);
				//return compilateur.traitement(result);
			}
			else {
				return null;
			}
		}
		return res;
	}
	
	private static String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}
	
	public static void main (String[] args) throws Exception{
		
	}

}
