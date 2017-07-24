package com.mcmo.mcmo3d.gl.util;

/**
 * Created by ZhangWei on 2017/3/21.
 */

public class FPS {
    private int count;
    private long prevMinuteStartTime;
    private float rate;
    private final int OneMinute=60000;

    public void reset(){
        count=0;
        prevMinuteStartTime=System.currentTimeMillis();
        rate=0;
    }

    public void update(){
        count++;
        long time=System.currentTimeMillis();
        long deltaT=(time-prevMinuteStartTime);
        rate=1000.0f*count/deltaT;
        if(deltaT>OneMinute){
            reset();
        }
    }

    public float getRate() {
        return rate;
    }
    public void log(){
        DebugUtil.logDebug("FPS","Log","fps = "+rate);
    }
}
