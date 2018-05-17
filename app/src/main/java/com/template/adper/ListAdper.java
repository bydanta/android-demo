package com.template.adper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huanfeng.tools.Utils;
import com.template.app.R;
import com.template.bean.Test;
import com.template.function.APP;

import java.util.ArrayList;
import java.util.HashMap;


public class ListAdper extends BaseAdapter {

    private Context context;
    private ArrayList<Test> listItem;
    public ListAdper(Context context, ArrayList<Test> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @Override
    public int getCount() {
        return listItem == null ? 0 : listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Test map = listItem.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list, null);
        }

        ImageView logo = (ImageView) convertView.findViewById(R.id.img_logo);
        TextView title = (TextView) convertView.findViewById(R.id.txt_title);
        TextView content = (TextView) convertView.findViewById(R.id.txt_content);

        Utils.imageLoader.displayImage(map.getImg(), logo);
        title.setText(map.getTitle());
        content.setText(map.getContent());

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }
}

