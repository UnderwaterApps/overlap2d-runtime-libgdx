package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ScissorComponent implements Component,Pool.Poolable {
	public float scissorX; 
	public float scissorY;
	public float scissorWidth; 
	public float scissorHeight;


    @Override
    public void reset() {
        scissorX=0f;
        scissorY=0f;
        scissorWidth=0f;
        scissorHeight=0f;
    }
}
