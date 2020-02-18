package groupe1_brillu_damand_guillet_renault.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
public class TestWeka {
	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	//final String CSV_FOLDER = "\"C:/Users/pbril/Documents/R_workspace/iris.csv\"";
	/*final String CSV_FOLDER = "\"C:/Users/A730437/Documents/MIAGE/IDM/R_IDM/iris.csv\"";
	
	@Test
	public void testWekaDT() throws Exception {
		MMLModel result = parseHelper.parse("datainput "+CSV_FOLDER+" separator ;\n"
				+ "mlframework Weka\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "accuracy\n"
				+ "");
		
		String traitementAlgo = Compilateur.traitementAlgo(result);	
		
		Files.write(traitementAlgo.getBytes(), new File("Main.java"));
		
		long startTime = System.nanoTime();
		
		Process generateClass = Runtime.getRuntime().exec("javac -cp \".;./weka-3.7.0.jar\" main.java");
		BufferedReader in = new BufferedReader(new InputStreamReader(generateClass.getInputStream()));
		BufferedReader out = new BufferedReader(new InputStreamReader(generateClass.getErrorStream()));
		String line1;
		while ((line1 = in.readLine()) != null) {
			System.out.println(line1);
	    }
		
		Process p = Runtime.getRuntime().exec("java -cp \".;./weka-3.7.0.jar\" main");

		in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		out = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line;
		Double metric = 0.0;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
			metric = Double.parseDouble(line.split("= ")[1]);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println(metric);
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
	}
	
	@Test
	public void testWekaRF() throws Exception {
		MMLModel result = parseHelper.parse("datainput "+CSV_FOLDER+" separator ;\n"
				+ "mlframework Weka\n"
				+ "algorithm RF\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "accuracy\n"
				+ "");
		
		String traitementAlgo = Compilateur.traitementAlgo(result);	
		
		Files.write(traitementAlgo.getBytes(), new File("Main.java"));
		long startTime = System.nanoTime();
		
		Process generateClass = Runtime.getRuntime().exec("javac -cp \".;./weka-3.7.0.jar\" main.java");
		BufferedReader in = new BufferedReader(new InputStreamReader(generateClass.getInputStream()));
		BufferedReader out = new BufferedReader(new InputStreamReader(generateClass.getErrorStream()));
		String line1;
		while ((line1 = in.readLine()) != null) {
			System.out.println(line1);
	    }
		
		Process p = Runtime.getRuntime().exec("java -cp \".;./weka-3.7.0.jar\" main");

		in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		out = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line;
		Double metric = 0.0;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
			metric = Double.parseDouble(line.split("= ")[1]);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println(metric);
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
	}
	
	@Test
	public void testWekaLogisticRegression() throws Exception {
		MMLModel result = parseHelper.parse("datainput "+CSV_FOLDER+" separator ;\n"
				+ "mlframework Weka\n"
				+ "algorithm LogisticRegression\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "accuracy\n"
				+ "");
		
		String traitementAlgo = Compilateur.traitementAlgo(result);	
		
		Files.write(traitementAlgo.getBytes(), new File("Main.java"));
		long startTime = System.nanoTime();
		
		Process generateClass = Runtime.getRuntime().exec("javac -cp \".;./weka-3.7.0.jar\" main.java");
		BufferedReader in = new BufferedReader(new InputStreamReader(generateClass.getInputStream()));
		BufferedReader out = new BufferedReader(new InputStreamReader(generateClass.getErrorStream()));
		String line1;
		while ((line1 = in.readLine()) != null) {
			//System.out.println(line1);
	    }
		
		Process p = Runtime.getRuntime().exec("java -cp \".;./weka-3.7.0.jar\" main");

		in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		out = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line;
		Double metric = 0.0;
		while ((line = in.readLine()) != null) {
			//System.out.println(line);
			metric = Double.parseDouble(line.split("= ")[1]);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println(Compilateur.getFramework(result));
		System.err.println(Compilateur.getAlgo(result));
		System.err.println(metric);
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
	}*/
	
}
