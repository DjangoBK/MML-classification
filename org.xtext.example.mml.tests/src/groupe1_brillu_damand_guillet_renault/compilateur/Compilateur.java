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

	static String DEFAULT_COLUMN_SEPARATOR = ",";

	String algo;
	String framework;

	public static void traitementAlgo(MMLModel result) throws IOException {
		EList<MLChoiceAlgorithm> algorithms = result.getAlgorithms();
		for (MLChoiceAlgorithm mlcalgo : algorithms) {
			FrameworkLang framworkLang = mlcalgo.getFramework();
			if (framworkLang == FrameworkLang.SCIKIT) {
				CompilateurScikitLearn compilateur = new CompilateurScikitLearn(result, mlcalgo);
				execScikit(compilateur.traitement(), "scikit", compilateur.getAlgo(), compilateur.getDataset());
			} else if (framworkLang == FrameworkLang.R) {
				CompilateurR compilateur = new CompilateurR(result, mlcalgo);
				execR(compilateur.traitement(), "R", compilateur.getAlgoName(), compilateur.getDataSet());
			} else if (framworkLang == FrameworkLang.JAVA_WEKA) {
				CompilateurWeka compilateur = new CompilateurWeka(result, mlcalgo);
				String test = compilateur.traitement();
				execWeka(test, "weka", compilateur.getAlgoName(), compilateur.getDataSet());
			}
		}
	}

	public static void execWeka(String traitementAlgo, String framework, String algo, String dataSet) throws IOException {
		Double sommeAcc = 0.0;
		Double sommeDur = 0.0;
		int compteur = 0;

		while (compteur < 20) {
			System.out.println("Etape : "+compteur);
			Files.write(traitementAlgo.getBytes(), new File("Main.java"));
			long startTime = System.nanoTime();

			Process generateClass = Runtime.getRuntime().exec("javac -cp \".;./weka-3.7.0.jar\" main.java");
			BufferedReader in = new BufferedReader(new InputStreamReader(generateClass.getInputStream()));
			while ((in.readLine()) != null) {
			}

			Process p = Runtime.getRuntime().exec("java -cp \".;./weka-3.7.0.jar\" main");

			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			Double metric = 0.0;
			while ((line = in.readLine()) != null) {
				if (!algo.equals("SVM"))
					metric = Double.parseDouble(line.split("= ")[1]);
			}
			long elapsedTime = System.nanoTime() - startTime;
			sommeAcc += metric;
			sommeDur += elapsedTime / 1000000000.0;
			compteur++;
		}

		Utils.displayConsole(framework, algo, dataSet, sommeAcc, sommeDur, compteur);
		Utils.createCsvFile(dataSet + "," + framework + "," + algo + "," + sommeAcc / compteur + "," + sommeDur / compteur);
	}

	public static void execScikit(String pandasCode, String framework, String algo, String dataSet) throws IOException {
		Double sommeAcc = 0.0;
		Double sommeDur = 0.0;
		int compteur = 0;

		while (compteur < 20) {
			System.out.println("Etape : "+compteur);

			Files.write(pandasCode.getBytes(), new File("mml_DT_acc.py"));

			long startTime = System.nanoTime();
			Process p = Runtime.getRuntime().exec("python mml_DT_acc.py");
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			String last_line = "";
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				last_line = line;
			}
			long elapsedTime = System.nanoTime() - startTime;
			System.err.println("temps d'execution : " + elapsedTime / 1000000000.0);
			Double acc = 0.0;
			if (!last_line.isEmpty()) {
				acc = Double.parseDouble(last_line);
			}
			sommeAcc += acc;
			sommeDur += elapsedTime / 1000000000.0;
			compteur++;
		}

		Utils.displayConsole(framework, algo, dataSet, sommeAcc, sommeDur, compteur);
		Utils.createCsvFile(dataSet + "," + framework + "," + algo + "," + sommeAcc / compteur + "," + sommeDur / compteur);
	}

	public static void execR(String pandasCode, String framework, String algo, String dataSet) throws IOException {
		Double sommeAcc = 0.0;
		Double sommeDur = 0.0;
		int compteur = 0;

		while (compteur < 20) {
			System.out.println("Etape : "+compteur);
			Files.write(pandasCode.getBytes(), new File("mml.R"));

			long startTime = System.nanoTime();
			Process p = Runtime.getRuntime().exec("Rscript mml.R");
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			String last = "";
			int c = 0;
			while ((line = in.readLine()) != null) {
				last = line;
			}
			long elapsedTime = System.nanoTime() - startTime;
			System.out.println(last);
			double metric = 0.0;
			if (!last.isEmpty()) {
				metric = Double.parseDouble(last);
			}
			sommeAcc += metric;
			sommeDur += elapsedTime / 1000000000.0;
			compteur++;
		}

		Utils.displayConsole(framework, algo, dataSet, sommeAcc, sommeDur, compteur);
		Utils.createCsvFile(dataSet + "," + framework + "," + algo + "," + sommeAcc / compteur + "," + sommeDur / compteur);
	}
	
	public static void execXgBoost(String traitementAlgo, String framework, String algo, String dataSet) throws IOException {
		Double sommeAcc = 0.0;
		Double sommeDur = 0.0;
		int compteur = 0;
		String line;

		while (compteur < 20) {
			System.out.println("Etape : "+compteur);
			Files.write(traitementAlgo.getBytes(), new File(""));
			long startTime = System.nanoTime();

			Process generateClass = Runtime.getRuntime().exec("");
			BufferedReader in = new BufferedReader(new InputStreamReader(generateClass.getInputStream()));

			Process p = Runtime.getRuntime().exec("");

			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			Double metric = 0.0;
			while ((line = in.readLine()) != null) {
				if (!algo.equals("SVM"))
					metric = Double.parseDouble(line.split("= ")[1]);
			}
			long elapsedTime = System.nanoTime() - startTime;
			sommeAcc += metric;
			sommeDur += elapsedTime / 1000000000.0;
			compteur++;
		}

		Utils.displayConsole(framework, algo, dataSet, sommeAcc, sommeDur, compteur);
		Utils.createCsvFile(dataSet + "," + framework + "," + algo + "," + sommeAcc / compteur + "," + sommeDur / compteur);
	}
}
