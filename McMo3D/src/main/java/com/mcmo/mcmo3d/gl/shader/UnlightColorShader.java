package com.mcmo.mcmo3d.gl.shader;

import android.content.res.Resources;
import android.graphics.Color;
import android.opengl.GLES20;

import com.mcmo.mcmo3d.gl.R;
import com.mcmo.mcmo3d.gl.geometry.graphic.Object3D;

import java.nio.FloatBuffer;

/**
 * Created by ZhangWei on 2017/2/9.
 */

public class UnlightColorShader extends AShader {
    private int color_a, color_r, color_g, color_b;
    private float a,r,g,b;
    private int uMVPHandler;
    private int aColorHandler;
    private int aPositionHandler;

    public UnlightColorShader(Resources resources) {
        super(R.raw.shader_unlit_color_v, R.raw.shader_unlit_color_f, resources);
    }

    public void setColor(int color) {
        color_a = Color.alpha(color);
        color_r = Color.red(color);
        color_g = Color.green(color);
        color_b = Color.blue(color);
        a =color_a / 255.0f;
        a = 1-a;
        r = color_r / 255.0f;
        g = color_g / 255.0f;
        b = color_b / 255.0f;
    }

    public void setColor(int a, int r, int g, int b) {
        color_a = a;
        color_r = r;
        color_g = g;
        color_b = b;
    }
    public int getBackgroundColor(){
        return Color.argb(color_a,color_r,color_g,color_b);
    }
    @Override
    protected void getParam() {
        uMVPHandler = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        aColorHandler = GLES20.glGetUniformLocation(mProgram, "aColor");
        aPositionHandler = GLES20.glGetAttribLocation(mProgram, "aPosition");
    }

    @Override
    public void draw(float[] mvpMatrix, FloatBuffer vertexBuffer, FloatBuffer textureBuffer, Object3D object3D) {
        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(uMVPHandler, 1, false, mvpMatrix, 0);
        GLES20.glUniform4f(aColorHandler, r, g, b, a);
        GLES20.glVertexAttribPointer(aPositionHandler, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES20.glEnableVertexAttribArray(aPositionHandler);
        GLES20.glDrawArrays(object3D.glDrawType(),0, object3D.getVCount());
    }

    @Override
    public void destroy() {

    }
}
