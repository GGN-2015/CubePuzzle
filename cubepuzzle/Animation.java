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
}


// Cube Left rotate
class AnimationLeft extends AnimationRotate {
    public void paint(Graphics g, double timeNow) throws Exception {
        //g.clearRect(0, 0, Constants.UI_WIDTH, Constants.UI_HEIGHT);
        Resources.drawChessboard(g, DrawPanel.gameNow);
        Resources.drawWin(g, DrawPanel.gameNow);
        Resources.drawCubeRotate(g, DrawPanel.gameNow, Constants.ANIME_LEFT, getDegree(timeNow));
    }
}

class AnimationRight extends AnimationRotate {
    public void paint(Graphics g, double timeNow) throws Exception {
        //g.clearRect(0, 0, Constants.UI_WIDTH, Constants.UI_HEIGHT);
        Resources.drawChessboard(g, DrawPanel.gameNow);
        Resources.drawWin(g, DrawPanel.gameNow);
        Resources.drawCubeRotate(g, DrawPanel.gameNow, Constants.ANIME_RIGHT, getDegree(timeNow));
    }
}

class AnimationIn extends AnimationRotate {
    public void paint(Graphics g, double timeNow) throws Exception {
        //g.clearRect(0, 0, Constants.UI_WIDTH, Constants.UI_HEIGHT);
        Resources.drawChessboard(g, DrawPanel.gameNow);
        Resources.drawWin(g, DrawPanel.gameNow);
        Resources.drawCubeRotate(g, DrawPanel.gameNow, Constants.ANIME_IN, getDegree(timeNow));
    }
}

class AnimationOut extends AnimationRotate {
    public void paint(Graphics g, double timeNow) throws Exception {
        //g.clearRect(0, 0, Constants.UI_WIDTH, Constants.UI_HEIGHT);
        Resources.drawChessboard(g, DrawPanel.gameNow);
        Resources.drawWin(g, DrawPanel.gameNow);
        Resources.drawCubeRotate(g, DrawPanel.gameNow, Constants.ANIME_OUT, getDegree(timeNow));
    }
}
