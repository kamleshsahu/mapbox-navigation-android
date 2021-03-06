package WeatherService;


import android.os.AsyncTask;

import com.mapbox.api.directions.v5.models.DirectionsRoute;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import WeatherService.Interface.IntermediatePointListener;
import WeatherService.Interface.PointMatrixListener;
import WeatherService.Interface.WeatherServiceListener;
import WeatherService.Interface.WeatherofPointListener;
import WeatherService.Methods.IntermediatePointsUpdater;
import WeatherService.Methods.PointMatrixForAll;
import WeatherService.Methods.WeatherFinder;
import WeatherService.Models.Darkskyapi;
import WeatherService.Models.StepCorrection;
import WeatherService.Models.mPoint;
import WeatherService.Models.mStep;

import static WeatherService.Methods.TimeFormatter.formatTimeforDisp;


public class WeatherUpdateService extends AsyncTask<Void,Object,Void>
        implements
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
    IntermediatePointsUpdater fn;
    WeatherServiceListener listener;
    Set<Integer> queue;
    int totalpoints;
    int count=0;
    int currStep=0;
    StepCorrection correction;

    public WeatherUpdateService(DirectionsRoute routedata, String timezoneid, long interval, long jstarttime, String travelmode, int currStep,StepCorrection correction) {
        this.routedata = routedata;
        this.timezoneid = timezoneid;
        this.interval = interval;
        this.jstarttime = jstarttime;
        this.travelmode = travelmode;
        this.currStep=currStep;
        this.correction=correction;
        queue=new HashSet<>();
    }

    public void setListener(WeatherServiceListener listener) {
        this.listener = listener;
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
        }
        else
            {
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


    @Override
    protected Void doInBackground(Void... voids) {
        fn = new IntermediatePointsUpdater(this);
        pointMatrixs = new PointMatrixForAll(this);
        wfs = new WeatherFinder(this);
        fn.extractListofPoints(interval,routedata,timezoneid,jstarttime,travelmode,currStep,correction);
        return null;
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
