package com.mcmo.mcmo3d.gl.camera;

import android.opengl.GLES20;

import com.mcmo.mcmo3d.gl.geometry.graphic.GLObject;

import java.util.List;

/**
 * Created by ZhangWei on 2017/2/21.
 */

public class Camera2D extends Camera {
    public Camera2D() {
        setLookAt(0,0,0,0,1,0,0,0,-1);
    }

    @Override
    public void onSizeChange(float width, float height,boolean defaultMatrix) {
        super.onSizeChange(width, height,defaultMatrix);

    }

    @Override
    public boolean setDefaultMatrix() {
        if(width==0||height==0){
            return false;
        }else{
            setLookAt(0,0,0,0,0,1,0,1,0);
            float ratio=1.0f*width/height;
            orthoM(-ratio,ratio,-1,1,1,100);
            return true;
        }
    }

    @Override
    public void draw(List<GLObject> models) {
        GLES20.glViewport(0,0,(int)getWidth()/2,(int)getHeight());
        super.draw(models);
    }
}
