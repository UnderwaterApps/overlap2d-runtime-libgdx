package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.uwsoft.editor.renderer.systems.render.logic.ShaderLogic;

public class ShaderComponent implements Component {
	public String shaderName;
	public ShaderProgram shaderProgram = null;
    public ShaderLogic shaderLogic;


    public void setShader(String name, ShaderProgram program) {
		shaderName = name;
		shaderProgram = program;
        shaderLogic = new ShaderLogic() {

            @Override
            public void draw(Batch batch, Entity entity, float parentAlpha) {
                batch.setShader(shaderProgram);

//                batch.getShader().setUniformf("deltaTime", Gdx.graphics.getDeltaTime());
//                batch.getShader().setUniformf("time", Overlap2dRenderer.timeRunning);

                GL20 gl = Gdx.gl20;
                int error;
                if ((error = gl.glGetError()) != GL20.GL_NO_ERROR) {
                    Gdx.app.log("opengl", "Error: " + error);
                    Gdx.app.log("opengl", shaderProgram.getLog());
                    //throw new RuntimeException( ": glError " + error);
                }

                if(textureRegionMapper.get(entity).polygonSprite != null) {
                    drawTiledPolygonSprite(batch, entity);
                } else {
                    drawSprite(batch, entity, parentAlpha);
                }
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
