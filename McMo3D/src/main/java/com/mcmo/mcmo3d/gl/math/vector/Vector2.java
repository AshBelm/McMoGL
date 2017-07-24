package com.mcmo.mcmo3d.gl.math.vector;

/**
 * Created by ZhangWei on 2017/1/16.
 */

public class Vector2 {
    public float x;
    public float y;

    public static Vector2 ZERO(){
        return new Vector2(0,0);
    }
    public Vector2() {
        this.x=0;
        this.y=0;
    }
    public Vector2(final Vector2 form){
        x=form.x;
        y=form.y;
    }
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void set(float x,float y){
        this.x=x;
        this.y=y;
    }

    public void add(Vector2 v) {
        x += v.x;
        y += v.y;
    }
    public void add(float x,float y){
        this.x+=x;
        this.y+=y;
    }
    public void add(float a){
        this.x+=a;
        this.y+=a;
    }
    public void subtract(Vector2 v) {
        x -= v.x;
        y -= v.y;
    }
    public void subtract(float x,float y){
        this.x-=x;
        this.y-=y;
    }
    public void subtract(float a){
        x-=a;
        y-=a;
    }
    public void dot(Vector2 v){
        x*=v.x;
        y*=v.y;
    }

    public void multiply(float a){
        x*=a;
        y*=a;
    }
    public void normalize(){
        float length= x*x+y*y;
        if(length>0){
            float mag= (float) (1.0f/Math.sqrt(length));
            x*=mag;
            y*=mag;
        }
    }
    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
