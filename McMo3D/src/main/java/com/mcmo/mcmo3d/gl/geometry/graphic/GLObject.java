package com.mcmo.mcmo3d.gl.geometry.graphic;

import android.content.Context;

import com.mcmo.mcmo3d.gl.component.Component;
import com.mcmo.mcmo3d.gl.math.Axis;
import com.mcmo.mcmo3d.gl.math.Transform3D;
import com.mcmo.mcmo3d.gl.math.matrix.Matrix4;
import com.mcmo.mcmo3d.gl.math.vector.Vector3;
import com.mcmo.mcmo3d.gl.physics.collision.Collider;
import com.mcmo.mcmo3d.gl.util.McMoGLConstant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ZhangWei on 2017/2/10.
 */

public class GLObject implements FrameUpdate {
    private String layer = McMoGLConstant.LAYER_DEFAULT;
    private int id;
    public Transform3D transform;
    public GLObject mParent;
    protected ArrayList<GLObject> mChild;
    private boolean blend_transparent;
    private boolean CULL_FACE=true;
    private HashMap<String,Component> mComponents;
    protected Collider mCollider;

    public GLObject() {
        transform = new Transform3D();
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void create(Context context){}
    public void destroy(Context context){
        if(mParent!=null){
            mParent.removeChild(this);
        }
        if(mChild!=null){
            for (GLObject o:mChild) {
                o.setParent(null);
            }
            mChild.clear();
        }
    };

    public void setCollider(Collider mCollider) {
        this.mCollider = mCollider;
    }

    public Collider getCollider() {
        return mCollider;
    }

    public void setTransparent(boolean blend_transparent) {
        this.blend_transparent = blend_transparent;
    }

    public boolean isTransparent() {
        return blend_transparent;
    }

    public void setCullFace(boolean FACE_CULL) {
        this.CULL_FACE = FACE_CULL;
    }

    public boolean isCullFace() {
        return CULL_FACE;
    }

    public Vector3 getPosition(){
        return transform.getPosition();
    }
    public Vector3 up(){
        return transform.up();
    }
    public Vector3 left(){
        return transform.left();
    }
    public void lookAt(GLObject other){
        this.transform.lookAt(other.transform);
    }
    public void lookAt(Transform3D transform){
        this.transform.lookAt(transform);
    }
    public void lookAt(GLObject other,Axis... axises){
        this.transform.lookAt(other.transform,axises);
    }

    public void rotate( float angle,Axis axis) {
        transform.rotate(axis, angle);
    }

    public void rotate(float angle,Vector3 axis){
        transform.rotate(angle, axis);
    }
    public void rotate(float angle,float x,float y,float z){
        transform.rotate(angle, x, y, z);
    }

    public void setRotate(float angle, float x, float y, float z) {
        transform.setRotate(angle, x, y, z);
    }
    public void setRotate(float angle,Axis axis){
        transform.setRotate(axis, angle);
    }
    public void setRotate(float angle,Vector3 axis){
        transform.setRotate(angle, axis);
    }

    public void setTransform(float x, float y, float z) {
        transform.setTransform(x, y, z);
    }

    public void transform(float x, float y, float z) {
        transform.transform(x, y, z);
    }

    public void scale(float x,float y,float z){
        transform.scale(x, y, z);
    }
    public Matrix4 getModelMatrix() {
        return transform.getModelMatrix();
    }

    public float[] getModelArray() {
        return transform.getModelArray();
    }

    public float[] getFinalModelArray() {
        if (mParent != null) {
            transform.tmpMatrix.multiplyMM(mParent.getFinalModelMatrix(), transform.mModelMatrix);
            return transform.tmpMatrix.getM();
        } else {
            transform.tmpMatrix.copyFrom(transform.mModelMatrix);
            return transform.tmpMatrix.getM();
        }
    }

    public Matrix4 getFinalModelMatrix() {
        if (mParent != null) {
            transform.tmpMatrix.multiplyMM(mParent.getFinalModelMatrix(), transform.mModelMatrix);
        } else {
            transform.tmpMatrix.copyFrom(transform.mModelMatrix);
        }
        return transform.tmpMatrix;
    }
    public Matrix4 getInvertModelMatrix(){
        return transform.mModelMatrix.invert();
    }

    public void setParent(GLObject parent) {
        mParent = parent;
    }

    public synchronized GLObject removeChild(GLObject child) {
        if (child == null) {
            return null;
        }
        boolean had = false;
        if (mChild != null) {
            had = mChild.remove(child);
        }
        if (had) {
            return child;
        } else {
            return null;
        }
    }

    public synchronized void addChild(GLObject child) {
        if (mChild == null) {
            mChild = new ArrayList<>();
        }
        if (!mChild.contains(child)) {
            mChild.add(child);
            if (child.mParent != null) {
                child.mParent.removeChild(child);
            }
            child.setParent(this);
        }
    }
    public void addComponent(Component component){
        String name=component.getClass().getName();
        if(mComponents==null){
            mComponents=new HashMap<>();
        }
        if(mComponents.containsKey(name)){
            Component del=mComponents.remove(name);
            del.destroy();
        }
        mComponents.put(name,component);
        component.create(this);
    }
    public Component removeComponent(Class<Component> componentClass){
        String name=componentClass.getName();
        if(mComponents==null){
            return null;
        }else{
            return mComponents.remove(name);
        }
    }
    public Component getComponent(Class<Component> componentClass){
        String name=componentClass.getName();
        if(mComponents==null){
            return null;
        }else{
            return mComponents.get(name);
        }
    }
    @Override
    public void onFrameUpdate(int refreshFrameRate) {}
}
