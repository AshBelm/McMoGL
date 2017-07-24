package com.mcmo.mcmo3d.gl.camera;

import android.opengl.Matrix;

import com.mcmo.mcmo3d.gl.geometry.graphic.GLObject;
import com.mcmo.mcmo3d.gl.geometry.graphic.Object3D;
import com.mcmo.mcmo3d.gl.geometry.graphic.prefab.Prefab;
import com.mcmo.mcmo3d.gl.math.matrix.Matrix4;

import java.util.HashSet;
import java.util.List;

/**
 * Created by ZhangWei on 2017/1/20.
 */

public class Camera extends GLObject{
    protected float[] mMVPMatrix;
    private Matrix4 mProjMatrix;
    private Matrix4 mVMatrix;
    protected float mFOV=60f;
    protected float mAspect=60f;
    protected float width,height;//屏幕尺寸像素
    protected float left,right,top,bottom,near,far;

    private HashSet<String> drawTags;

    public Camera() {
        this.mMVPMatrix=new float[16];
        this.mProjMatrix = new Matrix4();
        this.mVMatrix = new Matrix4();
        drawTags=new HashSet<>();
        addTag("default");
    }
    public void addTag(String tag){
        drawTags.add(tag);
    }
    public boolean removeTag(String tag){
        return drawTags.remove(tag);
    }
    public void setFOV(float mFOV) {
        this.mFOV = mFOV;
        float ratio = 1.0f * width / height;
        perspectiveM(mFOV, ratio, near, far);
//        if(near!=far||mAspect!=0){
//            top= (float) (Math.tan(Math.toRadians(mFOV/2))*near);
//            top=Math.abs(top);
//            bottom=-top;
//            right=top*mAspect;
//            left=-right;
//        }
    }

    public float getFOV() {
        return mFOV;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }

    public float getBottom() {
        return bottom;
    }

    public float getNear() {
        return near;
    }

    public float getFar() {
        return far;
    }

    public float[] getMVPMatrix() {
        return mMVPMatrix;
    }

    public void draw(List<GLObject> models){

        for (GLObject o:models) {
            if(!drawTags.contains(o.getTag())){
                continue;
            }
            if(o instanceof Object3D){
                ((Object3D) o).draw(calcMVPMatrix(o.getFinalModelArray()));
            }else if(o instanceof Prefab){
                draw((Prefab)o);
            }
        }
    }
    public void drawOneObject(GLObject glObject){
        if(glObject instanceof Object3D){
            ((Object3D) glObject).draw(calcMVPMatrix(glObject.getFinalModelArray()));
        }else if(glObject instanceof Prefab){
            draw((Prefab)glObject);
        }
    }
    public void draw(Prefab prefab){
        for (GLObject object:prefab.childs()) {
            if(object instanceof Object3D){
                ((Object3D) object).draw(calcMVPMatrix(object.getFinalModelArray()));
            }else if(object instanceof Prefab){
                draw((Prefab) object);
            }
        }
    }
    protected float[] calcMVPMatrix(float[] ModelMatrix)
    {
        Matrix.multiplyMM(mMVPMatrix, 0, getFinalVArray(), 0, ModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix.getM(), 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }
    /**
     * VMatrix
     * 获取相机9参数矩阵
     * @return
     */
    public Matrix4 getProjMatrix() {
        return mProjMatrix;
    }
    public Matrix4 getVMatrix(){
        return mVMatrix;
    }
    public float[] getProjArray(){
        return mProjMatrix.getM();
    }
    public float[] getVArray(){
        return mVMatrix.getM();
    }
    public float[] getFinalVArray(){
        transform.tmpMatrix.multiplyMM(getFinalModelMatrix(),mVMatrix);
        return transform.tmpMatrix.getM();
    }
    public Matrix4 getFinalVMatrix(){
        transform.tmpMatrix.multiplyMM(getFinalModelMatrix(),mVMatrix);
        return transform.tmpMatrix;
    }
    //重写getFinalModelXXX()这两个方法是因为，物体的防射变换一般效果是需要先旋转再平移，而相机相反需要先平移在旋转。
    @Override
    public Matrix4 getFinalModelMatrix() {
        if (mParent != null) {
            transform.tmpMatrix.multiplyMM(transform.mModelMatrix,mParent.getFinalModelMatrix());
        } else {
            transform.tmpMatrix.copyFrom(transform.mModelMatrix);
        }
        return transform.tmpMatrix;
    }

    @Override
    public float[] getFinalModelArray() {
        if (mParent != null) {
            transform.tmpMatrix.multiplyMM(transform.mModelMatrix,mParent.getFinalModelMatrix());
            return transform.tmpMatrix.getM();
        } else {
            transform.tmpMatrix.copyFrom(transform.mModelMatrix);
            return transform.tmpMatrix.getM();
        }
    }

    public void setLookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ){
        mVMatrix.setLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }
    public void perspectiveM(float fovy, float aspect, float zNear, float zFar){
        mProjMatrix.perspectiveM(fovy,aspect,zNear,zFar);
        mFOV=fovy;
        mAspect=aspect;
        getFrustumParamFromPerspective(fovy,aspect,zNear,zFar);
    }
    public boolean perspectiveParse(float[] r){
        return com.mcmo.mcmo3d.gl.math.matrix.Matrix.perspectiveParse(r,mProjMatrix.getM(),0);
    }
    private void getFrustumParamFromPerspective(float fovy,float aspect,float zNear,float zFar){
        float halfH= (float) (Math.tan(Math.toRadians(fovy/2))*zNear);
        halfH=Math.abs(halfH);
        top=halfH;
        bottom=-halfH;
        float halfW=halfH*aspect;
        left=-halfW;
        right=halfW;
        this.near=zNear;
        this.far=zFar;

    }
    public void frustumM(float left,float right,float bottom,float top,float near,float far){
        mProjMatrix.frustumM(left, right, bottom, top, near, far);
        this.left=left;
        this.right=right;
        this.bottom=bottom;
        this.top=top;
        this.near=near;
        this.far=far;
        mAspect=right/left;
    }
    public void orthoM(float left,float right,float bottom,float top,float near,float far){
        mProjMatrix.orthoM(0,left,right,bottom,top,near,far);
        this.left=left;
        this.right=right;
        this.bottom=bottom;
        this.top=top;
        this.near=near;
        this.far=far;
    }

    public void onSizeChange(float width,float height,boolean defaultMatrix){
        this.width=width;
        this.height=height;
        if(defaultMatrix){
            setDefaultMatrix();
        }
    }
    /**
     * 设置成常用的透视矩阵，相机为事件坐标中心，相机up指向y轴正方形，lookat向z轴正方向
     */
    public boolean setDefaultMatrix(){
        if(width==0||height==0){
            return false;
        }else{
            setLookAt(0,0,0,0,0,1,0,1,0);
            float ratio=1.0f*width/height;
            frustumM(-ratio,ratio,-1,1,1,100);
            return true;
        }
    }

    public void clear(){}

}
