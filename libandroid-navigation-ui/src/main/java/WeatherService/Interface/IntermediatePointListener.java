package WeatherService.Interface;

import java.util.Map;

import WeatherService.Models.mStep;

/**
 * Created by k on 4/19/2019.
 */

public interface IntermediatePointListener extends mError{

       void OnIntermediatePointsCalculated(Map<Integer, mStep> msteps);

}
