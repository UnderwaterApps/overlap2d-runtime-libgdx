package com.uwsoft.editor.renderer.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.uwsoft.editor.renderer.components.*;

/**
 * Created by aurel on 25/02/16.
 */
public class TransformSystem extends IteratingSystem {

    private ComponentMapper<ViewPortComponent> viewPortMapper = ComponentMapper.getFor(ViewPortComponent.class);
    private ComponentMapper<CompositeTransformComponent> compositeTransformMapper = ComponentMapper.getFor(CompositeTransformComponent.class);
    private ComponentMapper<NodeComponent> nodeMapper = ComponentMapper.getFor(NodeComponent.class);
    private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);

    public TransformSystem() {
        super(Family.all(ViewPortComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        computeCompositeTransformsRecursively(entity);
    }

    private void computeCompositeTransformsRecursively(Entity compositeEntity) {
        CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(compositeEntity);

        computeChildrenTransforms(compositeEntity, curCompositeTransformComponent);
    }

    private void computeChildrenTransforms(Entity rootEntity, CompositeTransformComponent curCompositeTransformComponent) {
        NodeComponent nodeComponent = nodeMapper.get(rootEntity);
        Entity[] children = nodeComponent.children.begin();
        TransformComponent transform = transformMapper.get(rootEntity);

        if (curCompositeTransformComponent.transform || transform.rotation != 0 || transform.scaleX !=1 || transform.scaleY !=1) {
            for (int i = 0, n = nodeComponent.children.size; i < n; i++) {
                Entity child = children[i];

                NodeComponent childNodeComponent = nodeMapper.get(child);

                if(childNodeComponent != null){
                    //Step into Composite
                    computeCompositeTransformsRecursively(child);
                }
            }
        }
        else {
            // No transform for this group, offset each child.
            TransformComponent compositeTransform = transformMapper.get(rootEntity);
            float offsetX = compositeTransform.offsetX, offsetY = compositeTransform.offsetY;

            if(viewPortMapper.has(rootEntity)){
                offsetX = 0;
                offsetY = 0;
            }

            for (int i = 0, n = nodeComponent.children.size; i < n; i++) {
                Entity child = children[i];

                TransformComponent childTransformComponent = transformMapper.get(child);

                childTransformComponent.offsetX = childTransformComponent.x + offsetX;
                childTransformComponent.offsetY = childTransformComponent.y + offsetY;

                NodeComponent childNodeComponent = nodeMapper.get(child);

                if(childNodeComponent != null){
                    //Step into Composite
                    computeCompositeTransformsRecursively(child);
                }

            }
        }
    }
}
