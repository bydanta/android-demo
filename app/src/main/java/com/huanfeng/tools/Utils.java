package com.huanfeng.tools;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.template.app.R;
import com.template.base.MainApplication;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class Utils {

    public static ImageLoader imageLoader = null;

    public static void print(Object... args) {
        if (args != null && args.length > 0) {
            String str = "";
            for (Object obj :
                    args) {
                str += obj + ", ";
            }
            if (str.length() > 0) {
                str = str.substring(0, str.length() - 1);
                Log.v("print", str);
            }
        }
    }

    public static void printStackTrace() {
        new Exception().printStackTrace();
    }

    public static int parseInt(Object obj) {
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception ex) {

        }
        return 0;
    }

    public static long parseLong(Object obj) {
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception ex) {

        }
        return 0;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 获取设备唯一ID
     **/
    public static String getDrvierID(Context context) {
        UUID uuid = null;
        final SharedPreferences prefs = context.getSharedPreferences("device_id.xml", 0);
        final String id = prefs.getString("device_id", null);
        if (id != null) {
            uuid = UUID.fromString(id);
        } else {
            final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            try {
                if (!"9774d56d682e549c".equals(androidId)) {
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                } else {
                    final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            prefs.edit().putString("device_id", uuid.toString()).commit();
        }
        return uuid.toString();
    }

    /**
     * MD5数据
     * *
     */
    public static String MD5(String string, int len) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder builder = new StringBuilder("");
            for (byte aB : b) {
                i = aB;
                if (i < 0) i += 256;
                if (i < 16)
                    builder.append("0");
                builder.append(Integer.toHexString(i));
            }
            if (len == 32) {
                return builder.toString();
            } else if (len == 16) {
                return builder.toString().substring(8, 24);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static double formatDecimal(double d, int len) {
        int v = (int) Math.pow(10, len);
        return Math.round(d * v) * 1.0 / v;
    }

    public static String formatDateTimeString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static boolean isStringNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 序列化 反序列化
     * *
     */
    public static Gson getGson() {
        return new Gson();
    }

    /**
     * 消息提示
     * *
     */
    public static void Toast(String str) {

        Toast.makeText(MainApplication.getContext(), str, Toast.LENGTH_SHORT).show();
    }

    public static void Toast(Context context, String str) {

        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * Bitmap转二进制
     **/
    public static byte[] GetBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * 二进制转Bitmap
     **/
    public static Bitmap GetBitmapFromByte(byte[] temp) {
        if (temp != null) {
            return BitmapFactory.decodeByteArray(temp, 0, temp.length);
        } else {
            return null;
        }
    }

    /**
     * 转换月份
     * *
     */
    public static String ConverMonth(int str, String str2) {

        if (str2.length() == 0) {

            switch (str) {

                case 1:
                    return "一月";
                case 2:
                    return "二月";
                case 3:
                    return "三月";
                case 4:
                    return "四月";
                case 5:
                    return "五月";
                case 6:
                    return "六月";
                case 7:
                    return "七月";
                case 8:
                    return "八月";
                case 9:
                    return "九月";
                case 10:
                    return "十月";
                case 11:
                    return "十一月";
                case 12:
                    return "十二月";
            }
        } else {

            switch (str2) {

                case "01":
                    return "一月";
                case "02":
                    return "二月";
                case "03":
                    return "三月";
                case "04":
                    return "四月";
                case "05":
                    return "五月";
                case "06":
                    return "六月";
                case "07":
                    return "七月";
                case "08":
                    return "八月";
                case "09":
                    return "九月";
                case "10":
                    return "十月";
                case "11":
                    return "十一月";
                case "12":
                    return "十二月";
            }
        }

        return "";
    }

    /**
     * 取当前时间
     * *
     */
    public static String GetDateNow() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());
    }
    public static String GetDateTimeNow() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        return df.format(new Date());
    }

    /**
     * 字符串转时间戳
     **/
    public static long GetStringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / milli;
    }
    public static long GetStringToDateTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / milli;
    }

    /**
     * 日期格式转换
     * *
     */
    private static long milli = 1000L;//为了匹配服务端以秒计算
    public static String LongTimeConvert(long str) {

        str = str * milli;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(str);
        return df.format(date);
    }

    public static String LongTimeConvertDate(long str) {

        str = str * milli;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(str);
        return df.format(date);
    }

    public static String LongTimeConvertMonthDay(long str) {

        str = str * milli;
        DateFormat df = new SimpleDateFormat("MM-dd");
        Date date = new Date(str);
        return df.format(date);
    }

    public static String LongTimeConvertMonth(long str) {

        str = str * milli;
        DateFormat df = new SimpleDateFormat("MM");
        Date date = new Date(str);
        return df.format(date);
    }

    public static String LongTimeConvertDay(long str) {

        str = str * milli;
        DateFormat df = new SimpleDateFormat("dd");
        Date date = new Date(str);
        return df.format(date);
    }

    public static String LongTimeConvertTime(long str) {

        str = str * milli;
        DateFormat df = new SimpleDateFormat("HH:mm");
        Date date = new Date(str);
        return df.format(date);
    }

    public static String LongTimeConvertHours(long str) {

        str = str * milli;
        DateFormat df = new SimpleDateFormat("HH");
        Date date = new Date(str);
        return df.format(date);
    }

    /**
     * 添加小时,返回小时
     * *
     */
    public static String AddHours(String date, String hours) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(date));
            cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(hours));
            return cal.getTime().getHours() + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 时间前推或后推分钟,其中str2表示分钟
     */
    public static String GetPreTimeM(String str, String str2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String mydate1 = "";
        try {

            Date date1 = format.parse(str);
            long Time = (date1.getTime() / 1000) + Integer.parseInt(str2) * 60;
            date1.setTime(Time * 1000);
            mydate1 = format.format(date1);
        } catch (Exception e) {

        }
        return mydate1;
    }

    /**
     * 时间前推或后推小时,其中str2表示小时
     */
    public static String GetPreTimeH(String str, String str2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String mydate1 = "";
        try {

            Date date1 = format.parse(str);
            long Time = (date1.getTime() / 1000) + Integer.parseInt(str2) * 60 * 60;
            date1.setTime(Time * 1000);
            mydate1 = format.format(date1);
        } catch (Exception e) {

        }
        return mydate1;
    }

    /**
     * 对比时间大小
     * *
     */
    public static int GetDateCompareTo(String str, String str2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {

            c1.setTime(df.parse(str));
            c2.setTime(df.parse(str2));

        } catch (ParseException e) {
            return 0;
        }

        //str = str2 返回0
        //str < str2 返回-1
        //str > str2 返回1
        return c1.compareTo(c2);
    }

    /**
     * 返回相差的小时
     **/
    public static long GetDifferHours(String sd1, String sd2) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            long interval = sdf.parse(sd1).getTime() - sdf.parse(sd2).getTime();
            interval = interval > 0 ? interval : interval * -1;
            interval /= 1000 * 60; //分
            long hour = interval / 60;
            long minute = interval % 60;

            return hour;

        } catch (ParseException e) {

            return 0;
        }
    }

    /**
     * 返回相差的分钟
     **/
    public static long GetDifferMinute(String sd1, String sd2) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {

            long interval = sdf.parse(sd1).getTime() - sdf.parse(sd2).getTime();
            interval = interval > 0 ? interval : interval * -1;
            interval /= 1000 * 60; //分
            long hour = interval / 60;
            return interval;

        } catch (ParseException e) {

            return 0;
        }
    }


    /**
     * 转为double为两位
     * *
     */
    public static double ConvertDouble(double str) {

        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(df.format(str));
    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isAppToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;

    }

    /**
     * 获取设备版本号
     * *
     */
    public static String getAppVersionName(Context context) {

        String versionName = "";

        try {

            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;

            if (versionName == null || versionName.length() <= 0) {
                return "";
            }

        } catch (Exception ignored) {

        }
        return versionName;
    }

    /**
     * 设置全局imageloader对象
     * *
     */
    public static ImageLoader getImageLoader(Context context) {

        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnFail(android.R.drawable.ic_menu_close_clear_cancel)
                .showImageOnLoading(R.mipmap.icon_empty)//正在加载
                .showImageForEmptyUri(R.mipmap.icon_stub)//图片为空
                .showImageOnFail(R.mipmap.icon_error)//加载失败
                .resetViewBeforeLoading(true)//设置图片在加载前是否重置、复位
                .delayBeforeLoading(10)//下载前的延迟时间
                .cacheInMemory(false)//内存缓存
                .cacheOnDisk(true)//磁盘缓存
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) //设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888)//设置图片的解码类型
                .displayer(new SimpleBitmapDisplayer())//直角或圆角 new RoundedBitmapDisplayer(20)
                .handler(new Handler())//default
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800)//内存缓存文件的最大长宽
                .diskCacheExtraOptions(480, 800, null).threadPoolSize(3)//本地缓存的详细信息(缓存的最大长宽)，最好不要设置这个
                .threadPriority(Thread.NORM_PRIORITY)//设置当前线程的优先级
                .tasksProcessingOrder(QueueProcessingType.FIFO)//default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024))//可以通过自己的内存缓存实现
                .memoryCacheSize(5 * 1024 * 1024)//内存缓存的最大值
                .memoryCacheSizePercentage(13)// default
                .diskCacheSize(50 * 1024 * 1024)//50 Mb sd卡(本地)缓存的最大值
                .diskCacheFileCount(100)//// 可以缓存的文件数量
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())//为使用HASHCODE对UIL进行加密命名
                .imageDownloader(new BaseImageDownloader(context))//default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())//default
                .writeDebugLogs()//打印debug log
                .defaultDisplayImageOptions(options).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        return imageLoader;
    }

    /**
     * ListView显示全部内容
     **/
    public static void setListViewHeightBasedOnChildren(ListView listView) {

        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
