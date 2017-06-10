package com.uwsoft.editor.renderer.physics;

import com.badlogic.ashley.core.Entity;

public interface Contacts {

public void beginContact(Entity contact);

	public void endContact(Entity contact);
}
