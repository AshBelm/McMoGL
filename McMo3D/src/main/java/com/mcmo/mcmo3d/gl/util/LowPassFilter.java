package com.mcmo.mcmo3d.gl.util;

/**
 * Created by Qing on 2016/6/1.
 */
public class LowPassFilter {
    protected float matrixAlpha = 0.15f;
    protected float gestureAlpha = 0.8f;

    private static class LowPassFilterHolder {
        private static final LowPassFilter INSTANCE = new LowPassFilter();
    }

    private LowPassFilter() {
    }

    public static final LowPassFilter getInstance() {
        return LowPassFilterHolder.INSTANCE;
    }

    public float[] filter(float[] oldData,float[] newData){
        for(int index = 0; index < oldData.length; index++){
            newData[index] = oldData[index] + matrixAlpha*(newData[index]-oldData[index]);
        }
        return newData;
    }

    public double filter(double oldData,double newData){
        newData = oldData + gestureAlpha*(newData-oldData);
        return newData;
    }
}
