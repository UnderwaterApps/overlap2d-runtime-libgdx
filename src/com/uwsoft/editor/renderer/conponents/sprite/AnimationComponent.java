package com.uwsoft.editor.renderer.conponents.sprite;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationComponent extends Component {
	public HashMap<String,Animation> animations = new  HashMap<String,Animation>();
}
