package com.uwsoft.editor.renderer.components.sprite;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Pool;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.data.FrameRange;

public class SpriteAnimationComponent implements Component,Pool.Poolable {
	public String animationName = "";
	public int fps = 24;
	public HashMap<String, FrameRange> frameRangeMap = new HashMap<String, FrameRange>();
    public String currentAnimation;
    public Animation.PlayMode playMode = Animation.PlayMode.LOOP;

	@Override
	public void reset() {
		animationName = "";
		fps = 24;
		frameRangeMap = new HashMap<String, FrameRange>();
		currentAnimation=null;
		playMode = Animation.PlayMode.LOOP;
	}
}
