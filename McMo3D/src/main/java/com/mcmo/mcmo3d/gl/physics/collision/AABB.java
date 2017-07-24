package com.mcmo.mcmo3d.gl.physics.collision;

import android.opengl.Matrix;

import com.mcmo.mcmo3d.gl.math.vector.Vector3;

/**
 * Created by ZhangWei on 2017/3/6.
 */

public class AABB implements Collider {
    public Vector3 max;
    public Vector3 min;

    public AABB() {
        min=new Vector3();
        max=new Vector3();
        reset();
    }
    public AABB(float[] vertex){
        min=new Vector3();
        max=new Vector3();
        generateCollideBox(vertex);
    }
    @Override
    public void generateCollideBox(float[] vertex) {
        if(vertex.length%3!=0){
            throw new RuntimeException("生成AABB包围盒传人数组长度必须是3的倍数");
        }
        reset();
        for (int i = 0,N=vertex.length; i < N; i++) {
            update(vertex[i],vertex[++i],vertex[++i]);
        }
    }
    public void reset(){
        min.x = min.y = min.z = Float.POSITIVE_INFINITY;//将最小点设为最大值
        max.x = max.y = max.z = Float.NEGATIVE_INFINITY;//将最大点设为最小值
    }
    //获取AABB所有顶点坐标的方法
    public Vector3[] getAllCorners(){
        Vector3[] result = new Vector3[8];
        for(int i=0; i<8; i++){
            result[i] = getCorner(i);
        }
        return result;
    }
    //获取AABB第i个顶点坐标的方法
    public Vector3 getCorner(int i){
        if(i<0||i>7){//检查i是否合法
            return null;
        }
        return new Vector3(
                ((i & 1) == 0) ? max.x : min.x,
                ((i & 2) == 0) ? max.y : min.y,
                ((i & 4) == 0) ? max.z : min.z
        );
    }
    //通过当前仿射变换矩阵求得仿射变换后的AABB包围盒的方法
    public AABB setToTransformedBox(float[] m)
    {
        //获取所有顶点的坐标
        Vector3[] va = this.getAllCorners();
        //用于存放仿射变换后的顶点数组
        float[] transformedCorners=new float[24];
        //将变换前的AABB包围盒的8个顶点与仿射变换矩阵m相乘，得到仿射变换后的OBB包围盒的所有顶点
        float[] tmpResult=new float[4];
        int count=0;
        for(int i=0;i<va.length;i++){
            float[] point=new float[]{va[i].x,va[i].y,va[i].z,1};//将顶点转换成齐次坐标
            Matrix.multiplyMV(tmpResult, 0, m, 0, point, 0);
            transformedCorners[count++]=tmpResult[0];
            transformedCorners[count++]=tmpResult[1];
            transformedCorners[count++]=tmpResult[2];
        }
        //通过构造器将OBB包围盒转换成AABB包围盒，并返回
        return new AABB(transformedCorners);
    }
    public boolean rayIntersect2(Vector3 rayStart,Vector3 dir){
        float tMin= 0.0f;
        float tMax=Float.MIN_VALUE;
        float ood,t1,t2;
        //X
        if(Math.abs(dir.x)==0.0f){
            return false;
        }else {
            ood=1.0f/dir.x;
            t1=(min.x-rayStart.x)*ood;
            t2=(max.x-rayStart.x)*ood;
            if(t1>t2){
                float temp = t1;
                t1=t2;
                t2=temp;
            }
            if(t1>tMin) tMin=t1;
            if(t2<tMax) tMax=t2;

            if(tMin > tMax) return false ;         }
        //Y
        if(Math.abs(dir.y)==0.0f){
            return false;
        }else {
            ood=1.0f/dir.y;
            t1=(min.y-rayStart.y)*ood;
            t2=(max.y-rayStart.y)*ood;
            if(t1>t2){
                float temp = t1;
                t1=t2;
                t2=temp;
            }
            if(t1>tMin) tMin=t1;
            if(t2<tMax) tMax=t2;

            if(tMin > tMax) return false ;
        }
        //Z
        if(Math.abs(dir.z)==0.0f){
            return false;
        }else {
            ood=1.0f/dir.z;
            t1=(min.z-rayStart.z)*ood;
            t2=(max.z-rayStart.z)*ood;
            if(t1>t2){
                float temp = t1;
                t1=t2;
                t2=temp;
            }
            if(t1>tMin) tMin=t1;
            if(t2<tMax) tMax=t2;

            if(tMin > tMax) return false ;
        }
        return true;
    }
    /*
	 * Woo提出的方法，先判断矩形边界框的哪个面会相交，
	 * 再检测射线与包含这个面的平面的相交性。
	 * 如果交点在盒子中，那么射线与矩形边界框相交，
	 * 否则不存在相交
	 */
    //和参数射线的相交性测试，如果不相交则返回值是一个非常大的数(大于1)
    //如果相交，返回相交时间t
    //t为0-1之间的值
    public float rayIntersect(
            Vector3 rayStart,//射线起点
            Vector3 rayDir,//射线长度和方向
            Vector3 returnNormal//可选的，相交点处法向量
    ){
        //如果未相交则返回这个大数
        final float kNoIntersection = Float.POSITIVE_INFINITY;
        //检查点在矩形边界内的情况，并计算到每个面的距离
        boolean inside = true;
        float xt, xn = 0.0f;
        if(rayStart.x<min.x){
            xt = min.x - rayStart.x;
            if(xt>rayDir.x){ return kNoIntersection; }
            xt /= rayDir.x;
            inside = false;
            xn = -1.0f;
        }
        else if(rayStart.x>max.x){
            xt = max.x - rayStart.x;
            if(xt<rayDir.x){ return kNoIntersection; }
            xt /= rayDir.x;
            inside = false;
            xn = 1.0f;
        }
        else{
            xt = -1.0f;
        }

        float yt, yn = 0.0f;
        if(rayStart.y<min.y){
            yt = min.y - rayStart.y;
            if(yt>rayDir.y){ return kNoIntersection; }
            yt /= rayDir.y;
            inside = false;
            yn = -1.0f;
        }
        else if(rayStart.y>max.y){
            yt = max.y - rayStart.y;
            if(yt<rayDir.y){ return kNoIntersection; }
            yt /= rayDir.y;
            inside = false;
            yn = 1.0f;
        }
        else{
            yt = -1.0f;
        }

//        float zt, zn = 0.0f;
//        if(rayStart.z<min.z){
//            zt = min.z - rayStart.z;
//            if(zt>rayDir.z){ return kNoIntersection; }
//            zt /= rayDir.z;
//            inside = false;
//            zn = -1.0f;
//        }
//        else if(rayStart.z>max.z){
//            zt = max.z - rayStart.z;
//            if(zt<rayDir.z){ return kNoIntersection; }
//            zt /= rayDir.z;
//            inside = false;
//            zn = 1.0f;
//        }
//        else{
//            zt = -1.0f;
//        }
        //是否在矩形边界框内？
        if(inside){
            if(returnNormal != null){
                returnNormal = rayDir.multiK(-1);
                returnNormal.normalize();
            }
            return 0.0f;
        }
        //选择最远的平面————发生相交的地方
        int which = 0;
        float t = xt;
        if(yt>t){
            which = 1;
            t=yt;
        }
//        if(zt>t){
//            which = 2;
//            t=zt;
//        }
        switch(which){
            case 0://和yz平面相交
            {
                float y=rayStart.y+rayDir.y*t;
                if(y<min.y||y>max.y){return kNoIntersection;}
                float z=rayStart.z+rayDir.z*t;
                if(z<min.z||z>max.z){return kNoIntersection;}
                if(returnNormal != null){
                    returnNormal.x = xn;
                    returnNormal.y = 0.0f;
                    returnNormal.z = 0.0f;
                }
            }
            break;
            case 1://和xz平面相交
            {
                float x=rayStart.x+rayDir.x*t;
                if(x<min.x||x>max.x){return kNoIntersection;}
                float z=rayStart.z+rayDir.z*t;
                if(z<min.z||z>max.z){return kNoIntersection;}
                if(returnNormal != null){
                    returnNormal.x = 0.0f;
                    returnNormal.y = yn;
                    returnNormal.z = 0.0f;
                }
            }
            break;
            case 2://和xy平面相交
            {
//                float x=rayStart.x+rayDir.x*t;
//                if(x<min.x||x>max.x){return kNoIntersection;}
//                float y=rayStart.y+rayDir.y*t;
//                if(y<min.y||y>max.y){return kNoIntersection;}
//                if(returnNormal != null){
//                    returnNormal.x = 0.0f;
//                    returnNormal.y = 0.0f;
//                    returnNormal.z = zn;
//                }
            }
            break;
        }
        return t;//返回相交点参数值
    }
    private void update(Vector3 p){
        if (p.x < min.x) { min.x = p.x; }
        if (p.x > max.x) { max.x = p.x; }
        if (p.y < min.y) { min.y = p.y; }
        if (p.y > max.y) { max.y = p.y; }
        if (p.z < min.z) { min.z = p.z; }
        if (p.z > max.z) { max.z = p.z; }
    }
    private void update(float x,float y,float z){
        if (x < min.x) { min.x = x; }
        if (x > max.x) { max.x = x; }
        if (y < min.y) { min.y = y; }
        if (y > max.y) { max.y = y; }
        if (z < min.z) { min.z = z; }
        if (z > max.z) { max.z = z; }
    }
    @Override
    public String toString() {
        return "AABB{" +
                "max=" + max +
                ", min=" + min +
                '}';
    }
}
