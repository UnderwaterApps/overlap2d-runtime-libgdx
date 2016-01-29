package com.uwsoft.editor.renderer.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.ParentNodeComponent;
import com.uwsoft.editor.renderer.components.PolygonComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class PhysicsSystem extends IteratingSystem {

    protected ComponentMapper<TransformComponent> transformComponentMapper = ComponentMapper.getFor(TransformComponent.class);

    private World world;

    public PhysicsSystem(World world) {
        super(Family.all(PhysicsBodyComponent.class).get());
        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = transformComponentMapper.get(entity);

        processBody(entity);

        PhysicsBodyComponent physicsBodyComponent = ComponentRetriever.get(entity, PhysicsBodyComponent.class);
        ParentNodeComponent parentNodeComponent = ComponentRetriever.get(entity, ParentNodeComponent.class);
        TransformComponent parentTransformComponent = ComponentRetriever.get(parentNodeComponent.parentEntity, TransformComponent.class);

        Body body = physicsBodyComponent.body;
        transformComponent.x = (body.getPosition().x / PhysicsBodyLoader.getScale()) - parentTransformComponent.x;
        transformComponent.y = (body.getPosition().y / PhysicsBodyLoader.getScale()) - parentTransformComponent.y;
        transformComponent.rotation = transformComponent.rotation + body.getAngle() * MathUtils.radiansToDegrees;
    }

    protected void processBody(Entity entity) {
        PhysicsBodyComponent physicsBodyComponent = ComponentRetriever.get(entity, PhysicsBodyComponent.class);
        PolygonComponent polygonComponent = ComponentRetriever.get(entity, PolygonComponent.class);
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        ParentNodeComponent parentNodeComponent = ComponentRetriever.get(entity, ParentNodeComponent.class);

        if (polygonComponent == null && physicsBodyComponent.body != null) {
            world.destroyBody(physicsBodyComponent.body);
            physicsBodyComponent.body = null;
        }

        if (physicsBodyComponent.body == null && polygonComponent != null) {
            if (polygonComponent.vertices == null) return;

            PhysicsBodyComponent bodyPropertiesComponent = ComponentRetriever.get(entity, PhysicsBodyComponent.class);
            physicsBodyComponent.body = PhysicsBodyLoader.getInstance().createBody(world, bodyPropertiesComponent, polygonComponent.vertices, new Vector2(1, 1));

            if (parentNodeComponent != null) {
                TransformComponent parentTransformComponent = ComponentRetriever.get(parentNodeComponent.parentEntity, TransformComponent.class);
                physicsBodyComponent.body.setTransform(new Vector2((parentTransformComponent.x + transformComponent.x) * PhysicsBodyLoader.getScale(),
                        (parentTransformComponent.y + transformComponent.y) * PhysicsBodyLoader.getScale()), physicsBodyComponent.body.getAngle());
            } else {
                physicsBodyComponent.body.setTransform(new Vector2(transformComponent.x * PhysicsBodyLoader.getScale(), transformComponent.y * PhysicsBodyLoader.getScale()), physicsBodyComponent.body.getAngle());
            }

            Gdx.app.log("PhysicsSystem::ProcessBody", "Body position: "+physicsBodyComponent.body.getPosition().scl(1/PhysicsBodyLoader.getScale()));

            physicsBodyComponent.body.setUserData(entity);
        }
    }

}
