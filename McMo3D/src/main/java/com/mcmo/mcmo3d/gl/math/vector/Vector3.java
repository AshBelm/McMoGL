package com.mcmo.mcmo3d.gl.math.vector;

import com.mcmo.mcmo3d.gl.math.Axis;

/**
 * Created by ZhangWei on 2017/1/16.
 */

public class Vector3 {
    public float x;
    public float y;
    public float z;
    public static Vector3 Left(){
        return new Vector3(1,0,0);
    }
    public static Vector3 Right(){
        return new Vector3(-1,0,0);
    }
    public static Vector3 Up(){
        return new Vector3(0,1,0);
    }
    public static Vector3 Down(){
        return new Vector3(0,-1,0);
    }
    public static Vector3 Fore(){
        return new Vector3(0,0,1);
    }
    public static Vector3 Back(){
        return new Vector3(0,0,-1);
    }
    public static Vector3 ZERO(){
        return new Vector3(0,0,0);
    }

    public Vector3(Axis axis){
        this(0,0,0);
        switch (axis){
            case X:
                x=1;
                break;
            case Y:
                y=1;
                break;
            case Z:
                z=1;
                break;
        }
    }

    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(final Vector3 from) {
        x = from.x;
        y = from.y;
        z = from.z;
    }
    public void set(Vector3 from){
        x=from.x;
        y=from.y;
        z=from.z;
    }
    public void set(float x,float y,float z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public void add(Vector3 v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    public void add(Vector2 v) {
        x += v.x;
        y += v.y;
    }

    public void add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void add(float a) {
        x += a;
        y += a;
        z += a;
    }

    public void subtract(Vector3 v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }

    public void subtract(Vector2 v) {
        x -= v.x;
        y -= v.y;
    }

    public void subtract(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    public void subtract(float a) {
        x -= a;
        y -= a;
        z -= a;
    }

    public void dot(Vector3 v) {
        x *= v.x;
        y *= v.y;
        z *= v.y;
    }
    public void dot(float a) {
        x *= a;
        y *= a;
        z *= a;
    }
    public Vector3 multiK(float k){
        return new Vector3(this.x*k,this.y*k,this.z*k);
    }
    public void cross(Vector3 v) {
        x = y * v.z - z * v.y;
        y = z * v.x - x * v.z;
        z = x * v.y - y * v.x;
    }
    public Vector3 minus(Vector3 v){
        return new Vector3(this.x-v.x,this.y-v.y,this.z-v.z);
    }
    public static Vector3 cross(Vector3 a, Vector3 b) {
        return new Vector3(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x);
    }
    public void normalize(){
        float length=x*x+y*y+z*z;
        if(length>0){
            float mag= (float) (1.0/Math.sqrt(length));
            x*=mag;
            y*=mag;
            z*=mag;
        }
    }
    public float[] array(){
        return new float[]{x,y,z};
    }
    public float[] arrayV(){
        return new float[]{x,y,z,1};
    }
    @Override
    public String toString() {
        return "Vector3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
