package com.mcmo.mcmo3d.gl.geometry.graphic;

import android.content.Context;

import com.mcmo.mcmo3d.gl.physics.collision.AABB;

import java.util.Arrays;

/**
 * Created by ZhangWei on 2017/7/25.
 */

public class Circle extends Object3D {
    private float radius;
    private int segment;

    public Circle(float radius, int segment) {
        this.radius = radius;
        this.segment = segment;
        if (segment < 3) {
            throw new IllegalArgumentException("The circle can not create by " + segment + " segment.");
        }
    }

    @Override
    protected void build(Context context) {
        super.build(context);
        if (mCollider != null) {
            if (mCollider instanceof AABB) {
                ((AABB) mCollider).max.z = 1;
                ((AABB) mCollider).min.z = -1;
            }
        }
    }

    @Override
    protected float[] buildVertexArray() {
        float[] vertex = new float[getVCount() * 3];
        Arrays.fill(vertex, 0.0f);
        float unitDegree = (float) (2 * Math.PI / segment);
        float degree = 0;
        //center point (0,0)
        float prevX = 0;//radius*sin(0);
        float prevY = radius;//radius*cos(0);
        for (int i = 0; i < segment; i++) {
            degree = unitDegree * (i + 1);
            float x = (float) (-radius * Math.sin(degree));
            float y = (float) (radius * Math.cos(degree));
            int startIndex = i * 9;
            vertex[startIndex] = x;
            vertex[startIndex + 1] = y;
            vertex[startIndex + 3] = prevX;
            vertex[startIndex + 4] = prevY;
            prevX = x;
            prevY = y;
        }
        return vertex;
    }

    @Override
    protected float[] buildTextureArray() {
        float[] texture = new float[getVCount() * 2];
        float unitDegree = (float) (2 * Math.PI / segment);
        float degree = 0;
        //center point (0.5,0.5)
        float prevX = 0.5f;
        float prevY = 0.0f;
        for (int i = 0; i < segment; i++) {
            degree = unitDegree * (i + 1);
            int startIndex = i * 6;
            float x = (float) (0.5f + 0.5f * Math.sin(degree));
            float y = (float) (0.5f - 0.5f * Math.cos(degree));
            texture[startIndex] = x;
            texture[startIndex + 1] = y;
            texture[startIndex + 2] = prevX;
            texture[startIndex + 3] = prevY;
            texture[startIndex + 4] = 0.5f;
            texture[startIndex + 5] = 0.5f;
            prevX = x;
            prevY = y;
        }
        return texture;
    }

    @Override
    public int getVCount() {
        return segment * 3;
    }
}
