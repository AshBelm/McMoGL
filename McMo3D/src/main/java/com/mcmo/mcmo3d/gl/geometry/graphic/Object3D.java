package com.mcmo.mcmo3d.gl.geometry.graphic;

import android.content.Context;
import android.opengl.GLES20;

import com.mcmo.mcmo3d.gl.material.Material;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by ZhangWei on 2017/1/22.
 */

public abstract class Object3D extends GLObject {
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTexCoorBuffer;
    private Material mMaterial;

    public Object3D() {
    }

    public void setMaterial(Material material) {
        this.mMaterial = material;
    }

    public Material getMaterial() {
        return mMaterial;
    }

    @Override
    public void create(Context context) {
        super.create(context);
        build(context);
        if (mMaterial != null) {
            mMaterial.create();
        }
    }

    protected void build(Context context) {
        //顶点缓冲
        float[] v = buildVertexArray();
        if (v != null)
            produceVertexBuffer(v);
        //纹理缓冲
        float[] t = buildTextureArray();
        if (t != null)
            produceTextureBuffer(t);
        if (mCollider != null) {
            mCollider.generateCollideBox(v);
        }
    }

    public float[] getVertexArray() {
        if (mVertexBuffer == null) {
            return null;
        }
        return mVertexBuffer.array();
    }

    public float[] getTexCoorArray() {
        if (mTexCoorBuffer == null)
            return null;
        return mTexCoorBuffer.array();
    }

    private void produceVertexBuffer(float[] array) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(array.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(array);
        mVertexBuffer.position(0);
    }

    private void produceTextureBuffer(float[] array) {
        ByteBuffer tbb = ByteBuffer.allocateDirect(array.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        mTexCoorBuffer = tbb.asFloatBuffer();
        mTexCoorBuffer.put(array);
        mTexCoorBuffer.position(0);
    }

    public void draw(float[] mvpMatrix) {
        if (mMaterial != null) {
            if (isCullFace())
                GLES20.glEnable(GLES20.GL_CULL_FACE);
            else
                GLES20.glDisable(GLES20.GL_CULL_FACE);
            if (isTransparent()) {
                GLES20.glEnable(GLES20.GL_BLEND);
                GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            }
            mMaterial.draw(mvpMatrix, mVertexBuffer, mTexCoorBuffer, this);
            if (isTransparent()) {
                GLES20.glDisable(GLES20.GL_BLEND);
            }
        }
    }

    @Override
    public void onFrameUpdate(int refreshFrameRate) {
        if (mMaterial != null)
            mMaterial.update(refreshFrameRate);
    }

    protected abstract float[] buildVertexArray();

    protected abstract float[] buildTextureArray();

    public abstract int getVCount();

    public int glDrawType() {
        return GLES20.GL_TRIANGLES;
    }

    @Override
    public void destroy(Context context) {
        super.destroy(context);
        if (mMaterial != null)
            mMaterial.destroy();
    }
}
