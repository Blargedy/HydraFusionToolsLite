package com.dji.sdk.sample.common.mission;

import android.widget.Toast;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.imageTransfer.I_ImageTransferer;
import com.dji.sdk.sample.common.integration.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.I_FlightController;
import com.dji.sdk.sample.common.integration.I_FlightControllerSource;
import com.dji.sdk.sample.common.integration.I_MissionManager;
import com.dji.sdk.sample.common.integration.I_MissionManagerSource;
import com.dji.sdk.sample.common.integration.MissionManager;
import com.dji.sdk.sample.common.integration.MissionManagerSource;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import dji.common.error.DJIError;
import dji.common.flightcontroller.DJILocationCoordinate2D;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.flightcontroller.DJIFlightController;
import dji.sdk.missionmanager.DJIMission;
import dji.sdk.missionmanager.DJIMissionManager;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.products.DJIAircraft;
import dji.sdk.sdkmanager.DJISDKManager;


/**
 * Created by Matthew on 2017-02-08.
 */

public class MissionController implements I_MissionController {

    private I_MissionManager missionManager_;
    private I_FlightController flightController_;
    private I_ImageTransferer imageTransferer_;
    private I_ApplicationContextManager contextManager_;
    private GeneratedMissionModel missionModel_;

    public MissionController(
            I_MissionManagerSource missionManagerSource,
            I_FlightControllerSource flightControllerSource,
            I_ImageTransferer imageTransferer,
            I_ApplicationContextManager contextManager,
            GeneratedMissionModel missionModel)
    {
        missionManager_ = missionManagerSource.getMissionManager();
        flightController_ = flightControllerSource.getFlightController();
        imageTransferer_ = imageTransferer;
        contextManager_ = contextManager;
        missionModel_ = missionModel;

    }

    public void handleWaypointReached(int waypoint)
    {
        //pause the mission every 5 waypoints
        if(waypoint%5 == 0)
        {
            pauseMission();

            /*
            Designed with the following expected to block until finished.
             */
            imageTransferer_.transferNewImagesFromDrone();

            resumeMission();
        }
    }

    public void pauseMission()
    {
        missionManager_.pauseMissionExecution(MissionHelper.completionCallback(
                contextManager_, "Paused mission to transfer photos",
                "Could not pause mission to transfer photos: "));
    }

    public void resumeMission()
    {
        missionManager_.resumeMissionExecution(MissionHelper.completionCallback(
                contextManager_,"Resumed mission","Could not resume mission:"));
    }

    public void takeOff()
    {
        flightController_.takeOff(MissionHelper.completionCallback(contextManager_,
                "Taking off Successfully", "Failed to Takeoff"));
    }

    @Override
    public void startMission()
    {
        if(!flightController_.getCurrentState().isFlying())
        {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "Aircraft not taken off. Attempting to take off.", Toast.LENGTH_LONG).show();
            takeOff();

            flightController_.getFlightLimitation().setMaxFlightRadiusLimitationEnabled(false, null);
            return;
        }
        else
        {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "Attempting to launch mission", Toast.LENGTH_LONG).show();
        }

        if (missionManager_ != null && flightController_.getCurrentState().isFlying())
        {
            try
            {
                DJIMission.DJIMissionProgressHandler progressHandler = new DJIMission.DJIMissionProgressHandler()
                {
                    @Override
                    public void onProgress(DJIMission.DJIProgressType type, float progress) {}
                };


                flightController_.getHomeLocation(new DJICommonCallbacks.DJICompletionCallbackWith<DJILocationCoordinate2D>() {
                    @Override
                    public void onSuccess(DJILocationCoordinate2D djiLocationCoordinate2D) {
                        Toast.makeText(contextManager_.getApplicationContext(), "Home location "+ djiLocationCoordinate2D.getLatitude() +
                                ", " + djiLocationCoordinate2D.getLongitude(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(DJIError djiError) {
                        Toast.makeText(contextManager_.getApplicationContext(), "Could not get home location " + djiError.getDescription(), Toast.LENGTH_LONG).show();
                    }
                });

                missionManager_.startMissionExecution(MissionHelper.completionCallback(
                        contextManager_, "Started Mission Successfully ","Failed to Prepare Mission. Exiting"));

            } catch (Throwable e)
            {
                Toast.makeText(contextManager_.getApplicationContext(), "Failed to Prepare Mission. Exiting", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else
        {
            Toast.makeText(contextManager_.getApplicationContext(), "Mission manager is null", Toast.LENGTH_LONG).show();
        }

    }//end startMission
}
