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

public class AmbientTextureShader extends AShader{
    public int mMVPHandler;
    public int mPositionHandler;
    public int mTexCoorHandler;
    public SimpleTexture texture;
    private Bitmap bitmap;
    public AmbientTextureShader(Resources resources, Bitmap bitmap) {
        super(R.raw.shader_ambient_v, R.raw.shader_ambient_f, resources);
        this.bitmap=bitmap;

    }

    @Override
    public void create() {
        super.create();
        texture=new SimpleTexture(bitmap);
        bitmap=null;
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
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture.getTextureId());

        GLES20.glDrawArrays(object3D.glDrawType(), 0,object3D.getVCount());
    }

    @Override
    public void destroy() {
        texture.release();
    }

}
