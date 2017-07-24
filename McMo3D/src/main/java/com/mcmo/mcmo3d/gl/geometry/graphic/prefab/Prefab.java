package com.mcmo.mcmo3d.gl.geometry.graphic.prefab;

import android.content.Context;

import com.mcmo.mcmo3d.gl.geometry.graphic.GLObject;

import java.util.ArrayList;

/**
 * Created by ZhangWei on 2017/2/10.
 */

public class Prefab extends GLObject {
    public ArrayList<GLObject> childs(){
        return mChild;
    }

    @Override
    public void create(Context context) {
        if(mChild==null){
            return;
        }
        for (GLObject obj:mChild) {
            obj.create(context);
        }
    }
}
