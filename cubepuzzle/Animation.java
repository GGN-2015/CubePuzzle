package cubepuzzle;

import java.awt.Image;
import java.awt.Graphics;

// Animation: paint based on the time
public abstract class Animation {
    protected Image iBuffer = null;
    protected Graphics gBuffer = null;

    public abstract void paint(Graphics g, double timeNow) throws Exception;
}

// the common part for rotation is that
// they all need to solve a degree based problem
abstract class AnimationRotate extends Animation {
    final double getDegree(double timeNow) {
        //System.out.println("timeNow = " + timeNow); // test
        double degreeFull = Math.PI / 2;
        double degreeNow  = degreeFull * timeNow / Constants.ANIME_DURATION;
        return degreeNow;
    }
    public abstract TupleReal getBasePoint(int bx, int by);
    public abstract TupleReal getRotatePoll(int bx, int by);
    public abstract int[] getOldxy(int bx, int by);
    public abstract int[] getSurfaceShow();

    final public void paint(Graphics g, double timeNow) throws Exception {
        if (iBuffer == null) {
            iBuffer = GameUI.drawPanel.createImage(GameUI.drawPanel.getWidth(), GameUI.drawPanel.getHeight());
            gBuffer = iBuffer.getGraphics();
        }
        gBuffer.setColor(GameUI.drawPanel.getBackground());
        gBuffer.fillRect(0, 0, GameUI.drawPanel.getWidth(), GameUI.drawPanel.getHeight());

        Resources.drawChessboard(gBuffer, DrawPanel.gameNow);
        Resources.drawWin(gBuffer, DrawPanel.gameNow);
        Resources.drawCubeRotate(gBuffer, DrawPanel.gameNow, this, getDegree(timeNow));

        g.drawImage(iBuffer, 0, 0, null);
    }
}


// Cube Left rotate
class AnimationLeft extends AnimationRotate {

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
