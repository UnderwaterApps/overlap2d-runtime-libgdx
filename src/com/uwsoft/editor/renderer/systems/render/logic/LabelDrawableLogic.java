package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.uwsoft.editor.renderer.components.*;
import com.uwsoft.editor.renderer.components.label.LabelComponent;

public class LabelDrawableLogic implements Drawable {

	private ComponentMapper<LabelComponent> labelComponentMapper;
	private ComponentMapper<TintComponent> tintComponentMapper;
	private ComponentMapper<DimensionsComponent> dimensionsComponentMapper;
	private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<ShaderComponent> shaderComponentMapper;
    private ComponentMapper<ParentNodeComponent> parentNodeComponentComponentMapper;

	private final Color tmpColor = new Color();

	public LabelDrawableLogic() {
		labelComponentMapper = ComponentMapper.getFor(LabelComponent.class);
		tintComponentMapper = ComponentMapper.getFor(TintComponent.class);
		dimensionsComponentMapper = ComponentMapper.getFor(DimensionsComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
        shaderComponentMapper = ComponentMapper.getFor(ShaderComponent.class);
        parentNodeComponentComponentMapper = ComponentMapper.getFor(ParentNodeComponent.class);
	}
	
	@Override
	public void draw(Batch batch, Entity entity, float parentAlpha) {
		TransformComponent entityTransformComponent = transformMapper.get(entity);
		LabelComponent labelComponent = labelComponentMapper.get(entity);
		DimensionsComponent dimenstionsComponent = dimensionsComponentMapper.get(entity);
		TintComponent tint = tintComponentMapper.get(entity);

		tmpColor.set(tint.color);
        ShaderComponent shaderComponent = shaderComponentMapper.get(entity);

        if (shaderComponentMapper.has(entity) && shaderComponent.shaderLogic != null) {
            batch.setShader(shaderComponent.shaderProgram);
            if (labelComponent.style.background != null) {
                batch.setColor(tmpColor);
                labelComponent.style.background.draw(batch, entityTransformComponent.x, entityTransformComponent.y, dimenstionsComponent.width, dimenstionsComponent.height);
                //System.out.println("LAbel BG");
            }

            if(labelComponent.style.fontColor != null) tmpColor.mul(labelComponent.style.fontColor);
            //tmpColor.a *= TODO consider parent alpha
            tmpColor.a *= tintComponentMapper.get(parentNodeComponentComponentMapper.get(entity).parentEntity).color.a;

            labelComponent.cache.tint(tmpColor);
            labelComponent.cache.setPosition(entityTransformComponent.x, entityTransformComponent.y);
            labelComponent.cache.draw(batch);
            batch.setShader(null);
        } else {
            if (labelComponent.style.background != null) {
                batch.setColor(tmpColor);
                labelComponent.style.background.draw(batch, entityTransformComponent.x, entityTransformComponent.y, dimenstionsComponent.width, dimenstionsComponent.height);
                //System.out.println("LAbel BG");
            }

            if(labelComponent.style.fontColor != null) tmpColor.mul(labelComponent.style.fontColor);
            //tmpColor.a *= TODO consider parent alpha
            tmpColor.a *= tintComponentMapper.get(parentNodeComponentComponentMapper.get(entity).parentEntity).color.a;

            labelComponent.cache.tint(tmpColor);
            labelComponent.cache.setPosition(entityTransformComponent.x, entityTransformComponent.y);
            labelComponent.cache.draw(batch);
        }
	}

}
