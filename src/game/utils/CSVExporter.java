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
            calculateMean();
            fileWriter.write(CSVExporter.CONTAINER);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void calculateMean(){
        String[] rows = CONTAINER.split(";");
        Integer[] table = new Integer[rows.length-2];
        for(int i = 1; i < rows.length-1; i++){
            table[i-1] = Integer.parseInt(rows[i].split(",")[1]);
        }
        Integer p1Counter = 0, p2Counter = 0;
        Integer p1Rounds = 0, p2Rounds = 0;
        int i = 0;
        for(Integer time : table){
            if(i % 2 == 0){
                p1Counter += (time);
                p1Rounds++;
            }else{
                p2Counter += (time);
                p2Rounds++;
            }
            i++;
        }
        Float p1Mean = Float.valueOf(p1Counter)/Float.valueOf(p1Rounds);
        Float p2Mean = Float.valueOf(p2Counter)/Float.valueOf(p2Rounds);
        String tmp =("\nMean1, "+p1Mean+";\nMean2, "+p2Mean).replace(".",",");
        //System.out.println(tmp);
        CSVExporter.CONTAINER+=tmp;
    }
}
