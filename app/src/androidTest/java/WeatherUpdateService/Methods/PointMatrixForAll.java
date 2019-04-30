package WeatherUpdateService.Methods;

import java.util.Iterator;
import java.util.Map;

import WeatherUpdateService.Interface.PointMatrixListener;
import WeatherUpdateService.Models.mPoint;
import WeatherUpdateService.Models.mStep;

/**
 * Created by k on 4/4/2019.
 */

public class PointMatrixForAll {

    PointMatrixListener matrixListener=null;
    public PointMatrixForAll(PointMatrixListener matrixListener) {
        this.matrixListener = matrixListener;
    }

    public void setMatrixListener(PointMatrixListener matrixListener) {
        this.matrixListener = matrixListener;
    }

    public void calc(Map<Integer,mStep> msteps, String travelmode){

        Iterator<Map.Entry<Integer,mStep>> iterator=msteps.entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry<Integer,mStep> currstep= iterator.next();

            Map<Integer,mPoint> mpoints=currstep.getValue().getInterms();

            if(mpoints!=null && mpoints.size()>0) {
                String timezoneid = currstep.getValue().getTimezoneid();
                long jstarttime = currstep.getValue().getJstarttime();

                PointMatrix pointMatrix = new PointMatrix(
                        currstep.getKey(),
                        currstep.getValue().getStep_StartPoint(),
                        mpoints,
                        travelmode,
                        timezoneid,
                        jstarttime,
                        currstep.getValue().getAft_duration()
                );
                pointMatrix.setMatrixListener(matrixListener);
                pointMatrix.calc();
            }
        }
    }
}
