package com.WeatherService.Interface;

import com.WeatherService.Models.mPoint;
import com.WeatherService.Models.mStep;

import java.util.Map;

/**
 * Created by k on 4/19/2019.
 */

public interface WeatherServiceListener extends mError{

    void OnWeatherDataListReady(Map<Integer, mStep> msteps);

    void onWeatherOfPointReady(int id, mPoint mpoint);
//
    void onWeatherOfStepReady(int step_id, mStep mstep);

    void onWeatherDataListProgressChange(int progress);
}
