package com.mcmo.mcmo3d.gl.material;

import com.mcmo.mcmo3d.gl.texture.ATexture;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ZhangWei on 2017/2/7.
 */

public class TextureManager {
    private static TextureManager instance;
    private List<ATexture> textures;

    public static TextureManager getInstance() {
        if (instance == null)
            instance = new TextureManager();
        return instance;
    }

    private TextureManager() {
        textures = Collections.synchronizedList(new CopyOnWriteArrayList<ATexture>());
    }

    public void addTexture(ATexture t) {
        boolean contain = false;
        for (int i = 0, N = textures.size(); i < N; i++) {
            if (textures.get(i).getTextureId() == t.getTextureId()) {
                contain = true;
                break;
            }
        }
        if (!contain)
            textures.add(t);
    }
    public void removeTexture(ATexture t){
        for (int i = 0; i < textures.size(); i++) {
            if(textures.get(i).equals(t)){
                ATexture a=textures.remove(i);
                a.release();
                break;
            }
        }
    }
}
