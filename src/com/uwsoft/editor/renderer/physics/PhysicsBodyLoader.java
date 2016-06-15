package com.uwsoft.editor.renderer.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.utils.TransformMathUtils;

/**
 * Created by azakhary on 9/28/2014.
 */
public class PhysicsBodyLoader {

    private static PhysicsBodyLoader instance;

    public float scale;

    public float mul;

    private PhysicsBodyLoader() {
        mul = 20f;
    }

    public static PhysicsBodyLoader getInstance() {
        if(instance == null) {
            instance = new PhysicsBodyLoader();
        }

        return instance;
    }

    public void setScaleFromPPWU(float pixelPerWU) {
        scale = 1f/(mul*pixelPerWU);
    }

    public static float getScale() {
        return getInstance().scale;
    }

    public Body createBody(World world, Entity entity, PhysicsBodyComponent physicsComponent, Vector2[][] minPolygonData, TransformComponent transformComponent) {

        FixtureDef fixtureDef = new FixtureDef();

        if(physicsComponent != null) {
            fixtureDef.density = physicsComponent.density;
            fixtureDef.friction = physicsComponent.friction;
            fixtureDef.restitution = physicsComponent.restitution;

            fixtureDef.isSensor = physicsComponent.sensor;

            fixtureDef.filter.maskBits = physicsComponent.filter.maskBits;
            fixtureDef.filter.groupIndex = physicsComponent.filter.groupIndex;
            fixtureDef.filter.categoryBits = physicsComponent.filter.categoryBits;
        }

        BodyDef bodyDef = new BodyDef();
        Vector2 sceneCoords = TransformMathUtils.localToSceneCoordinates(entity, new Vector2(0, 0));
        bodyDef.position.set((sceneCoords.x + transformComponent.originX) * PhysicsBodyLoader.getScale() , (sceneCoords.y + transformComponent.originY)* PhysicsBodyLoader.getScale() );
        bodyDef.angle = transformComponent.rotation * MathUtils.degreesToRadians;

        bodyDef.awake = physicsComponent.awake;
        bodyDef.allowSleep = physicsComponent.allowSleep;
        bodyDef.bullet = physicsComponent.bullet;

        if(physicsComponent.bodyType == 0) {
            bodyDef.type = BodyDef.BodyType.StaticBody;
        } else if (physicsComponent.bodyType == 1){
            bodyDef.type = BodyDef.BodyType.KinematicBody;
        } else {
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }

        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();

        for(int i = 0; i < minPolygonData.length; i++) {
        	float[] verts = new float[minPolygonData[i].length * 2];
        	for(int j=0;j<verts.length;j+=2){
                float tempX = minPolygonData[i][j / 2].x;
                float tempY = minPolygonData[i][j/2].y;

                minPolygonData[i][j/2].x -= transformComponent.originX;
                minPolygonData[i][j/2].y -= transformComponent.originY;

                minPolygonData[i][j/2].x *= transformComponent.scaleX;
                minPolygonData[i][j/2].y *= transformComponent.scaleY;

        		verts[j] = minPolygonData[i][j/2].x * scale ;
        		verts[j+1] = minPolygonData[i][j/2].y * scale;

                minPolygonData[i][j / 2].x = tempX;
                minPolygonData[i][j/2].y = tempY;

        	}
            polygonShape.set(verts);
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }

        return body;
    }

}
