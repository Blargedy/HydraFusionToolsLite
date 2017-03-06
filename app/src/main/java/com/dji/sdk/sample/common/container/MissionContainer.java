package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerator;
import com.dji.sdk.sample.common.mission.src.CustomMissionBuilder;
import com.dji.sdk.sample.common.mission.api.I_MissionController;
import com.dji.sdk.sample.common.mission.src.MissionController;
import com.dji.sdk.sample.common.mission.src.MissionGenerator;
import com.dji.sdk.sample.common.mission.src.MissionStepCompletionCallback;
import com.dji.sdk.sample.common.mission.src.MissionPreparer;
import com.dji.sdk.sample.common.mission.src.WaypointReachedHandler;
import com.dji.sdk.sample.common.presenter.MapPresenter;
import com.dji.sdk.sample.common.presenter.MissionGenerationPresenter;
import com.dji.sdk.sample.common.presenter.MissionControllerPresenter;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.view.FlightControlView;

/**
 * Created by Julia on 2017-02-04.
 */

public class MissionContainer
{
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;

    private MissionController missionController_;

    private WaypointReachedHandler waypointReachedHandler_;
    private MissionStepCompletionCallback missionStepCompletionCallback_;
    private CustomMissionBuilder customMissionBuilder_;
    private MissionPreparer missionPreparer_;
    private MissionGenerator missionGenerator_;

    public MissionContainer(
            IntegrationLayerContainer integrationLayerContainer,
            ImageTransferContainer imageTransferContainer,
            I_ApplicationContextManager contextManager)
    {

        initialMissionModel_ = new InitialMissionModel();
        generatedMissionModel_ = new GeneratedMissionModel();

        missionController_ = new MissionController(
                integrationLayerContainer.missionManagerSource(),
                integrationLayerContainer.flightControllerSource(),
                contextManager);

        waypointReachedHandler_ = new WaypointReachedHandler(
                missionController_,
                imageTransferContainer.imageTransferer());
        missionStepCompletionCallback_ = new MissionStepCompletionCallback(
                waypointReachedHandler_);
        customMissionBuilder_ = new CustomMissionBuilder(
                initialMissionModel_,
                generatedMissionModel_,
                contextManager,
                missionStepCompletionCallback_);
        missionPreparer_ = new MissionPreparer(
                integrationLayerContainer.missionManagerSource(),
                integrationLayerContainer.flightControllerSource(),
                generatedMissionModel_);
        missionGenerator_ = new MissionGenerator(
                customMissionBuilder_,
                missionPreparer_,
                imageTransferContainer.imageTransferModuleInitializer());
    }

    public InitialMissionModel initialMissionModel()
    {
        return initialMissionModel_;
    }

    public GeneratedMissionModel generatedMissionModel()
    {
        return generatedMissionModel_;
    }

    public I_MissionController missionController()
    {
        return missionController_;
    }

    public I_MissionGenerator missionGenerator()
    {
        return missionGenerator_;
    }
}
