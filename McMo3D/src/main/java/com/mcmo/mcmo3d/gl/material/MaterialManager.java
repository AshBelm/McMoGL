package com.mcmo.mcmo3d.gl.material;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ZhangWei on 2017/3/22.
 */

public class MaterialManager {
    private static MaterialManager instance = null;
    private List<Material> mMaterials;
    private MaterialManager(){
        mMaterials= Collections.synchronizedList(new CopyOnWriteArrayList<Material>());
    }
    public static MaterialManager getInstance(){
        if(instance==null){
            instance=new MaterialManager();
        }
        return instance;
    }

    /**
     *
     * @param material
     * @return if material exist return false else true;
     */
    public boolean addMaterial(Material material){
        if(!mMaterials.contains(material)){
            mMaterials.add(material);
            return true;
        }
        return false;
    }
    public boolean removeMaterial(Material material){
        return mMaterials.remove(material);
    }
    public void clear(){
        mMaterials.clear();
    }

}
