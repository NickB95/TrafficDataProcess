/**
 * Created by nickburrell on 03/03/2017.
 */
public class RoadData
{
    int total;
    int count;
    double average;

    public RoadData(int _total, int _count, double _average)
    {
        total = _total;
        count = _count;
        average = _average;
    }

    // Function that takes in the value to be added, most likely the total vehicle count, then adds to the attr's IF
    // the counts were taken at the correct time of day
    public void addValue(int value, int timeOfDay)
    {
        if (timeOfDay < 8 || timeOfDay > 20)
            return;

        total += value;

        count ++;

        average = total / count;
    }
}
