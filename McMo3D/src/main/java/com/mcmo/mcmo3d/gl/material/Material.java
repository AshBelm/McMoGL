package com.mcmo.mcmo3d.gl.material;

import com.mcmo.mcmo3d.gl.geometry.graphic.Object3D;
import com.mcmo.mcmo3d.gl.shader.AShader;

import java.nio.FloatBuffer;

/**
 * Created by ZhangWei on 2017/1/23.
 */

public class Material {
    private String mSceneName;
    private AShader shader;

    public Material() {
    }

    public void create(){
        shader.create();
        MaterialManager.getInstance().addMaterial(this);
    }

    public Material(AShader shader) {
        this.shader = shader;
    }

    public void setSceneName(String mSceneName) {
        this.mSceneName = mSceneName;
    }

    public String getSceneName() {
        return mSceneName;
    }

    public void setShader(AShader shader) {
        this.shader = shader;
    }

    public AShader getShader() {
        return shader;
    }

    public void draw(float[] mvpMatrix, FloatBuffer vertexBuffer, FloatBuffer textureBuffer, Object3D object3D){
        shader.draw(mvpMatrix, vertexBuffer, textureBuffer, object3D);
    }
    public void update(int refreshFrameRate){
        shader.update(refreshFrameRate);
    }
    public void destroy(){
        if(shader!=null){
            shader.destroy();
        }
    }
}
