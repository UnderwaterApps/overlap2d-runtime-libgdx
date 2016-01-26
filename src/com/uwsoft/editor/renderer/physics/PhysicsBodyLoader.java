package com.uwsoft.editor.renderer.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;

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

    public Body createBody(World world, PhysicsBodyComponent physicsComponent, Vector2[][] minPolygonData, Vector2 mulVec, float rotationRad) {

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
        bodyDef.position.set(0, 0);

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

                minPolygonData[i][j/2].x -= physicsComponent.centerX;
                minPolygonData[i][j/2].y -= physicsComponent.centerY;

                minPolygonData[i][j/2].x *= mulVec.x;
                minPolygonData[i][j/2].y *= mulVec.y;

        		verts[j] = (minPolygonData[i][j/2].x * (float)Math.cos(rotationRad) - minPolygonData[i][j/2].y * (float)Math.sin(rotationRad)) * scale ;
        		verts[j+1] = (minPolygonData[i][j/2].x * (float)Math.sin(rotationRad) + minPolygonData[i][j/2].y * (float)Math.cos(rotationRad)) * scale;

                verts[j] += physicsComponent.centerX * scale;
                verts[j+1] += physicsComponent.centerY * scale;

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
