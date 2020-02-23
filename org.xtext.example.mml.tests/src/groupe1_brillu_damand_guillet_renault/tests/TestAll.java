package groupe1_brillu_damand_guillet_renault.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
//	final String CSV_FOLDER = "\"C:/Users/A730437/Documents/MIAGE/IDM/R_IDM/iris.csv\"";	
//	final String CSV_FOLDER = "D:\\Perso\\Cours de fac\\Master\\Sem3\\IDM\\MML-classification\\org.xtext.example.mml.tests/iris.csv\"";
	final static String MML_FOLDER = "mml-files/";
	
	@Test
	public void testFromFolder() throws Exception {
		
		//On recupere tous les noms de fichiers 
		List<String> results = new ArrayList<String>();
		File[] filesInFolder = new File(MML_FOLDER).listFiles();
		for (File file : filesInFolder) {
			System.out.println(file.getName());
			BufferedReader mmlReader = new BufferedReader(new FileReader(file));
			String lineR="";
			String mml="";
			while ((lineR = mmlReader.readLine()) != null) {
				mml += lineR;
			}
			MMLModel result = parseHelper.parse(mml);
						
			Compilateur.traitementAlgo(result);	
		}
	}
	
	@Test
	public void testOneFile() throws Exception {
		File file = new File(MML_FOLDER + "scikit_lr.mml");
		
		BufferedReader mmlReader = new BufferedReader(new FileReader(file));
		String lineR="";
		String mml="";
		while ((lineR = mmlReader.readLine()) != null) {
			mml += lineR;
		}
		MMLModel result = parseHelper.parse(mml);
		
		Compilateur.traitementAlgo(result);
	}
}
