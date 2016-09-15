package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.HashSet;
import java.util.Set;

public class MainItemComponent implements Component,Pool.Poolable {
    public int uniqueId = 0;
	public String itemIdentifier = "";
	public String libraryLink = "";
    public Set<String> tags = new HashSet<String>();
    public String customVars = "";
	public int entityType;
	public boolean visible = true;

	@Override
	public void reset() {
		uniqueId = 0;
		itemIdentifier = "";
		libraryLink = "";
		tags.clear();
		customVars = "";
		entityType=0;
		visible = true;
	}
}
