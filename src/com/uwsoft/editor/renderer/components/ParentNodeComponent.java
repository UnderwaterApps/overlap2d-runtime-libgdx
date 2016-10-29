package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class ParentNodeComponent implements Component,Pool.Poolable {
	public Entity parentEntity = null;

	@Override
	public void reset() {
		parentEntity=null;
	}
}
