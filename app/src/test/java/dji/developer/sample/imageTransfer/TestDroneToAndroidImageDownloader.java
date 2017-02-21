package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.imageTransfer.I_ImageTransferPathsSource;
import com.dji.sdk.sample.common.integration.I_MediaDataFetcher;
import com.dji.sdk.sample.common.integration.I_MediaDownloadListener;
import com.dji.sdk.sample.common.mission.I_MissionController;

import dji.sdk.camera.DJIMedia;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Julia on 2017-02-17.
 */

public class TestDroneToAndroidImageDownloader
{
    private I_ImageTransferPathsSource pathSource_ = mock(I_ImageTransferPathsSource.class);
    private I_MediaDataFetcher mediaDataFetcher_ = mock(I_MediaDataFetcher.class);
    private I_MissionController missionController_ = mock(I_MissionController.class);

    private DroneToAndroidImageDownloader patient_ = new DroneToAndroidImageDownloader(
            pathSource_,
            mediaDataFetcher_,
            missionController_);

    @Test
    public void willRequestAndroidImagePathFromPathSource()
    {
        int imageCount = 3;

        patient_.downloadImagesFromDrone(makeListOfImages(imageCount));

        verify(pathSource_, atLeastOnce()).androidDeviceImagePath();
    }

    @Test
    public void willFetchMediaDataForEachImageInList()
    {
        int size = 3;
        File expectedImagePath = new File("pathname");
        ArrayList<DJIMedia> imagesToDownload = makeListOfImages(size);
        ArrayList<DJIMedia> expectedImagesToDownload = (ArrayList<DJIMedia>)imagesToDownload.clone();
        when(pathSource_.androidDeviceImagePath()).thenReturn(expectedImagePath);
        makeMediaDataFetcherCallOnSuccessCallback();

        patient_.downloadImagesFromDrone(imagesToDownload);

        verifyEachImageWasDownloaded(expectedImagesToDownload, expectedImagePath);
    }

    @Test
    public void willResumeMissionAfterImagesHaveBeenDownloaded()
    {
        int size = 3;
        File imagePath = new File("pathname");
        ArrayList<DJIMedia> imagesToDownload = makeListOfImages(size);
        when(pathSource_.androidDeviceImagePath()).thenReturn(imagePath);
        makeMediaDataFetcherCallOnSuccessCallback();

        patient_.downloadImagesFromDrone(imagesToDownload);

        InOrder inOrder = inOrder(mediaDataFetcher_, missionController_);
        inOrder.verify(mediaDataFetcher_, times(size)).fetchMediaData(
                any(DJIMedia.class), any(File.class), any(I_MediaDownloadListener.class));
        inOrder.verify(missionController_).resumeMission();
    }

    private ArrayList<DJIMedia> makeListOfImages(int size)
    {
        ArrayList<DJIMedia> imagesToDownload = new ArrayList<>();
        for(int i = 0; i < size; i++)
        {
            imagesToDownload.add(new DJIMedia());
        }
        return  imagesToDownload;
    }

    private void makeMediaDataFetcherCallOnSuccessCallback()
    {
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                patient_.onSuccess(null);
                return null;
            }})
                .when(mediaDataFetcher_).fetchMediaData(
                    any(DJIMedia.class), any(File.class), any(I_MediaDownloadListener.class));
    }

    private void verifyEachImageWasDownloaded(
            ArrayList<DJIMedia> expectedImagesToDownload, File expectedImagePath)
    {
        int size = expectedImagesToDownload.size();

        ArgumentCaptor<DJIMedia> images = ArgumentCaptor.forClass(DJIMedia.class);
        ArgumentCaptor<File> imagePath = ArgumentCaptor.forClass(File.class);
        ArgumentCaptor<I_MediaDownloadListener> callback = ArgumentCaptor.forClass(I_MediaDownloadListener.class);

        verify(mediaDataFetcher_, times(size)).fetchMediaData(
                images.capture(), imagePath.capture(), callback.capture());

        for(int i = 0; i < size; i++)
        {
            assertTrue(images.getAllValues().contains(expectedImagesToDownload.get(i)));
            assertEquals(expectedImagePath, imagePath.getAllValues().get(i));
            assertSame(patient_, callback.getAllValues().get(i));
        }
    }
}
