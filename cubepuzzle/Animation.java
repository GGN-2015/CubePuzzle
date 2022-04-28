package cubepuzzle;

import java.awt.Graphics;

// Animation: paint based on the time
public abstract class Animation {
    public abstract void paint(Graphics g, double timeNow) throws Exception;
}

// the common part for rotation is that
// they all need to solve a degree based problem
abstract class AnimationRotate extends Animation {
    double getDegree(double timeNow) {
        //System.out.println("timeNow = " + timeNow); // test
        double degreeFull = Math.PI / 2;
        double degreeNow  = degreeFull * timeNow / Constants.ANIME_DURATION;
        return degreeNow;
    }
    public abstract void paint(Graphics g, double timeNow) throws Exception;
    public abstract TupleReal getBasePoint(int bx, int by);
    public abstract TupleReal getRotatePoll(int bx, int by);
    public abstract int[] getOldxy(int bx, int by);
    public abstract int[] getSurfaceShow();
}


// Cube Left rotate
class AnimationLeft extends AnimationRotate {
    public void paint(Graphics g, double timeNow) throws Exception {
        //g.clearRect(0, 0, Constants.UI_WIDTH, Constants.UI_HEIGHT);
        Resources.drawChessboard(g, DrawPanel.gameNow);
        Resources.drawWin(g, DrawPanel.gameNow);
        Resources.drawCubeRotate(g, DrawPanel.gameNow, this, getDegree(timeNow));
    }

    @Override
    public TupleReal getBasePoint(int bx, int by) {
        return new TupleReal(bx, by + 1, 0);
    }

    @Override
    public TupleReal getRotatePoll(int bx, int by) {
        return TupleReal.VECX;
    }

    @Override
    public int[] getOldxy(int bx, int by) {
        return new int[] {bx, by + 1};
    }

    @Override
    public int[] getSurfaceShow() {
        return new int[] {Constants.CUBE_FRONT, Constants.CUBE_RIGHT, Constants.CUBE_BOTTOM, Constants.CUBE_TOP};
    }
}

class AnimationRight extends AnimationRotate {
    public void paint(Graphics g, double timeNow) throws Exception {
        //g.clearRect(0, 0, Constants.UI_WIDTH, Constants.UI_HEIGHT);
        Resources.drawChessboard(g, DrawPanel.gameNow);
        Resources.drawWin(g, DrawPanel.gameNow);
        Resources.drawCubeRotate(g, DrawPanel.gameNow, this, getDegree(timeNow));
    }

    @Override
    public TupleReal getBasePoint(int bx, int by) {
        return  new TupleReal(bx, by, 0);
    }

    @Override
    public int[] getOldxy(int bx, int by) {
        int oldx = bx;
        int oldy = by - 1;
        return new int[] {oldx, oldy};
    }

    @Override
    public TupleReal getRotatePoll(int bx, int by) {
        return TupleReal.VECX.mul(-1);
    }

    @Override
    public int[] getSurfaceShow() {
        return new int[] {Constants.CUBE_FRONT, Constants.CUBE_TOP, Constants.CUBE_LEFT, Constants.CUBE_RIGHT};
    }
}

class AnimationIn extends AnimationRotate {
    public void paint(Graphics g, double timeNow) throws Exception {
        //g.clearRect(0, 0, Constants.UI_WIDTH, Constants.UI_HEIGHT);
        Resources.drawChessboard(g, DrawPanel.gameNow);
        Resources.drawWin(g, DrawPanel.gameNow);
        Resources.drawCubeRotate(g, DrawPanel.gameNow, this, getDegree(timeNow));
    }

    @Override
    public TupleReal getBasePoint(int bx, int by) {
        return new TupleReal(bx + 1, by, 0);
    }

    @Override
    public TupleReal getRotatePoll(int bx, int by) {
        return TupleReal.VECY.mul(-1);
    }

    @Override
    public int[] getOldxy(int bx, int by) {
        int oldx = bx + 1;
        int oldy = by;
        return new int[] {oldx, oldy};
    }

    @Override
    public int[] getSurfaceShow() {
        return new int[] {Constants.CUBE_FRONT, Constants.CUBE_RIGHT, Constants.CUBE_BOTTOM, Constants.CUBE_TOP};
    }
}

class AnimationOut extends AnimationRotate {
    public void paint(Graphics g, double timeNow) throws Exception {
        //g.clearRect(0, 0, Constants.UI_WIDTH, Constants.UI_HEIGHT);
        Resources.drawChessboard(g, DrawPanel.gameNow);
        Resources.drawWin(g, DrawPanel.gameNow);
        Resources.drawCubeRotate(g, DrawPanel.gameNow, this, getDegree(timeNow));
    }

    @Override
    public TupleReal getBasePoint(int bx, int by) {
        return new TupleReal(bx, by, 0);
        
    }

    @Override
    public TupleReal getRotatePoll(int bx, int by) {
        return TupleReal.VECY;
    }

    @Override
    public int[] getOldxy(int bx, int by) {
        int oldx = bx - 1;
        int oldy = by;
        return new int[] {oldx, oldy};
    }

    @Override
    public int[] getSurfaceShow() {
        return new int[] {Constants.CUBE_TOP, Constants.CUBE_RIGHT, Constants.CUBE_FRONT, Constants.CUBE_BACK};
    }
}
