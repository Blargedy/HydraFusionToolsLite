package com.dji.sdk.sample.common.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dji.sdk.sample.common.container.MissionContainer;
import com.dji.sdk.sample.common.presenter.ProductConnectionPresenter;
import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.utility.UserPermissionRequester;
import com.dji.sdk.sample.common.view.SimpleDemoView;

public class SimpleDemoActivity extends AppCompatActivity
{
    private UserPermissionRequester permissionRequester_;
    private ApplicationContextManager contextManager_;

    private SimpleDemoView simpleDemoView_;

    private MissionContainer missionContainer_;

    private ProductConnectionPresenter productConnectionPresenter_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionRequester_ = new UserPermissionRequester();
        contextManager_ = new ApplicationContextManager(this);

        simpleDemoView_ = new SimpleDemoView(this);

        missionContainer_ = new MissionContainer(simpleDemoView_, contextManager_);

        productConnectionPresenter_ = new ProductConnectionPresenter(
                simpleDemoView_.connectionStatusText(),
                contextManager_);

        permissionRequester_.requestPermissions(this);

        setContentView(simpleDemoView_);
    }


}