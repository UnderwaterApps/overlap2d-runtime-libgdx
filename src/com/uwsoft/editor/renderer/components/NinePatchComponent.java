package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.utils.Pool;

public class NinePatchComponent implements Component,Pool.Poolable {
	public String textureRegionName;
	public NinePatch ninePatch;

	@Override
	public void reset() {
		textureRegionName=null;
		ninePatch=null;
	}
}
