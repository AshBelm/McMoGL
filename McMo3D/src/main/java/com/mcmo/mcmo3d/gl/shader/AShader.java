package com.mcmo.mcmo3d.gl.shader;

import android.content.res.Resources;

import com.mcmo.mcmo3d.gl.geometry.graphic.Object3D;
import com.mcmo.mcmo3d.gl.util.ShaderUtil;

import java.nio.FloatBuffer;

/**
 * Created by ZhangWei on 2017/1/23.
 */

public abstract class AShader {
    protected int mProgram;
    protected int mVertexHandler;
    private String str_vertex;
    private String str_fragment;

    public AShader(String vertex, String fragment, Resources resources) {
        str_vertex= ShaderUtil.loadFromAssetsFile(vertex,resources);
        str_fragment = ShaderUtil.loadFromAssetsFile(fragment,resources);
    }
    public AShader(int vertex,int fragment,Resources resources){
        str_vertex=ShaderUtil.loadFromRawFile(vertex,resources);
        str_fragment=ShaderUtil.loadFromRawFile(fragment,resources);
    }
    public void create(){
        this.mProgram = ShaderUtil.createProgram(str_vertex,str_fragment);
        getParam();
    }
    protected abstract void getParam();
    public abstract void draw(float[] mvpMatrix, FloatBuffer vertexBuffer, FloatBuffer textureBuffer, Object3D object3D);
    public void update(int refreshFrameRate){}
    public abstract void destroy();
}
