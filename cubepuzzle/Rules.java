package cubepuzzle;

public class Rules extends Constants{
    public static boolean checkPosEnterAvailable(String gridType, Cube myCube) throws Exception {
        gridType = gridType.toLowerCase();
        String[] dirs = gridType.split("\\.", 2);
        if(dirs[0].equals(RESOURCE_BASIC)) {
            return Basic.checkPosEnterAvailable(dirs[1], myCube);
        }else {
            System.err.println("Rules.checkPosEnterAvailable: ");
            System.err.println("\tUnkown girdType " + gridType);
        }
        return true;
    }

    public class Basic {
        public static boolean checkPosEnterAvailable(String gridType, Cube myCube) throws Exception {
            // none means there is no limitation on this grid
            if(gridType.equals(RESOURCE_NONE)) {
                return true;
            }else
            // keepout means no cube can ENTER this grid
            if(gridType.equals(RESOURCE_KEEPOUT)) {
                return false;
            }else
            // to get in this grid
            // a certain color is needed to be on the top
            if(gridType.startsWith(RESOURCE_COLORCHECK)) {
                int colorId = Integer.valueOf(gridType.split("\\.", 2)[1]);
                return colorId == myCube.surfaceColor[CUBE_TOP];
            }else {
                throw new Exception("in Rules.Basic, gridType: " + gridType + " unknown");
            }
        }
    }
}
