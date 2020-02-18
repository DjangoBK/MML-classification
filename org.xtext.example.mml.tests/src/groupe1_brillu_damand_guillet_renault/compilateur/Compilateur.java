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
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.LogisticRegression;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SVM;
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
		
	public static void traitementAlgo(MMLModel result) throws IOException {
		EList<MLChoiceAlgorithm> algorithms = result.getAlgorithms();
		String res = "";
		
		for(MLChoiceAlgorithm mlcalgo : algorithms) {
			FrameworkLang framworkLang = mlcalgo.getFramework();
			if(framworkLang == FrameworkLang.SCIKIT) {
				System.out.println(mlcalgo.toString());
				CompilateurScikitLearn compilateur = new CompilateurScikitLearn(result, mlcalgo);
				execScikit(compilateur.traitement(), "scikit", compilateur.getAlgo(), compilateur.getDataset());
			}
			else if(framworkLang == FrameworkLang.R) {
				CompilateurR compilateur = new CompilateurR(result, mlcalgo);
				execR(compilateur.traitement(), "R", compilateur.getAlgoName(), compilateur.getDataSet());
			}
			else if(framworkLang == FrameworkLang.JAVA_WEKA) {
				CompilateurWeka compilateur = new CompilateurWeka(result, mlcalgo);
				execWeka(compilateur.traitement());
			}
		}
	}
	
	public static void execWeka(String traitementAlgo) throws IOException {
		Files.write(traitementAlgo.getBytes(), new File("Main.java"));
		long startTime = System.nanoTime();
		
		Process generateClass = Runtime.getRuntime().exec("javac -cp \".;./weka-3.7.0.jar\" main.java");
		BufferedReader in = new BufferedReader(new InputStreamReader(generateClass.getInputStream()));
		BufferedReader out = new BufferedReader(new InputStreamReader(generateClass.getErrorStream()));
		String line1;
		while ((line1 = in.readLine()) != null) {}
		
		Process p = Runtime.getRuntime().exec("java -cp \".;./weka-3.7.0.jar\" main");

		in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		out = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line;
		Double metric = 0.0;
		while ((line = in.readLine()) != null) {
			metric = Double.parseDouble(line.split("= ")[1]);
	    }
		long elapsedTime = System.nanoTime() - startTime;
		System.err.println(metric);
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
	}
	
	public static void execScikit(String pandasCode, String framework, String algo, String dataSet) throws IOException {
		Double sommeAcc = 0.0;
		Double sommeDur = 0.0;
		int rep = 0;
		
		while(rep<20) {
			System.out.println(rep);
			
			Files.write(pandasCode.getBytes(), new File("mml_DT_acc.py"));
			
			long startTime = System.nanoTime();
			Process p = Runtime.getRuntime().exec("python mml_DT_acc.py");
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line; 
			String last_line ="";
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				 last_line = line;
		    }
			long elapsedTime = System.nanoTime() - startTime;
			System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
			Double acc = Double.parseDouble(last_line);
			sommeAcc += acc;
			sommeDur += elapsedTime/1000000000.0;
			rep++;
		}
		System.err.println("dataset = " + dataSet);
		System.err.println("framework = " + framework);
		System.err.println("algo = " + algo);
		System.err.println("moyenne acc = " + sommeAcc/rep);
		System.err.println("moyenne temps = " + sommeDur/rep);
	}
	
	public static void execR(String pandasCode, String framework, String algo, String dataSet) throws IOException {
		Files.write(pandasCode.getBytes(), new File("mml.R"));
		
		long startTime = System.nanoTime();
		Process p = Runtime.getRuntime().exec("Rscript mml.R");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		String last="";
		int c = 0;
		while ((line = in.readLine()) != null) {
			last = line;
	    }
		long elapsedTime = System.nanoTime() - startTime;
		double metric = Double.parseDouble(last);
		System.err.println("dataset = " + dataSet);
		System.err.println("framework = " + framework);
		System.err.println("algo = " + algo);
		System.err.println("metric = " + metric);
		System.err.println("temps d'execution : " + elapsedTime/1000000000.0);
		in.close();
	}
}
