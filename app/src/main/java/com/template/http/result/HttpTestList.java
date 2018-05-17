package com.template.http.result;

import com.template.bean.Test;
import com.template.http.HttpResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANTA on 2015/12/3.
 * 视图模型
 */
public class HttpTestList extends HttpResults {

    private ArrayList<Test> list;

    public ArrayList<Test> getList() {
        return list;
    }

    public void setList(ArrayList<Test> list) {
        this.list = list;
    }
}
