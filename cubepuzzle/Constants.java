package cubepuzzle;

public class Constants {
    // define the 6 surfaces of a cube
    // xface, yface, zface are the three faces that can be seen on the screen
    public static final int CUBE_X_FACE = 4;
    public static final int CUBE_X_BACK = 5;
    public static final int CUBE_Y_FACE = 2;
    public static final int CUBE_Y_BACK = 0;
    public static final int CUBE_Z_FACE = 1;
    public static final int CUBE_Z_BACK = 3;
    
    // another usefull name for the six surface of a cube
    // this discription is more clear and consious
    public static final int CUBE_FRONT  = CUBE_X_FACE;
    public static final int CUBE_BACK   = CUBE_X_BACK;
    public static final int CUBE_RIGHT  = CUBE_Y_FACE;
    public static final int CUBE_LEFT   = CUBE_Y_BACK;
    public static final int CUBE_TOP    = CUBE_Z_FACE;
    public static final int CUBE_BOTTOM = CUBE_Z_BACK;


    // define the color_id which may appear on the ground/cube
    // use colorError to repersent a block can not be entered.
    public static final int COLOR_ERROR  = -1;
    public static final int COLOR_WHITE  =  0;
    public static final int COLOR_BLACK  =  1;
    public static final int COLOR_RED    =  2;
    public static final int COLOR_GREEN  =  3;
    public static final int COLOR_BLUE   =  4;
    public static final int COLOR_ORANGE =  5;
    public static final int COLOR_YELLOW =  6;
    public static final int COLOR_BROWN  =  7;
}
