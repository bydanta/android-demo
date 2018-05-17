package com.template.demo;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.huanfeng.tools.BitmapTools;
import com.huanfeng.tools.Utils;
import com.template.app.R;
import com.template.base.BaseActivity;
import com.template.function.ActionSheetDialog;
import com.template.function.AlertDialog;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Album_Demo_Activity extends BaseActivity implements OnClickListener {

    private Button btn1;
    private Button btn2;
    private ImageView img;

    //获取相机 图库变量
    private static final int PHOTO_WITH_DATA = 18;//从SD卡中得到图片
    private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片
    private Uri imgUri;
    private Bitmap curr_bitmap;

    @Override
    protected void initContentView(Bundle savedInstanceState) {

        setContentView(R.layout.album_demo);
        initView();
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.img);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                doTakePhoto();
                break;
            case R.id.btn2:
                doPickPhotoFromGallery();
                break;
            default:
                break;
        }
    }

    //**************************************** 获取图片代码段 ***********************************************

    //从相册获取图片
    private void doPickPhotoFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/png");
        startActivityForResult(intent, PHOTO_WITH_DATA);//取得相片后返回到本画面
    }

    //拍照获取相片
    private void doTakePhoto() {

        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, String.valueOf(System.currentTimeMillis()) + ".png");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            imgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);//保存相片方向
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);//保存相片地址
            startActivityForResult(intent, PHOTO_WITH_CAMERA);
        }
        catch (Exception ex){
            print(ex.getMessage());
        }
    }

    //选择相册照片或拍照之后会返回处理 其他ACTIVITY返回信息处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_WITH_CAMERA:
                    //拍照获取图片
                    if (data != null && data.getData() != null) {
                        imgUri = data.getData();//照片的原始资源地址
                    }
                    if (imgUri != null && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { //是否有SD卡
                        String path = BitmapTools.getPathFromUri(topActivity, imgUri);
                        int orientation = BitmapTools.readPictureDegree(path);//获取旋转角度
                        new DismissGoodsBitmapAsync().execute(PHOTO_WITH_CAMERA, orientation);//异步操作
                    } else {
                        Utils.Toast(Album_Demo_Activity.this , "检测到没有SD卡");
                    }
                    break;
                case PHOTO_WITH_DATA: {
                    //从图库中选择图片
                    if (data != null && data.getData() != null) {
                        imgUri = data.getData();//照片的原始资源地址
                    }
                    if (imgUri != null) {
                        new DismissGoodsBitmapAsync().execute(PHOTO_WITH_DATA, imgUri);//异步才做
                    } else {
                        Utils.Toast(Album_Demo_Activity.this , "图库图片获取错误");
                    }
                    break;
                }
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //异步处理上传的图片
    public class DismissGoodsBitmapAsync extends AsyncTask<Object, Void, Bitmap> {

        Bitmap smallBitmap = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            int bitmapType = (int) params[0];
            switch (bitmapType) {
                case PHOTO_WITH_CAMERA://相机图片
                    Bitmap bit;
                    int orientation = (int) params[1];
                    try {

                        //获取图像
                        bit = MediaStore.Images.Media.getBitmap(Album_Demo_Activity.this.getContentResolver(), imgUri);
                        if (orientation > 0) {
                            bit = BitmapTools.rotateBitmap(orientation, bit);
                        }
                        assert bit != null;
                        smallBitmap = BitmapTools.resizeImage(bit, 500, 300);

                    } catch (IOException e) {

                        Utils.Toast(Album_Demo_Activity.this ,e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case PHOTO_WITH_DATA://图库图片
                    Uri bitmapUri = (Uri) params[1];
                    try {

                        smallBitmap = BitmapTools.getScaleBitmapByUri(Album_Demo_Activity.this, bitmapUri, 500);

                    } catch (FileNotFoundException e) {

                        Utils.Toast(Album_Demo_Activity.this ,e.getMessage());
                        e.printStackTrace();
                    }
                    break;
            }
            return smallBitmap;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (smallBitmap != null) {
                curr_bitmap = smallBitmap;
                img.setImageBitmap(curr_bitmap);

            } else {
                Utils.Toast(Album_Demo_Activity.this , "图片没有获取成功");
            }
        }
    }
}
