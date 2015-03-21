package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import configuration.Configuration;

/**
 * Created by ManuGil on 09/03/15.
 */
public class AssetLoader {

    public static Texture logoTexture, dotT, colorCircleT, outBorderT, insideBorderT, barsT, backgroundT;
    public static TextureRegion logo, square, dot, colorCircle, outBorder, insideBorder, bar1, bar2, bar3, bar4,
            flashBar, flashCircle, background;

    public static BitmapFont font, fontS, fontXS, fontB;
    private static Preferences prefs;

    public static Sound click, success, end;

    public static void load() {
        //LOGO TEXTURE "logo.png"
        logoTexture = new Texture(Gdx.files.internal("logo.png"));
        logoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        logo = new TextureRegion(logoTexture, 0, 0, logoTexture.getWidth(),
                logoTexture.getHeight());

        square = new TextureRegion(new Texture(Gdx.files.internal("square.png")), 0, 0, 10, 10);
        dotT = new Texture(Gdx.files.internal("dot.png"));
        dot = new TextureRegion(dotT, 0, 0, dotT.getWidth(), dotT.getHeight());

        colorCircleT = new Texture(Gdx.files.internal("circle.png"));
        colorCircleT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        colorCircle = new TextureRegion(colorCircleT, 0, 0, 820,
                colorCircleT.getHeight());
        flashCircle = new TextureRegion(colorCircleT, 0, 0, 820, colorCircleT.getHeight());

        insideBorderT = new Texture(Gdx.files.internal("insideBorder.png"));
        insideBorderT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        insideBorder = new TextureRegion(insideBorderT, 0, 0, insideBorderT.getWidth(),
                insideBorderT.getHeight());

        outBorderT = new Texture(Gdx.files.internal("outBorder.png"));
        outBorderT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        outBorder = new TextureRegion(outBorderT, 0, 0, outBorderT.getWidth(),
                outBorderT.getHeight());

        backgroundT = new Texture(Gdx.files.internal("background.png"));
        backgroundT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        background = new TextureRegion(backgroundT, 0, 0, backgroundT.getWidth(),
                backgroundT.getHeight());

        barsT = new Texture(Gdx.files.internal("bars.png"));
        barsT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        bar1 = new TextureRegion(barsT, 0, 0, 30, barsT.getHeight());
        bar2 = new TextureRegion(barsT, 30, 0, 30, barsT.getHeight());
        bar3 = new TextureRegion(barsT, 60, 0, 30, barsT.getHeight());
        bar4 = new TextureRegion(barsT, 90, 0, 30, barsT.getHeight());
        flashBar = new TextureRegion(barsT, 120, 0, 30, barsT.getHeight());

        //LOADING FONT
        Texture tfont = new Texture(Gdx.files.internal("sans.png"), true);
        tfont.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);

        font = new BitmapFont(Gdx.files.internal("sans.fnt"),
                new TextureRegion(tfont), true);
        font.setScale(1.9f, -1.9f);
        font.setColor(FlatColors.WHITE);

        fontB = new BitmapFont(Gdx.files.internal("sans.fnt"),
                new TextureRegion(tfont), true);
        fontB.setScale(1.4f, -1.4f);
        fontB.setColor(FlatColors.WHITE);

        fontS = new BitmapFont(Gdx.files.internal("sans.fnt"),
                new TextureRegion(tfont), true);
        fontS.setScale(1.2f, -1.2f);
        fontS.setColor(FlatColors.WHITE);

        fontXS = new BitmapFont(Gdx.files.internal("sans.fnt"),
                new TextureRegion(tfont), true);
        fontXS.setScale(0.9f, -0.9f);
        fontXS.setColor(FlatColors.WHITE);


        //PREFERENCES - SAVE DATA IN FILE
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);

        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }

        if (!prefs.contains("games")) {
            prefs.putInteger("games", 0);
        }

        click = Gdx.audio.newSound(Gdx.files.internal("blip_click.wav"));
        success = Gdx.audio.newSound(Gdx.files.internal("blip_success.wav"));
        end = Gdx.audio.newSound(Gdx.files.internal("blip_end.wav"));
    }

    public static void dispose() {
        font.dispose();
        fontS.dispose();
        fontXS.dispose();
        fontB.dispose();
        dotT.dispose();
        logoTexture.dispose();
        click.dispose();
        success.dispose();
        end.dispose();
        colorCircleT.dispose();
        insideBorderT.dispose();
        outBorderT.dispose();
        backgroundT.dispose();
        barsT.dispose();

    }

    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public static void addGamesPlayed() {
        prefs.putInteger("games", prefs.getInteger("games") + 1);
        prefs.flush();
    }

    public static int getGamesPlayed() {
        return prefs.getInteger("games");
    }

    public static void setAds(boolean removeAdsVersion) {
        prefs.putBoolean("ads", removeAdsVersion);
        prefs.flush();
    }

    public static boolean getAds() {
        return prefs.getBoolean("ads", false);
    }
}
