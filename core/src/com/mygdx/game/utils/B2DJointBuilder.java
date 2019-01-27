package com.mygdx.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;

import static com.mygdx.game.utils.B2DConstants.PPM;

public class B2DJointBuilder {
    private B2DJointBuilder() {}

    public static Joint createPrismaticJoint(World world, Body bodyA, Body bodyB, float upperLimit, float lowerLimit,
                                             Vector2 anchorA, Vector2 anchorB) {
    Joint joint;
        PrismaticJointDef pDef = new PrismaticJointDef();
        pDef.bodyA = bodyA;
        pDef.bodyB = bodyB;
        pDef.collideConnected = false;

        pDef.enableLimit = true;
        pDef.localAnchorA.set(anchorA);
        pDef.localAnchorB.set(anchorB);
        pDef.localAxisA.set(0, 1);
        pDef.upperTranslation = upperLimit / PPM;
        pDef.lowerTranslation = lowerLimit / PPM;

        return world.createJoint(pDef);

    }
}
