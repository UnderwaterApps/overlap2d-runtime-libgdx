package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

public class TintComponent implements Component,Pool.Poolable {
	public Color color = new Color();

	@Override
	public void reset() {
		color.set(0f,0f,0f,0f);
	}
}
