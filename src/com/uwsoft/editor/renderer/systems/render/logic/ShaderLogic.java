package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.uwsoft.editor.renderer.components.ShaderComponent;

/**
 * Created by Simone on 8/5/2016.
 */
public abstract class ShaderLogic {

    public abstract void begin(Batch batch, ShaderComponent shader);

    public abstract void end(Batch batch);
}
