package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Pong;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.utils.B2DBodyBuilder;
import com.mygdx.game.utils.B2DJointBuilder;

import static com.mygdx.game.utils.B2DConstants.PPM;

public class GameScreen extends AbstractScreen {

    OrthographicCamera cam;

    //Box2D
//    Box2DDebugRenderer b2dr;
    World world;

    //Game Bodies
    Body ball;
    Body paddleLeft, goalLeft;
    Body paddleRight, goalRight;

    //Game Logic
    int pointsToWin;
    boolean leftScored;
    BitmapFont scoreLeft = new BitmapFont();
    BitmapFont scoreRight = new BitmapFont();

    int textLeft = 0;
    int textRight = 0;

    //Sprites
    private Sprite leftPaddleSprite = new Sprite(new Texture("PaddleWhite.png"));
    private Sprite rightPaddleSprite = new Sprite(new Texture("PaddleWhite.png"));
    private Sprite ballSprite = new Sprite(new Texture("Ball.png"));

    public GameScreen(final Pong app) {
        super(app);

        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Pong.V_WIDTH, Pong.V_Height);
        FitViewport viewp = new FitViewport(Pong.V_WIDTH, Pong.V_Height, cam);
        stage.setViewport(viewp);
        leftScored = false;

        //Setup fonts for scores
        scoreLeft.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scoreRight.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scoreLeft.getData().setScale(2f);
        scoreRight.getData().setScale(2f);

        pointsToWin = 5;

    }

    @Override
    public void show() {
//        Debugger
//        this.b2dr = new Box2DDebugRenderer();

        if (textLeft == pointsToWin) {
            app.gsm.getScreen().setTitleText("PLAYER WINS");
            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU);
            textLeft = 0;
            textRight = 0;
        } else if (textRight == pointsToWin) {
            app.gsm.getScreen().setTitleText("COMPUTER WINS");
            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU);
            textLeft = 0;
            textRight = 0;
        }
        this.world = new World(new Vector2(0f, 0f), false);
        createCollisionDetection();

        initArena();

    }


    @Override
    public void update(float dt) {
        world.step(1f / Pong.FPS, 6, 2);

        //Get touch pos and move paddle

        Vector3 touchPos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f));
        float mousePosToWorld = (touchPos.y / PPM);
        float mouseLerp = paddleLeft.getPosition().y + (mousePosToWorld - paddleLeft.getPosition().y) * .2f;
        float botLerp = paddleRight.getPosition().y + (ball.getPosition().y - paddleRight.getPosition().y) * .2f;

        if (mouseLerp * PPM > cam.viewportHeight - 20f) {
            mouseLerp = (cam.viewportHeight - 20f) / PPM;
        } else if (mouseLerp * PPM < 20f) {
            mouseLerp = 20f / PPM;
        }
        paddleLeft.setTransform(paddleLeft.getPosition().x, mouseLerp, paddleLeft.getAngle());
        paddleRight.setTransform(paddleRight.getPosition().x, botLerp, paddleRight.getAngle());
