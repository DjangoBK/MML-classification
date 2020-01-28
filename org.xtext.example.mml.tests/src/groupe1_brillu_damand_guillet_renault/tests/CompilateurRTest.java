package groupe1_brillu_damand_guillet_renault.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.xml.crypto.AlgorithmMethod;

import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.services.MmlGrammarAccess.MLChoiceAlgorithmElements;
import org.xtext.example.mydsl.tests.MmlInjectorProvider;

import com.google.common.io.Files;
import com.google.inject.Inject;

import groupe1_brillu_damand_guillet_renault.compilateur.Compilateur;
import groupe1_brillu_damand_guillet_renault.compilateur.CompilateurR;
import groupe1_brillu_damand_guillet_renault.compilateur.CompilateurRPierreWork;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class CompilateurRTest {
	
	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	final String CSV_FOLDER = "\"C:/Users/pbril/Documents/R_workspace/iris.csv\"";
	
	@Test
	public void testR() throws Exception {
		MMLModel result = parseHelper.parse("datainput "+CSV_FOLDER+" separator ;\n"
				+ "mlframework R\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "accuracy\n"
				+ "");
		
		
		
		String pandasCode = Compilateur.traitementAlgo(result);	
		
		Files.write(pandasCode.getBytes(), new File("mml.R"));
		// end of Python generation
		
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
}
