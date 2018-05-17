package com.template.function;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.template.app.R;

public class ProgersssDialog extends Dialog {
    private ImageView img;
    private TextView txt;

    public ProgersssDialog(Context context) {

        super(context, R.style.progress_dialog);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_progress, null);
        img = (ImageView) view.findViewById(R.id.progress_dialog_img);
        txt = (TextView) view.findViewById(R.id.progress_dialog_txt);

        Animation anim = AnimationUtils.loadAnimation(context, R.anim.loading_dialog_progressbar);
        img.setAnimation(anim);
        txt.setText("正在加载数据...");

        setContentView(view);
        show();
    }

    public ProgersssDialog(Context context, String str) {

        super(context, R.style.progress_dialog);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_progress, null);
        img = (ImageView) view.findViewById(R.id.progress_dialog_img);
        txt = (TextView) view.findViewById(R.id.progress_dialog_txt);

        Animation anim = AnimationUtils.loadAnimation(context, R.anim.loading_dialog_progressbar);
        img.setAnimation(anim);
        txt.setText(str);

        setContentView(view);
        show();
    }

    public void setMsg(String msg) {
        txt.setText(msg);
    }

    public void setMsg(int msgId) {
        txt.setText(msgId);
    }

    public void stop() {
        dismiss();
    }
}