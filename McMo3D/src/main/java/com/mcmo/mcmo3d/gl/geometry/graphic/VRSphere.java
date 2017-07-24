package com.mcmo.mcmo3d.gl.geometry.graphic;

import android.content.Context;

/**
 * Created by ZhangWei on 2017/3/8.
 */

public class VRSphere extends Sphere {
    public VRSphere(Context context, float r) {
        super(context, r);
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
