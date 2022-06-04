package cubepuzzle;

public interface Constants {
    // animation constants
    public static final double ANIME_DURATION = 0.3;  // unit: sec
    public static final double ANIME_FPS      = 15.0; // frames per second
    public static final int ANIME_NONE        = 0;    // no animation
    public static final int ANIME_LEFT        = 1;
    public static final int ANIME_RIGHT       = 2;
    public static final int ANIME_IN          = 3;
    public static final int ANIME_OUT         = 4;

    
    // button names
    public static final String BUTTON_RESTART    = "Restart"    ;
    public static final String BUTTON_RUNCOMMAND = "Run Command";
    public static final String BUTTON_GET_TIPS   = "Get Tips"   ;
    public static final String BUTTON_EDIT       = "Edit Map"   ;
    public static final String Button_SET_VALUE  = "Set Value"  ;
    public static final String BUTTON_GET_VALUE  = "Get Value"  ;
    public static final String BUTTON_SAVE_FILE  = "Save File"  ;

    // chessboard arguments
    public static final int CHESSBOARD_EXTRA_WIDTH = 0;


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
    public static final int COLOR_PINK   =  7;
    public static final int COLOR_GRAY   =  8;
    public static final int COLOR_CYAN   =  9;

    public static final int    CP_HEIGHT     = 300;
    public static final int    CP_WIDTH      = 200;
    public static final int    CP_SEPERATE   = 10;
    public static final String CP_TITLE      = "Cube Puzzle Controller Panel";
    public static final String CP_EDIT_TITLE = "Cube Puzzle Edit Frame";
    public static final int    CP_ROWS       = 20;
    public static final int    CP_COLS       = 1;

    // define the 6 surfaces of a cube
    // xface, yface, zface are the three faces that can be seen on the screen
    public static final int CUBE_X_FACE = 4;
    public static final int CUBE_X_BACK = 1;
    public static final int CUBE_Y_FACE = 2;
    public static final int CUBE_Y_BACK = 0;
    public static final int CUBE_Z_FACE = 5;
    public static final int CUBE_Z_BACK = 3;
    

    // another usefull name for the six surface of a cube
    // this discription is more clear and consious
    public static final int CUBE_FRONT  = CUBE_X_FACE;
    public static final int CUBE_BACK   = CUBE_X_BACK;
    public static final int CUBE_RIGHT  = CUBE_Y_FACE;
    public static final int CUBE_LEFT   = CUBE_Y_BACK;
    public static final int CUBE_TOP    = CUBE_Z_FACE;
    public static final int CUBE_BOTTOM = CUBE_Z_BACK;

    
    // these error code is used when error occured
    // and the program exit
    public static final int ERROR_NO_GAME_FOLDER       = 1;
    public static final int ERROR_RUNTIME_NO_GAME_FILE = 2;


    // dir related
    public static final String DIR_GAME = "./games";

    
    // constants in the game
    public static final String GAME_NOT_START = "You need to start a game first!";
    public static final String GAME_NO_SOURCE = "There is no game in the games folder!";


    // sizeof the MainGUI
    public static final int MAIN_WIDTH  = 1920;
    public static final int MAIN_HEIGHT = 1080;


    // constants for math drawing
    // this constansts need to correspond with the UI constants
    public static final int    MATH_MAX_WIDTH   = 2000;
    public static final double MATH_EPS         = 1e-6;
    public static final int    MATH_RNDCNT      = 20;
    public static final int    MATH_MIN_DEG     = 2;


    // messages in the game
    public static final String MSG_GAMEERR = "something is wrong with the game file";
    public static final String MSG_WIN = "You have already win";


    // Resource constants for Resource.Basic
    // basic elements of the game
    public static final String RESOURCE_BASIC      = "basic"     ;
    public static final String RESOURCE_NONE       = "none"      ;
    public static final String RESOURCE_KEEPOUT    = "keepout"   ;
    public static final String RESOURCE_COLORCHECK = "colorcheck";

    
    // describe the surface of the square
    public interface SURFACE {
        // link the surfaces with it's corner id
        final int[] cubeLeft   = {1, 0, 4, 5};
        final int[] cubeRight  = {3, 2, 6, 7};
        final int[] cubeTop    = {1, 3, 7, 5};
        final int[] cubeBottom = {0, 2, 6, 4};
        final int[] cubeBack   = {1, 3, 2, 0};
        final int[] cubeFront  = {5, 7, 6, 4};
    }

    
    // TIPS constants
    public static final String TIPS_NOGAME  = "Please start a new game";
    public static final String TIPS_YOULOSE = "You have lost";


    // UI constants to show the fram
    public static final int UI_WIDTH  = 1000;
    public static final int UI_HEIGHT =  1000;
    public static final String UI_TITLE = "Cube Puzzle";
}
