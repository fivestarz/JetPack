package gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import configuration.Settings;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;

/**
 * Created by ManuGil on 22/03/15.
 */
public class Coin {

    private GameWorld world;
    private int x, y;
    private float radius;
    private Sprite sprite;
    private Body body, point;

    public Coin(GameWorld world, int x, int y, float radius) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.radius = radius;

        sprite = new Sprite(AssetLoader.meteor);
        sprite.setPosition(x, y);
        sprite.setSize(radius * 2, radius * 2);
        sprite.setRotation(MathUtils.random(0, 360));
        sprite.setOriginCenter();
        sprite.setColor(FlatColors.GREEN);
        sprite.setAlpha(0.7f);

        BodyDef bodyDefC = new BodyDef();
        bodyDefC.type = BodyDef.BodyType.DynamicBody;
        bodyDefC.position.set(sprite.getX() / world.PIXELS_TO_METERS,
                sprite.getY() / world.PIXELS_TO_METERS);

        body = world.getWorldB().createBody(bodyDefC);
        body.setAngularDamping(0.5f);
        //body.setFixedRotation(true);
        body.setGravityScale(0);

        BodyDef bodyDefP = new BodyDef();
        bodyDefP.type = BodyDef.BodyType.DynamicBody;
        bodyDefP.position.set((sprite.getX() - 10) / world.PIXELS_TO_METERS,
                sprite.getY() / world.PIXELS_TO_METERS);
        point = world.getWorldB().createBody(bodyDefP);
        point.setGravityScale(0);

        CircleShape shape = new CircleShape();
        shape.setPosition(new Vector2(sprite.getWidth() / 2 / world.PIXELS_TO_METERS,
                sprite.getHeight()
                        / 2 / world.PIXELS_TO_METERS));
        shape.setRadius(radius / world.PIXELS_TO_METERS);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0.05f;

        //TODO: Check multicursor stuff in Android Studio

        //TODO: Spring Stuff
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.bodyA = body;

        jointDef.bodyB = point;

        jointDef.initialize(body, point,
                new Vector2(body.getPosition().x + (radius / world.PIXELS_TO_METERS),
                        body.getPosition().y + (radius / world.PIXELS_TO_METERS)),
                new Vector2(point.getPosition().x + (radius / world.PIXELS_TO_METERS),
                        point.getPosition().y + (radius / world.PIXELS_TO_METERS)));
        jointDef.dampingRatio = 1f;
        jointDef.frequencyHz = 500;
        jointDef.length = 15f / world.PIXELS_TO_METERS;
        jointDef.collideConnected = false;


        body.createFixture(fixtureDef);
        //point.createFixture(fixtureDefP);
        world.getWorldB().createJoint(jointDef);
        //world.getWorldB().createJoint(jointDef2);
        shape.dispose();
        reset();
    }

    public void update(float delta) {

        sprite.setPosition((body.getPosition().x * world.PIXELS_TO_METERS),
                (body.getPosition().y * world.PIXELS_TO_METERS));
        point.setTransform(this.x / world.PIXELS_TO_METERS, this.y / world.PIXELS_TO_METERS, 0);
        limitVel();
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        sprite.setOrigin(0, 0);
    }

    private void limitVel() {
        if (body.getLinearVelocity().y > Settings.COIN_MAX_VEL) {
            body.setLinearVelocity(body.getLinearVelocity().x, Settings.COIN_MAX_VEL);
        }
        if (body.getLinearVelocity().y < -Settings.COIN_MAX_VEL) {
            body.setLinearVelocity(body.getLinearVelocity().x, -Settings.COIN_MAX_VEL);
        }

        if (body.getLinearVelocity().x > Settings.COIN_MAX_VEL) {
            body.setLinearVelocity(Settings.COIN_MAX_VEL, body.getLinearVelocity().y);
        }
        if (body.getLinearVelocity().x < -Settings.COIN_MAX_VEL) {
            body.setLinearVelocity(-Settings.COIN_MAX_VEL, body.getLinearVelocity().y);
        }
    }

    public void render(SpriteBatch batcher, ShapeRenderer shapeRenderer) {
        sprite.draw(batcher);
    }

    public void reset() {

    }
}
