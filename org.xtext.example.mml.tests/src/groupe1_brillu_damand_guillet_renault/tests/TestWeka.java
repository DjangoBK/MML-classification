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
	
	@Test
	public void testR() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"/media/hippolyte/Hippolyte/Cour/iris.csv\" separator ;\n"
				+ "mlframework Weka\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "recall\n"
				+ "");
		
		//Compilateur c = Compilateur.(result);
		//c.traitement(result);
		
		//String pandasCode = Compilateur.loadData(result);
		String pandasCode = Compilateur.traitementAlgo(result);	
		
		Files.write(pandasCode.getBytes(), new File("main.java"));
		long startTime = System.nanoTime();
		Process p = Runtime.getRuntime().exec("java -cp weka-3.7.0.jar main.java");

		/* Start the process */
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader out = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
		// end of Python generation
	}
	
	@Test
	public void testWeka2() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"/media/hippolyte/Hippolyte/Cour/iris.csv\" separator ;\n"
				+ "mlframework Weka\n"
				+ "algorithm RF\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "precision\n"
				+ "");
		
		//Compilateur c = Compilateur.(result);
		//c.traitement(result);
		
		//String pandasCode = Compilateur.loadData(result);
		String pandasCode = Compilateur.traitementAlgo(result);	
		
		Files.write(pandasCode.getBytes(), new File("main.java"));
		long startTime = System.nanoTime();
		Process p = Runtime.getRuntime().exec("java -cp weka-3.7.0.jar main.java");

		/* Start the process */
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader out = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
		// end of java generation
	}
	
	@Test
	public void testWeka3() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"/media/hippolyte/Hippolyte/Cour/iris.csv\" separator ;\n"
				+ "mlframework Weka\n"
				+ "algorithm LogisticRegression\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "precision\n"
				+ "");
		
		//Compilateur c = Compilateur.(result);
		//c.traitement(result);
		
		//String pandasCode = Compilateur.loadData(result);
		String pandasCode = Compilateur.traitementAlgo(result);	
		
		Files.write(pandasCode.getBytes(), new File("main.java"));
		long startTime = System.nanoTime();
		Process p = Runtime.getRuntime().exec("java -cp weka-3.7.0.jar main.java");

		/* Start the process */
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader out = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
		// end of Python generation
	}
	
}
