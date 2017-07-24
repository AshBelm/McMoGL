package com.mcmo.mcmo3d.gl.shader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.mcmo.mcmo3d.gl.R;
import com.mcmo.mcmo3d.gl.geometry.graphic.Object3D;
import com.mcmo.mcmo3d.gl.material.TextureManager;
import com.mcmo.mcmo3d.gl.texture.SimpleTexture;

import java.nio.FloatBuffer;

/**
 * Created by ZhangWei on 2017/2/24.
 */

public class AlphaFuseShader extends AShader {
    private int mMVPHandler;
    private int mPositionHandler;
    private int mTexCoorHandler;
    private int mTexOneHandler;
    private int mTexTwoHandler;
    private SimpleTexture textureOne;
    private SimpleTexture textureTwo;
    private float fuseFactor=0.0f;
    private int mFuseFactorHandler;
    private int statu=STATU_WAIT;
    public static final int STATU_REDUCE = 1;
    public static final int STATU_PLUS = 2;
    public static final int STATU_WAIT = 3;
    private Bitmap bitmap;
    private Bitmap bitmapOne,bitmapTwo;

    public AlphaFuseShader(Resources resources, Bitmap resIdOne, Bitmap resIdTwo) {
        super(R.raw.shader_alphe_fuse_v, R.raw.shader_alphe_fuse_f, resources);
        bitmapOne=resIdOne;
        bitmapTwo=resIdTwo;
    }

    @Override
    public void create() {
        super.create();
        textureOne=new SimpleTexture(bitmapOne);
        textureTwo=new SimpleTexture(bitmapTwo);
        bitmapOne=null;
        bitmapTwo=null;

    }

    @Override
    protected void getParam() {
        mMVPHandler = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mPositionHandler = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mTexCoorHandler = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        mFuseFactorHandler = GLES20.glGetUniformLocation(mProgram, "fuseFactor");
        mTexOneHandler = GLES20.glGetUniformLocation(mProgram,"sTextureOne");
        mTexTwoHandler = GLES20.glGetUniformLocation(mProgram,"sTextureTwo");
    }

    @Override
    public void draw(float[] mvpMatrix, FloatBuffer vertexBuffer, FloatBuffer textureBuffer, Object3D object3D) {
        //生成新纹理
        if(bitmap!=null){
            if(statu==STATU_REDUCE){
                GLES20.glDeleteTextures(1,new int[]{textureOne.getTextureId()},0);
                textureOne=new SimpleTexture(bitmap);
            }else{
                GLES20.glDeleteTextures(1,new int[]{textureTwo.getTextureId()},0);
                textureTwo=new SimpleTexture(bitmap);
            }
            bitmap=null;
        }
        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(mMVPHandler, 1, false, mvpMatrix, 0);
        GLES20.glUniform1f(mFuseFactorHandler,fuseFactor);
        GLES20.glVertexAttribPointer(mPositionHandler, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(mTexCoorHandler, 2, GLES20.GL_FLOAT, false, 2 * 4, textureBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        GLES20.glEnableVertexAttribArray(mTexCoorHandler);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureOne.getTextureId());
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureTwo.getTextureId());
        GLES20.glUniform1i(mTexOneHandler,0);
        GLES20.glUniform1i(mTexTwoHandler,1);

        GLES20.glDrawArrays(object3D.glDrawType(), 0, object3D.getVCount());
    }

    public void changeBitmap(Bitmap bitmap) {
        this.bitmap=bitmap;
        if (fuseFactor >= 0.5f) {
            statu = STATU_REDUCE;
            fuseFactor=1.0f;
        } else {
            statu = STATU_PLUS;
            fuseFactor=0.0f;
        }
    }

    @Override
    public void update(int refreshFrameRate) {
        //变换
        if (statu == STATU_PLUS) {
            fuseFactor += 0.02f;
            if (fuseFactor >= 1.0f) {
                statu = STATU_WAIT;
            }
        } else if (statu == STATU_REDUCE) {
            fuseFactor -= 0.02f;
            if (fuseFactor <= 0.0f) {
                statu = STATU_WAIT;
            }
        }
    }

    @Override
    public void destroy() {
        TextureManager.getInstance().removeTexture(textureOne);
        TextureManager.getInstance().removeTexture(textureTwo);
    }
}
