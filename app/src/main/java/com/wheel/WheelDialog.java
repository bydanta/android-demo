package com.wheel;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import com.template.app.R;

/**
 * 多列滚动控件
 */
public class WheelDialog {

    private List<WheelView> list = new ArrayList<>();

    public WheelDialog(List<WheelView> list){

        if(list.size() > 0){
            this.list = list;
        }
    }

    /**
     * 监听整个事件
     * **/
    public TosAdapterView.OnItemSelectedListener mListener = new TosAdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {

            ((WheelTextView) view).setTextSize(20);
            int index = Integer.parseInt(view.getTag().toString());
            int count = parent.getChildCount();
            if (index < count - 1) {
                ((WheelTextView) parent.getChildAt(index + 1)).setTextSize(20);
            }
            if (index > 0) {
                ((WheelTextView) parent.getChildAt(index - 1)).setTextSize(20);
            }
            formatData();
        }

        @Override
        public void onNothingSelected(TosAdapterView<?> parent) {

        }
    };

    /**
     * 获取初始数据集合
     * **/
    public NumberAdapter NumberAdapters(Context context, String[] data, Resources resources){

        return  new NumberAdapter(context,data,resources);
    }

    /**
     * 返回选中的数据
     * **/
    public List<Integer> formatData() {

        List<Integer> intList = new ArrayList<>();

        for(int i=0;i<list.size();i++){
            intList.add(list.get(i).getSelectedItemPosition());
        }

        return intList;
    }

    private class NumberAdapter extends BaseAdapter {
        int mHeight = 50;
        String[] mData = null;
        Context context = null;
        Resources resource = null;

        public NumberAdapter(Context context,String[] data, Resources resource) {
            mHeight = Utils.dipToPx(context, mHeight);
            this.mData = data;
            this.context = context;
            this.resource = resource;
        }

        @Override
        public int getCount() {
            return (null != mData) ? mData.length : 0;
        }

        @Override
        public View getItem(int arg0) {
            return getView(arg0, null, null);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WheelTextView textView = null;

            ColorStateList csl = resource.getColorStateList(R.color.wheel_pressed);

            if (null == convertView) {
                convertView = new WheelTextView(context);
                convertView.setLayoutParams(new TosGallery.LayoutParams(-1,mHeight));
                textView = (WheelTextView) convertView;
                textView.setTextSize(20);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(csl);
            }

            String text = mData[position];
            if (null == textView) {
                textView = (WheelTextView) convertView;
            }
            textView.setText(text);
            return convertView;
        }
    }

}
