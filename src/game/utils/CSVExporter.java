package game.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by stefano on 19/09/16.
 */
public class CSVExporter {

    public static String CONTAINER = "move,time;\n";


    public static void printCSV(){

        try {
            File file = new File("time.csv");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(CSVExporter.CONTAINER);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
