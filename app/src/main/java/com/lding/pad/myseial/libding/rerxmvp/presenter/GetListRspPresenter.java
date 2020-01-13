package com.lding.pad.myseial.libding.rerxmvp.presenter;


import com.lding.pad.myseial.libding.entity.GetListRsp;
import com.lding.pad.myseial.libding.rerxmvp.base.BasePresenter;
import com.lding.pad.myseial.libding.rerxmvp.interfaceUtils.interfaceUtilsAll;
import com.lding.pad.myseial.libding.rerxmvp.module.GetAllDataModuleImpl;

import java.util.Map;

public class GetListRspPresenter extends BasePresenter<interfaceUtilsAll.GetListRspView> {

    private GetAllDataModuleImpl getListRspModule;

    public GetListRspPresenter() {
        this.getListRspModule = new GetAllDataModuleImpl();
    }

    public void getGetListRsp(Map<String, String> params) {
        getListRspModule.getGetListRsp(params, new interfaceUtilsAll.GetListRspCallBackListener() {
            @Override
            public void onSuccess(GetListRsp getListRsp) {
                if (getView() != null) {
                    getView().onGetUser(getListRsp);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
