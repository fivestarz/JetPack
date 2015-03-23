package gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import configuration.Settings;
import gameworld.GameWorld;
import helpers.AssetLoader;
import tweens.SpriteAccessor;
import tweens.VectorAccessor;

/**
 * Created by ManuGil on 22/03/15.
 */
public class Coin {

    private GameWorld world;
    private int x, y;
    private float radius;
    private Sprite sprite;
    private Body body, point;
    private ParticleEffect effect;
    private TweenManager manager;

    public Coin(GameWorld world, int x, int y, float radius) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.radius = radius;

        sprite = new Sprite(AssetLoader.coin);
        sprite.setPosition(x, y);
        sprite.setSize(radius * 2, radius * 2);
        sprite.setRotation(MathUtils.random(0, 360));
        sprite.setOriginCenter();
        sprite.setAlpha(0.8f);

        BodyDef bodyDefC = new BodyDef();
        bodyDefC.type = BodyDef.BodyType.DynamicBody;
        bodyDefC.position.set(sprite.getX() / world.PIXELS_TO_METERS,
                sprite.getY() / world.PIXELS_TO_METERS);

        body = world.getWorldB().createBody(bodyDefC);
        body.setAngularDamping(0.5f);
        body.setLinearDamping(0.5f);
        //body.setFixedRotation(true);
        body.setLinearVelocity(MathUtils.random(-2, 2), MathUtils.random(-2, 2));
        body.setGravityScale(0);

        BodyDef bodyDefP = new BodyDef();
        bodyDefP.type = BodyDef.BodyType.DynamicBody;
        bodyDefP.position.set((sprite.getX()) / world.PIXELS_TO_METERS,
                (sprite.getY() + 10) / world.PIXELS_TO_METERS);
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
        fixtureDef.filter.categoryBits = Settings.CATEGORY_COIN;
        fixtureDef.filter.maskBits = Settings.MASK_COIN;
        //TODO: Check multicursor stuff in Android Studio

        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.bodyA = body;

        jointDef.bodyB = point;

        jointDef.initialize(body, point,
                new Vector2(body.getPosition().x + (radius / world.PIXELS_TO_METERS),
                        body.getPosition().y + (radius / world.PIXELS_TO_METERS)),
                new Vector2(point.getPosition().x + (radius / world.PIXELS_TO_METERS),
                        point.getPosition().y + (radius / world.PIXELS_TO_METERS)));
        jointDef.dampingRatio = 0f;
        jointDef.frequencyHz = 50;
        jointDef.length = Settings.COIN_JOINT_DISTANCE / world.PIXELS_TO_METERS;
        jointDef.collideConnected = false;
        body.createFixture(fixtureDef);
        //point.createFixture(fixtureDefP);
        world.getWorldB().createJoint(jointDef);
        //world.getWorldB().createJoint(jointDef2);
        shape.dispose();

        //PARTICLE EFFECT
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("coin.p"), Gdx.files.internal(""));
        effect.setPosition(300, 300);


        //TWEENS
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Vector2.class, new VectorAccessor());
        manager = new TweenManager();

        reset();
    }

    public void update(float delta) {

        manager.update(delta);
        sprite.setPosition((body.getPosition().x * world.PIXELS_TO_METERS),
                (body.getPosition().y * world.PIXELS_TO_METERS));
        point.setTransform(this.x / world.PIXELS_TO_METERS, this.y / world.PIXELS_TO_METERS, 0);
        limitVel();
        effect.update(delta);
        effect.setPosition(body.getWorldPoint(body.getLocalCenter()).x * world.PIXELS_TO_METERS,
                body.getWorldPoint(body.getLocalCenter()).y * world.PIXELS_TO_METERS);
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
        effect.draw(batcher);
    }

    public void reset() {
        scale(0, .4f, .1f);
    }

    public void scale(float from, float duration, float delay) {
        sprite.setScale(from);
        Tween.to(sprite, SpriteAccessor.SCALE, duration).target(1).delay(delay)
                .ease(TweenEquations.easeOutBounce).start(manager);
    }
}
