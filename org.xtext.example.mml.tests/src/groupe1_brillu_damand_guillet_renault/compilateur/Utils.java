package groupe1_brillu_damand_guillet_renault.compilateur;

import java.io.FileWriter;

public class Utils {

	/**
     * Permet d'ecrire dans un csv
     * @param data - la chaine que l'on veut ecrire
     */
    public static void createCsvFile(String data) {

        try {
            //penser a changer le chemin du file
            FileWriter writer = new FileWriter("./src/main/resources/data.csv",true);
            writer.write(data+"\n");
            writer.close();

        } catch (Exception e) {
            System.out.println("update impossible");
        }
    }
}
