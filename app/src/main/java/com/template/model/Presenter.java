package com.template.model;

/**
 * Presenter实现
 */
public class Presenter implements IPresenter {

    private IView iView;
    private IModel iModel;

    public Presenter(IView iView){

        this.iView = iView;
        this.iModel = new Model();
    }

    @Override
    public void performOnClick() {

        iModel.getData(new IModel.ICallback() {
            @Override
            public void onResult(String data) {
                iView.setData(data);
            }
        });
    }
}
