/**
 * Created by nickburrell on 03/03/2017.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TrafficDataProcess
{
    HashMap<String, RoadData>

    public static void run()
    {

    }

    public static void read()
    {
        String csvFile = "/Users/nickburrell/Downloads/Raw-count-data-major-roads.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);

                System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}