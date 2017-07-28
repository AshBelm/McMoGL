package com.mcmo.mcmo3d.gl.geometry.graphic.prefab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;

import com.mcmo.mcmo3d.gl.geometry.graphic.Plane;
import com.mcmo.mcmo3d.gl.material.Material;
import com.mcmo.mcmo3d.gl.math.Axis;
import com.mcmo.mcmo3d.gl.shader.UnlightTextureShader;

/**
 * Created by ZhangWei on 2017/2/10.
 */

public class Text2D extends Prefab{
    private Plane planeText;
    private String text;
    private float textSize;
    private final float GRAPH_SCALE = 0.025f;
    private float mWidth,mHeight;
    private float mPaddingLeft,mPaddingRight,mPaddingTop,mPaddingBottom;
    private int mBgColor;

    public Text2D(Context context, String text) {
        this(context,text,20);
    }
    public Text2D(Context context, String text, float textSize) {
        this(context,text,textSize,0xff000000);
    }
    public Text2D(Context context, String text, float textSize,int bgColor) {
        this(context,text,textSize,bgColor,0,0,0,0);
    }
    public Text2D(Context context, String text, float textSize,int bgColor,int paddingLeft,int paddingRight,int paddingTop,int paddingBottom) {
        this.text=text;
        this.textSize=textSize;
        this.mBgColor=bgColor;
        this.mPaddingBottom=paddingBottom;
        this.mPaddingTop = paddingTop;
        this.mPaddingLeft = paddingLeft;
        this.mPaddingRight = paddingRight;

        TextPaint paint=new TextPaint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setColor(0xffffffff);
        Paint.FontMetrics fm=paint.getFontMetrics();

        int height= (int) (fm.bottom-fm.top);
        int width= (int) paint.measureText(text);
        width+=paddingLeft;
        width+=paddingRight;
        height+=paddingBottom;
        height+=paddingTop
        ;
        float descent=fm.descent;
        Bitmap bitmap=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        canvas.drawColor(bgColor);
        canvas.drawText(text,paddingLeft,height-descent+paddingBottom,paint);
        canvas.save();

        UnlightTextureShader tShader=new UnlightTextureShader(context.getResources(),bitmap);
        Material tMaterial=new Material();
        tMaterial.setShader(tShader);

        mWidth=width*GRAPH_SCALE;
        mHeight=height*GRAPH_SCALE;

        planeText=new Plane(Axis.Z,mWidth,mHeight);
        planeText.setMaterial(tMaterial);
        planeText.setTransparent(true);

        addChild(planeText);
    }
    public void changeText(String text){
        TextPaint paint=new TextPaint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setColor(0xffffffff);
        Paint.FontMetrics fm=paint.getFontMetrics();

        int height= (int) (fm.bottom-fm.top);
        int width= (int) paint.measureText(text);
        width+=mPaddingLeft;
        width+=mPaddingRight;
        height+=mPaddingBottom;
        height+=mPaddingTop
        ;
        float descent=fm.descent;
        Bitmap bitmap=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        canvas.drawColor(mBgColor);
        canvas.drawText(text,mPaddingLeft,height-descent+mPaddingBottom,paint);
        canvas.save();
        UnlightTextureShader shader= (UnlightTextureShader) planeText.getMaterial().getShader();
        shader.changeTexture(bitmap);
        bitmap=null;
    }

    @Override
    public void setCullFace(boolean FACE_CULL) {
        planeText.setCullFace(FACE_CULL);
    }

    public float getWidth() {
        return mWidth;
    }

    public float getHeight() {
        return mHeight;
    }
}
