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
}
