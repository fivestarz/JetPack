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
 * Created by ManuGil on 22/03/15.
 */
public class Coin {

    private GameWorld world;
    private int x, y;
    private float radius;
    private Sprite sprite;
    private Body body;

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

        BodyDef bodyDefC = new BodyDef();
        bodyDefC.type = BodyDef.BodyType.DynamicBody;
        bodyDefC.position.set(sprite.getX() / world.PIXELS_TO_METERS,
                sprite.getY() / world.PIXELS_TO_METERS);

        body = world.getWorldB().createBody(bodyDefC);
        body.setGravityScale(0);

        CircleShape shape = new CircleShape();
        shape.setPosition(new Vector2(sprite.getWidth() / 2 / world.PIXELS_TO_METERS,
                sprite.getHeight()
                        / 2 / world.PIXELS_TO_METERS));
        shape.setRadius(radius / world.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = .7f;
        fixtureDef.restitution = 0.3f;
        fixtureDef.friction = 1f;

        body.createFixture(fixtureDef);
        shape.dispose();

        reset();
    }

    public void update(float delta) {
        sprite.setPosition((body.getPosition().x * world.PIXELS_TO_METERS),
                (body.getPosition().y * world.PIXELS_TO_METERS));
    }

    public void render(SpriteBatch batcher, ShapeRenderer shapeRenderer) {

    }

    public void reset(){

    }
}
