package com.mcmo.mcmo3d.gl.geometry.graphic;

import android.content.Context;

import com.mcmo.mcmo3d.gl.math.Axis;
import com.mcmo.mcmo3d.gl.physics.collision.AABB;

/**
 * 平面，默认水平方向即xz平面
 * Created by ZhangWei on 2017/2/10.
 */

public class Plane extends Object3D{
    private Axis mOrientation;
    private float width=1.0f;
    private float height=1.0f;
    public Plane(Context context,Axis axis, float width, float height) {
        mOrientation=axis;
        this.width=width;
        this.height=height;
    }

    @Override
    protected void build(Context context) {
        super.build(context);
        if(mCollider!=null){
            if(mCollider instanceof AABB){
                ((AABB) mCollider).max.z=1;
                ((AABB) mCollider).min.z=-1;
            }
        }
    }

    @Override
    protected float[] buildVertexArray() {
        float[] vertex=null;
        switch (mOrientation){
            case X:
                vertex=createYZPlane();
                break;
            case Y:
                vertex=createXZPlane();
                break;
            case Z:
                vertex=createXYPlane();
                break;
        }
        return vertex;
    }

    //<editor-fold desc="create Vertex">
    private float[] createXYPlane(){
        float w=width/2.0f;
        float h=height/2.0f;
        float[] vertex=new float[]{
                w,-h,0,//
                -w,-h,0,//
                w,h,0,//

                w,h,0,//
                -w,-h,0,//
                -w,h,0//
        };
        return vertex;
    }
    private float[] createXZPlane(){
        float w=width/2.0f;
        float h=height/2.0f;
        float[] vertex=new float[]{
                w,0,-h,//
                -w,0,-h,//
                w,0,h,//

                w,0,h,//
                -w,0,-h,//
                -w,0,h//
        };
        return vertex;
    }
    private float[] createYZPlane(){
        float w=width/2.0f;
        float h=height/2.0f;
        float[] vertex=new float[]{
                0,-h,-w,//
                0,-h,w,//
                0,h,-w,//

                0,h,-w,//
                0,-h,w,//
                0,h,w//
        };
        return vertex;
    }
    //</editor-fold>

    @Override
    protected float[] buildTextureArray() {
        float[] texture=new float[]{
                0,1,//
                1,1,//
                0,0,//
                0,0,//
                1,1,//
                1,0
        };
        return texture;
    }

    @Override
    public int getVCount() {
        return 6;
    }
}
