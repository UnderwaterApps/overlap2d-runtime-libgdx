package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.uwsoft.editor.renderer.systems.action.data.ActionData;

/**
 * Created by ZeppLondon on 10/13/2015.
 */
public class ActionComponent implements Component,Pool.Poolable {
    public Array<ActionData> dataArray = new Array<ActionData>(true, 0);

    @Override
    public void reset() {
        dataArray.clear();
    }
}
