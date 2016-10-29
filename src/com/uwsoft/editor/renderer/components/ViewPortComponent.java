package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ViewPortComponent implements Component,Pool.Poolable {
	public Viewport viewPort;

	@Override
	public void reset() {
		viewPort=null;
	}
}
