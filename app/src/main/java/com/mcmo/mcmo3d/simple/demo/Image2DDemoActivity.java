package com.mcmo.mcmo3d.simple.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;

import com.mcmo.mcmo3d.gl.camera.Camera2D;
import com.mcmo.mcmo3d.gl.geometry.graphic.ScreenPlane;
import com.mcmo.mcmo3d.gl.material.Material;
import com.mcmo.mcmo3d.gl.render.Scene;
import com.mcmo.mcmo3d.gl.shader.UnlightTextureShader;
import com.mcmo.mcmo3d.gl.view.MMSurFaceView;
import com.mcmo.mcmo3d.simple.R;

/**
 * Created by ZhangWei on 2017/2/21.
 */

public class Image2DDemoActivity extends Activity {
    private MMSurFaceView surFaceView;
    private Scene mScene;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surFaceView=new MMSurFaceView(this);
        setContentView(surFaceView);
        mScene=new Image2DScene();
        surFaceView.setScene(mScene);

    }

    private class Image2DScene extends Scene{
        private Camera2D mCamera2D;
//        private Plane mPlane;
        private ScreenPlane screenPlane;
        @Override
        public void onCreate(Context context) {
            screenPlane=new ScreenPlane(context);

            mCamera2D=new Camera2D();
            mCamera2D.setLookAt(0,0,0,0,0,1,0,1,0);
            addCamera(mCamera2D);
//
//            mPlane=new Plane(context, Axis.Z,2,2);
//            mPlane.transform(0,0,2);
            Material material=new Material();
            Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.girl1);

            UnlightTextureShader shader=new UnlightTextureShader(context.getResources(), bitmap);
            material.setShader(shader);
//            mPlane.setMaterial(material);
//
//            addToScene(mPlane);
            screenPlane.setMaterial(material);
            addToScene(screenPlane);
        }

        @Override
        public void onSizeChange(int width, int height) {
            mCamera2D.onSizeChange(width, height,true);
            screenPlane.onSizeChange(width, height);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return super.onTouchEvent(event);
        }


    }

}
