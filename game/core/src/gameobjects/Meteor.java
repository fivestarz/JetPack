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

    public Meteor(GameWorld world, int x, int y, float radius) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.radius = radius;
        sprite = new Sprite(AssetLoader.dot);

        sprite.setPosition(x, y);
        sprite.setSize(radius * 2, radius * 2);
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
    }

    public void update(float delta) {
        sprite.setPosition((body.getPosition().x * world.PIXELS_TO_METERS),
                (body.getPosition().y * world.PIXELS_TO_METERS));
        // Ditto for rotation
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        sprite.setOriginCenter();

        body.setLinearVelocity(body.getLinearVelocity().nor().x * 2,
                body.getLinearVelocity().nor().y * 2);
        limitVel();
        outOfBounds();
    }

    private void limitVel() {

        if (body.getLinearVelocity().y > 2) {
            body.setLinearVelocity(body.getLinearVelocity().x, 2);
        }
        if (body.getLinearVelocity().y < -2) {
            body.setLinearVelocity(body.getLinearVelocity().x, -2);
        }

        if (body.getLinearVelocity().x > 2) {
            body.setLinearVelocity(2, body.getLinearVelocity().y);
        }
        if (body.getLinearVelocity().x < -2) {
            body.setLinearVelocity(-2, body.getLinearVelocity().y);
        }
    }

    private void outOfBounds() {
        if (body.getPosition().y * world.PIXELS_TO_METERS > (world.gameHeight - world.marginOfPoints + 5)) {
            reset();
        } else if (body.getPosition().y * world.PIXELS_TO_METERS < world.marginOfPoints - 5) {
            reset();
        } else if (body.getPosition().x * world.PIXELS_TO_METERS > (world.gameWidth - world.marginOfPoints + 5)) {
            reset();
        } else if (body.getPosition().x * world.PIXELS_TO_METERS <  world.marginOfPoints - 5) {
            reset();
        }
    }

    public void render(SpriteBatch batcher, ShapeRenderer shapeRenderer) {
        sprite.draw(batcher);
    }

    public void reset() {
        Vector2 randomPoint = world.getPoints()
                .get(MathUtils.random(0, world.getPoints().size - 1));
        Vector2 randomPointDir = world.getPointsDir()
                .get(MathUtils.random(0, world.getPointsDir().size - 1));
        body.setTransform(randomPoint.x / world.PIXELS_TO_METERS,
                randomPoint.y / world.PIXELS_TO_METERS, 0);
        //body.setLinearVelocity(Math.random() < 0.5 ? 2 : -2, Math.random() < 0.5 ? 2 : -2);
        Vector2 vel = new Vector2((randomPointDir.x) - (sprite.getX()),
                (randomPointDir.y) - (sprite.getY()));
        body.setLinearVelocity(vel.nor().x * 10, vel.nor().y * 10);
        //body.setLinearVelocity(MathUtils.random(-10, 10), MathUtils.random(-10, 10));
    }
}
