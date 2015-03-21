package noon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;

import helpers.AssetLoader;
import screens.SplashScreen;

public class NoonGame extends Game {
	private ActionResolver actionresolver;


    public NoonGame(ActionResolver actionresolver) {
        this.actionresolver = actionresolver;
    }

    @Override
    public void create() {
        AssetLoader.load();
        setScreen(new SplashScreen(this, actionresolver));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
