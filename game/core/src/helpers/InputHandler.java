package helpers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import gameworld.GameWorld;

/**
 * Created by ManuGil on 09/03/15.
 */
public class InputHandler implements InputProcessor {

    private GameWorld world;
    private float scaleFactorX;
    private float scaleFactorY;
    private int activeTouch = 0;

    public InputHandler(GameWorld world, float scaleFactorX, float scaleFactorY) {
        this.world = world;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.RIGHT) {
            world.getHero().clickedRight();
        } else if (keycode == Input.Keys.LEFT) {
            world.getHero().clickedLeft();
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.R) {
            world.reset();
        }
        if (keycode == Input.Keys.RIGHT) {
            world.getHero().notClickedRight();
        } else if (keycode == Input.Keys.LEFT) {
            world.getHero().notClickedLeft();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        activeTouch++;
        if (world.isRunning()) {
            if (activeTouch == 1) {
                if (screenX > world.gameWidth / 2) {
                    world.getHero().clickedRight();
                } else {
                    world.getHero().clickedLeft();
                }
            }else if(activeTouch==2){
                if(world.getHero().clickedLeft){
                    world.getHero().clickedRight();
                }else if(world.getHero().clickedRight){
                    world.getHero().clickedLeft();
                }
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        activeTouch--;

        if (world.isRunning()) {
            if (activeTouch == 0) {
                world.getHero().notClickedRight();
                world.getHero().notClickedLeft();
            } else {
                if (screenX > world.gameWidth / 2) {
                    world.getHero().notClickedRight();
                } else {
                    world.getHero().notClickedLeft();
                }
            }
        } else if (world.isTutorial()) {
            world.finishTutorial();
        }
        /*if (activeTouch == 0) {
            world.getHero().notClickedLeft();
            world.getHero().notClickedRight();
        }*/
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (world.gameHeight - screenY / scaleFactorY);
    }
}
