package cubepuzzle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    // get a number list 0 .. maxLen - 1 in type string[]
    public static String[] getNumberList(int maxLen) {
        String[] lis = new String[maxLen];
        for(int i = 0; i < maxLen; i ++) {
            lis[i] = Integer.toString(i); // get the string value of an integer
        }
        return lis;
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

    public static boolean gameDirExist() {
        File gamesDir = new File(Constants.DIR_GAME);
        if(!gamesDir.exists() || !gamesDir.isDirectory()) return false;
        return true;
    }

    public static String[] getGames() {
        List<String> fileNameList = new ArrayList<>();

        File gamesDir = new File(Constants.DIR_GAME);
        for(File file: gamesDir.listFiles()) {
            if(file.exists() && !file.isDirectory()) fileNameList.add(file.getName());
        }

        String[] ansList = new String[fileNameList.size()];
        for(int i = 0; i < ansList.length; i ++) {
            ansList[i] = fileNameList.get(i);
        }
        return ansList;
    }
}
