package com.template.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.template.app.R;
import com.template.base.BaseActivity;
import com.template.model.IPresenter;
import com.template.model.IView;
import com.template.model.Presenter;

import org.xutils.view.annotation.ViewInject;

/**
 * MVP模式
 * *
 */
public class Presenter_Demo_Activity extends BaseActivity implements IView, View.OnClickListener {

    private IPresenter presenter;

    private TextView txt_mesg;
    private EditText edit_name;
    private EditText edit_pwd;
    private Button btn_submit;

    @Override
    protected void initContentView(Bundle savedInstanceState) {

        setContentView(R.layout.activity_presenter_demo);

        txt_mesg = (TextView)findViewById(R.id.txt_mesg);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_pwd = (EditText)findViewById(R.id.edit_pwd);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        presenter = new Presenter(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btn_submit){
            presenter.performOnClick();
        }
    }

    @Override
    public void setData(String data) {
        txt_mesg.setText(data);
    }

    @Override
    public String getName() {
        return edit_name.getText().toString();
    }

    @Override
    public String getPwd() {
        return edit_pwd.getText().toString();
    }
}
