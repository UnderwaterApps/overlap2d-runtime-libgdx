package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Pool;

public class ShaderComponent implements Component,Pool.Poolable {
	public String shaderName;
	private ShaderProgram shaderProgram = null;

	public void setShader(String name, ShaderProgram program) {
		shaderName = name;
		shaderProgram = program;
	}

	public ShaderProgram getShader() {
		return shaderProgram;
	}

	public void clear() {
		shaderName = null;
		shaderProgram = null;
	}

	@Override
	public void reset() {
		shaderName=null;
		shaderProgram=null;
	}
}
