package com.mcmo.mcmo3d.gl.render;

import android.graphics.Color;

/**
 * Created by ZhangWei on 2017/7/24.
 */

public class GlSettings {
    public int clearColor = 0xffffffff;
    protected float clearColorRed = 1.0f;
    protected float clearColorGreen = 1.0f;
    protected float clearColorBlue = 1.0f;
    protected float clearColorAlpha = 1.0f;

    /**
     * 设置背景色
     *
     * @param color
     */
    public void setClearColor(int color) {
        clearColor = color;
        analysisColor();
    }

    /**
     * 设置背景色
     * @param red   Red component [0..255] of the color
     * @param green Green component [0..255] of the color
     * @param blue  Blue component [0..255] of the color
     * @param alpha Alpha component [0..255] of the color
     */
    public void setClearColor(int red, int green, int blue, int alpha) {
        clearColorRed = red/255.0f;
        clearColorGreen = green/255.0f;
        clearColorBlue = blue/255.0f;
        clearColorAlpha = alpha;
        clearColor = Color.argb(red,green,blue,alpha);
    }


    public void analysisColor() {
        int red = Color.red(clearColor);
        int green = Color.green(clearColor);
        int blue = Color.blue(clearColor);
        int alpha = Color.alpha(clearColor);
        clearColorRed =  red/255.0f;
        clearColorGreen =  green/255.0f;
        clearColorBlue =  blue/255.0f;
        clearColorAlpha = alpha/255.0f;
    }
}