//        stage.act(dt);
        cam.update();
        app.batch.setProjectionMatrix(cam.combined);
        app.shapeBatch.setProjectionMatrix(cam.combined);
    }

    @Override
    public void render(float dt) {
        super.render(dt);

//        Debugger
//        b2dr.render(world, cam.combined.cpy().scl(PPM));

        leftPaddleSprite.setPosition((paddleLeft.getPosition().x * PPM) - 5, (paddleLeft.getPosition().y * PPM) - 20);
        rightPaddleSprite.setPosition((paddleRight.getPosition().x * PPM) - 5, (paddleRight.getPosition().y * PPM) - 20);
        ballSprite.setPosition((ball.getPosition().x * PPM) - 6, (ball.getPosition().y * PPM) - 6);
//        stage.draw();
        app.batch.begin();
        scoreLeft.draw(app.batch, Integer.toString(textLeft), (cam.viewportWidth / 2) - 20, cam.viewportHeight - 17);
        scoreRight.draw(app.batch, Integer.toString(textRight), (cam.viewportWidth / 2) + 20, cam.viewportHeight - 17);
        leftPaddleSprite.draw(app.batch);
        rightPaddleSprite.draw(app.batch);
        ballSprite.draw(app.batch);
        app.batch.end();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
        if (world != null) {
            world.dispose();
        }
    }

    private void initArena() {
        createWalls();
        ball = B2DBodyBuilder.createBall(world, cam.viewportWidth / 2, cam.viewportHeight / 2, 6f);

        //Setup paddles
        paddleLeft = B2DBodyBuilder.createBox(world, 40, cam.viewportHeight / 2, 10, 40,
                false, false, "PADDLELEFT");
        paddleRight = B2DBodyBuilder.createBox(world, cam.viewportWidth - 40, cam.viewportHeight / 2, 10, 40,
                false, false, "PADDLERIGHT");

        //Setup goals
        goalLeft = B2DBodyBuilder.createBox(world, 5, cam.viewportHeight / 2, 10, cam.viewportHeight,
                true, true, "GOALLEFT");
        goalRight = B2DBodyBuilder.createBox(world, cam.viewportWidth - 5, cam.viewportHeight / 2, 10, cam.viewportHeight,
                true, true, "GOALRIGHT");

        //Create goal to paddle joints
        B2DJointBuilder.createPrismaticJoint(world, goalLeft, paddleLeft, cam.viewportHeight / 2, -cam.viewportHeight / 2,
                new Vector2(35 / PPM, 0), new Vector2(0, 0));

        B2DJointBuilder.createPrismaticJoint(world, goalRight, paddleRight, cam.viewportHeight / 2, -cam.viewportHeight / 2,
                new Vector2(-35 / PPM, 0), new Vector2(0, 0));

        //Launch ball and logic for y-coordinate
        int yCoord = MathUtils.random(-30, 30);
        if (yCoord <= 0) {
            yCoord -= 20;
        } else {
            yCoord += 20;
        }

        if (leftScored) {
            ball.applyForceToCenter(new Vector2(50, yCoord), false);
        } else {
            ball.applyForceToCenter(new Vector2(-50, yCoord), false);
        }


    }

    private void createWalls() {

        Vector2[] verts = new Vector2[5];
        verts[0] = new Vector2(1f / PPM, 1f / PPM);
        verts[1] = new Vector2(cam.viewportWidth / PPM, 1f / PPM);
        verts[2] = new Vector2(cam.viewportWidth / PPM, (cam.viewportHeight) / PPM);
        verts[3] = new Vector2(1f / PPM, (cam.viewportHeight) / PPM);
        verts[4] = new Vector2(1f / PPM, 1f / PPM);
        B2DBodyBuilder.createChainShape(world, verts);
    }

    private void createCollisionDetection() {
        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                Body bodyA = fixtureA.getBody();
                Body bodyB = fixtureB.getBody();

                Object objA = bodyA.getUserData();
                Object objB = bodyB.getUserData();

                if (objA.getClass() == String.class && objB.getClass() == String.class) {
                    String strA = (String) objA;
                    String strB = (String) objB;
                    if ((strA.equals("BALL") && strB.equals("GOALLEFT")) || (strA.equals("GOALLEFT") && strB.equals("BALL"))) {
                        leftScored = false;
                        textRight++;
                        show();
                    } else if ((strA.equals("BALL") && strB.equals("GOALRIGHT")) || (strA.equals("GOALRIGHT") &&
                            strB.equals("BALL"))) {
                        leftScored = true;
                        textLeft++;
                        show();
                    } else if ((strA.equals("BALL") && strB.equals("PADDLELEFT")) || (strA.equals("PADDLELEFT") &&
                            strB.equals("BALL"))) {
                        ball.applyForceToCenter(-5, 0, false);

                    } else if ((strA.equals("BALL") && strB.equals("PADDLERIGHT")) || (strA.equals("PADDLERIGHT") &&
                            strB.equals("BALL"))) {
                        ball.applyForceToCenter(5, 0, false);
                    } else {
                        ball.setLinearVelocity(ball.getLinearVelocity().x * 1.02f, ball.getLinearVelocity().y * 1.02f);
                    }
                }


            }

            // Collision methods which are not needed, but required to be implemented
            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }


        });
    }
}

