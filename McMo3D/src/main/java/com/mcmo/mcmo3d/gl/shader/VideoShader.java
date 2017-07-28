package com.mcmo.mcmo3d.gl.shader;

import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.view.Surface;

import com.mcmo.mcmo3d.gl.R;
import com.mcmo.mcmo3d.gl.geometry.graphic.Object3D;

import java.nio.FloatBuffer;


/**
 * Created by ZhangWei on 2017/4/5.
 */

public class VideoShader extends AShader {
    private final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;
    public int mMVPHandler;
    public int mPositionHandler;
    public int mTexCoorHandler;
    private SurfaceTexture surfaceTexture;
    private int mTextureId;

    private Callback mCallback;
    private CallbackTexture mCallbackTexture;

    public VideoShader(Resources resources) {
        super(R.raw.shader_unlit_v, R.raw.shader_video_f, resources);

    }

    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public void setCallbackTexture(CallbackTexture callbackTexture) {
        this.mCallbackTexture = callbackTexture;
    }

    @Override
    public void create() {
        super.create();
        int[] textureId = new int[1];
        GLES20.glGenTextures(1, textureId, 0);//生成纹理id
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, textureId[0]);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);//设置MIN采样方式
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);//设置MAG采样方式
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);//设置S轴拉伸方式
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);//设置T轴拉伸方式
        mTextureId=textureId[0];
        surfaceTexture=new SurfaceTexture(mTextureId);
        // TODO: 2017/7/26 这个地方的实现可能有问题
        if(mCallbackTexture!=null){
            mCallbackTexture.onSurfaceCreate(surfaceTexture);
        }else{
            surface=new Surface(surfaceTexture);
            if(mCallback!=null)
                mCallback.onSurfaceCreate(surface);
        }
    }
    private Surface surface;

    public Surface getSurface() {
        return surface;
    }

    @Override
    protected void getParam() {
        mMVPHandler = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mPositionHandler = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mTexCoorHandler = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
    }

    @Override
    public void draw(float[] mvpMatrix, FloatBuffer vertexBuffer, FloatBuffer textureBuffer, Object3D object3D) {

        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(mMVPHandler, 1, false, mvpMatrix, 0);
        GLES20.glVertexAttribPointer(mPositionHandler, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(mTexCoorHandler,2,GLES20.GL_FLOAT,false,2*4,textureBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        GLES20.glEnableVertexAttribArray(mTexCoorHandler);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES,mTextureId);

        GLES20.glDrawArrays(object3D.glDrawType(), 0,object3D.getVCount());
    }

    @Override
    public void destroy() {
        if(surface!=null){
            surface.release();
            surface=null;
        }
        if(surfaceTexture!=null){
            surfaceTexture.release();
            surfaceTexture=null;
        }
    }

    @Override
    public void update(int refreshFrameRate) {
        super.update(refreshFrameRate);
        surfaceTexture.updateTexImage();
    }
    public interface Callback{
        public void onSurfaceCreate(Surface surface);
    }
    public interface CallbackTexture{
        public void onSurfaceCreate(SurfaceTexture surfaceTexture);
    }
}
