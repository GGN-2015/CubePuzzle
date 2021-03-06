package cubepuzzle;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Resources {
    public static void drawChessboardGrid(Graphics g2d, String gridName, int posX, int posY) throws Exception {
        if(gridName == null) {
            System.err.println("Resources.drawChessboardGrid:");
            System.err.println("\tyou need to initialize gameNow.chessboard before draw it.");
        }else {
            // Ignore lowerCase and upperCase
            gridName = gridName.toLowerCase();
            // ! remember just split once
            String[] dir = gridName.split("\\.", 2); 
            // basic method that provided by the Game itself
            if(dir[0].equals(Constants.RESOURCE_BASIC)) {
                Basic.drawChessboardGrid(g2d, dir[1], posX, posY);
            }else {
                throw new Exception("draw method for gridName: '" + gridName + "'' not found.");
            }
        }
    }

    public static void drawChessboardEdge(Graphics g2d, int posX, int posY) throws Exception {
        //! do not add name Constants.RESOURCE_BASIC because you are using basic
        Basic.drawChessboardGridWithColor((Graphics2D)g2d, posX, posY, Constants.COLOR_GRAY);
    }

    public static TupleReal[] getTupleRealArrayForCube(int bx, int by, int bz) {
        TupleReal[] p3d = new TupleReal[8];
        for(int i = 0; i < p3d.length; i ++) {
            double nx = bx + ((i >> 2) & 1);
            double ny = by + ((i >> 1) & 1);
            double nz = bz + ((i >> 0) & 1);
            p3d[i] = new TupleReal(nx, ny, nz);
        }
        //! debug rotate only
        // p3d = MathTransform.rotate(p3d, TupleReal.ZERO, TupleReal.VECX, Math.PI / 6);
        return p3d;
    }

    // get p2d array for p3d array
    public static PairInt[] getPairIntArrayForCube(int bx, int by, int bz) {
        TupleReal[] p3d = getTupleRealArrayForCube(bx, by, bz);
        return MathTransform.getPairIntArrayForTupleRealArray(p3d);
    }

    public static void drawCubeOld(Graphics g2d, Game gameNow) {
        PairInt[] cubeCorners = getPairIntArrayForCube(gameNow.posX, gameNow.posY, 0);
        PairInt[] sufaceFront = {cubeCorners[5], cubeCorners[4], cubeCorners[6], cubeCorners[7]};
        PairInt[] sufaceTop   = {cubeCorners[5], cubeCorners[7], cubeCorners[3], cubeCorners[1]};
        PairInt[] sufaceRight = {cubeCorners[6], cubeCorners[7], cubeCorners[3], cubeCorners[2]};
        Basic.drawFilledShape((Graphics2D)g2d, sufaceFront, gameNow.myCube.surfaceColor[Constants.CUBE_FRONT]);
        Basic.drawLineShape  ((Graphics2D)g2d, sufaceFront, Constants.COLOR_BLACK);
        Basic.drawFilledShape((Graphics2D)g2d, sufaceTop  , gameNow.myCube.surfaceColor[Constants.CUBE_TOP]);
        Basic.drawLineShape  ((Graphics2D)g2d, sufaceTop  , Constants.COLOR_BLACK);
        Basic.drawFilledShape((Graphics2D)g2d, sufaceRight, gameNow.myCube.surfaceColor[Constants.CUBE_RIGHT]);
        Basic.drawLineShape  ((Graphics2D)g2d, sufaceRight, Constants.COLOR_BLACK);

        //! debug only
        // Basic.drawLineShape  ((Graphics2D)g2d, sufaceFront, Constants.COLOR_BLACK);
        // Basic.drawLineShape  ((Graphics2D)g2d, sufaceTop  , Constants.COLOR_BLACK);
        // Basic.drawLineShape  ((Graphics2D)g2d, sufaceRight, Constants.COLOR_BLACK);
    }

    public static void drawCube(Graphics g2d, Game gameNow) {
        TupleReal[] p3d = getTupleRealArrayForCube(gameNow.posX, gameNow.posY, 0);
        int[][] cubeSurfaces = Utils.getCubeSurfaces();

        //! need to change 
        Surface[] surfaceArr = new Surface[6];

        // for every direction
        for(int dirId: new int[] {Constants.CUBE_BACK, Constants.CUBE_FRONT, Constants.CUBE_BOTTOM, Constants.CUBE_TOP ,Constants.CUBE_LEFT, Constants.CUBE_RIGHT}) {
            surfaceArr[dirId] = new Surface();
            for(int j = 0; j < cubeSurfaces[dirId].length; j ++) {
                surfaceArr[dirId].add(p3d[cubeSurfaces[dirId][j]]);
            }
        }

        boolean[] isHide = MathTransform.checkHide(surfaceArr);
        for(int i = 0; i < 6; i ++) {
            if(!isHide[i]) {
                int colorId = gameNow.myCube.surfaceColor[i]; // mycube not mylastcube
                PairInt[] p2d = MathTransform.getPairIntArrayForTupleRealArray(surfaceArr[i].toArray());
                Basic.drawFilledShape((Graphics2D)g2d, p2d, colorId);
                Basic.drawLineShape((Graphics2D)g2d, p2d, Constants.COLOR_BLACK);
            }
        }
    }

    // draw the ground of the game
    public static void drawChessboard(Graphics g2d, Game gameNow) throws Exception {
        int extraWidth = Constants.CHESSBOARD_EXTRA_WIDTH;
        for(int i = -extraWidth; i <= gameNow.getLenX() + extraWidth - 1; i ++) {
            for(int j = -extraWidth; j <= gameNow.getLenY() + extraWidth - 1; j ++) {
                if(0 <= i && i < gameNow.getLenX() && 0 <= j && j < gameNow.getLenY()) {
                    drawChessboardGrid(g2d, gameNow.chessboard[i][j], i, j);
                }else {
                    //? we draw the chessboard edge to mute the noise when making animation
                    //? it is a must and feature
                    drawChessboardEdge(g2d, i, j);
                }
            }
        }
    }

    public static void drawWin(Graphics g, Game gameNow) {
        PairInt[] p2dArr = Basic.getPairIntArr(gameNow.getEndX(), gameNow.getEndY());
        Basic.drawLineShape((Graphics2D)g, p2dArr, Constants.COLOR_CYAN);
        setColor(g, Constants.COLOR_CYAN);
        Basic.drawLine((Graphics2D)g, p2dArr[0], p2dArr[2]);
        Basic.drawLine((Graphics2D)g, p2dArr[1], p2dArr[3]);
    }

    public static void setColor(Graphics g, int colorId) {
        boolean find = false;
        if(colorId == Constants.COLOR_YELLOW) {g.setColor(Color.YELLOW); find = true;}
        if(colorId == Constants.COLOR_RED)    {g.setColor(Color.RED);    find = true;}
        if(colorId == Constants.COLOR_GREEN)  {g.setColor(Color.GREEN);  find = true;}
        if(colorId == Constants.COLOR_GRAY)   {g.setColor(Color.GRAY);   find = true;}
        if(colorId == Constants.COLOR_BLUE)   {g.setColor(Color.BLUE);   find = true;}
        if(colorId == Constants.COLOR_ORANGE) {g.setColor(Color.ORANGE); find = true;}
        if(colorId == Constants.COLOR_WHITE)  {g.setColor(Color.WHITE);  find = true;}
        if(colorId == Constants.COLOR_BLACK)  {g.setColor(Color.BLACK);  find = true;}
        if(colorId == Constants.COLOR_PINK)   {g.setColor(Color.PINK);   find = true;}
        if(colorId == Constants.COLOR_CYAN)   {g.setColor(Color.CYAN);   find = true;}
        if(!find) {
            System.err.println("Resources.setColor:");
            System.err.println("\tcolorName " + colorId + " not found.");
            // color not found: return color BLACK as result
            g.setColor(Color.BLACK);
        }
    }

    // draw a rotating cube
    public static void drawCubeRotate(Graphics g, Game gameNow, AnimationRotate animationRotate, double angle) throws Exception {
        //! you need to rotate based on the old position X, Y
        //! but I can only get the new position
        int bx = gameNow.posX;
        int by = gameNow.posY;

        
        // combine them into an array (eazy to use)
        int[][] cubeSurfaces = Utils.getCubeSurfaces();

        // old position of the cube
        int[] oldxy = animationRotate.getOldxy(bx, by);

        // rotation poll message
        TupleReal basePoint  = animationRotate.getBasePoint(bx, by);
        TupleReal rotatePoll = animationRotate.getRotatePoll(bx, by);
        //! int[] surfaceShow    = animationRotate.getSurfaceShow();

        //! get the basePoint rand rotationPoll by animation constants
        // p3d is the non-rotated cube 
        TupleReal[] p3d = getTupleRealArrayForCube(oldxy[0], oldxy[1], 0);

        // draw the four surfaces
        p3d = MathTransform.rotate(p3d, basePoint, rotatePoll, angle);

        Surface[] surfaceArr = new Surface[6];

        // for every direction
        for(int dirId: new int[] {Constants.CUBE_BACK, Constants.CUBE_FRONT, Constants.CUBE_BOTTOM, Constants.CUBE_TOP ,Constants.CUBE_LEFT, Constants.CUBE_RIGHT}) {
            surfaceArr[dirId] = new Surface();
            for(int j = 0; j < cubeSurfaces[dirId].length; j ++) {
                surfaceArr[dirId].add(p3d[cubeSurfaces[dirId][j]]);
            }
        }

        boolean[] isHide = MathTransform.checkHide(surfaceArr);
        for(int i = 0; i < 6; i ++) {
            if(!isHide[i]) {
                int colorId = gameNow.myLastCube.surfaceColor[i];
                PairInt[] p2d = MathTransform.getPairIntArrayForTupleRealArray(surfaceArr[i].toArray());
                Basic.drawFilledShape((Graphics2D)g, p2d, colorId);
                Basic.drawLineShape((Graphics2D)g, p2d, Constants.COLOR_BLACK);
            }
        }
    }

    public class Basic {
        public static void drawChessboardGrid(Graphics g2d, String gridName, int posX, int posY) throws Exception {
            // remember to add the '.' if you want to check in this way
            if(gridName.startsWith(Constants.RESOURCE_COLORCHECK + ".")) {
                int colorId = Integer.valueOf(gridName.split("\\.")[1]);
                drawChessboardGridWithColor((Graphics2D)g2d, posX, posY, colorId);
            }else 
            if(gridName.equals(Constants.RESOURCE_NONE)) {
                drawChessboardGridEmpty((Graphics2D)g2d, posX, posY);
            }else
            if(gridName.equals(Constants.RESOURCE_KEEPOUT)) {
                drawChessboardGridKeepout((Graphics2D)g2d, posX, posY);
            }else
            {
                throw new Exception("gridName: " + gridName + " can not be found in class 'Basic'.");
            }
        }

        public static void drawFilledShape(Graphics2D g2d, PairInt[] p2dArr, int colorId) {
            Resources.setColor(g2d, colorId);
            Path2D.Double parallelogram = new Path2D.Double();
            parallelogram.moveTo(p2dArr[0].x, p2dArr[0].y);
            for(int i = 1; i < p2dArr.length; i ++) {
                parallelogram.lineTo(p2dArr[i].x, p2dArr[i].y);
            }
            parallelogram.closePath();
            g2d.fill(parallelogram);
        }

        public static void drawLineShape(Graphics2D g2d, PairInt[] p2dArr, int colorId) {
            Resources.setColor(g2d, colorId);
            for(int i = 0; i + 1 < p2dArr.length; i ++) {
                drawLine(g2d, p2dArr[i], p2dArr[i+1]);
            }
            drawLine(g2d, p2dArr[0], p2dArr[p2dArr.length - 1]);
        }

        public static PairInt[] getPairIntArr(int posX, int posY) {
            PairInt[] p2d = new PairInt[4];
            for(int v = 0; v < p2d.length; v ++) {
                int dirX = (v >> 1) & 1;
                int dirY = (v >> 0) & 1;
                try {
                    p2d[v] = MathTransform.transform(new TupleReal(posX + dirX, posY + dirY, 0));
                }
                catch(Exception exp) {
                    exp.printStackTrace();
                }
            }
            PairInt[] p2dArr = {p2d[0], p2d[2], p2d[3], p2d[1]};
            return p2dArr;
        }

        public static void drawChessboardGridWithColor(Graphics2D g2d, int posX, int posY, int colorId) {
            PairInt[] p2dArr = getPairIntArr(posX, posY);
            drawFilledShape(g2d, p2dArr, colorId);
            drawLineShape(g2d, p2dArr, Constants.COLOR_BLACK);
        }

        public static void drawLine(Graphics2D g2d, PairInt posFrom, PairInt posTo) {
            g2d.drawLine(posFrom.x, posFrom.y, posTo.x, posTo.y);
        }

        public static void drawChessboardGridEmpty(Graphics2D g2d, int posX, int posY) {
            int id = (posX + posY) % 2;
            int colorId = -1;
            if(id == 0) colorId = Constants.COLOR_BLACK;
            else        colorId = Constants.COLOR_WHITE;
            PairInt[] p2dArr = getPairIntArr(posX, posY);
            drawFilledShape(g2d, p2dArr, colorId);
            drawLineShape(g2d, p2dArr, Constants.COLOR_BLACK);
        }

        public static void drawChessboardGridKeepout(Graphics2D g2d, int posX, int posY) {
            PairInt[] p2dArr = getPairIntArr(posX, posY);
            drawFilledShape(g2d, p2dArr, Constants.COLOR_WHITE);
            drawLineShape(g2d, p2dArr, Constants.COLOR_BLACK);
            //draw a cross tag
            drawLine(g2d, p2dArr[0], p2dArr[2]);
            drawLine(g2d, p2dArr[1], p2dArr[3]);
        }
    }
}
