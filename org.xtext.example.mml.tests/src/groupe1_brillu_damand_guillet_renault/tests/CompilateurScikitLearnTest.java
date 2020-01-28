package groupe1_brillu_damand_guillet_renault.tests;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.tests.MmlInjectorProvider;

import com.google.common.io.Files;
import com.google.inject.Inject;

import groupe1_brillu_damand_guillet_renault.compilateur.*;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class CompilateurScikitLearnTest {
	
	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	final String CSV_FOLDER = "\"C:/Users/pbril/Documents/R_workspace/iris.csv\"";
	
	@Test
	public void loadModel() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"foo.csv\"\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "recall\n"
				+ "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");			
		Assertions.assertEquals("foo.csv", result.getInput().getFilelocation());			
		
	}		
	
	@Test
	public void compilerTest() throws Exception {
		MMLModel result = parseHelper.parse("datainput "+CSV_FOLDER+" separator ;\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "accuracy\n"
				+ "");
		
//		MMLModel result =parseHelper.parse("datainput \"C:/CSVFile/iris.csv\" \r\n" + 
//				"mlframework scikit-learn\r\n" + 
//				"algorithm SVM kernel=linear classification C-classification TrainingTest{percentageTraining 20}\r\n" + 
//				"recall");
		
//		MMLModel result = parseHelper.parse("datainput \"C:/CSVFile/iris.csv\" \r\n" + 
//				"mlframework scikit-learn algorithm SVM classification one-classification TrainingTest {percentageTraining 20}\r\n" + 
//				"accuracy");
//		
//		MMLModel result =parseHelper.parse("datainput \"C:/CSVFile/iris.csv\"\r\n" + 
//				"mlframework scikit-learn\r\n" + 
//				"algorithm RandomForest TrainingTest{percentageTraining 20}"
//				+ "accuracy");
		
		//String pandasCode = Compilateur.loadData(result);
		String pandasCode = Compilateur.traitementAlgo(result);	
		
		Files.write(pandasCode.getBytes(), new File("mml.py"));
		// end of Python generation
		
		
		/*
		 * Calling generated Python script (basic solution through systems call)
		 * we assume that "python" is in the path
		 */
		long startTime = System.nanoTime();
		Process p = Runtime.getRuntime().exec("python mml.py");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line; 
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
	}
	
//	private String mkValueInSingleQuote(String val) {
//		return "'" + val + "'";
//	}

}
