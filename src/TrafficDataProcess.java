/**
 * Created by nickburrell on 03/03/2017.
 */
import uk.me.jstott.jcoord.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TrafficDataProcess
{
    static HashMap<String, HashMap<String, RoadData>> hashMap = new HashMap<String, HashMap<String, RoadData>>();

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
                String[] data = line.split(cvsSplitBy);

                // add to hashMap by road
                LatLng cpLocation = new OSRef(Double.parseDouble(data[4]), Double.parseDouble(data[5])).toLatLng();

                String roadName = data[6];

                LatLng jBefore = new OSRef(Double.parseDouble(data[8]), Double.parseDouble(data[9])).toLatLng();

                LatLng jAfter = new OSRef(Double.parseDouble(data[11]), Double.parseDouble(data[12])).toLatLng();

                int hour = Integer.parseInt(data[17]);

                int count = Integer.parseInt(data[26]);

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