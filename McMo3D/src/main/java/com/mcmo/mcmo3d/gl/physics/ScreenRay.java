package com.mcmo.mcmo3d.gl.physics;

import android.opengl.Matrix;
import android.util.Log;

import com.mcmo.mcmo3d.gl.camera.Camera;
import com.mcmo.mcmo3d.gl.math.vector.Vector3;

import java.util.Arrays;

/**
 * Created by ZhangWei on 2017/3/8.
 */

public class ScreenRay extends Ray {

    public void ray(float touchX,float touchY,float screenWidth,float screenHeight,float left,float right,float bottom,float top,float near,float far,float[] cameraVM){
        //求视口的坐标中心在原点时，触控点的坐标
        float x0=touchX-screenWidth/2;
        float y0=screenHeight/2-touchY;
        float x_near=x0*Math.abs(right-left)/screenWidth;
        float y_near=y0*Math.abs(bottom-top)/screenHeight;
        Vector3 nearPos=new Vector3(x_near,y_near,-near);//z的位置取决于相机位置和相机朝向，默认相机在原点朝向正方向
        float ratio = far/near;
        Vector3 farPos= new Vector3(ratio*x_near,ratio*y_near,-far);
        Log.e("ScreenRay", "screen : near="+nearPos.toString()+" far="+farPos.toString() );
        float[] cameraInverse=new float[16];
        Matrix.invertM(cameraInverse,0,cameraVM,0);
        float[] nearArray=new float[4];
        float[] farArray=new float[4];
        Matrix.multiplyMV(nearArray,0,cameraInverse,0,nearPos.arrayV(),0);
        Matrix.multiplyMV(farArray,0,cameraInverse,0,farPos.arrayV(),0);
        Log.e("ScreenRay", "world : near="+ Arrays.toString(nearArray)+" far="+Arrays.toString(farArray) );
        start.set(nearArray[0],nearArray[1],nearArray[2]);
        end.set(farArray[0],farArray[1],farArray[2]);
    }
    public void ray(float touchX, float touchY, Camera camera){
        ray(touchX,touchY,camera.getWidth(),camera.getHeight(),camera.getLeft(),camera.getRight(),camera.getBottom(),camera.getTop(),camera.getNear(),camera.getFar(),camera.getFinalVArray());
    }
}
