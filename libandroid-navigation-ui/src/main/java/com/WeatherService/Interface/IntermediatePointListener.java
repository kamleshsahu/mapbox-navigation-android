package com.WeatherService.Interface;

import com.WeatherService.Models.mStep;

import java.util.Map;

/**
 * Created by k on 4/19/2019.
 */

public interface IntermediatePointListener extends mError{

       void OnIntermediatePointsCalculated(Map<Integer, mStep> msteps);

}
