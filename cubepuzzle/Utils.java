package cubepuzzle;

public interface Utils {
    public static String getDirName(int dirValue) {
        if(dirValue == Constants.ANIME_IN)         return "IN";
        else if(dirValue == Constants.ANIME_OUT)   return "OUT";
        else if(dirValue == Constants.ANIME_LEFT)  return "LEFT";
        else if(dirValue == Constants.ANIME_RIGHT) return "RIGHT";
        else {
            return "NOTDIR";
        }
    }

    public static int[][] getCubeSurfaces() {
        int[][] cubeSurfaces = new int[6][4];
        cubeSurfaces[Constants.CUBE_BACK]   = Constants.SURFACE.cubeBack   ;
        cubeSurfaces[Constants.CUBE_FRONT]  = Constants.SURFACE.cubeFront  ;
        cubeSurfaces[Constants.CUBE_LEFT]   = Constants.SURFACE.cubeLeft   ;
        cubeSurfaces[Constants.CUBE_RIGHT]  = Constants.SURFACE.cubeRight  ;
        cubeSurfaces[Constants.CUBE_TOP]    = Constants.SURFACE.cubeTop    ;
        cubeSurfaces[Constants.CUBE_BOTTOM] = Constants.SURFACE.cubeBottom ;
        return cubeSurfaces;
    }

    public static double getOx() {
        return Constants.UI_WIDTH / 2;
    }

    public static double getOy() {
        return Constants.UI_HEIGHT / 2;
    }
}
