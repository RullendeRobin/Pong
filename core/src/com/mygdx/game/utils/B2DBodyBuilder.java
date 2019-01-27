package com.mygdx.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.game.utils.B2DConstants.PPM;

public final class B2DBodyBuilder {

    private B2DBodyBuilder() {}

    public static Body createBox(World world, float x, float y, float width, float height, boolean isStatic, boolean isSensor, String str) {
        Body body;

        BodyDef bDef = new BodyDef();
        bDef.type = isStatic? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bDef.fixedRotation = true;
        bDef.position.set(x / PPM, y / PPM);
        body = world.createBody(bDef);
        body.setUserData(str);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1.0f;
        fDef.isSensor = isSensor;
        body.createFixture(fDef);
        shape.dispose();
        return body;
    }

    public static Body createBall(World world, float x, float y, float radius) {
        Body body;
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.fixedRotation = true;
        bDef.position.set(x / PPM, y / PPM);
        body = world.createBody(bDef);
        body.setUserData("BALL");

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1.0f;
        fDef.friction = 0f;
        fDef.restitution = 1f;
        body.createFixture(fDef);
        shape.dispose();
        return body;
    }

    public static Body createChainShape(World world, Vector2[] verts) {
        Body body;
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bDef);
        body.setUserData("CHAIN");

        ChainShape shape = new ChainShape();
        shape.createChain(verts);

        body.createFixture(shape, 1.0f);
        shape.dispose();
        return body;
    }

}
