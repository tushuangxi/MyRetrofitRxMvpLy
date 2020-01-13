package com.lding.pad.myseial.libding.rerxmvp.interfaceUtils;


import com.lding.pad.myseial.libding.entity.GetListRsp;

public class interfaceUtilsAll {



    public interface IBaseView {
    }


    //GetListRsp
    public  interface GetListRspView extends interfaceUtilsAll.IBaseView {
        void onGetUser(GetListRsp getListRsp);
    }

    public interface GetListRspCallBackListener {
        void onSuccess(GetListRsp getListRsp);

        void onFailure(Throwable t);
    }


}
