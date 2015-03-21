package gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 20/03/15.
 */
public class Hero {
    private GameWorld world;
    private int x, y;
    private float width, height;
    private Sprite sprite;
    private Body body;
    public boolean clickedRight, clickedLeft;

    public Hero(GameWorld world, int x, int y, float width, float height) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        sprite = new Sprite(AssetLoader.colorCircle);

        sprite.setPosition(x, y);
        sprite.setSize(width, height);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2) /
                        world.PIXELS_TO_METERS,
                (sprite.getY() + sprite.getHeight() / 2) / world.PIXELS_TO_METERS);
        bodyDef.fixedRotation = true;

        body = world.getWorldB().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / world.PIXELS_TO_METERS, sprite.getHeight()
                / 2 / world.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.2f;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 1f;
        body.createFixture(fixtureDef);
        shape.dispose();

    }

    public void update(float delta) {
        sprite.setPosition((body.getPosition().x * world.PIXELS_TO_METERS) - sprite.
                        getWidth() / 2,
                (body.getPosition().y * world.PIXELS_TO_METERS) - sprite.getHeight() / 2);
        // Ditto for rotation
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        sprite.setOriginCenter();

        if (body.getLinearVelocity().y > 6) {
            body.setLinearVelocity(body.getLinearVelocity().x, 6);
        } else {
            if (clickedRight) {
                body.applyForceToCenter(2f, +2f, true);
            }
            if (clickedLeft) {
                body.applyForceToCenter(-2f, +2f, true);
            }
        }

        limitVel();
        outOfBounds();
    }

    private void limitVel() {
        if (body.getLinearVelocity().y < -6) {
            body.setLinearVelocity(body.getLinearVelocity().x, -6);
        }

        if (body.getLinearVelocity().x > 3) {
            body.setLinearVelocity(3, body.getLinearVelocity().y);
        }
        if (body.getLinearVelocity().x < -3) {
            body.setLinearVelocity(-3, body.getLinearVelocity().y);
        }
    }


    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        sprite.draw(batch);

    }

    private void outOfBounds() {
        if (body.getPosition().y * world.PIXELS_TO_METERS > (world.gameHeight + (sprite
                .getHeight() / 2))) {
            body.setTransform(body.getPosition().x,
                    (((-sprite.getHeight() / 2)) / world.PIXELS_TO_METERS), 0);
        } else if (body.getPosition().y * world.PIXELS_TO_METERS < -sprite
                .getHeight() / 2) {
            body.setTransform(body.getPosition().x,
                    (world.gameHeight + (sprite.getHeight() / 2)) / world.PIXELS_TO_METERS, 0);
        }

        if (body.getPosition().x * world.PIXELS_TO_METERS > (world.gameWidth + (sprite
                .getWidth() / 2))) {
            body.setTransform((((-sprite.getWidth() / 2)) / world.PIXELS_TO_METERS),
                    body.getPosition().y,
                    0);
        } else if (body.getPosition().x * world.PIXELS_TO_METERS < -sprite
                .getWidth() / 2) {
            body.setTransform((world.gameWidth + (sprite.getWidth() / 2)) / world.PIXELS_TO_METERS,
                    body.getPosition().y, 0);
        }
    }

    public void clickedLeft() {
        world.getHero().getBody().applyForceToCenter(0, 7, true);
        world.getHero().clickedLeft = true;
    }

    public void clickedRight() {
        world.getHero().getBody().applyForceToCenter(0, 7, true);
        world.getHero().clickedRight = true;
    }

    public void notClickedLeft() {
        world.getHero().getBody().applyForceToCenter(+2, -6, true);
        world.getHero().clickedLeft = false;
    }

    public void notClickedRight() {
        world.getHero().getBody().applyForceToCenter(-2, -6, true);
        world.getHero().clickedRight = false;
    }

    public Body getBody() {
        return body;
    }
}
