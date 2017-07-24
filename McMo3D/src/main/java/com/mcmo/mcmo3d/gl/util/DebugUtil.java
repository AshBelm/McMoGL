package com.mcmo.mcmo3d.gl.util;

import android.util.Log;

/**
 * Created by ZhangWei on 2016/12/16.
 */

public class DebugUtil {
    public static final boolean DEBUG = true;

    public static void logError(String className, String methodName, String errorMsg) {
        if (DEBUG)
            Log.e("DebugUtil", className + "#" + methodName + " : " + errorMsg);
    }

    public static void systemln(String className, String methodName, String msg) {
        if (DEBUG)
            System.out.println(className + "#" + methodName + " : " + msg);
    }

    public static void logDebug(String className, String methodName, String errorMsg) {
        if (DEBUG)
            Log.d("DebugUtil", className + "#" + methodName + " : " + errorMsg);
    }
    public static void logInfo(String className, String methodName, String errorMsg) {
        if (DEBUG)
            Log.i("DebugUtil", className + "#" + methodName + " : " + errorMsg);
    }
}
