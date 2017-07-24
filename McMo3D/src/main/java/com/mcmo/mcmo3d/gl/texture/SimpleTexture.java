package com.mcmo.mcmo3d.gl.texture;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.mcmo.mcmo3d.gl.util.TextureUtil;

/**
 * Created by ZhangWei on 2017/2/7.
 */

public class SimpleTexture extends ATexture {

    public SimpleTexture(Bitmap bitmap) {
        textureId= TextureUtil.createImageTexture(bitmap);
        addTexture();
    }

    public SimpleTexture(Resources resources, int resId) {
        textureId = TextureUtil.createImageTexture(resources,resId);
        addTexture();
    }
}
