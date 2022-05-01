package cubepuzzle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
 
// describe a game for algorithm
// because chessboard is fixed, we only need to know the cube
class GameStatus {
    Cube cubeNow;
    int posX, posY;

    // get the status of a game
    GameStatus(Game game) {
        cubeNow = game.myCube.clone();
        posX = game.posX;
        posY = game.posY;
    }

    GameStatus(Cube cubeNow, int posX, int posY) {
        this.cubeNow = cubeNow;
        this.posX    = posX;
        this.posY    = posY;
    }

    void debugOutput() {
        System.out.print(posX + " " + posY + ": ");
        for(int i = 0; i < cubeNow.surfaceColor.length; i ++) {
            System.out.print( cubeNow.surfaceColor[i] + " ");
        }
        System.out.println();
    }

    // given a game, check if the statues is ok to exist in the game
    public boolean checkInGame(Game game) throws Exception {
        if(0 <= posX && posX < game.getLenX() && 0 <= posY && posY < game.getLenY()) {
            return Rules.checkPosEnterAvailable(game.chessboard[posX][posY], cubeNow);
        }else {
            // if position is not in the graph, it is illegal
            return false;
        }
    }

    public GameStatus getNextStatus(int dir) throws Exception {
        Cube tmpCubeNow = this.cubeNow.clone();
        int nPosX = posX, nPosY = posY;

        if(dir == Constants.ANIME_IN) {
            nPosX --;
            tmpCubeNow.rollIn();
        }else if(dir == Constants.ANIME_OUT) {
            nPosX ++;
            tmpCubeNow.rollOut();
        }else if(dir == Constants.ANIME_LEFT) {
            nPosY --;
            tmpCubeNow.rollLeft();
        }else if(dir == Constants.ANIME_RIGHT) {
            nPosY ++;
            tmpCubeNow.rollRight();
        }
        else throw new Exception("dir: " + dir + " unknown");

        return new GameStatus(tmpCubeNow, nPosX, nPosY);
    }

    @Override
    public boolean equals(Object x) {
        GameStatus rhs = (GameStatus)x;
        return posX == rhs.posX && posY == rhs.posY && cubeNow.equals(rhs.cubeNow);
    }

    @Override
    public int hashCode() {
        return posX * 25 + posY + cubeNow.hashCode() * 625;
    }
}


// we use this algorithm to find a solution for a game
public class FindWayAlgo {
    Game gameNow;
    int stepCnt = -1;
    int[] tips = null;
    boolean fail = false;

    // solve the Game when the object is initialized
    public FindWayAlgo(Game game)  throws Exception {
        // do not solve for empty game
        if(game == null) return;

        // deep copy of a game
        gameNow = game.clone();
        Queue<GameStatus> queue = new LinkedList<>();

        // lastNode is used to print the output chain
        HashMap<GameStatus, GameStatus> lastNode = new HashMap<GameStatus, GameStatus>();
        HashMap<GameStatus, Integer> nodeDepth   = new HashMap<GameStatus, Integer>();
        HashMap<GameStatus, Integer> lastDir     = new HashMap<GameStatus, Integer>();

        GameStatus beginStatus = new GameStatus(game);
        queue.offer(beginStatus);
        nodeDepth.put(beginStatus, 0); // depth means the stepcnt to reach the place

        GameStatus endStatus = null;

        OUTERWHILE: while(!queue.isEmpty()) {
            GameStatus statusNow = queue.poll();
            Integer dep = nodeDepth.get(statusNow);

            for(int dir: new int[] {Constants.ANIME_IN, Constants.ANIME_OUT, Constants.ANIME_LEFT, Constants.ANIME_RIGHT}) {
                GameStatus statusNxt = statusNow.getNextStatus(dir);
                if(statusNxt.checkInGame(game) && nodeDepth.get(statusNxt) == null) {
                    // this is the first time that the node is visited
                    queue.offer(statusNxt);

                    // initialize the values
                    nodeDepth.put(statusNxt, dep + 1);
                    lastNode.put(statusNxt, statusNow);
                    lastDir.put(statusNxt, dir);

                    // if status is win status
                    //! dep + 1 must be > 0, so needn't to check
                    if(statusNxt.posX == gameNow.getEndX() && statusNxt.posY == gameNow.getEndY()) {
                        endStatus = statusNxt;
                        break OUTERWHILE; // break to speed up
                    }
                }
            }
        }

        if(endStatus != null) {
            stepCnt = nodeDepth.get(endStatus);
            tips = new int[stepCnt];
            GameStatus statusTmp = endStatus;
            for(int i = stepCnt - 1; i >= 0; i --) {
                tips[i] = lastDir.get(statusTmp);
                statusTmp = lastNode.get(statusTmp);
            }
        }else {
            fail = true;
        }
    }

    public static void main(String[] args) throws Exception {
        Game gameTest = new Game("games/tutorial.1.txt");
        FindWayAlgo algo = new FindWayAlgo(gameTest);
        System.out.println("stepCnt: " + algo.stepCnt);

        // output an operation array
        System.out.print("tips: ");
        for(int dir: algo.tips) {
            System.out.print(Utils.getDirName(dir) + " ");
        }
        System.out.println();
    }

    public Object getTips() {
        //! when you fail a game, tips is also 'null'
        //! so judge fail first
        if(fail == true) return Constants.TIPS_YOULOSE;
        if(tips == null) return Constants.TIPS_NOGAME;

        // return the name of the first operation if possible
        return Utils.getDirName(tips[0]) + "(" + tips.length +  " steps left)";
    }
}
