package com.dji.sdk.sample.common.integration.api;

import com.dji.sdk.sample.common.mission.api.I_CameraGeneratedNewMediaFileCallback;

import dji.common.camera.DJICameraSettingsDef;

/**
 * Created by Julia on 2017-03-12.
 */

public interface I_Camera
{
    enum CameraMode { SHOOT_PHOTO, MEDIA_DOWNLOAD }

    void setCameraMode(CameraMode mode, I_CompletionCallback callback);
    void setCameraGeneratedNewMediaFileCallback(I_CameraGeneratedNewMediaFileCallback callback);
    void setPhotoFileFormat(
            DJICameraSettingsDef.CameraPhotoFileFormat  photoFileFormat,
            I_CompletionCallback callback);
    void setExposureModeToAutomatic(I_CompletionCallback callback);
    void shootSinglePhoto(I_CompletionCallback callback);
}
