package com.mcmo.mcmo3d.gl.geometry.graphic;

import android.content.Context;

/**
 * 用于实现天空宇的球形,也可以用来开发VR全景视频和图片
 * Created by ZhangWei on 2017/3/8.
 */

public class SkySphere extends Sphere {
    public SkySphere( float r) {
        super(r);
    }

    @Override
    public float[] generateTexCoor(int bw, int bh) {
        float[] result= super.generateTexCoor(bw, bh);
        for (int i = 0; i < result.length; i+=2) {
            result[i]=1-result[i];
        }
        return result;
    }
}
