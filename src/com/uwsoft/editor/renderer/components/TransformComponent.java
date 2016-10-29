package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

public class TransformComponent implements Component,Pool.Poolable {
	public float x; 
	public float y;
	public float scaleX	=	1f; 
	public float scaleY	=	1f;
	public float rotation;
	public float originX;
	public float originY;

	private TransformComponent backup = null;

	public TransformComponent() {

	}

	public TransformComponent(TransformComponent component) {
		x = component.x;
		y = component.y;
		scaleX = component.scaleX;
		scaleY = component.scaleY;
		rotation = component.rotation;
		originX = component.originX;
		originY = component.originY;
	}

	public void disableTransform() {
		backup = new TransformComponent(this);
		x = 0;
		y = 0;
		scaleX = 1f;
		scaleY = 1f;
		rotation = 0;
	}

	public void enableTransform() {
		if(backup == null) return;
		x = backup.x;
		y = backup.y;
		scaleX = backup.scaleX;
		scaleY = backup.scaleY;
		rotation = backup.rotation;
		originX = backup.originX;
		originY = backup.originY;
		backup = null;
	}

	@Override
	public void reset() {
		x=0;
		y=0;
		scaleX	=	1f;
		scaleY	=	1f;
		rotation=0;
		originX=0;
		originY=0;

		backup = null;
	}
}
