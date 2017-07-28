package com.mcmo.mcmo3d.gl.geometry.graphic;

import android.opengl.GLES20;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by ZhangWei on 2017/3/10.
 */

public class Ring extends Object3D {
    private int segment = 20;//分段默认是20段
    private float rInner;
    private float rOuter;
    private int vCount;

    public Ring(float radiusOuter, float radiusInner, int segment) {
        if (segment < 3) {
            throw new RuntimeException("can generate a ring when segment  = " + segment);
        }
        this.rOuter = radiusOuter;
        this.rInner = radiusInner;
        this.segment = segment;
        vCount = 6 * (segment+1);
    }

    public Ring(float radiusOuter, float radiusInner) {
        this(radiusOuter, radiusInner, 20);
    }

    @Override
    public int glDrawType() {
        return GLES20.GL_TRIANGLES;
    }

    @Override
    protected float[] buildVertexArray() {
        float[] vertex = new float[vCount * 3];
        float unitDegree = (float) (2 * Math.PI / segment);
        float xIn0, xIn1, xOut0, xOut1, yIn0, yIn1, yOut0, yOut1;
        float degree = 0;
        float sin = 1.0f;
        float cos = 1.0f;
        sin = (float) Math.sin(degree);
        cos = (float) Math.cos(degree);
        xOut0 = cos * rOuter;
        yOut0 = sin * rOuter;
        xIn0 = cos * rInner;
        yIn0 = sin * rInner;
        int count = 0;
        for (int i = 0; i < segment; i++) {
            degree += unitDegree;
            sin = (float) Math.sin(degree);
            cos = (float) Math.cos(degree);
            xOut1 = cos * rOuter;
            yOut1 = sin * rOuter;
            xIn1 = cos * rInner;
            yIn1 = sin * rInner;
            vertex[count] = xOut0;
            vertex[++count] = yOut0;
            vertex[++count] = 0;
            vertex[++count] = xIn0;
            vertex[++count] = yIn0;
            vertex[++count] = 0;
            vertex[++count] = xOut1;
            vertex[++count] = yOut1;
            vertex[++count] = 0;
            //
            vertex[++count] = xOut1;
            vertex[++count] = yOut1;
            vertex[++count] = 0;
            vertex[++count] = xIn0;
            vertex[++count] = yIn0;
            vertex[++count] = 0;
            vertex[++count] = xIn1;
            vertex[++count] = yIn1;
            vertex[++count] = 0;
            vertex[++count] = 0;
            //
            xOut0 = xOut1;
            xIn0 = xIn1;
            yOut0 = yOut1;
            yIn0 = yIn1;
        }

        Log.e("TAG", "buildVertexArray "+ Arrays.toString(vertex));
        return vertex;
    }

    @Override
    protected float[] buildTextureArray() {
        return new float[0];
    }

    @Override
    public int getVCount() {
        return vCount;
    }
}
