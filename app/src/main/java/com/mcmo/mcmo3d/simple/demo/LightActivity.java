package com.mcmo.mcmo3d.simple.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.os.Bundle;

import com.mcmo.mcmo3d.gl.camera.Camera;
import com.mcmo.mcmo3d.gl.geometry.graphic.Sphere;
import com.mcmo.mcmo3d.gl.material.Material;
import com.mcmo.mcmo3d.gl.render.Scene;
import com.mcmo.mcmo3d.gl.shader.AmbientTextureShader;
import com.mcmo.mcmo3d.gl.view.MMSurFaceView;
import com.mcmo.mcmo3d.simple.R;

/**
 * Created by ZhangWei on 2017/4/17.
 */

public class LightActivity extends Activity{
    MMSurFaceView surFaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surFaceView=new MMSurFaceView(this);
        setContentView(surFaceView);
        surFaceView.setScene(new MyScene());
    }
    private class MyScene extends Scene{
        Camera camera;
        Sphere sphere;
        @Override
        public void onCreate(Context context) {
            camera=new Camera();
            addCamera(camera);
            sphere = new Sphere(context,10);
            sphere.transform(0,0,20);
            Material material=new Material();
            AmbientTextureShader shader=new AmbientTextureShader(context.getResources(), BitmapFactory.decodeResource(context.getResources(), R.drawable.earth));
            material.setShader(shader);
            sphere.setMaterial(material);
            addToScene(sphere);
        }

        @Override
        public void onSizeChange(int width, int height) {
            super.onSizeChange(width, height);
            GLES20.glViewport(0,0,width,height);
            camera.onSizeChange(width,height,true);
        }
    }
}
