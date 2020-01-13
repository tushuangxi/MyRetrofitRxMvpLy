package com.lding.pad.myseial.libding.rerxmvp.view;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.lding.pad.myseial.R;
import com.lding.pad.myseial.libding.entity.GetListRsp;
import com.lding.pad.myseial.libding.http.ServiceMapParams;
import com.lding.pad.myseial.libding.rerxmvp.base.BaseMvpActivity;
import com.lding.pad.myseial.libding.rerxmvp.interfaceUtils.interfaceUtilsAll;
import com.lding.pad.myseial.libding.rerxmvp.presenter.GetListRspPresenter;


public class GetListRspActivity extends BaseMvpActivity<interfaceUtilsAll.GetListRspView, GetListRspPresenter> implements interfaceUtilsAll.GetListRspView {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPresenter().getGetListRsp(ServiceMapParams.getGetListRspMapParams());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {


    }

    @Override
    public GetListRspPresenter createPresenter() {
        return new GetListRspPresenter();
    }

    @Override
    public interfaceUtilsAll.GetListRspView createView() {
        return this;
    }

    @Override
    public void onGetUser(GetListRsp getListRsp) {

        Toast.makeText(GetListRspActivity.this,getListRsp.getFemale().iterator().next().getName(),Toast.LENGTH_SHORT).show();
    }
}
