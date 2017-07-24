package com.mcmo.mcmo3d.gl.util;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by weizhang210142 on 2016/6/2.
 */
public class GLU {
//    private static volatile boolean isGlChecked = false;
//    private static int mEGLMajorVersion;
//    private static int mEGLMinorVersion;
//    private static int mGLESMajorVersion;
    /**
     * Return an error string from a GL or GLU error code.
     *
     * @param error - a GL or GLU error code.
     * @return the error string for the input error code, or NULL if the input
     *         was not a valid GL or GLU error code.
     */
    public static String gluErrorString(int error) {
        switch (error) {
            case GL10.GL_NO_ERROR:
                return "no error";
            case GL10.GL_INVALID_ENUM:
                return "invalid enum";
            case GL10.GL_INVALID_VALUE:
                return "invalid value";
            case GL10.GL_INVALID_OPERATION:
                return "invalid operation";
            case GL10.GL_STACK_OVERFLOW:
                return "stack overflow";
            case GL10.GL_STACK_UNDERFLOW:
                return "stack underflow";
            case GL10.GL_OUT_OF_MEMORY:
                return "out of memory";
            default:
                return null;
        }
    }
//    private static void checkGLVersion(){
//        final EGL10 egl = (EGL10) EGLContext.getEGL();
//        final EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
//
//        final int[] version = new int[2];
//    }
}
