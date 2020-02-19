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
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
}
