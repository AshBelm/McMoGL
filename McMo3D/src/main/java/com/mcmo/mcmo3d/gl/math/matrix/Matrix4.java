package com.mcmo.mcmo3d.gl.math.matrix;

import android.opengl.Matrix;

import com.mcmo.mcmo3d.gl.math.Axis;
import com.mcmo.mcmo3d.gl.math.vector.Vector3;

/**
 * Created by ZhangWei on 2017/1/16.
 */

public class Matrix4 {
    private float[] m = new float[16];

    public float[] getM() {
        return m;
    }

    public Matrix4() {
        identity();
    }

    public void set(float[] value, int offset) {
        if (value.length < (16 + offset)) {
            throw new RuntimeException("value length can not be small than 16 + offset");
        }
        for (int i = 0; i < value.length; i++) {
            m[i] = value[i + offset];
        }
    }

    public void setTranslate(float x, float y, float z) {
        identity();
        Matrix.translateM(m, 0, x, y, z);
    }

    public void multiplyMM(Matrix4 left, Matrix4 right) {
        Matrix.multiplyMM(m, 0, left.m, 0, right.m, 0);
    }

    public void postMM(Matrix4 left) {
        Matrix.multiplyMM(m, 0, left.m, 0, m, 0);
    }

    public void preMM(Matrix4 right) {
        Matrix.multiplyMM(m, 0, m, 0, right.m, 0);
    }

    public void postMM(float[] lm,int offset){
        Matrix.multiplyMM(m, 0, lm, offset, m, 0);
    }
    public void preMM(float[] rm,int offset) {
        Matrix.multiplyMM(m, 0, m, 0, rm, offset);
    }

    public void setTranslate(Vector3 v) {
        setTranslate(v.x, v.y, v.z);
    }

    public void translate(float x, float y, float z) {
        Matrix.translateM(m, 0, x, y, z);
    }

    public void translate(Vector3 v) {
        translate(v.x, v.y, v.z);
    }

    public void setRotate(Axis axis, float angle) {
        switch (axis) {
            case X:
                Matrix.setRotateM(m, 0, angle, 1.0f, 0, 0);
                break;
            case Y:
                Matrix.setRotateM(m, 0, angle, 0, 1.0f, 0);
                break;
            case Z:
                Matrix.setRotateM(m, 0, angle, 0, 0, 1.0f);
                break;
        }
    }

    public void setRotate(float angle, float x, float y, float z) {
        Matrix.setRotateM(m, 0, angle, x, y, z);
    }

    public void setRotate(float angle, Vector3 axis) {
        Matrix.setRotateM(m, 0, angle, axis.x, axis.y, axis.z);
    }

    public void rotate(Axis axis, float angle) {
        switch (axis) {
            case X:
                Matrix.rotateM(m, 0, angle, 1.0f, 0, 0);
                break;
            case Y:
                Matrix.rotateM(m, 0, angle, 0, 1.0f, 0);
                break;
            case Z:
                Matrix.rotateM(m, 0, angle, 0, 0, 1.0f);
                break;
        }
    }

    public void rotate(float angle, Vector3 axis) {
        Matrix.rotateM(m, 0, angle, axis.x, axis.y, axis.z);
    }
    public void rotate(float angle,float x,float y,float z){
        Matrix.rotateM(m,0,angle,x,y,z);
    }
    public void scale(float x, float y, float z) {
        Matrix.scaleM(m, 0, x, y, z);
    }

    public void identity() {
        com.mcmo.mcmo3d.gl.math.matrix.Matrix.setIdentity(m, 0);
    }

    public void setLookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        Matrix.setLookAtM(m, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public void perspectiveM(float fovy, float aspect, float zNear, float zFar) {
        Matrix.perspectiveM(m, 0, fovy, aspect, zNear, zFar);
    }

    public void frustumM(float left, float right, float bottom, float top, float near, float far) {
        Matrix.frustumM(m, 0, left, right, bottom, top, near, far);
    }

    public void orthoM(int mOffset, float left, float right, float bottom, float top, float near, float far) {
        Matrix.orthoM(m, mOffset, left, right, bottom, top, near, far);
    }

    public void copyFrom(Matrix4 other) {
        System.arraycopy(other.m, 0, m, 0, m.length);
    }

    public Matrix4 invert(){
        Matrix4 matrix4=new Matrix4();
        Matrix.invertM(matrix4.m,0,m,0);
        return matrix4;
    }

    @Override
    public String toString() {
        String str = m[0] + " , " + m[4] + " , " + m[8] + " , " + m[12] + ",\n"
                + m[1] + " , " + m[5] + " , " + m[9] + " , " + m[13] + ",\n"
                + m[2] + " , " + m[6] + " , " + m[10] + " , " + m[14] + ",\n"
                + m[3] + " , " + m[7] + " , " + m[11] + " , " + m[15] + ",\n";
        return str;
    }
    public String toStringRow() {
        String str = m[0] + " , " + m[1] + " , " + m[2] + " , " + m[3] + ",\n"
                + m[4] + " , " + m[5] + " , " + m[6] + " , " + m[7] + ",\n"
                + m[8] + " , " + m[9] + " , " + m[10] + " , " + m[11] + ",\n"
                + m[12] + " , " + m[13] + " , " + m[14] + " , " + m[15] + ",\n";
        return str;
    }
}
