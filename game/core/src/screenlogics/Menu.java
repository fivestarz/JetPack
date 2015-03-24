package screenlogics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

/**
 * Created by ManuGil on 23/03/15.
 */
public class Menu {
    private GameWorld world;
    public Array<MenuButton> menubuttons = new Array<MenuButton>();
    public MenuButton playButton, shareButton, removeadsButton, scoresButton, title;
    private Value second = new Value();
    private TweenManager manager;
    private TweenCallback cbStartGame;

    public Menu(final GameWorld world) {
        this.world = world;
        title = new MenuButton(world,
                world.gameWidth / 2 - (AssetLoader.title.getRegionWidth() / 2),
                world.gameHeight - 80 - AssetLoader.title.getRegionHeight(),
                AssetLoader.title.getRegionWidth(), AssetLoader.title.getRegionHeight(),
                AssetLoader.title, Color.WHITE);

        playButton = new MenuButton(world, world.gameWidth / 2 - 400, 130 + 360 + 20, 800, 120,
                AssetLoader.playButton, Color.WHITE);
        scoresButton = new MenuButton(world, world.gameWidth / 2 - 400,
                100 + 240 + 20, 800, 120,
                AssetLoader.scoresButton, Color.WHITE);
        shareButton = new MenuButton(world, world.gameWidth / 2 - 400,
                70 + 120 + 20, 800, 120,
                AssetLoader.shareButton, Color.WHITE);
        removeadsButton = new MenuButton(world, world.gameWidth / 2 - 400, 60, 800, 120,
                AssetLoader.removeadsButton, Color.WHITE);

        menubuttons.add(title);
        menubuttons.add(playButton);
        menubuttons.add(scoresButton);
        menubuttons.add(shareButton);
        menubuttons.add(removeadsButton);
        manager = new TweenManager();
        cbStartGame = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                world.gameState = GameState.TUTORIAL;
                world.getTutorial().fadeIn(.3f, 0f);
                world.resetGAME();
            }
        };
    }

    public void start() {
        for (int i = 0; i < menubuttons.size; i++) {
            menubuttons.get(i).effectX(menubuttons.get(i).getPosition().x - world.gameWidth,
                    menubuttons.get(i).getPosition().x, .5f, .05f * i);
        }
        world.gameState = GameState.MENU;
    }

    public void update(float delta) {
        manager.update(delta);
        for (int i = 0; i < menubuttons.size; i++) {
            menubuttons.get(i).update(delta);
        }
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        for (int i = 0; i < menubuttons.size; i++) {
            menubuttons.get(i).render(batch, shapeRenderer);
        }
    }

    public void startGame() {
        Tween.to(second, -1, .6f).target(1).setCallback(cbStartGame)
                .setCallbackTriggers(TweenCallback.COMPLETE).start(
                manager);
        for (int i = menubuttons.size - 1; i >= 0; i--) {
            menubuttons.get(i).effectX(menubuttons.get(i).getPosition().x,
                    menubuttons.get(i).getPosition().x + world.gameWidth, .5f, .25f - (i * .05f));
        }

    }

    public void makeThemReturn() {
        for (int i = 0; i <menubuttons.size; i++) {
            menubuttons.get(i).setX(menubuttons.get(i).getPosition().x-(world.gameWidth*1));
        }
    }
}