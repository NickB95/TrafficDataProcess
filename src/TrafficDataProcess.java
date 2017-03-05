/**
 * Created by nickburrell on 03/03/2017.
 */
import uk.me.jstott.jcoord.*;
import com.opencsv.*;

import org.json.simple.JSONObject;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.*;

public class TrafficDataProcess
{
    static HashMap<String, HashMap<String, RoadData>> hashMap = new HashMap<String, HashMap<String, RoadData>>();

    static JSONObject masterObj;

    // Test var
    static int zeroCount = 0;

    public static void main(String args[])
    {
        run();
    }

    public static void run()
    {
        // Timing
        long tStart = System.currentTimeMillis();



        System.out.println("Reading data...");
        read();
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        System.out.println(elapsedSeconds);


        System.out.println("Generating JSON...");
        generateJSON();

        //Write to file
        System.out.println("Writing to file...");
        writeToFile();
        System.out.println("Writing done");

        System.out.println(zeroCount);
    }

    private static void print()
    {
        for(String objname:hashMap.keySet())
        {
            System.out.println(objname);

            HashMap<String, RoadData> subMap = hashMap.get(objname);

            for(String subname:subMap.keySet())
            {
                System.out.println(subname);
                System.out.println(subMap.get(subname).count);
                System.out.println(subMap.get(subname).roadName);
            }
        }
    }

    private static void generateJSON()
    {
        // Foreach entry in the primary HashMap

        Iterator <HashMap.Entry<String, HashMap<String, RoadData>>> iterator = hashMap.entrySet().iterator();

        masterObj = new JSONObject();

        while(iterator.hasNext())
        {
            // Get next
            HashMap.Entry<String, HashMap<String, RoadData>> next = iterator.next();

            JSONObject objToAdd = new JSONObject();

            // Get sub hashMap Iterator
            Iterator <HashMap.Entry<String, RoadData>> subIterator = next.getValue().entrySet().iterator();

            while(subIterator.hasNext())
            {
                // Get next
                HashMap.Entry<String, RoadData> subNext = subIterator.next();

                JSONObject subObjToAdd = new JSONObject();

                subObjToAdd.put("total", subNext.getValue().total);
                subObjToAdd.put("count", subNext.getValue().count);
                subObjToAdd.put("average", subNext.getValue().average);
                subObjToAdd.put("cpLocation", subNext.getValue().cpLocation.toString());
                subObjToAdd.put("roadName", subNext.getValue().roadName);
                subObjToAdd.put("jBefore", subNext.getValue().jBefore.toString());
                subObjToAdd.put("jAfter", subNext.getValue().jAfter.toString());


                objToAdd.put(subNext.getKey(), subObjToAdd);
            }

            masterObj.put(next.getKey(), objToAdd);
        }
    }

    private static void writeToFile()
    {
        Writer writerr = null;

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("/Users/nickburrell/IdeaProjects/TrafficDataProcess/output/output.JSON"), "utf-8")))
        {
            writer.write(masterObj.toJSONString());

            writer.close();
        }
        catch (FileNotFoundException fnf)
        {
            System.out.println("File not found");
        }
        catch (UnsupportedEncodingException uee)
        {
            System.out.println("Unsupported Encoding");
        }
        catch(IOException ioe)
        {
            System.out.println("IOExc");
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

                int[] indexes = new int []{4, 5, 6, 8, 9, 11, 12, 17, 30};

                // if any of the accessed data is empty/null, skip
                if (isEmpty(indexes, nextLine))
                    continue;

                // add to hashMap by road
                LatLng cpLocation = new OSRef(Double.parseDouble(nextLine[4]), Double.parseDouble(nextLine[5])).toLatLng();

                String roadName = nextLine[6];

                LatLng jBefore = new OSRef(Double.parseDouble(nextLine[8]), Double.parseDouble(nextLine[9])).toLatLng();

                LatLng jAfter = new OSRef(Double.parseDouble(nextLine[11]), Double.parseDouble(nextLine[12])).toLatLng();

                int hour = Integer.parseInt(nextLine[17]);

                int count = Integer.parseInt(nextLine[30]);

                // Temp string
                String beforeAfter = jBefore.toString() + jAfter.toString();

                HashMap<String, RoadData> dataMap = hashMap.get(roadName);

                if (count == 0)
                    zeroCount++;

                if (dataMap != null)
                {
                    RoadData roadData = dataMap.get(beforeAfter);

                    if (roadData != null)
                    {
                        roadData.addValue(count, hour);
                    }
                    else
                    {
                        RoadData dataToAdd = new RoadData(roadName, cpLocation, jBefore, jAfter, count);

                        dataMap.put(beforeAfter, dataToAdd);
                    }
                }
                else
                {
                    RoadData dataToAdd = new RoadData(roadName, cpLocation, jBefore, jAfter, count);

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