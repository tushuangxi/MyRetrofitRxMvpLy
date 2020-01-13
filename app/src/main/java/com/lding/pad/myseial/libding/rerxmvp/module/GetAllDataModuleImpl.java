package com.lding.pad.myseial.libding.rerxmvp.module;


import com.lding.pad.myseial.libding.entity.GetListRsp;
import com.lding.pad.myseial.libding.http.manager.BaseSubscriber;
import com.lding.pad.myseial.libding.http.manager.RetrofitManager;
import com.lding.pad.myseial.libding.rerxmvp.interfaceUtils.interfaceUtilsAll;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GetAllDataModuleImpl {

    public void getGetListRsp(Map<String, String> params, final interfaceUtilsAll.GetListRspCallBackListener listener) {
        RetrofitManager.getInstance().create()
                .getGetListRsp(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<GetListRsp>() {

                    @Override
                    public void onSuccess(GetListRsp getListRsp) {
                        if (listener != null) {
                            listener.onSuccess(getListRsp);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (listener != null) {
                            listener.onFailure(t);
                        }
                    }
                });
    }

}
