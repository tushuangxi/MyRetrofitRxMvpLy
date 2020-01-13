package com.lding.pad.myseial.libding.rerxmvp.base;


import com.lding.pad.myseial.libding.rerxmvp.interfaceUtils.interfaceUtilsAll;

public class BasePresenter<T extends interfaceUtilsAll.IBaseView> {

    private T view;

    public void attachView(T view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }

    public T getView() {
        return view;
    }
}
