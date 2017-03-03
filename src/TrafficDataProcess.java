/**
 * Created by nickburrell on 03/03/2017.
 */
import uk.me.jstott.jcoord.*;
import com.opencsv.*;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TrafficDataProcess
{
    static HashMap<String, HashMap<String, RoadData>> hashMap = new HashMap<String, HashMap<String, RoadData>>();

    public static void main(String args[])
    {
        run();
    }

    public static void run()
    {
        read();

        System.out.println("Done");
        for(String objname:hashMap.keySet())
        {
            System.out.println(objname);

            HashMap<String, RoadData> subMap = hashMap.get(objname);

            /*for(String subname:subMap.keySet())
            {
                System.out.println(subname);
                System.out.println(subMap.get(subname).count);
                System.out.println(subMap.get(subname).roadName);
            }*/
        }
    }

    public static void read()
    {
        CSVReader reader = null;
        try {

            String csvFile = "/Users/nickburrell/Downloads/Raw-count-data-major-roads.csv";
            reader = new CSVReader(new FileReader(csvFile));
            String [] nextLine;

            // bool to skip first line
            boolean skipLine = true;

            while ((nextLine = reader.readNext()) != null)
            {
                if (skipLine)
                {
                    skipLine = false;
                    continue;
                }

                int[] indexes = new int []{4, 5, 6, 8, 9, 11, 12, 17, 26};

                // if any of the accessed data is empty/null, skip
                if (isEmpty(indexes, nextLine))
                    continue;

                // add to hashMap by road
                LatLng cpLocation = new OSRef(Double.parseDouble(nextLine[4]), Double.parseDouble(nextLine[5])).toLatLng();

                String roadName = nextLine[6];

                LatLng jBefore = new OSRef(Double.parseDouble(nextLine[8]), Double.parseDouble(nextLine[9])).toLatLng();

                LatLng jAfter = new OSRef(Double.parseDouble(nextLine[11]), Double.parseDouble(nextLine[12])).toLatLng();

                int hour = Integer.parseInt(nextLine[17]);

                int count = Integer.parseInt(nextLine[26]);

                // Temp string
                String beforeAfter = jBefore.toString() + jAfter.toString();

                HashMap<String, RoadData> dataMap = hashMap.get(roadName);

                if (dataMap != null)
                {
                    RoadData roadData = dataMap.get(beforeAfter);

                    if (roadData != null)
                    {
                        roadData.addValue(count, hour);
                    }
                    else
                    {
                        RoadData dataToAdd = new RoadData(roadName, cpLocation, jBefore, jAfter);

                        dataMap.put(beforeAfter, dataToAdd);
                    }
                }
                else
                {
                    RoadData dataToAdd = new RoadData(roadName, cpLocation, jBefore, jAfter);

                    HashMap<String, RoadData> dataMapToAdd = new HashMap<String, RoadData>();

                    dataMapToAdd.put(beforeAfter, dataToAdd);

                    hashMap.put(roadName, dataMapToAdd);
                }


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to check wheter any of the indexes of a string array are empty
    private static boolean isEmpty(int[] indexes, String[] array)
    {
        for (int i : indexes)
        {
            if (array[i].isEmpty() || array[i] == null)
                return true;
        }

        return false;
    }
}