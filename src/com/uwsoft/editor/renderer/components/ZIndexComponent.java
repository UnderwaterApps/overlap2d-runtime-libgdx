package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ZIndexComponent implements Component,Pool.Poolable {
    private int zIndex = 0;
    public boolean needReOrder = false;
    public String layerName = "";
    public int layerIndex;

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
        needReOrder = true;
    }

    @Override
    public void reset() {
        zIndex = 0;
        needReOrder = false;
        layerName = "";
        layerIndex=0;

    }
}