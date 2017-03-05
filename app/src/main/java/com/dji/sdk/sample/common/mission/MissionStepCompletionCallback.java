package com.dji.sdk.sample.common.mission;

import android.widget.Toast;

import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferer;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import dji.common.error.DJIError;

/**
 * Created by eric7 on 2017-02-21.
 */

public class MissionStepCompletionCallback implements I_CompletionCallback
{
    private I_MissionController controller_;
    private I_ApplicationContextManager contextManager_;
    private I_ImageTransferer imageTransferer_;
    private int waypointCounter_;

    public MissionStepCompletionCallback(
            I_MissionController controller,
            I_ImageTransferer imageTransferer,
            I_ApplicationContextManager contextManager)
    {
        controller_ = controller;
        imageTransferer_ = imageTransferer;
        contextManager_ = contextManager;
        waypointCounter_ = 0;
    }

    public void onResult(DJIError error)
    {
        if (error == null) {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "Successfully reached Waypoint "+Integer.toString(waypointCounter_), Toast.LENGTH_SHORT).show();
            handleWaypointReached(waypointCounter_);
            waypointCounter_++;

        } else {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "could not reach waypoint "+Integer.toString(waypointCounter_)+". "+error.getDescription(), Toast.LENGTH_LONG).show();
        }
    }

    void handleWaypointReached(int waypointCount)
    {
        if(waypointCount%5 == 0)
        {
            controller_.pauseMission();
            //imageTransferer_.transferNewImagesFromDrone();
        }
    }

}

