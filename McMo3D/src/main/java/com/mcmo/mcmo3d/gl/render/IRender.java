package com.mcmo.mcmo3d.gl.render;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ZhangWei on 2017/3/24.
 */

public interface IRender {
    public void onSurfaceCreated(GL10 gl, EGLConfig config);

    public void onSurfaceChanged(GL10 gl, int width, int height);

    public void onDrawFrame(GL10 gl);

    public void onSurfaceDestroy();

    public void onResume();

    public void onDestroy();
}
