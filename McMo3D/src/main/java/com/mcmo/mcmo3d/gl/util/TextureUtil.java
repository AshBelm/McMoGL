package com.mcmo.mcmo3d.gl.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by weizhang210142 on 2016/6/21.
 */
public class TextureUtil {

    public static int createImageTexture(Bitmap bitmap) {
        int[] textureId = new int[1];
        GLES20.glGenTextures(1, textureId, 0);//生成纹理id
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);//设置MIN采样方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);//设置MAG采样方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);//设置S轴拉伸方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);//设置T轴拉伸方式
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        return textureId[0];
    }
    public static void createImageTexture(Bitmap bitmap,int textureId){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);//设置MIN采样方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);//设置MAG采样方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);//设置S轴拉伸方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);//设置T轴拉伸方式
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

    public static int createImageTexture(Resources resources, int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, id);
        return createImageTexture(bitmap);
    }

    public static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

    public static int createSurfaceTexture() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        int textureId = textures[0];
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, textureId);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        return textureId;
    }

    public static float[] genSampleTextureCoordinate(int column, int row) {
        float[] coor = new float[column * row * 6 * 2];
        float stepColumn = 1.0f / column;
        float stepRow = 1.0f / row;
        int index = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                float s = j * stepColumn;
                float t = i * stepRow;
                coor[index++]=0;
                coor[index++]=0;
                coor[index++]=0;
                coor[index++]=0.33333334f;
                coor[index++]=0.25f;
                coor[index++]=0;

                coor[index++]=0;
                coor[index++]=0.25f;
                coor[index++]=0;
                coor[index++]=0.33333334f;
                coor[index++]=0.25f;
                coor[index++]=0.33333334f;
//                coor[index++] = s;
//                coor[index++] = t;
//
//                coor[index++] = s;
//                coor[index++] = t + stepRow;
//
//                coor[index++] = s + stepColumn;
//                coor[index++] = t;
//                //
//                coor[index++] = s + stepColumn;
//                coor[index++] = t;
//
//                coor[index++] = s;
//                coor[index++] = t + stepRow;
//
//                coor[index++] = s + stepColumn;
//                coor[index++] = t + stepRow;
            }
        }
        return coor;
    }
}
