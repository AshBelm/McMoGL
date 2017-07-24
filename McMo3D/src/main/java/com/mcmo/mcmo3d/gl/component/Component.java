package com.mcmo.mcmo3d.gl.component;

import com.mcmo.mcmo3d.gl.geometry.graphic.GLObject;

/**
 * Created by ZhangWei on 2017/2/17.
 */

public interface Component {
    public void create(GLObject o);
    public void destroy();
}
