package com.mcmo.mcmo3d.gl.render;

import android.graphics.Color;

/**
 * Created by ZhangWei on 2017/7/24.
 */

public class GlSettings {
    public int clearColor = 0xffffffff;
    protected float clearColorRead = 1.0f;
    protected float clearColorGreen = 1.0f;
    protected float clearColorBlue = 1.0f;
    protected float clearColorAlpha = 1.0f;

    public  void analysis(){
        clearColorRead = Color.red(clearColor);
        clearColorGreen = Color.green(clearColor);
        clearColorBlue = Color.blue(clearColor);
    }
}
