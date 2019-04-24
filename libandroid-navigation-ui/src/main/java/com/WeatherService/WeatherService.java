package com.WeatherService;


import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.WeatherService.Interface.IntermediatePointListener;
import com.WeatherService.Interface.PointMatrixListener;
import com.WeatherService.Interface.WeatherServiceListener;
import com.WeatherService.Interface.WeatherofPointListener;
import com.WeatherService.Methods.IntermediatePoints;
import com.WeatherService.Methods.PointMatrixForAll;
import com.WeatherService.Methods.WeatherFinder;
import com.WeatherService.Models.Darkskyapi;
import com.WeatherService.Models.mPoint;
import com.WeatherService.Models.mStep;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.WeatherService.Methods.TimeFormatter.formatTimeforDisp;


public class WeatherService implements
        PointMatrixListener,
        WeatherofPointListener,
        IntermediatePointListener
{



    private DirectionsRoute routedata=null;
    private String timezoneid;
    private long interval=50000;
    private long jstarttime;
    private String travelmode;
    Map<Integer,mStep> msteps;
    WeatherFinder wfs;
    PointMatrixForAll pointMatrixs;
    IntermediatePoints fn;
    WeatherServiceListener listener;
    Set<Integer> queue;
    int totalpoints;
    int count=0;


    public WeatherService( DirectionsRoute routedata, String timezoneid, long interval, long jstarttime, String travelmode) {
        this.routedata = routedata;
        this.timezoneid = timezoneid;
        this.interval = interval;
        this.jstarttime = jstarttime;
        this.travelmode = travelmode;
        queue=new HashSet<>();
    }

    public void setListener(WeatherServiceListener listener) {
        this.listener = listener;
    }

    public void calc_data(){



       fn = new IntermediatePoints(this);
        pointMatrixs = new PointMatrixForAll(this);
        wfs = new WeatherFinder(this);
       fn.extractListofPoints(interval,routedata,timezoneid,jstarttime,travelmode);



   }

    @Override
    public void OnIntermediatePointsCalculated(Map<Integer, mStep> msteps) {
        this.msteps = msteps;

        queue.addAll(msteps.keySet());
        for(int key:msteps.keySet()){
            Map<Integer,mPoint> interms;
            if((interms=msteps.get(key).getInterms())!=null)
            queue.addAll(interms.keySet());
        }
        totalpoints=queue.size();

        for (Map.Entry<Integer, mStep> mstep : msteps.entrySet()) {

            wfs.calcWeather(mstep.getKey(),
                    mstep.getValue().getStep_StartPoint().latitude(),
                    mstep.getValue().getStep_StartPoint().longitude(),
                    mstep.getValue().getSDFTime());
        }

        pointMatrixs.calc(msteps,travelmode);
    }

    @Override
    public void OnPointMatrixCalculated(int id) {

       int step_id=id-(id%1000);

       mPoint mpoint=msteps.get(step_id).getInterms().get(id);

        wfs.calcWeather(id,
                mpoint.getPoint().latitude(),
                mpoint.getPoint().longitude(),
                mpoint.getDs_arr_time());

    }


    @Override
    public void OnWeatherFetched(int id, Darkskyapi response) {
        int step_id=id-(id%1000);

        if(id%1000 != 0) {
            mPoint mpoint = msteps.get(step_id).getInterms().get(id);
            mpoint.setWeather_data(response.getCurrently());
            mpoint.setDisplay_arrtime(formatTimeforDisp(response.getCurrently().getTime(),response.getTimezone()));

            if(listener!=null)
                listener.onWeatherOfPointReady(id,mpoint);
        }else{
           mStep mstep= msteps.get(step_id);
           mstep.setWeatherdata(response.getCurrently());
           mstep.setDisplay_arrtime(formatTimeforDisp(response.getCurrently()
                    .getTime(),response.getTimezone()));
           if(listener!=null)
               listener.onWeatherOfStepReady(step_id,mstep);
        }

        queue.remove(id);

        if(listener!=null)
        if(queue.size()==0){
            listener.OnWeatherDataListReady(msteps);
        }else{
            listener.onWeatherDataListProgressChange((count++ *100)/totalpoints);
        }
    }

    @Override
    public void onError(String etitle, String emsg) {
        if(listener!=null)
        listener.onError(etitle,emsg);
    }



//        public static void main(String[] args) {
//        Point sp=Point.fromLngLat(-105.2705, 40.015);
//        Point dp=Point.fromLngLat(-104.9653, 39.7348);
//        String profile= DirectionsCriteria.PROFILE_DRIVING;
//        //       jstarttime=System.currentTimeMillis();
//        Calendar calendar=Calendar.getInstance();
//
//        String timezoneid=calendar.getTimeZone().getID();
//        long jstarttime=calendar.getTimeInMillis();
//        String travelmode= DirectionsCriteria.PROFILE_DRIVING;
//
//        long interval=10000;
//
//
//        RouteFinder rf=new RouteFinder(sp, dp, profile, "", new Callback<DirectionsResponse>() {
//            @Override
//            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
//                System.out.println(response.body());
//                DirectionsRoute routedata=response.body().routes().get(0);
//
//
//                WeatherService service;
//                service = new WeatherService(routedata,timezoneid,interval,jstarttime,travelmode);
//                service.calc_data();
//
////                IntermediatePoints fn= new IntermediatePoints(interval,routedata,timezoneid,jstarttime,travelmode);
////                List<mStep> msteps= fn.extractListofPoints();
////
////                System.out.println(msteps);
////
////                PointMatrixForAll pointMatrixs=new PointMatrixForAll(msteps,travelmode);
////                pointMatrixs.calc();
////
////                System.out.println(msteps);
////
////                WeatherForAllPoints weatherForAllPoints=new WeatherForAllPoints(msteps);
////                weatherForAllPoints.calcWeather();
////
////                System.out.println(msteps);
//
//
//            }
//
//            @Override
//            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
//                t.printStackTrace();
//                System.out.println(t.getMessage());
//            }
//        });
//        rf.find();
//    }
    
}
