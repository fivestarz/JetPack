package configuration;

import helpers.AssetLoader;

/**
 * Created by ManuGil on 09/03/15.
 */

public class Configuration {

    public static final String GAME_NAME = "Impossible Jetpack";
    public static boolean DEBUG = false;
    public static final boolean SPLASHSCREEN = true;

    //ADMOB IDS
    public static final String AD_UNIT_ID_BANNER = "ca-app-pub-6147578034437241/4745179018";
    public static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-6147578034437241/6221912212";
    public static float AD_FREQUENCY = .9f;

    //In App Purchases
    public static final String ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0Yqrjl7YEidktXOoxyYAivS1sKOlQeB2sTDuGZnDz0D8jKh7o9iFs/EsrLhS++4C1+Y6VWVZDqmMUuocRVVl1D78X2kBHNHZR8Au8wVWe5Zm50ZOeEsY+YTQpjMsJKrNCghScWaJPHShQiktlA6uC91W8z/54TG/atkIgai+fY5pvesqP7lLC0fONtlfLOMyjQnJvUkIQPaSBj2wImJHPAccPMW56olapIYspFe9JGPhiI1qYTXhr4Vz06VyyPcO34ZYbuyIkES5kAWmVA/PjAS+2oEEbB1I6AtmeJosWQHyfRZ4PJvC7bvs4RuPGwNHZ+mgQhUj084DAe2NPoXVJwIDAQAB";
    public static final String PRODUCT_ID = "removeads";

    //LEADERBOARDS
    public static final String LEADERBOARD_HIGHSCORE = "CgkI66bg6vUHEAIQBQ";
    public static final String LEADERBOARD_GAMESPLAYED = "CgkI66bg6vUHEAIQBg";

    //Share Message
    public static final String SHARE_MESSAGE = "Can you beat my high score of " + AssetLoader
            .getHighScore() + " at " + GAME_NAME + "?";
}
