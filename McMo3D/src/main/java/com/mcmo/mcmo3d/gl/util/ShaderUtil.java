package com.mcmo.mcmo3d.gl.util;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by weizhang210142 on 2016/6/2.
 */
public class ShaderUtil {
    private static final String TAG = "ShaderUtil";

    /**
     * 加载指定着色器
     *
     * @param shaderType
     * @param source
     * @return
     */
    public static int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);//创建一个shader并记录id
        if(shader!=0){                                 //如果创建成功
            //加载shader的源代码
            GLES20.glShaderSource(shader, source);
            //编译shader
            GLES20.glCompileShader(shader);
            int[] compiled =new int[1];
            //获取shader编译情况
            GLES20.glGetShaderiv(shader,GLES20.GL_COMPILE_STATUS,compiled,0);
            if(compiled[0] == 0){//编译失败删除此shader
                Log.e(TAG, "loadShader: Could not compile this shader "+shaderType+" - "+GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader=0;
            }
        }
        return shader;
    }

    /**
     * 创建着色器程序
     * @param vertexSource
     * @param fragmentSource
     * @return
     */
    public static int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader=loadShader(GLES20.GL_VERTEX_SHADER,vertexSource);//加载顶点着色器
        if(vertexShader==0){
            return 0;
        }
        int fragmentShader=loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentSource);//加载片元着色器
        if(fragmentShader==0){
            return 0;
        }
        int program=GLES20.glCreateProgram();//创建程序
        if(program!=0){
            GLES20.glAttachShader(program,vertexShader);//向程序加人顶点着色器
            ShaderUtil.checkGlError("glAttachShader-vertexShader");
            GLES20.glAttachShader(program,fragmentShader);//向程序加入片元着色器
            ShaderUtil.checkGlError("glAttachShader-fragmentShader");
            GLES20.glLinkProgram(program);//连接程序
            int[] linkStatus=new int[1];
            GLES20.glGetProgramiv(program,GLES20.GL_LINK_STATUS,linkStatus,0);//获取连接状态
            if(linkStatus[0]!=GLES20.GL_TRUE){
                Log.e(TAG, "createProgram Could not link program "+GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);//删除程序
                program=0;
            }
        }
        return program;
    }

    /**
     * 检查操作是否有错误
     * @param operation
     */
    public static void checkGlError(String operation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, "checkGlError: " + operation + " glError:" + GLU.gluErrorString(error) + " " + error);
            throw new RuntimeException("OpenGL Error: " + GLU.gluErrorString(error) + " " + error);
        }
    }

    /**
     * 从shader中加载着色器内容的方法
     * @param name
     * @param res
     * @return
     */
    public static String loadFromAssetsFile(String name, Resources res) {
        String result = null;
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            is = res.getAssets().open(name);
            int ch = 0;
            bos = new ByteArrayOutputStream();
            while ((ch = is.read()) != -1) {
                bos.write(ch);
            }
            byte[] buff = bos.toByteArray();
            result=new String(buff,"UTF-8");
            result=result.replaceAll("\\r\\n","\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (bos != null) {
                    bos.close();
                    bos = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static String loadFromRawFile(int resId, Resources res) {
        String result = null;
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            is = res.openRawResource(resId);
            int ch = 0;
            bos = new ByteArrayOutputStream();
            while ((ch = is.read()) != -1) {
                bos.write(ch);
            }
            byte[] buff = bos.toByteArray();
            result=new String(buff,"UTF-8");
            result=result.replaceAll("\\r\\n","\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (bos != null) {
                    bos.close();
                    bos = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}

