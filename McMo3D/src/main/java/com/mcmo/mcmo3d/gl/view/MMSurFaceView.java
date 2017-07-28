package com.mcmo.mcmo3d.gl.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.mcmo.mcmo3d.gl.render.GlRender;
import com.mcmo.mcmo3d.gl.render.GlSettings;
import com.mcmo.mcmo3d.gl.render.Scene;
import com.mcmo.mcmo3d.gl.util.DebugUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ZhangWei on 2017/1/20.
 */

public class MMSurFaceView extends GLSurfaceView implements ISurface {
    private static final String TAG = "MMSurFaceView";
    protected boolean mTransparent = false;
    private RenderDelegate mRenderDelegate;
    public MMSurFaceView(Context context) {
        super(context);
        init(context);
//        getHolder().addCallback(this);
    }

    public MMSurFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public void setTransparent(boolean transparent) {
        this.mTransparent = transparent;
    }

    public boolean isTransparent() {
        return mTransparent;
    }

    private void init(Context context){
        setEGLContextClientVersion(2);
        if(mTransparent){//这个设置必须在setRender之前
            setEGLConfigChooser(8,8,8,8,16,0);
            getHolder().setFormat(PixelFormat.TRANSLUCENT);
            setZOrderOnTop(true);
        }

        mRenderDelegate=new RenderDelegate(context);
        setRenderer(mRenderDelegate);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    public void onSettings(GlSettings settings){}
    public void setFrameRate(int rate){
        mRenderDelegate.render.setFrameRate(rate);
    }
    public void setScene(Scene scene){
        mRenderDelegate.render.setCurrentScene(scene);
    }

    //region 生命周期函数(surfaceView)

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        DebugUtil.logDebug("MMSurFaceView","life cycle","SurfaceView attach to window");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DebugUtil.logDebug("MMSurFaceView","life cycle","SurfaceView detached to window");
        mRenderDelegate.render.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        DebugUtil.logDebug("MMSurFaceView","life cycle","SurfaceView created");
        if(mRenderDelegate!=null && mRenderDelegate.isCreated()){
            mRenderDelegate.render.onResume();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        DebugUtil.logDebug("MMSurFaceView","life cycle","SurfaceView sizeChanged width="+w+" height="+h);
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        DebugUtil.logDebug("MMSurFaceView","life cycle","SurfaceView destroyed");
        mRenderDelegate.render.onSurfaceDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //这个方法没被调用
        DebugUtil.logDebug("MMSurFaceView","life cycle","SurfaceView resume");
//        mRenderDelegate.render.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //这个方法没被调用
        DebugUtil.logDebug("MMSurFaceView","life cycle","SurfaceView pause");
//        mRenderDelegate.render.onPause();
    }

    //endregion

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if(mRenderDelegate!=null||mRenderDelegate.render!=null){
            return mRenderDelegate.render.onTouchEvent(event);
//        }else{
//            return super.onTouchEvent(event);
//        }
    }

    @Override
    public void requestRenderUpdate() {
        requestRender();
    }

    private class RenderDelegate implements Renderer{
        private GlRender render;

        public boolean isCreated(){
            if(render!=null&&render.getCurScene()!=null){
                return render.getCurScene().isCreated();
            }
            return false;
        }

        public RenderDelegate(Context context) {
            render=new GlRender(context);
            render.setISurface(MMSurFaceView.this);
            GlSettings settings = new GlSettings();
            onSettings(settings);
            settings.analysis();
            render.setGlSettings(settings);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            render.onSurfaceCreated(gl,config);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            render.onSurfaceChanged(gl, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            render.onDrawFrame(gl);
        }
    }

}
