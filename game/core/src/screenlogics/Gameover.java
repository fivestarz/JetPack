package screenlogics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import gameworld.GameState;
import gameworld.GameWorld;
import helpers.AssetLoader;
import tweens.Value;
import ui.MenuButton;
import ui.Text;

/**
 * Created by ManuGil on 23/03/15.
 */
public class Gameover {
    private GameWorld world;
    public Array<MenuButton> menubuttons = new Array<MenuButton>();
    public MenuButton playButtonOver, shareButtonOver, backButtonOver, removeadsButton, board;
    private Value second = new Value();
    private TweenManager manager;
    private TweenCallback cbStartGame, cbStartMenu;
    private Text scoreText, bestText;

    public Gameover(final GameWorld world) {
        this.world = world;
        playButtonOver = new MenuButton(world,
                world.gameWidth / 2 - AssetLoader.playButtonOver.getRegionWidth() - 30,
                40 + 60 + 120 + world.gameHeight,
                AssetLoader.playButtonOver.getRegionWidth(),
                AssetLoader.playButtonOver.getRegionHeight(),
                AssetLoader.playButtonOver, Color.WHITE);

        shareButtonOver = new MenuButton(world, world.gameWidth / 2 + 30,
                40 + 60 + 120 + world.gameHeight,
                AssetLoader.shareButtonOver.getRegionWidth(),
                AssetLoader.shareButtonOver.getRegionHeight(),
                AssetLoader.shareButtonOver, Color.WHITE);

        backButtonOver = new MenuButton(world, world.gameWidth / 2 - 400,
                world.gameHeight - 60 - AssetLoader.backButtonOver
                        .getRegionHeight() + world.gameHeight,
                AssetLoader.backButtonOver.getRegionWidth(),
                AssetLoader.backButtonOver.getRegionHeight(),
                AssetLoader.backButtonOver, Color.WHITE);

        board = new MenuButton(world, world.gameWidth / 2 - 400,
                world.gameHeight - 60 - AssetLoader.boardOver
                        .getRegionHeight() - 35 + world.gameHeight,
                AssetLoader.boardOver.getRegionWidth(),
                AssetLoader.boardOver.getRegionHeight(),
                AssetLoader.boardOver, Color.WHITE);
        removeadsButton = new MenuButton(world, world.gameWidth / 2 - 400, 40 + world.gameHeight,
                800, 120,
                AssetLoader.removeadsButton, Color.WHITE);

        menubuttons.add(playButtonOver);
        menubuttons.add(shareButtonOver);
        menubuttons.add(backButtonOver);
        menubuttons.add(removeadsButton);
        menubuttons.add(board);

        manager = new TweenManager();

        cbStartGame = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                world.gameState = GameState.TUTORIAL;
                world.getTutorial().fadeIn(.3f, 0f);
                world.resetGAME();
            }
        };

        cbStartMenu = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {

                world.getMenu().makeThemReturn();
                world.getMenu().start(0f);
            }
        };

        //TEXTS
        scoreText = new Text(world, world.gameWidth / 2 - 360,
                world.gameHeight - 60 - AssetLoader.boardOver
                        .getRegionHeight() - 35 + world.gameHeight,
                AssetLoader.boardOver.getRegionWidth() - 80,
                AssetLoader.boardOver.getRegionHeight(),
                AssetLoader.transparent, Color.WHITE, "0", AssetLoader.fontS,
                Color.WHITE, 170,
                BitmapFont.HAlignment.RIGHT);

        bestText = new Text(world, world.gameWidth / 2 - 360,
                world.gameHeight - 60 - AssetLoader.boardOver
                        .getRegionHeight() - 35 + world.gameHeight,
                AssetLoader.boardOver.getRegionWidth() - 80,
                AssetLoader.boardOver.getRegionHeight(),
                AssetLoader.transparent, Color.WHITE, AssetLoader.getHighScore() + "",
                AssetLoader.fontS,
                Color.WHITE, 320,
                BitmapFont.HAlignment.RIGHT);
    }

    public void start() {
        AssetLoader.woosh.play();
        scoreText.setText(world.getScore()+"");
        bestText.setText(AssetLoader.getHighScore()+"");
        world.gameState = GameState.GAMEOVER;
        for (int i = 0; i < menubuttons.size; i++) {
            menubuttons.get(i).effectY(menubuttons.get(i).getPosition().y,
                    menubuttons.get(i).getPosition().y - 1080, .4f, 0f);
        }
    }


    public void startGame() {
        AssetLoader.woosh.play();
        Tween.to(second, -1, .6f).target(1).setCallback(cbStartGame)
                .setCallbackTriggers(TweenCallback.COMPLETE).start(
                manager);
        for (int i = menubuttons.size - 1; i >= 0; i--) {
            menubuttons.get(i).effectY(menubuttons.get(i).getPosition().y,
                    menubuttons.get(i).getPosition().y + 1080, .5f, 0f);
        }

    }

    public void update(float delta) {
        manager.update(delta);
        for (int i = 0; i < menubuttons.size; i++) {
            menubuttons.get(i).update(delta);
        }
        scoreText.update(delta);
        scoreText.setY(board.getPosition().y);

        bestText.update(delta);
        bestText.setY(board.getPosition().y);

    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, ShaderProgram fontshader) {
        for (int i = 0; i < menubuttons.size; i++) {
            menubuttons.get(i).render(batch, shapeRenderer);
        }
        scoreText.render(batch, shapeRenderer, fontshader);
        bestText.render(batch, shapeRenderer, fontshader);
    }

    public void startMenu() {

        AssetLoader.woosh.play();
        Tween.to(second, -1, .4f).target(1).setCallback(cbStartMenu)
                .setCallbackTriggers(TweenCallback.COMPLETE).start(
                manager);
        for (int i = menubuttons.size - 1; i >= 0; i--) {
            menubuttons.get(i).effectY(menubuttons.get(i).getPosition().y,
                    menubuttons.get(i).getPosition().y + 1080, .4f, 0f);
        }
    }
}
