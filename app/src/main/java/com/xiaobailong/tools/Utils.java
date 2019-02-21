package com.xiaobailong.tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by dongyuangui on 2017/6/2.
 */

public class Utils {
    public static boolean isRightIdNum(String idString) {

        if (idString == null || idString.length() < 15) {
            return false;
        }
        if (idString.length() == 15) {
            String s;
            try {
                s = IdNumberUtil.Convert(idString);
                if (s == null) {
                    return false;
                }
                return IdNumberUtil.getInfof(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (idString.length() == 18) {
            return IdNumberUtil.getInfoe(idString);
        } else if (idString.length() != 15 && idString.length() != 18) {
            return false;
        }
        return true;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static final String APP_PACKAGE_NAME = "com.*.*";//包名

    /**
     * 启动薄荷App
     * @param context
     */
    public static void launchapp(Context context) {
        // 判断是否安装过App，否则去市场下载
        if (isAppInstalled(context, APP_PACKAGE_NAME)) {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(APP_PACKAGE_NAME));
        } else {
            Toast.makeText(context,"未安装app，请先安装",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检测某个应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void nosdcard(Context firstActivity) {
            Toast.makeText(firstActivity, "没有sdcard无法显示文件", Toast.LENGTH_LONG).show();
    }
}
