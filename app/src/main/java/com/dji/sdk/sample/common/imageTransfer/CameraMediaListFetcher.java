package com.dji.sdk.sample.common.imageTransfer;

import com.dji.sdk.sample.common.integration.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.I_MediaManager;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;

/**
 * Created by Julia on 2017-02-12.
 */

public class CameraMediaListFetcher implements
        I_CameraMediaListFetcher
{
    private I_MediaManagerSource mediaManagerSource_;
    private I_DroneImageDownloadSelector downloadSelector_;
    private I_DroneToAndroidImageDownloader imageDownloader_;

    public CameraMediaListFetcher(
            I_MediaManagerSource mediaManagerSource,
            I_DroneImageDownloadSelector downloadSelector,
            I_DroneToAndroidImageDownloader imageDownloader)
    {
        mediaManagerSource_ = mediaManagerSource;
        downloadSelector_ = downloadSelector;
        imageDownloader_ = imageDownloader;
    }

    @Override
    public void fetchMediaListFromCamera(I_CameraMediaListDownloadListener listener)
    {
        I_MediaManager mediaManager = mediaManagerSource_.getMediaManager();
        mediaManager.fetchMediaList(listener);
    }
}
