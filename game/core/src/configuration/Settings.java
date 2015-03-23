package configuration;

/**
 * Created by ManuGil on 21/03/15.
 */
public class Settings {
    //The values listed above are game variables, that modify how the game actually is
    //I do not recommend changing this values at all
    //Take care

    //GAMEWORLD
    public static final int NUMBER_INITIAL_BACKGROUND_STARS = 180;

    //COLLISION
    public static final short CATEGORY_HERO = 0x0001;  // 0000000000000001 in binary
    public static final short CATEGORY_COIN = 0x0002; // 0000000000000010 in binary
    public static final short CATEGORY_METEOR = 0x0004; // 0000000000000100 in binary
    public static final short MASK_HERO = CATEGORY_METEOR; // or ~CATEGORY_PLAYER
    public static final short MASK_COIN = CATEGORY_METEOR; // or ~CATEGORY_MONSTER
    public static final short MASK_METEOR = CATEGORY_HERO | CATEGORY_COIN; // or ~CATEGORY_MONSTER

    //final short MASK_SCENERY = -1;
    //HERO


    //METEORS
    public static final int NUMBER_INITIAL_METEORS = 15;
    public static final float METEOR_MIN_VEL = 2.5f;
    public static final float METEOR_MAX_VEL = 3f;

    //COINS
    public static final int NUMBER_INITIAL_COINS = 3;
    public static final float COIN_MAX_VEL = 0.2f;
    public static final float COIN_JOINT_DISTANCE = 15f;
}
