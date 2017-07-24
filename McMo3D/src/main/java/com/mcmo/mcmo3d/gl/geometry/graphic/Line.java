package com.mcmo.mcmo3d.gl.geometry.graphic;

import android.opengl.GLES20;

import com.mcmo.mcmo3d.gl.math.vector.Vector3;

/**
 * Created by ZhangWei on 2017/2/22.
 */

public class Line extends Object3D {
    private Vector3 start;
    private Vector3 end;

    public Line(Vector3 start, Vector3 end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected float[] buildVertexArray() {
        float[] vertex = new float[]{
                start.x, start.y, start.z,
                end.x, end.y, end.z
        };
        return vertex;
    }

    @Override
    protected float[] buildTextureArray() {
        return null;
    }

    @Override
    public int getVCount() {
        return 2;
    }

    @Override
    public void draw(float[] mvpMatrix) {
        GLES20.glLineWidth(2);
        super.draw(mvpMatrix);
        GLES20.glLineWidth(1);
    }

    @Override
    public int glDrawType() {
        return GLES20.GL_LINES;
    }
}
