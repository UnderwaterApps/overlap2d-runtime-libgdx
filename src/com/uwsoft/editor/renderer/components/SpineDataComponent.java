package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class SpineDataComponent implements Component,Pool.Poolable {
	public String animationName = "";
	public String currentAnimationName = "";

	@Override
	public void reset() {
		animationName="";
		currentAnimationName="";
	}
}
