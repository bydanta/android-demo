package com.template.model;

/**
 * Model
 */
public interface IModel {

    void getData(ICallback iCallback);

    public interface ICallback{
        public void onResult(String data);
    }
}
