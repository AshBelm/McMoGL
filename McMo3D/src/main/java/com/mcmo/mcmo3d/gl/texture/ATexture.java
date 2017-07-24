package com.mcmo.mcmo3d.gl.texture;

import android.opengl.GLES20;

import com.mcmo.mcmo3d.gl.material.TextureManager;

/**
 * Created by ZhangWei on 2017/2/7.
 */

public abstract class ATexture {
    protected int textureId = -1;

    public int getTextureId() {
        return textureId;
    }

    protected void addTexture() {
        TextureManager.getInstance().addTexture(this);
    }

    public void release() {
        GLES20.glDeleteTextures(1, new int[]{textureId}, 0);
        TextureManager.getInstance().removeTexture(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj!=null&&obj instanceof ATexture){
            return ((ATexture) obj).getTextureId()==textureId;
        }
        return false;
    }
}
