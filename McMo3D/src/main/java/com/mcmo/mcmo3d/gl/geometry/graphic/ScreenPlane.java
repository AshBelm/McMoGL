package com.mcmo.mcmo3d.gl.geometry.graphic;

import android.content.Context;
import android.opengl.GLES20;

import com.mcmo.mcmo3d.gl.camera.Camera;
import com.mcmo.mcmo3d.gl.math.Axis;

/**
 * Created by ZhangWei on 2017/2/21.
 */

public class ScreenPlane extends Plane {
    private ScreenCamera camera;
    public ScreenPlane(Context context) {
        super(Axis.Z, 2, 2);
        camera=new ScreenCamera();
        camera.setLookAt(0,0,0,0,0,1,0,1,0);
        camera.orthoM(-1,1,-1,1,1,10);
        this.setTransform(0,0,2);
    }
    public void onSizeChange(int width,int height){
        camera.onSizeChange(width, height,false);
    }
    @Override
    public void draw(float[] mvpMatrix) {
        camera.draw(this);
    }
    public void drawEx(float[] mvpMatrix){
        super.draw(mvpMatrix);
    }
    private class ScreenCamera extends Camera{
        public void draw(ScreenPlane o) {
            GLES20.glViewport(0,0,(int)getWidth(),(int)getHeight());
            o.drawEx(calcMVPMatrix(o.getFinalModelArray()));
        }
    }
}
