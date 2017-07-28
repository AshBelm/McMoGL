package com.mcmo.mcmo3d.gl.geometry.graphic.prefab;

import android.content.Context;
import android.graphics.Bitmap;

import com.mcmo.mcmo3d.gl.geometry.graphic.Line;
import com.mcmo.mcmo3d.gl.geometry.graphic.Plane;
import com.mcmo.mcmo3d.gl.material.Material;
import com.mcmo.mcmo3d.gl.math.Axis;
import com.mcmo.mcmo3d.gl.math.vector.Vector3;
import com.mcmo.mcmo3d.gl.shader.UnlightColorShader;
import com.mcmo.mcmo3d.gl.shader.UnlightTextureShader;

/**
 * Created by ZhangWei on 2017/2/21.
 */

public class CommentMark extends Prefab {
    private Text2D text2D;
    private Plane mAvatar;
    private Line line;
    private float mWidth,mHeight;

    public CommentMark(Context context,String text,Bitmap bitmap) {
        UnlightTextureShader shader=new UnlightTextureShader(context.getResources(),bitmap);
        init(context, text, shader);
    }

    private void init(Context context, String text, UnlightTextureShader shader) {
        text2D= new Text2D(context,text,80,0x5500f00f,10,0,0,0);
        mAvatar=new Plane(Axis.Z,text2D.getHeight(),text2D.getHeight());
        line=new Line(Vector3.ZERO(),new Vector3(0,3,0));
        Material mLine=new Material();
        UnlightColorShader sLine=new UnlightColorShader(context.getResources());
        sLine.setColor(0xff000000);
        mLine.setShader(sLine);
        line.setMaterial(mLine);

        mWidth=text2D.getWidth()+text2D.getHeight();
        mHeight=text2D.getHeight()+3;
        float halfW=text2D.getHeight()/2;
        float tranX_text=-halfW-text2D.getWidth()/2;

        text2D.transform(tranX_text,3+text2D.getHeight()/2,0);
        mAvatar.transform(0,3+text2D.getHeight()/2,0);
        addChild(mAvatar);
        addChild(text2D);
        addChild(line);
        Material material=new Material();
        material.setShader(shader);
        mAvatar.setMaterial(material);
    }
}
