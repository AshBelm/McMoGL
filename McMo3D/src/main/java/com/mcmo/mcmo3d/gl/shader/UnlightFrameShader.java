package com.mcmo.mcmo3d.gl.shader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.mcmo.mcmo3d.gl.R;
import com.mcmo.mcmo3d.gl.geometry.graphic.Object3D;
import com.mcmo.mcmo3d.gl.texture.SimpleTexture;

import java.nio.FloatBuffer;

/**
 * Created by ZhangWei on 2017/2/7.
 */

public class UnlightFrameShader extends AShader{
    public int mMVPHandler;
    public int mPositionHandler;
    public int mTexCoorHandler;
    public int mWHParamHandler;
    public int mIndexHandler;
    public int mFrameIndex=1;
    public SimpleTexture texture;
    public Bitmap bitmap;
    private int orientationCount,verticalCount;
    private int updateRate=10;
    private int count=0;
    public UnlightFrameShader(Resources resources, Bitmap bitmap,int orientation,int vertical) {
        super(R.raw.shader_unlit_frames_v, R.raw.shader_unlit_f, resources);
        this.orientationCount=orientation;
        this.verticalCount=vertical;
        this.bitmap=bitmap;

    }

    @Override
    public void create() {
        super.create();
        texture=new SimpleTexture(bitmap);
        bitmap=null;
    }

    public void nextFrame(){
        mFrameIndex++;
        if(mFrameIndex>=orientationCount*verticalCount){
            mFrameIndex=0;
        }
    }
    public void setFrameIndex(int index) {
        this.mFrameIndex = index;
    }

    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
    }

    public int getUpdateRate() {
        return updateRate;
    }

    @Override
    public void update(int refreshFrameRate) {
        count++;
        if(count>(1.0f*refreshFrameRate/updateRate)){
            count=0;
            nextFrame();
        }

    }

    @Override
    public void destroy() {
        texture.release();
    }

    @Override
    protected void getParam() {
        mMVPHandler = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mPositionHandler = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mTexCoorHandler = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        mWHParamHandler = GLES20.glGetUniformLocation(mProgram,"uWHParam");
        mIndexHandler = GLES20.glGetUniformLocation(mProgram,"uFrameIndex");
    }

    @Override
    public void draw(float[] mvpMatrix, FloatBuffer vertexBuffer, FloatBuffer textureBuffer,Object3D object3D) {
        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(mMVPHandler, 1, false, mvpMatrix, 0);
        GLES20.glUniform2f(mWHParamHandler,orientationCount,verticalCount);
        GLES20.glUniform1f(mIndexHandler,mFrameIndex);
        GLES20.glVertexAttribPointer(mPositionHandler, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(mTexCoorHandler,2,GLES20.GL_FLOAT,false,2*4,textureBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        GLES20.glEnableVertexAttribArray(mTexCoorHandler);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture.getTextureId());

        GLES20.glDrawArrays(object3D.glDrawType(), 0,object3D.getVCount());

    }

}
