package cubepuzzle;

//describe a cube (without position) rolling on the chessboard
public class Cube extends Constants {
    int[] surfaceColor;
    public Cube() { // create a cube for a player
        surfaceColor = new int[6];
        surfaceColor[0] = COLOR_YELLOW;
        surfaceColor[1] = COLOR_RED   ;
        surfaceColor[2] = COLOR_GREEN ;
        surfaceColor[3] = COLOR_PINK ;
        surfaceColor[4] = COLOR_BLUE  ;
        surfaceColor[5] = COLOR_ORANGE;
    }

    // this private method is used to 'roll the cube'
    private void surfaceShift(int s1, int s2, int s3, int s4) {
        int tmp = surfaceColor[s1];
        surfaceColor[s1] = surfaceColor[s2];
        surfaceColor[s2] = surfaceColor[s3];
        surfaceColor[s3] = surfaceColor[s4];
        surfaceColor[s4] = tmp;
    }

    @Override // deep copy a cube
    public Cube clone() {
        Cube cubeTmp = new Cube();
        for(int i = 0; i < surfaceColor.length; i ++) {
            cubeTmp.surfaceColor[i] = surfaceColor[i];
        }
        return cubeTmp;
    }

    // four type of cube rolling
    public void rollLeft() {
        surfaceShift(CUBE_LEFT, CUBE_TOP, CUBE_RIGHT, CUBE_BOTTOM);
    }
    public void rollRight() {
        surfaceShift(CUBE_BOTTOM, CUBE_RIGHT, CUBE_TOP, CUBE_LEFT);
    }
    public void rollIn() {
        surfaceShift(CUBE_BACK, CUBE_TOP, CUBE_FRONT, CUBE_BOTTOM);
    }
    public void rollOut() {
        surfaceShift(CUBE_BOTTOM, CUBE_FRONT, CUBE_TOP, CUBE_BACK);
    }

    // show the style of cube (without bottom) in command line user interface
    public void debugShow() {
        System.out.println("Cube.debugShow:");
        System.out.println(" " + " " + surfaceColor[CUBE_BACK] + " " + " ");
        System.out.println(surfaceColor[CUBE_LEFT] + " " + surfaceColor[CUBE_TOP] + " " + surfaceColor[CUBE_RIGHT]);
        System.out.println(" " + " " + surfaceColor[CUBE_FRONT] + " " + " ");
        System.out.println();
    }

    // this function is only used to debug the class Cube
    public static void main(String[] args) {
        Cube myCube = new Cube();
        myCube.debugShow();
        myCube.rollLeft();
        myCube.debugShow();
        myCube.rollOut();
        myCube.debugShow();
        myCube.rollRight();
        myCube.debugShow();
        myCube.rollIn();
        myCube.debugShow();
        myCube.rollLeft();
        myCube.debugShow();
    }
}
