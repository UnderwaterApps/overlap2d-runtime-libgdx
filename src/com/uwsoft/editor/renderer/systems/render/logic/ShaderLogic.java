package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.*;

/**
 * Created by Simone on 8/5/2016.
 */
public abstract class ShaderLogic {

    protected ComponentMapper<TintComponent> tintComponentComponentMapper;
    protected ComponentMapper<TextureRegionComponent> textureRegionMapper;
    protected ComponentMapper<TransformComponent> transformMapper;
    protected ComponentMapper<DimensionsComponent> dimensionsComponentComponentMapper;
    protected ComponentMapper<ShaderComponent> shaderComponentMapper;

    public ShaderLogic() {
        tintComponentComponentMapper = ComponentMapper.getFor(TintComponent.class);
        textureRegionMapper = ComponentMapper.getFor(TextureRegionComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
        dimensionsComponentComponentMapper = ComponentMapper.getFor(DimensionsComponent.class);
        shaderComponentMapper = ComponentMapper.getFor(ShaderComponent.class);
    }

    public abstract void draw(Batch batch, Entity entity, float parentAlpha);

    public void drawSprite(Batch batch, Entity entity, float parentAlpha) {
        TintComponent tintComponent = tintComponentComponentMapper.get(entity);
        TransformComponent entityTransformComponent = transformMapper.get(entity);
        TextureRegionComponent entityTextureRegionComponent = textureRegionMapper.get(entity);
        DimensionsComponent dimensionsComponent = dimensionsComponentComponentMapper.get(entity);
        batch.setColor(tintComponent.color.r, tintComponent.color.g, tintComponent.color.b, tintComponent.color.a * parentAlpha);

        batch.draw(entityTextureRegionComponent.region,
                entityTransformComponent.x, entityTransformComponent.y,
                entityTransformComponent.originX, entityTransformComponent.originY,
                dimensionsComponent.width, dimensionsComponent.height,
                entityTransformComponent.scaleX, entityTransformComponent.scaleY,
                entityTransformComponent.rotation);
    }

    public void drawPolygonSprite(Batch batch, Entity entity) {

        TintComponent tintComponent = tintComponentComponentMapper.get(entity);
        TransformComponent entityTransformComponent = transformMapper.get(entity);
        TextureRegionComponent entityTextureRegionComponent = textureRegionMapper.get(entity);

        DimensionsComponent dimensionsComponent = dimensionsComponentComponentMapper.get(entity);

        entityTextureRegionComponent.polygonSprite.setPosition(entityTransformComponent.x, entityTransformComponent.y);
        entityTextureRegionComponent.polygonSprite.setRotation(entityTransformComponent.rotation);
        entityTextureRegionComponent.polygonSprite.setOrigin(entityTransformComponent.originX, entityTransformComponent.originY);
        entityTextureRegionComponent.polygonSprite.setColor(tintComponent.color);
        entityTextureRegionComponent.polygonSprite.draw((PolygonSpriteBatch) batch);
    }

    public void drawTiledPolygonSprite(Batch batch, Entity entity) {
        batch.flush();
        TintComponent tintComponent = tintComponentComponentMapper.get(entity);
        TransformComponent entityTransformComponent = transformMapper.get(entity);
        TextureRegionComponent entityTextureRegionComponent = textureRegionMapper.get(entity);

        DimensionsComponent dimensionsComponent = dimensionsComponentComponentMapper.get(entity);
        float ppwu = dimensionsComponent.width/entityTextureRegionComponent.region.getRegionWidth();

        Vector2 atlasCoordsVector = new Vector2(entityTextureRegionComponent.region.getU(), entityTextureRegionComponent.region.getV());
        Vector2 atlasSizeVector = new Vector2(entityTextureRegionComponent.region.getU2()-entityTextureRegionComponent.region.getU(), entityTextureRegionComponent.region.getV2()-entityTextureRegionComponent.region.getV());

        batch.getShader().setUniformi("isRepeat", 1);
        batch.getShader().setUniformf("atlasCoord", atlasCoordsVector);
        batch.getShader().setUniformf("atlasSize", atlasSizeVector);
        //System.out.println(entityTransformComponent.originX);
        //batch.setColor(tintComponent.color);
        entityTextureRegionComponent.polygonSprite.setColor(tintComponent.color);
        entityTextureRegionComponent.polygonSprite.setOrigin(entityTransformComponent.originX * ppwu, entityTransformComponent.originY * ppwu);
        entityTextureRegionComponent.polygonSprite.setPosition(entityTransformComponent.x, entityTransformComponent.y);
        entityTextureRegionComponent.polygonSprite.setRotation(entityTransformComponent.rotation);
        entityTextureRegionComponent.polygonSprite.setScale(ppwu);
        entityTextureRegionComponent.polygonSprite.draw((PolygonSpriteBatch) batch);
        batch.flush();
        batch.getShader().setUniformi("isRepeat", 0);

    }


}
