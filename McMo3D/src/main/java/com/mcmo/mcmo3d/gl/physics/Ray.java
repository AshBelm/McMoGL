package com.mcmo.mcmo3d.gl.physics;

import com.mcmo.mcmo3d.gl.math.vector.Vector3;

/**
 * Created by ZhangWei on 2017/3/6.
 */

public class Ray {
    public Vector3 start;
    public Vector3 end;

    public Ray() {
        start=new Vector3();
        end=new Vector3();
    }

    public Vector3 getStart() {
        return start;
    }

    public void setStart(Vector3 start) {
        this.start = start;
    }

    public Vector3 getEnd() {
        return end;
    }

    public void setEnd(Vector3 end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
    //    /**
//     * 此方法要求相机eyez为正数
//     * @param touchX      触摸点x
//     * @param touchY      触摸点y
//     * @param view_width  openglsurface 的宽度 单位像素(要和触摸点的单位相同)
//     * @param view_height openglsurface 的高度 单位像素(要和触摸点的单位相同)
//     * @param left        视窗的左截面
//     * @param right       视窗的截面
//     * @param top         视窗的截面
//     * @param bottom      视窗的截面
//     * @param near        近截面
//     * @param far         远截面
//     * @return             返回物体是否被碰触到
//     */
//    public static final boolean hitTarget(Collider collider) {
//        //求视口的坐标中心在原点时，触控点的坐标
//        float x0=touchX-view_width/2;
//        float y0=view_height/2-touchY;
//        float x_near=x0*Math.abs(right-left)/view_width;
//        float y_near=y0*Math.abs(bottom-top)/view_height;
//        Vector3 nearPos=new Vector3(x_near,y_near,near);//z的位置取决于相机位置和相机朝向，默认相机在原点朝向正方向
//        float ratio = far/near;
//        Vector3 farPos= new Vector3(ratio*x_near,ratio*y_near,far);
//        Log.e("Ray", "screen : near="+nearPos.toString()+" far="+farPos.toString() );
//        float[] cameraInverse=new float[16];
//        Matrix.invertM(cameraInverse,0,cameraVM,0);
//        float[] nearArray=new float[4];
//        float[] farArray=new float[4];
//        Matrix.multiplyMV(nearArray,0,cameraInverse,0,nearPos.arrayV(),0);
//        Matrix.multiplyMV(farArray,0,cameraInverse,0,farPos.arrayV(),0);
//        Log.e("Ray", "world : near="+ Arrays.toString(nearArray)+" far="+Arrays.toString(farArray) );
//        float[] mv=new float[16];
//        Matrix.multiplyMM(mv,0,cameraVM,0,modelM,0);
//        float[] modelInverse=new float[16];
//        Matrix.invertM(modelInverse,0,mv,0);
//        float[] nearObject=new float[4];
//        float[] farObject=new float[4];
//        Matrix.multiplyMV(nearObject,0,mv,0,nearArray,0);
//        Matrix.multiplyMV(farObject,0,mv
//                ,0,farArray,0);
//        Log.e("Ray", "object : near="+ Arrays.toString(nearObject)+" far="+Arrays.toString(farObject) );
//        Log.e("Ray", "collider : "+collider.toString() );
//        return  collider.rayIntersect(new Vector3(nearObject[0],nearObject[1],nearObject[2]),new Vector3(farObject[0],farObject[1],farObject[2]));
//    }
}
