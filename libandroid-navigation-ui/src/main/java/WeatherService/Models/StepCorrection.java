package WeatherService.Models;

import com.mapbox.geojson.Point;

/**
 * Created by k on 5/3/2019.
 */

public class StepCorrection {

    int newdistance;
    int newduration;
    Point newlocation;

    public StepCorrection(int newdistance, int newduration, Point newlocation) {
        this.newdistance = newdistance;
        this.newduration = newduration;
        this.newlocation = newlocation;
    }

    public int getNewdistance() {
        return newdistance;
    }

    public void setNewdistance(int newdistance) {
        this.newdistance = newdistance;
    }

    public int getNewduration() {
        return newduration;
    }

    public void setNewduration(int newduration) {
        this.newduration = newduration;
    }

    public Point getNewlocation() {
        return newlocation;
    }

    public void setNewlocation(Point newlocation) {
        this.newlocation = newlocation;
    }
}
