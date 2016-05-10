package com.uwsoft.editor.renderer.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.SnapshotArray;
import com.uwsoft.editor.renderer.components.*;
import com.uwsoft.editor.renderer.utils.TransformMathUtils;

public class CompositeSystem extends IteratingSystem {

    private ComponentMapper<DimensionsComponent> dimensionsMapper;
    private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<NodeComponent> nodeMapper;
    private ComponentMapper<CompositeTransformComponent> compositeMapper;

    private DimensionsComponent dimensionsComponent;
    private NodeComponent nodeComponent;

    private final Vector2 p1 = new Vector2();
    private final Vector2 p2 = new Vector2();
    private final Vector2 p3 = new Vector2();
    private final Vector2 p4 = new Vector2();
    private final Vector2 tmpBoundPoints = new Vector2();

    public CompositeSystem() {
        super(Family.all(CompositeTransformComponent.class).get());
        dimensionsMapper = ComponentMapper.getFor(DimensionsComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
        nodeMapper = ComponentMapper.getFor(NodeComponent.class);
        compositeMapper = ComponentMapper.getFor(CompositeTransformComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        dimensionsComponent = dimensionsMapper.get(entity);
        nodeComponent = nodeMapper.get(entity);
//		if(dimensionsComponent.boundBox == null){
//			dimensionsComponent.boundBox = new Rectangle();
//		}
//        recalculateSize();
        CompositeTransformComponent compositeTransformComponent = compositeMapper.get(entity);
        ViewPortComponent viewPortComponent = entity.getComponent(ViewPortComponent.class);
        if (compositeTransformComponent != null && compositeTransformComponent.automaticResize && viewPortComponent == null) {
            recalculateSize();
        }
    }

    private void recalculateSize() {
        float lowerX = Float.MAX_VALUE;
        float lowerY = Float.MAX_VALUE;
        float upperX = Float.MIN_VALUE;
        float upperY = Float.MIN_VALUE;
        SnapshotArray<Entity> entities = nodeComponent.children;
        for (Entity entity : entities) {
            TransformComponent transformComponent = transformMapper.get(entity);
            DimensionsComponent childDimCom = dimensionsMapper.get(entity);
            float x = transformComponent.x;
            float y = transformComponent.y;
            float width = childDimCom.width;
            float height = childDimCom.height;

            Matrix3 transMat = TransformMathUtils.transform(transformComponent);

            p1.set(x, y).mul(transMat);
            p2.set(x + width, y).mul(transMat);
            p3.set(x + width, y + height).mul(transMat);
            p4.set(x, y + height).mul(transMat);

            tmpBoundPoints.set(lowerX, 0);
            lowerX = getX(MinMaxOp.MIN, p1, p2, p3, p4, tmpBoundPoints);

            tmpBoundPoints.set(upperX, 0);
            upperX = getX(MinMaxOp.MAX, p1, p2, p3, p4, tmpBoundPoints);

            tmpBoundPoints.set(0, lowerY);
            lowerY = getY(MinMaxOp.MIN, p1, p2, p3, p4, tmpBoundPoints);

            tmpBoundPoints.set(0, upperY);
            upperY = getY(MinMaxOp.MAX, p1, p2, p3, p4, tmpBoundPoints);
        }

        for (Entity entity : entities) {
            if (lowerX == 0 && lowerY == 0) break;
            TransformComponent transformComponent = transformMapper.get(entity);
            transformComponent.x -= lowerX;
            transformComponent.y -= lowerY;
        }

        dimensionsComponent.width = (upperX - lowerX);
        dimensionsComponent.height = (upperY - lowerY);
        lowerX = 0;
        lowerY = 0;
        dimensionsComponent.boundBox.set(lowerX, lowerY, dimensionsComponent.width, dimensionsComponent.height);
    }

    private float getX(MinMaxOp op, Vector2... points) {
        float pointX = points[0].x;
        for (Vector2 point : points) {
            pointX = op.compare(pointX, point.x);
        }
        return pointX;
    }

    private float getY(MinMaxOp op, Vector2... points) {
        float pointY = points[0].y;
        for (Vector2 point : points) {
            pointY = op.compare(pointY, point.y);
        }
        return pointY;
    }

    private enum MinMaxOp {
        MIN("<") {
            @Override
            public float compare(float a, float b) {
                return (a < b) ? a : b;
            }
        },

        MAX(">") {
            @Override
            public float compare(float a, float b) {
                return (a > b) ? a : b;
            }
        };

        private String minMaxOperator;

        MinMaxOp(String minMaxOperator) {
            this.minMaxOperator = minMaxOperator;
        }

        public abstract float compare(float a, float b);
    }
}
