package com.dji.sdk.sample.common.mission.src;

import com.dji.sdk.sample.common.integration.api.I_BatteryStateUpdateCallback;
import com.dji.sdk.sample.common.utility.I_MissionErrorNotifier;

import dji.common.battery.DJIBatteryState;

/**
 * Created by Julia on 2017-03-23.
 */

public class BatteryTemperatureWarningNotifier implements I_BatteryStateUpdateCallback
{
    private static final int BATTERY_TEMPERATURE_THRESHOLD = 15;
    private static final String BATTERY_TEMPERATURE_WARNING_MESSAGE = "Warning: Battery temperature is below 15 degrees celsius";
    private I_MissionErrorNotifier missionErrorNotifier_;
    private boolean hasErrorBeenShown_;

    public BatteryTemperatureWarningNotifier(
            I_MissionErrorNotifier missionErrorNotifier)
    {
        missionErrorNotifier_ = missionErrorNotifier;
        hasErrorBeenShown_ = false;
    }

    @Override
    public void onResult(DJIBatteryState state)
    {
        if(!hasErrorBeenShown_)
        {
            if (state.getBatteryTemperature() < BATTERY_TEMPERATURE_THRESHOLD)
            {
                missionErrorNotifier_.notifyErrorOccurred(BATTERY_TEMPERATURE_WARNING_MESSAGE);
                hasErrorBeenShown_ = true;
            }
        }
    }
}