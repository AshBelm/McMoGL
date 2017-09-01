package com.mcmo.mcmo3d.gl.render;

import android.content.Context;
import android.opengl.GLES20;
import android.view.MotionEvent;

import com.mcmo.mcmo3d.gl.util.DebugUtil;
import com.mcmo.mcmo3d.gl.util.FPS;
import com.mcmo.mcmo3d.gl.view.ISurface;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ZhangWei on 2017/1/20.
 */

public class GlRender implements IRender {
    private byte[] lock=new byte[0];
    protected Context context;
    private int mSurfaceWidth,mSurfaceHeight;
    protected ISurface mSurface;
    private Scene mCurScene;
    private GlSettings mGlSettings;
    //帧数
    private ScheduledExecutorService mTimer;
    private int mFrameRate = 30;
    private FPS fps;


    public GlRender(Context context) {
        this.context = context;
        fps=new FPS();
    }

    public Context getContext() {
        return context;
    }

    public void setISurface(ISurface surface) {
        this.mSurface = surface;
    }

    public GlSettings getGlSettings() {
        return mGlSettings;
    }

    public void setGlSettings(GlSettings glSettings) {
        this.mGlSettings = glSettings;
    }

    public void setCurrentScene(Scene scene) {
        mCurScene = scene;
        mCurScene.setRender(this);
    }

    public void setFrameRate(int rate) {
        if (rate < 0) {
            throw new RuntimeException("Frame Rate can not be negative");
        }
        this.mFrameRate = rate;
    }

    public int getFrameRate() {
        return mFrameRate;
    }

    public int getHeight() {
        return mSurfaceHeight;
    }

    public int getWidth() {
        return mSurfaceWidth;
    }

    private synchronized void startRender() {
        if(!mCurScene.isCreated()){
            return;
        }
        if (mTimer == null) {
            mTimer = Executors.newScheduledThreadPool(1);
            mTimer.scheduleAtFixedRate(new RequestRenderTask(), 0, 1000 / mFrameRate, TimeUnit.MILLISECONDS);
            fps.reset();
            DebugUtil.logDebug("GLRender","startRender","start timer  rate = "+mFrameRate);
        }else{
            DebugUtil.logDebug("GLRender","startRender","timer already started");
        }
    }
    private boolean stopRendering(){
        if(mTimer!=null){
            mTimer.shutdownNow();
            mTimer=null;
            DebugUtil.logDebug("GLRender","stopRendering","stop timer");
            return true;
        }
        return false;
    }
    public Scene getCurScene() {
        return mCurScene;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        DebugUtil.logDebug("GLRender","life cycle","render created");

        GLES20.glClearColor(mGlSettings.clearColorRed, mGlSettings.clearColorGreen, mGlSettings.clearColorBlue, mGlSettings.clearColorAlpha);
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        if (mCurScene != null) {
            mCurScene.onPreCreate(context);
            mCurScene.onCreate(context);
            mCurScene.onPostCreate(context);
        }
        if(mFrameRate>0){
            startRender();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        DebugUtil.logDebug("GLRender","life cycle","render changed width="+width+" h="+height);
        this.mSurfaceWidth=width;
        this.mSurfaceHeight=height;
        GLES20.glViewport(0, 0, width, height);
        if (mCurScene != null) {
            mCurScene.onSizeChange(width, height);
        }
        startRender();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if(mCurScene!=null){
            mCurScene.executeGLTask();
        }
        synchronized (lock){
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            if (mCurScene != null) {
                mCurScene.onRender(mFrameRate,fps.getRate());
            }
            fps.update();
//            fps.log();
        }
    }

    @Override
    public void onResume() {
        DebugUtil.logDebug("GLRender","life cycle","render onResume");
        startRender();
        if(mCurScene!=null){
            mCurScene.onResume();
        }
    }

    @Override
    public void onSurfaceDestroy() {
        DebugUtil.logDebug("GLRender","life cycle","render surfaceDestroy");
        stopRendering();
        if(mCurScene!=null){
            mCurScene.onPause();
        }
    }

    @Override
    public void onDestroy() {
        DebugUtil.logDebug("GLRender","life cycle","render Destroy");
        stopRendering();
        if(mCurScene!=null){
            mCurScene.onDestroy();
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        synchronized (lock){
            if(mCurScene!=null){
                return mCurScene.onTouchEvent(event);
            }
            return false;
        }
    }

    private class RequestRenderTask implements Runnable {

        @Override
        public void run() {
            if (mSurface != null) {
                mSurface.requestRenderUpdate();
            }
        }
    }
}
