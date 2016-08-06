package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.uwsoft.editor.renderer.systems.render.logic.ShaderLogic;

public class ShaderComponent implements Component {
	public String shaderName;
	private ShaderProgram shaderProgram = null;
    public ShaderLogic shaderLogic;


    public void setShader(String name, ShaderProgram program) {
		shaderName = name;
		shaderProgram = program;
        shaderLogic = new ShaderLogic() {
            @Override
            public void begin(Batch batch, ShaderComponent shader) {
                batch.setShader(shader.getShader());
            }

            @Override
            public void end(Batch batch) {
                batch.setShader(null);
            }
        };
	}

	public ShaderProgram getShader() {
		return shaderProgram;
	}

	public void clear() {
		shaderName = null;
		shaderProgram = null;
	}
}
