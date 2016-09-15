package com.uwsoft.editor.renderer.components.light;

import box2dLight.Light;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.uwsoft.editor.renderer.data.LightVO.LightType;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;

public class LightObjectComponent implements Component,Pool.Poolable {

	public LightType type;

	public int rays = 12;
	public float distance = 300;
	public float directionDegree = 0;
	public float coneDegree = 30;
	public float softnessLength = 1f;
	public boolean isStatic = true;
	public boolean isXRay = true;
	public Light lightObject = null;

	public LightObjectComponent(LightType type) {
		this.type = type;
	}

	public LightObjectComponent(){

	}

	public LightType getType(){
		return type;
	}

	@Override
	public void reset() {
		rays = 12;
		distance = 300;
		directionDegree = 0;
		coneDegree = 30;
		softnessLength = 1f;
		isStatic = true;
		isXRay = true;
		lightObject = null;
	}
}
