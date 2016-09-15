package com.uwsoft.editor.renderer.components.particle;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Pool;

public class ParticleComponent implements Component,Pool.Poolable {
	public String particleName = "";
	public ParticleEffect particleEffect;
	public float worldMultiplyer = 1f;
	private float scaleFactor = 1f;

	public void scaleEffect(float scale){
		scaleFactor = scale;
		particleEffect.scaleEffect(scaleFactor*worldMultiplyer);
	}

	public float getScaleFactor(){
		return scaleFactor;
	}

	//please use this method to start effects for the scale to be applied
	public void startEffect(){
		scaleEffect(scaleFactor);
		particleEffect.start();
	}

	@Override
	public void reset() {
		particleName = "";
		particleEffect=null;
		worldMultiplyer = 1f;
		scaleFactor = 1f;
	}
}
