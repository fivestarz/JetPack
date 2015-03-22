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

import configuration.Settings;
import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 20/03/15.
 */
public class Meteor {
    private GameWorld world;
    private int x, y;
    private float radius;
    private Sprite sprite;
    private Body body;
    private float velRandom;
    private ParticleEffect effect;
    private float angleVel = MathUtils.random(-3f, 3f);

    public Meteor(GameWorld world, int x, int y, float radius) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.radius = radius;

        //ToDO: Solve Texture
        sprite = new Sprite(AssetLoader.meteor);
        sprite.setPosition(x, y);
        sprite.setSize(radius * 2, radius * 2);
        sprite.setRotation(MathUtils.random(0, 360));
        sprite.setOriginCenter();
        //sprite.setAlpha(MathUtils.random(0.6f, 0.8f));
        BodyDef bodyDef1 = new BodyDef();
        bodyDef1.type = BodyDef.BodyType.DynamicBody;
        bodyDef1.position.set(sprite.getX() / world.PIXELS_TO_METERS,
                sprite.getY() / world.PIXELS_TO_METERS);

        body = world.getWorldB().createBody(bodyDef1);
        body.setGravityScale(0);
        CircleShape shape = new CircleShape();
        shape.setPosition(new Vector2(sprite.getWidth() / 2 / world.PIXELS_TO_METERS,
                sprite.getHeight()
                        / 2 / world.PIXELS_TO_METERS));
        shape.setRadius(radius / world.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = .7f;
        fixtureDef.restitution = 1;
        fixtureDef.friction = 0;

        body.createFixture(fixtureDef);
        shape.dispose();
        reset();

        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("meteor.p"), Gdx.files.internal(""));
        effect.setPosition(300, 300);

    }

    public void update(float delta) {
        sprite.setRotation(sprite.getRotation() + angleVel);
        sprite.setPosition((body.getPosition().x * world.PIXELS_TO_METERS),
                (body.getPosition().y * world.PIXELS_TO_METERS));
        // Ditto for rotation
        //sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        sprite.setOriginCenter();
        effect.update(delta);
        effect.setPosition(sprite.getX() + (sprite.getWidth() / 2),
                sprite.getY() + (sprite.getHeight() / 2));
        body.setLinearVelocity(body.getLinearVelocity().nor().x * velRandom,
                body.getLinearVelocity().nor().y * velRandom);
        limitVel();
        outOfBounds();
    }

    private void limitVel() {

        if (body.getLinearVelocity().y > Settings.METEOR_MAX_VEL) {
            body.setLinearVelocity(body.getLinearVelocity().x, Settings.METEOR_MAX_VEL);
        }
        if (body.getLinearVelocity().y < -Settings.METEOR_MAX_VEL) {
            body.setLinearVelocity(body.getLinearVelocity().x, -Settings.METEOR_MAX_VEL);
        }

        if (body.getLinearVelocity().x > Settings.METEOR_MAX_VEL) {
            body.setLinearVelocity(Settings.METEOR_MAX_VEL, body.getLinearVelocity().y);
        }
        if (body.getLinearVelocity().x < -Settings.METEOR_MAX_VEL) {
            body.setLinearVelocity(-Settings.METEOR_MAX_VEL, body.getLinearVelocity().y);
        }
    }

    private void outOfBounds() {
        if (body.getPosition().y * world.PIXELS_TO_METERS > (world.gameHeight - world.marginOfPoints + 5)) {
            reset();
        } else if (body.getPosition().y * world.PIXELS_TO_METERS < world.marginOfPoints - 5) {
            reset();
        } else if (body
                .getPosition().x * world.PIXELS_TO_METERS > (world.gameWidth - world.marginOfPoints + 5)) {
            reset();
        } else if (body.getPosition().x * world.PIXELS_TO_METERS < world.marginOfPoints - 5) {
            reset();
        }
    }

    public void render(SpriteBatch batcher, ShapeRenderer shapeRenderer) {
        sprite.draw(batcher);
        effect.draw(batcher);
    }

    public void reset() {
        velRandom = MathUtils.random(Settings.METEOR_MIN_VEL, Settings.METEOR_MAX_VEL);
        Vector2 randomPoint = world.getPoints()
                .get(MathUtils.random(0, world.getPoints().size - 1));
        Vector2 randomPointDir = world.getPointsDir()
                .get(MathUtils.random(0, world.getPointsDir().size - 1));
        body.setTransform(randomPoint.x / world.PIXELS_TO_METERS,
                randomPoint.y / world.PIXELS_TO_METERS, 0);
        //body.setLinearVelocity(Math.random() < 0.5 ? 2 : -2, Math.random() < 0.5 ? 2 : -2);
        //Vector2 vel = new Vector2((randomPointDir.x) - (sprite.getX()),(randomPointDir.y) - (sprite.getY()));
        //body.setLinearVelocity(vel.nor().x * 10, vel.nor().y * 10);
        body.setLinearVelocity(MathUtils.random(-10, 10), MathUtils.random(-10, 10));
    }
}
