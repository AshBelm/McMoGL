package com.mcmo.mcmo3d.gl.physics.collision;

import com.mcmo.mcmo3d.gl.math.vector.Vector3;

/**
 * Created by ZhangWei on 2017/3/6.
 */

public interface Collider {
    public void generateCollideBox(float[] vertex);

    public float rayIntersect(Vector3 rayStart,//射线起点
                              Vector3 rayDir,//射线长度和方向
                              Vector3 returnNormal//可选的，相交点处法向量
    );
}
