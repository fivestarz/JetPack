package com.madtriangle.noon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import noon.NoonGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Color Buttons";
        config.height= (int) (1080 / 2);
        config.width= (int) (1720 / 2);
        new LwjglApplication(new NoonGame(new ActionResolverDesktop()), config);
    }
}
