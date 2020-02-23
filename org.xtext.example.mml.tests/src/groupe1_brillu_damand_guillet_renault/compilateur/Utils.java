package groupe1_brillu_damand_guillet_renault.compilateur;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Utils {

	/**
     * Permet d'ecrire dans un csv
     * @param data - la chaine que l'on veut ecrire
     */
    public static void createCsvFile(String data) {

        try {
            //penser a changer le chemin du file
            FileWriter writer = new FileWriter("data.csv",true);
            writer.write(data+"\n");
            writer.close();

        } catch (Exception e) {
            System.out.println("update impossible");
        }
    }
    
    public static String getLastCol(String fileLocation, String separator) {
    	File file = new File(fileLocation);
    	try {
			String fl = Files.lines(file.toPath()).findFirst().get();
			String[] array = fl.split(separator);
			System.out.println(array[array.length-1]);
			return array[array.length-1];
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static String getCol(String fileLocation, String separator, int col) {
    	File file = new File(fileLocation);
    	try {
			String fl = Files.lines(file.toPath()).findFirst().get();
			String[] array = fl.split(separator);
			System.out.println(col);
			return array[col-1];
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static int getNbCol(String fileLocation, String separator) {
    	File file = new File(fileLocation);
    	try {
			String fl = Files.lines(file.toPath()).findFirst().get();
			String[] array = fl.split(separator);
			return array.length;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return 0;
    }
    

    public static void displayConsole(String framework, String algo, String dataSet, Double sommeAcc ,Double sommeDur ,int compteur) {
    	System.out.println("\n----------------- Infos ----------------\n");
    	System.out.println("Dataset used : " + dataSet);
		System.out.println("Framework used : " + framework);
		System.out.println("Algo used : " + algo);
		System.out.println("Moyenne accuracy = " + sommeAcc/compteur);
		System.out.println("Moyenne temps = " + sommeDur/compteur);
		System.out.println("\n----------------------------------------\n");
    }
}
