package com.mcmo.mcmo3d.gl.render;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.mcmo.mcmo3d.gl.camera.Camera;
import com.mcmo.mcmo3d.gl.geometry.graphic.GLObject;
import com.mcmo.mcmo3d.gl.material.MaterialManager;
import com.mcmo.mcmo3d.gl.util.DebugUtil;
import com.mcmo.mcmo3d.gl.util.GLTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ZhangWei on 2017/1/16.
 */

public abstract class Scene {

    private List<GLObject> models = Collections.synchronizedList(new CopyOnWriteArrayList<GLObject>());
    private Camera mMainCamera;//场景主相机
    private List<Camera> mCameras;//其他相机
    private boolean created = false;
    private LinkedList<GLTask> mGLTasks = new LinkedList<>();
    protected GlRender mRender;

    public void setRender(GlRender render) {
        this.mRender = render;
    }

    protected Context getContext() {
        return mRender.getContext();
    }

    public boolean isCreated() {
        return created;
    }

    public List<GLObject> getModels() {
        return models;
    }

    protected void onPreCreate(Context context){
    }
    public abstract void onCreate(Context context);
    protected void onPostCreate(Context context) {
        DebugUtil.logDebug("Scene","life cycle","Scene Created");
        created = true;
    }
    public void onResume(){
        DebugUtil.logDebug("Scene","life cycle","Scene Resume");
    }
    public void onPause(){
        DebugUtil.logDebug("Scene","life cycle","Scene Pause");
    }
    public void onRender(int settingRate, float realRate) {
        for (GLObject o:models){
            o.onFrameUpdate(settingRate);
        }
        // TODO: 2017/7/28 是否要给相机一个顺序的设置？还是依照物体的z顺序再去相应的相机来画 
        if(mMainCamera!=null&&mMainCamera.isEnable()){
            mMainCamera.onFrameUpdate(settingRate);
            mMainCamera.draw(models);
        }
        if(mCameras!=null){
            for (Camera camera:mCameras) {
                camera.onFrameUpdate(settingRate);
                camera.draw(models);
            }
        }
    }

    public void onDestroy(){
        DebugUtil.logDebug("Scene","life cycle","Scene destroy");
        clearScene();
    }

    public void clearScene(){
        for (GLObject o:models) {
            o.destroy(getContext());
        }
        models.clear();
        MaterialManager.getInstance().clear();
        mMainCamera.clear();
    }

    public void addToScene(GLObject object) {
        addGLObject(object);
    }
    public void addCamera(final Camera camera){
        if(mCameras==null){
            mCameras = new ArrayList<>();
        }
        if(!mCameras.contains(camera)){
            mCameras.add(camera);
        }
        GLTask task = new GLTask() {
            @Override
            public void execute() {
                camera.create(getContext());
            }
        };
        offerTask(task);
    }
    public void setMainCamera(final Camera camera) {
        mMainCamera = camera;
        GLTask task=new GLTask() {
            @Override
            public void execute() {
                camera.create(getContext());
            }
        };
        offerTask(task);
    }

    public Camera getMainCamera() {
        return mMainCamera;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void onSizeChange(int width, int height){
        DebugUtil.logDebug("Scene","life cycle","Scene onSizeChange width="+width+" height="+height);
    }

    //<editor-fold desc="Task method">
    protected void executeGLTask(){
        synchronized (mGLTasks){
            GLTask task=mGLTasks.poll();
            while (task!=null){
                task.execute();
                task=mGLTasks.poll();
            }
        }
    }
    public void offerTask(GLTask task){
        synchronized (mGLTasks){
            mGLTasks.offer(task);
        }
    }
    private void addGLObject(final GLObject object){
        final GLTask task=new GLTask() {
            @Override
            public void execute() {
                object.create(getContext());
                models.add(object);
            }
        };
        offerTask(task);
    }
    public boolean removeFromScene(int id){
        return removeObject(id);
    }
    private boolean removeObject(int id){
        for (int i = 0; i < models.size(); i++) {
            if(models.get(i).getId()==id){
                GLObject object=models.remove(i);
                object.destroy(getContext());
                return true;
            }
        }
        return false;
    }
    public boolean removeFromScene(GLObject object){
        return removeObject(object);
    }
    private boolean removeObject(GLObject object){
        if(models.contains(object)){
            models.remove(object);
            object.destroy(getContext());
            return true;
        }
        return false;
    }
    //</editor-fold>

    public void created(SurfaceHolder holder){

    }
}
