package WeatherUpdateService.Models;

import com.mapbox.api.directions.v5.models.DirectionsRoute;

/**
 * Created by k on 4/24/2019.
 */

public class WeatherServiceBuilder {

    DirectionsRoute route;
    int selectedroute;
    long interval;
    long jstarttime;
    String timezone;
    String travelmode;


  public void Builder(DirectionsRoute route, int selectedroute, long interval, long jstarttime, String timezone, String travelmode) {
        this.route = route;
        this.selectedroute = selectedroute;
        this.interval = interval;
        this.jstarttime = jstarttime;
        this.timezone = timezone;
        this.travelmode = travelmode;
    }
}
