package groupe1_brillu_damand_guillet_renault.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.tests.MmlInjectorProvider;

import com.google.common.io.Files;
import com.google.inject.Inject;

import groupe1_brillu_damand_guillet_renault.compilateur.Compilateur;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class TestAll {
	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	final String CSV_FOLDER = "\"C:/Users/pbril/Documents/R_workspace/iris.csv\"";
	
	@Test
	public void testWekaDT() throws Exception {
		MMLModel result = parseHelper.parse("datainput "+CSV_FOLDER+" separator ;\n"
				+ "mlframework Weka\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "recall\n"
				+ "");
		
		String traitementAlgo = Compilateur.traitementAlgo(result);	
		
		Files.write(traitementAlgo.getBytes(), new File("main.java"));
		long startTime = System.nanoTime();
		Process p = Runtime.getRuntime().exec("java -cp weka-3.7.0.jar main.java");

		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader out = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
	}
	
	@Test
	public void testWekaRF() throws Exception {
		MMLModel result = parseHelper.parse("datainput "+CSV_FOLDER+" separator ;\n"
				+ "mlframework Weka\n"
				+ "algorithm RF\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "precision\n"
				+ "");
		
		String traitementAlgo = Compilateur.traitementAlgo(result);	
		
		Files.write(traitementAlgo.getBytes(), new File("main.java"));
		long startTime = System.nanoTime();
		Process p = Runtime.getRuntime().exec("java -cp weka-3.7.0.jar main.java");

		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader out = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
	}
	
	@Test
	public void testWekaLogisticRegression() throws Exception {
		MMLModel result = parseHelper.parse("datainput "+CSV_FOLDER+" separator ;\n"
				+ "mlframework Weka\n"
				+ "algorithm LogisticRegression\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "precision\n"
				+ "");
		
		String traitementAlgo = Compilateur.traitementAlgo(result);	
		
		Files.write(traitementAlgo.getBytes(), new File("main.java"));
		long startTime = System.nanoTime();
		Process p = Runtime.getRuntime().exec("java -cp weka-3.7.0.jar main.java");

		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader out = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
	}
	
	@Test
	public void testRDT() throws Exception {
		MMLModel result = parseHelper.parse("datainput "+CSV_FOLDER+" separator ;\n"
				+ "mlframework R\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "accuracy\n"
				+ "");
		
		String pandasCode = Compilateur.traitementAlgo(result);	
		
		Files.write(pandasCode.getBytes(), new File("mml.R"));
		
		long startTime = System.nanoTime();
		Process p = Runtime.getRuntime().exec("Rscript mml.R");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line; 
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
		in.close();
	}
	
	@Test
	public void testRRF() throws Exception {
		MMLModel result = parseHelper.parse("datainput "+CSV_FOLDER+" mlframework R algorithm RandomForest TrainingTest{percentageTraining 70} accuracy");
		
		String pandasCode = Compilateur.traitementAlgo(result);	
		
		Files.write(pandasCode.getBytes(), new File("mml.R"));
		
		long startTime = System.nanoTime();
		Process p = Runtime.getRuntime().exec("Rscript mml.R");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line; 
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
		in.close();
	}
	
	@Test
	public void testRSVM() throws Exception {
		MMLModel result = parseHelper.parse("datainput" + CSV_FOLDER + "mlframework R algorithm SVM TrainingTest {percentageTraining 70} accuracy");
		
		
		
		
		String pandasCode = Compilateur.traitementAlgo(result);	
		
		Files.write(pandasCode.getBytes(), new File("mml.R"));
		
		long startTime = System.nanoTime();
		Process p = Runtime.getRuntime().exec("Rscript mml.R");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line; 
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
		in.close();
	}
	
	@Test
	public void testScikitDT() throws Exception {
		MMLModel result = parseHelper.parse("datainput "+CSV_FOLDER+" separator ;\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "accuracy\n"
				+ "");
		String pandasCode = Compilateur.traitementAlgo(result);	
		
		Files.write(pandasCode.getBytes(), new File("mml.py"));
		
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
	
	@Test
	public void testScikitSVM() throws Exception {		
		MMLModel result =parseHelper.parse("datainput "+CSV_FOLDER+" \r\n" + 
				"mlframework scikit-learn\r\n" + 
				"algorithm SVM kernel=linear classification C-classification TrainingTest{percentageTraining 20}\r\n" + 
				"recall");
		
		String pandasCode = Compilateur.traitementAlgo(result);	
		
		Files.write(pandasCode.getBytes(), new File("mml.py"));
		
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
	
	@Test
	public void testScikitRF() throws Exception {
		MMLModel result =parseHelper.parse("datainput "+CSV_FOLDER+" \r\n" +
				"mlframework scikit-learn\r\n" + 
				"algorithm RandomForest TrainingTest{percentageTraining 20}"
				+ "accuracy");

		String pandasCode = Compilateur.traitementAlgo(result);	
		
		Files.write(pandasCode.getBytes(), new File("mml.py"));

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
}
