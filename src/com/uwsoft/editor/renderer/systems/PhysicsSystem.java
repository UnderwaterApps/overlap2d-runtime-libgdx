package com.uwsoft.editor.renderer.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.PolygonComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.TransformMathUtils;
import com.uwsoft.editor.renderer.physics.Contacts;
import com.uwsoft.editor.renderer.scripts.IScript;

public class PhysicsSystem extends IteratingSystem implements ContactListener {

	protected ComponentMapper<TransformComponent> transformComponentMapper = ComponentMapper.getFor(TransformComponent.class);

	private final float TIME_STEP = 1f/60;
	private World world;
	private boolean isPhysicsOn = true;
	private float accumulator = 0;

	public PhysicsSystem(World world) {
		super(Family.all(PhysicsBodyComponent.class).get());
		this.world = world;
		world.setContactListener(this);
	}

	@Override
	public void update (float deltaTime) {
		for (int i = 0; i < getEntities().size(); ++i) {
			processEntity(getEntities().get(i), deltaTime);
		}

		if (world != null && isPhysicsOn) {
			doPhysicsStep(deltaTime);
		}
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		TransformComponent transformComponent =  transformComponentMapper.get(entity);

		processBody(entity);

		PhysicsBodyComponent physicsBodyComponent = ComponentRetriever.get(entity, PhysicsBodyComponent.class);
		Body body = physicsBodyComponent.body;
		transformComponent.x = 0;
		transformComponent.y = 0;
		transformComponent.rotation = 0;
		Vector2 localCoords = TransformMathUtils.sceneToLocalCoordinates(entity, body.getPosition().cpy().scl(1 / PhysicsBodyLoader.getScale()));
		transformComponent.x = localCoords.x - transformComponent.originX;
		transformComponent.y = localCoords.y - transformComponent.originY;
		transformComponent.rotation = body.getAngle() * MathUtils.radiansToDegrees;
	}

	protected void processBody(Entity entity) {
		PhysicsBodyComponent physicsBodyComponent = ComponentRetriever.get(entity, PhysicsBodyComponent.class);
		PolygonComponent polygonComponent = ComponentRetriever.get(entity, PolygonComponent.class);
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);

		if(polygonComponent == null && physicsBodyComponent.body != null) {
			world.destroyBody(physicsBodyComponent.body);
			physicsBodyComponent.body = null;
		}

		if(physicsBodyComponent.body == null && polygonComponent != null) {
			if(polygonComponent.vertices == null) return;

            DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

            physicsBodyComponent.centerX = dimensionsComponent.width/2;
            physicsBodyComponent.centerY = dimensionsComponent.height/2;

			PhysicsBodyComponent bodyPropertiesComponent = ComponentRetriever.get(entity, PhysicsBodyComponent.class);
			physicsBodyComponent.body = PhysicsBodyLoader.getInstance().createBody(world, entity, bodyPropertiesComponent, polygonComponent.vertices, transformComponent);

			physicsBodyComponent.body.setUserData(entity);
		}
	}

	private void doPhysicsStep(float deltaTime) {
		// fixed time step
		// max frame time to avoid spiral of death (on slow devices)
		float frameTime = Math.min(deltaTime, 0.25f);
		accumulator += frameTime;
		while (accumulator >= TIME_STEP) {
			world.step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}
	}

	public void setPhysicsOn(boolean isPhysicsOn) {
		this.isPhysicsOn = isPhysicsOn;
	}
	@Override
	public void beginContact(Contact contact) {
		collision_contact(contact, true);

	}

	@Override
	public void endContact(Contact contact) {
		collision_contact(contact, false);
	}

	private void collision_contact(Contact contact, boolean in) {
		// Get both fixtures
		Fixture f1 = contact.getFixtureA();
		Fixture f2 = contact.getFixtureB();
		// Get both bodies
		Body b1 = f1.getBody();
		Body b2 = f2.getBody();

		// Get our objects that reference these bodies
		Object o1 = b1.getUserData();
		Object o2 = b2.getUserData();

		// cast to entity
		Entity et1 = (Entity) o1;
		Entity et2 = (Entity) o2;
		// get script comp
		ScriptComponent ic1 = ComponentRetriever.get(et1, ScriptComponent.class);
		ScriptComponent ic2 = ComponentRetriever.get(et2, ScriptComponent.class);

		// cast script to contacts, if scripts implement contacts
		for (IScript sc : ic1.scripts) {
			try {
				Contacts ct = (Contacts) sc;
				if (in)
					ct.beginContact(et2);
				else
					ct.endContact(et2);
			} catch (ClassCastException exc) {

			}

		}
		for (IScript sc : ic2.scripts) {
			try {
				Contacts ct = (Contacts) sc;
				if (in)
					ct.beginContact(et1);
				else
					ct.endContact(et1);
			} catch (ClassCastException exc) {

			}

		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
