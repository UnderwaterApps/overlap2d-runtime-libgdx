package com.uwsoft.editor.renderer.components.sprite;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Pool;

public class AnimationComponent implements Component,Pool.Poolable {
	public HashMap<String,Animation> animations = new  HashMap<String,Animation>();

	@Override
	public void reset() {
		animations.clear();
	}
}
