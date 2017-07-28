package com.mcmo.mcmo3d.gl.geometry.graphic;

import android.content.Context;
import android.opengl.GLES20;

import java.util.ArrayList;

/**
 * Created by ZhangWei on 2017/1/22.
 */

public class Sphere extends Object3D {
    private int vCount;
    private float r;
    private float angleSpan=6.0f;
    public Sphere(float r) {
        this.r=r;
    }

    public Sphere(float r, float angleSpan) {
        this.r = r;
        this.angleSpan = angleSpan;
    }

    @Override
    protected float[] buildVertexArray() {
        final float UNIT_SIZE=1.0f;
        ArrayList<Float> alVertix=new ArrayList<Float>();//存放顶点坐标的ArrayList
        for(float vAngle=90;vAngle>-90;vAngle=vAngle-angleSpan){//垂直方向angleSpan度一份
            for(float hAngle=360;hAngle>0;hAngle=hAngle-angleSpan){//水平方向angleSpan度一份
                //纵向横向各到一个角度后计算对应的此点在球面上的坐标
                double xozLength=r*UNIT_SIZE*Math.cos(Math.toRadians(vAngle));
                float x1=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
                float z1=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
                float y1=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(vAngle)));
                xozLength=r*UNIT_SIZE*Math.cos(Math.toRadians(vAngle-angleSpan));
                float x2=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
                float z2=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
                float y2=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(vAngle-angleSpan)));
                xozLength=r*UNIT_SIZE*Math.cos(Math.toRadians(vAngle-angleSpan));
                float x3=(float)(xozLength*Math.cos(Math.toRadians(hAngle-angleSpan)));
                float z3=(float)(xozLength*Math.sin(Math.toRadians(hAngle-angleSpan)));
                float y3=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(vAngle-angleSpan)));
                xozLength=r*UNIT_SIZE*Math.cos(Math.toRadians(vAngle));
                float x4=(float)(xozLength*Math.cos(Math.toRadians(hAngle-angleSpan)));
                float z4=(float)(xozLength*Math.sin(Math.toRadians(hAngle-angleSpan)));
                float y4=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(vAngle)));
                //构建第一三角形
                alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
                alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
                alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
                //构建第二三角形
                alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
                alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
                alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);
            }}
        vCount=alVertix.size()/3;//顶点的数量为坐标值数量的1/3，因为一个顶点有3个坐标
        //将alVertix中的坐标值转存到一个float数组中
        float vertices[]=new float[vCount*3];
        for(int i=0;i<alVertix.size();i++){
            vertices[i]=alVertix.get(i);
        }
        return vertices;
    }

    @Override
    protected float[] buildTextureArray() {
        float[] texCoor=generateTexCoor(//获取切分整图的纹理数组
                (int)(360/angleSpan), //纹理图切分的列数
                (int)(180/angleSpan)  //纹理图切分的行数
        );
        return texCoor;
    }

    @Override
    public int getVCount() {
        return vCount;
    }
    //自动切分纹理产生纹理数组的方法
    public float[] generateTexCoor(int bw,int bh){
        float[] result=new float[bw*bh*6*2];
        float sizew=1.0f/bw;//列数
        float sizeh=1.0f/bh;//行数
        int c=0;
        for(int i=0;i<bh;i++){
            for(int j=0;j<bw;j++){
                //每行列一个矩形，由两个三角形构成，共六个点，12个纹理坐标
                float s=j*sizew;
                float t=i*sizeh;
                result[c++]=s;
                result[c++]=t;
                result[c++]=s;
                result[c++]=t+sizeh;
                result[c++]=s+sizew;
                result[c++]=t;
                result[c++]=s+sizew;
                result[c++]=t;
                result[c++]=s;
                result[c++]=t+sizeh;
                result[c++]=s+sizew;
                result[c++]=t+sizeh;
            }}
        return result;
    }

    @Override
    public void draw(float[] mvpMatrix) {
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        super.draw(mvpMatrix);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
    }
}
