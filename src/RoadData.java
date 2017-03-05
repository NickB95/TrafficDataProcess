/**
 * Created by nickburrell on 03/03/2017.
 */

import uk.me.jstott.jcoord.*;

public class RoadData
{
    int total = 0;
    int count = 0;
    float average = 0;

    LatLng cpLocation;

    String roadName;

    LatLng jBefore;

    LatLng jAfter;

    public RoadData(String _roadName, LatLng _cpLocation, LatLng _jBefore, LatLng _jAfter, int _total)
    {
        cpLocation = _cpLocation;

        jBefore = _jBefore;
        jAfter = _jAfter;

        roadName = _roadName;

        total = _total;
        average = this.total;
        count = 1;
    }

    // Function that takes in the value to be added, most likely the total vehicle count, then adds to the attr's IF
    // the counts were taken at the correct time of day
    public void addValue(int value, int timeOfDay)
    {
        if (timeOfDay < 8 || timeOfDay > 20)
            return;

        this.total += value;

        this.count ++;

        this.average = (float)this.total / (float)this.count;
    }
}
