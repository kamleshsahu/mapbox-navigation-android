package WeatherService.UIutils;

import com.mapboxweather.kamleshsahu.mapboxdemo.R;

import java.util.HashMap;
import java.util.Map;

import static WeatherService.Constants.*;
import static WeatherService.Constants.WEATHER_CLEAR_DAY;
import static WeatherService.Constants.WEATHER_CLEAR_NIGHT;
import static WeatherService.Constants.WEATHER_CLOUDY;
import static WeatherService.Constants.WEATHER_FOG;
import static WeatherService.Constants.WEATHER_HAIL;
import static WeatherService.Constants.WEATHER_PARTLY_CLOUDY_DAY;
import static WeatherService.Constants.WEATHER_PARTLY_CLOUDY_NIGHT;
import static WeatherService.Constants.WEATHER_RAIN;
import static WeatherService.Constants.WEATHER_SLEET;
import static WeatherService.Constants.WEATHER_SNOW;
import static WeatherService.Constants.WEATHER_THUNDERSTORM;
import static WeatherService.Constants.WEATHER_TORNADO;
import static WeatherService.Constants.WEATHER_WIND;

public class weatherIconMap {

    private Map<String, Integer> iconMap;

    public weatherIconMap() {
        iconMap=new HashMap<>();

        iconMap.put(WEATHER_CLEAR_DAY,R.drawable.clear_day);
        iconMap.put(WEATHER_CLOUDY,R.drawable.cloudy);

        iconMap.put(WEATHER_CLEAR_NIGHT,R.drawable.clear_night);

        iconMap.put(WEATHER_FOG,R.drawable.fog);

        iconMap.put(WEATHER_HAIL,R.drawable.hail);

        iconMap.put(WEATHER_PARTLY_CLOUDY_DAY,R.drawable.partly_cloudy_day);

        iconMap.put(WEATHER_PARTLY_CLOUDY_NIGHT,R.drawable.partly_cloudy_night);

        iconMap.put(WEATHER_RAIN,R.drawable.rain);

        iconMap.put(WEATHER_SLEET,R.drawable.sleet);

        iconMap.put(WEATHER_SNOW,R.drawable.snow);

        iconMap.put(WEATHER_THUNDERSTORM,R.drawable.thunderstorm);

        iconMap.put(WEATHER_TORNADO,R.drawable.tornado);

        iconMap.put(WEATHER_WIND,R.drawable.wind);

    }

    public int getWeatherResource(String icon) {

        if (icon!=null && iconMap.get(icon) != null) {
            return iconMap.get(icon);
        } else {
            //   return R.drawable.maneuver_starting;
            return R.drawable.not_found;
        }
    }

}
