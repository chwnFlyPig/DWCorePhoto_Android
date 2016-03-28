package davidwang.tm.tools;

import android.content.Context;
import android.widget.Toast;

import davidwang.tm.dwcorephoto.BaseApplication;

public class ToastUtils {

    private ToastUtils() {
    }

    private static void show(Context context, int resId, int duration) {
        if (context != null) {
            try {
                Toast.makeText(context, resId, duration).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void show(Context context, String message, int duration) {
        if (context != null && !StringUtil.isEmpty(message)) {
            Toast.makeText(context, message, duration).show();
        }
    }

    public static void showShort(int resId) {
        try {
            Toast.makeText(BaseApplication.getAppContext(), resId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showShort(String message) {
        if (!StringUtil.isEmpty(message)) {
            try {
                Toast.makeText(BaseApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showLong(int resId) {
        try {
            Toast.makeText(BaseApplication.getAppContext(), resId, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLong(String message) {
        if (!StringUtil.isEmpty(message)) {
            try {
                Toast.makeText(BaseApplication.getAppContext(), message, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
