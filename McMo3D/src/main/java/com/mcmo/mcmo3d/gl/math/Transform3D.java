package com.mcmo.mcmo3d.gl.math;

import android.util.Log;

import com.mcmo.mcmo3d.gl.math.matrix.Matrix;
import com.mcmo.mcmo3d.gl.math.matrix.Matrix4;
import com.mcmo.mcmo3d.gl.math.vector.Vector3;

import java.util.Arrays;

/**
 * Created by ZhangWei on 2017/1/20.
 */

public class Transform3D {
    public Matrix4 tmpMatrix;
    public Matrix4 mModelMatrix;
//    public Vector3 mPosition;


    public Transform3D() {
        mModelMatrix = new Matrix4();
        tmpMatrix = new Matrix4();
//        mPosition = Vector3.ZERO();
    }

    public void set(float[] value,int offset){
        mModelMatrix.set(value, offset);
    }
    public void postMM(float[] lm,int offset){
        mModelMatrix.postMM(lm, offset);
    }
    public void prevMM(float[] rm,int offset){
        mModelMatrix.preMM(rm, offset);
    }
    public void rotate(Axis axis, float angle) {
        mModelMatrix.rotate(axis, angle);
    }

    public void rotate(float angle,Vector3 axis){
        mModelMatrix.rotate(angle, axis);
    }
    public void rotate(float angle,float x,float y,float z){
        mModelMatrix.rotate(angle, x, y, z);
    }

    public void setRotate(float angle, float x, float y, float z) {
        mModelMatrix.setRotate(angle, x, y, z);
    }
    public void setRotate(float angle,Vector3 axis){
        mModelMatrix.setRotate(angle, axis);
    }

    public void setRotate(Axis axis, float angle) {
        mModelMatrix.setRotate(axis, angle);
    }

    public void setTransform(float x, float y, float z) {
        mModelMatrix.setTranslate(x, y, z);
    }

    public void transform(float x, float y, float z) {
        mModelMatrix.translate(x, y, z);
    }

    public void scale(float x, float y, float z) {
        mModelMatrix.scale(x, y, z);
    }

    public Matrix4 getModelMatrix() {
        return mModelMatrix;
    }

    public float[] getModelArray() {
        return mModelMatrix.getM();
    }

    public void lookAt(Transform3D other){
        lookAt(other,Axis.Y,Axis.X);
    }

    public void lookAt(Transform3D other, Axis... axises) {
        Vector3 p = getPosition();
        Vector3 op = other.getPosition();
        float dx = p.x - op.x;
        float dz = p.z - op.z;
        float dy = p.y - op.y;
        float rotateX, rotateY, rotateZ;
        if (dz == 0) {
            rotateY = dx > 0 ? 90 : dx == 0 ? 0 : -90;
        } else {
            rotateY = (float) Math.toDegrees(Math.atan(dx / dz));
            if (dz <= 0) {
                rotateY += 180;
            }
        }
        float xz = (float) Math.sqrt(dx * dx + dz * dz);
        if (xz == 0) {
            rotateX = dy > 0 ? -90 : dy == 0 ? 0 : 90;
        } else {
            rotateX = -(float) Math.toDegrees(Math.atan(dy / xz));
        }
        setTransform(p.x, p.y, p.z);
        for (Axis axis:axises) {
            switch (axis){
                case X:
                    rotate(Axis.X, rotateX);
                    break;
                case Y:
                    rotate(Axis.Y, rotateY);
                    break;
                case Z:
            }
        }
    }

    public Vector3 getPosition() {
        float[] m = mModelMatrix.getM();
        Vector3 position = new Vector3(m[12], m[13], m[14]);
        return position;
    }

    public Vector3 up() {
//        Vector3 upPosition = Matrix.multiplyMV(Vector3.Up(), mModelMatrix.getM());
        float[] rm=new float[4];
        float[] m=new float[]{0,1,0,1};
        android.opengl.Matrix.multiplyMV(rm,0,mModelMatrix.getM(),0,m,0);
        Vector3 upPosition=new Vector3(rm[0],rm[1],rm[2]);
        Vector3 position = getPosition();
        upPosition.subtract(position);
        Log.e("up", "up "+ Arrays.toString(rm)+"");
        return upPosition;
    }
    public Vector3 left(){
        Vector3 leftPosition = Matrix.multiplyMV(Vector3.Left(), mModelMatrix.getM());
        Vector3 position = getPosition();
        leftPosition.subtract(position);
        return leftPosition;
    }
    public Vector3 right(){
        Vector3 rightPosition = Matrix.multiplyMV(Vector3.Right(), mModelMatrix.getM());
        Vector3 position = getPosition();
        rightPosition.subtract(position);
        return rightPosition;
    }
}
