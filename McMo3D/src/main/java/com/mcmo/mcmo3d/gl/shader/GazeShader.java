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

public class GazeShader extends AShader {
    private int color_a, color_r, color_g, color_b;
    private int color_al, color_rl, color_gl, color_bl;
    private float a,r,g,b;
    private float al,rl,gl,bl;
    private int uMVPHandler;
    private int aColorHandler;
    private int aPositionHandler;
    private int aRadiusHandler;
    private float mRadiusFactor;
    private float aLoadingDegree;
    private int aDegreeHandler;
    private int aLoadingColorHandler;

    public GazeShader(Resources resources) {
        super(R.raw.shader_gaze_v, R.raw.shader_unlit_color_f, resources);
    }

    public void setRadiusFactor(float factor) {
        this.mRadiusFactor = factor;
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
    public void setLoadingColor(int color){
        color_al = Color.alpha(color);
        color_rl = Color.red(color);
        color_gl = Color.green(color);
        color_bl = Color.blue(color);
        al = color_al/255.0f;
        rl = color_rl/255.0f;
        gl = color_gl/255.0f;
        bl = color_bl/255.0f;
        al = 1-al;
    }
    public void setProgress(float progress){
        if(progress<0){
            aLoadingDegree=0;
        }else if(progress>100){
            aLoadingDegree=360;
        }else{
            aLoadingDegree=3.6f * progress;
        }
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
        aLoadingColorHandler = GLES20.glGetUniformLocation(mProgram, "aLoadingColor");
        aRadiusHandler = GLES20.glGetUniformLocation(mProgram,"aRadiusFactor");
        aDegreeHandler = GLES20.glGetUniformLocation(mProgram,"aLoadingDegree");
        aPositionHandler = GLES20.glGetAttribLocation(mProgram, "aPosition");
    }

    @Override
    public void draw(float[] mvpMatrix, FloatBuffer vertexBuffer, FloatBuffer textureBuffer, Object3D object3D) {
        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(uMVPHandler, 1, false, mvpMatrix, 0);;
        GLES20.glUniform4f(aColorHandler, r, g, b, a);
        GLES20.glUniform4f(aLoadingColorHandler,rl,gl,bl,al);
        GLES20.glUniform1f(aRadiusHandler, mRadiusFactor);
        GLES20.glUniform1f(aDegreeHandler, aLoadingDegree);
        GLES20.glVertexAttribPointer(aPositionHandler, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES20.glEnableVertexAttribArray(aPositionHandler);
        GLES20.glDrawArrays(object3D.glDrawType(),0, object3D.getVCount());
    }


    @Override
    public void destroy() {

    }
}
