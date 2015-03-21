package gameworld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import configuration.Configuration;
import gameobjects.Background;
import gameobjects.Hero;
import gameobjects.Meteor;
import gameobjects.Star;
import helpers.AssetLoader;
import helpers.FlatColors;
import noon.ActionResolver;
import noon.NoonGame;

/**
 * Created by ManuGil on 09/03/15.
 */

public class GameWorld {

    public final float w;
    //GENERAL VARIABLES
    public float gameWidth;
    public float gameHeight;
    public float worldWidth;
    public float worldHeight;

    public ActionResolver actionResolver;
    public NoonGame game;
    public GameWorld world = this;

    //GAME CAMERA
    private GameCam camera;

    //VARIABLES
    private GameState gameState;
    private int score;
    private final int numberOfStars = 180;
    private final int numberOfMeteors = 10;
    private final int numberOfPoints = 10;
    public final int marginOfPoints = 100;

    //GAMEOBJECTS
    private Background background;
    private Array<Star> stars = new Array<Star>();
    private Array<Meteor> meteors = new Array<Meteor>();
    private Array<Vector2> points = new Array<Vector2>();
    private Array<Vector2> pointsDir = new Array<Vector2>();
    private Meteor meteor;
    private Hero hero;

    //BOX2D
    private World worldB;
    Body body;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    Sprite sprite;

    public final float PIXELS_TO_METERS = 100f;
    public boolean clicked, clickedLeft, clickedRight;

    public GameWorld(NoonGame game, ActionResolver actionResolver, float gameWidth,
                     float gameHeight, float worldWidth, float worldHeight) {
        this.gameWidth = gameWidth;
        this.w = gameHeight / 100;
        this.gameHeight = gameHeight;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.game = game;
        this.actionResolver = actionResolver;

        //TODO: Remove this line
        actionResolver.viewAd(false);
        camera = new GameCam(this, 0, 0, gameWidth, gameHeight);
        gameState = GameState.RUNNING;
        reset();

    }

    public void reset() {
        background = new Background(this, 0, 0, gameWidth, gameHeight, AssetLoader.square,
                world.parseColor("11031A", 1f));

        stars.clear();
        for (int i = 0; i < numberOfStars; i++) {
            stars.add(new Star(world));
        }

        points.clear();
        pointsDir.clear();
        for (int i = 0; i < numberOfPoints; i++) {
            points.add(new Vector2(marginOfPoints, gameHeight / (numberOfPoints + 1) * (i + 1)));
            points.add(new Vector2(gameWidth - marginOfPoints,
                    gameHeight / (numberOfPoints + 1) * (i + 1)));
            points.add(new Vector2(gameWidth / (numberOfPoints + 1) * (i + 1), marginOfPoints));
            points.add(new Vector2(gameWidth / (numberOfPoints + 1) * (i + 1),
                    gameHeight - marginOfPoints));
            pointsDir.add(new Vector2(gameWidth / 2, gameHeight / (numberOfPoints + 1) * (i + 1)));
        }

        //BOX2D
        worldB = new World(new Vector2(0, -6.8f), true);
        debugRenderer = new Box2DDebugRenderer();
        hero = new Hero(this, 500, 500, 60, 60);
        meteors.clear();
        int j = 0;
        for (int i = 0; i < numberOfMeteors; i++) {
            meteor = new Meteor(this, (int) MathUtils.random(0, gameWidth),
                    (int) MathUtils.random(0, gameHeight), 20);
            meteors.add(meteor);
        }
    }


    public void update(float delta) {
        worldB.step(1f / 60f, 6, 2);
        for (int i = 0; i < numberOfStars; i++) {
            stars.get(i).update(delta);
        }
        hero.update(delta);
        for (int i = 0; i < numberOfMeteors; i++) {
            meteors.get(i).update(delta);
        }
        meteor.update(delta);
    }


    public void render(SpriteBatch batcher, ShapeRenderer shapeRenderer, ShaderProgram fontShader) {
        batcher.end();
        debugMatrix = batcher.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);
        batcher.begin();
        camera.render(batcher, shapeRenderer);

        //RENDERING GAME OBJECTS
        background.render(batcher, shapeRenderer);
        for (int i = 0; i < numberOfMeteors; i++) {
            meteors.get(i).render(batcher, shapeRenderer);
        }
        hero.render(batcher, shapeRenderer);

        for (int i = 0; i < numberOfStars; i++) {
            stars.get(i).render(batcher, shapeRenderer);
        }
        if (Configuration.DEBUG) {
            batcher.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(FlatColors.WHITE);
            for (int i = 0; i < points.size; i++) {
                shapeRenderer.circle(points.get(i).x, points.get(i).y, 4);
            }
            shapeRenderer.setColor(FlatColors.YELLOW);
            for (int i = 0; i < pointsDir.size; i++) {
                shapeRenderer.circle(pointsDir.get(i).x, pointsDir.get(i).y, 4);
            }
            shapeRenderer.end();
            debugRenderer.render(worldB, debugMatrix);
            batcher.begin();
        }

    }

    public void finishGame() {
        saveScoreLogic();
    }

    private void saveScoreLogic() {
        AssetLoader.addGamesPlayed();
        int gamesPlayed = AssetLoader.getGamesPlayed();

        // GAMES PLAYED ACHIEVEMENTS!
        actionResolver.submitScore(score);
        actionResolver.submitGamesPlayed(gamesPlayed);
        if (score > AssetLoader.getHighScore()) {
            AssetLoader.setHighScore(score);
        }
        //checkAchievements();
    }

    public void startGame() {
        score = 0;
    }

    public GameCam getCamera() {
        return camera;
    }

    public int getScore() {
        return score;
    }


    public static Color parseColor(String hex, float alpha) {
        String hex1 = hex;
        if (hex1.indexOf("#") != -1) {
            hex1 = hex1.substring(1);
            // Gdx.app.log("Hex", hex1);
        }
        Color color = Color.valueOf(hex1);
        color.a = alpha;
        return color;
    }

    public Body getBody() {
        return body;
    }

    public boolean isRunning() {
        return gameState == GameState.RUNNING;
    }

    public World getWorldB() {
        return worldB;
    }

    public Hero getHero() {
        return hero;
    }

    public Array<Vector2> getPoints() {
        return points;
    }

    public Array<Vector2> getPointsDir() {
        return pointsDir;
    }
}