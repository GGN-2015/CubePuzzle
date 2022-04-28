package cubepuzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    public final int lenX, lenY, beginX, beginY, endX, endY;
    int posX, posY, stepCnt = 0;
    String chessboard[][];
    public String[] information;

    public Cube myCube;
    public Cube myLastCube;

    public Game(int lenX, int lenY, int beginX, int beginY, int endX, int endY) {
        this.lenX   = lenX;
        this.lenY   = lenY;
        this.beginX = beginX;
        this.beginY = beginY;
        this.endX   = endX;
        this.endY   = endY;
        this.myCube     = new Cube();
        this.myLastCube = new Cube();
        chessboard = new String[lenX][lenY];
        for(int i = 0; i < lenX; i ++) {
            for(int j = 0; j < lenY; j ++) {
                chessboard[i][j] = "basic.none";
            }
        }
        posX = beginX;
        posY = beginY;
    }

    //read in game information from a file
    public Game(String fileName) throws FileNotFoundException{
        File file = new File(fileName);
        Scanner fin  = new Scanner(file);
        lenX   = fin.nextInt();
        lenY   = fin.nextInt();
        beginX = fin.nextInt();
        beginY = fin.nextInt();
        endX   = fin.nextInt();
        endY   = fin.nextInt();
        chessboard = new String[lenX][lenY];
        for(int i = 0; i < lenX; i ++) {
            for(int j = 0; j < lenY; j ++) {
                chessboard[i][j] = fin.next();
            }
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        while(fin.hasNextLine()) {
            String tmp = fin.nextLine().trim();
            if(!tmp.equals("")) {   // TMP is not an empty string
                arrayList.add(tmp); // add it to the information of the game
            }
        }
        information = new String[arrayList.size()];
        for(int i = 0; i < arrayList.size(); i ++) { // copy the information to the string
            information[i] = arrayList.get(i);
        }
        fin.close();
        posX = beginX;
        posY = beginY;
        myCube     = new Cube();
        myLastCube = new Cube();
    }

    // when you want to restart the game from the beginning
    public void restart() {
        posX = beginX; 
        posY = beginY;
        stepCnt = 0;
        myCube     = new Cube(); // remember to get a new cube
        myLastCube = new Cube();
    }

    // when you come to endX, endY and step > 0
    // which means you can not win if you don't move
    public boolean win() {
        return posX == endX && posY == endY && stepCnt > 0;
    }

    // you have enter a new grid on board, check whether this move is valid
    public boolean checkPosEnter() throws Exception {
        // for debug, we say every move is valid
        // return true;
        return Rules.checkPosEnterAvailable(chessboard[posX][posY], myCube);
    }

    // there are four directions to move the cube
    // basic idea: move in ; check ; move out on failure
    public boolean moveLeft() throws Exception {
        myLastCube = myCube.clone();
        if(posY > 0) {
            stepCnt ++;
            posY --;
            myCube.rollLeft();
            if(!checkPosEnter()) {
                stepCnt --;
                posY ++;
                myCube.rollRight();
                return false;
            }
            return true;
        }
        return false;
    }
    public boolean moveRight() throws Exception {
        myLastCube = myCube.clone();
        if(posY < lenY - 1) {
            stepCnt ++;
            posY ++;
            myCube.rollRight();
            if(!checkPosEnter()) {
                stepCnt --;
                posY --;
                myCube.rollLeft();
                return false;
            }
            return true;
        }
        return false;
    }
    public boolean moveIn() throws Exception {
        myLastCube = myCube.clone();
        if(posX > 0) {
            stepCnt ++;
            posX --;
            myCube.rollIn();
            if(!checkPosEnter()) {
                stepCnt --;
                posX ++;
                myCube.rollOut();
                return false;
            }
            return true;
        }
        return false;
    }
    public boolean moveOut() throws Exception {
        myLastCube = myCube.clone();
        if(posX < lenX - 1) {
            stepCnt ++;
            posX ++;
            myCube.rollOut();
            if(!checkPosEnter()) {
                stepCnt --;
                posX --;
                myCube.rollIn();
                return false;
            }
            return true;
        }
        return false;
    }

    // only used to debug a game
    public void debugShow() {
        System.out.println("Game.debugShow:");
        myCube.debugShow();
        for(int i = 0; i < lenX; i ++) {
            for(int j = 0; j < lenY; j ++) {
                if(i == posX && j == posY) {
                    System.out.print("basic.cube" + "\t");
                }else {
                    System.out.print(chessboard[i][j] + "\t"); 
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // only used to debug a game
    public void debugShowAndMove(Scanner cin) throws Exception {
        debugShow();
        if(win()) return;
        System.out.print(">>>");
        String ope = cin.next().toLowerCase();
        for(int i = 0; i < ope.length(); i ++) {
            if(ope.charAt(i) == 'w') moveIn();
            if(ope.charAt(i) == 'a') moveLeft();
            if(ope.charAt(i) == 's') moveOut();
            if(ope.charAt(i) == 'd') moveRight();
        }
    }

    // this main fucntion is only used to debuge class Game
    public static void main(String[] args) throws Exception {
        Game tutorial_0 = new Game("games/tutorial.0.txt");
        Scanner cin = new Scanner(System.in);
        do {
            tutorial_0.debugShowAndMove(cin);
        } while(!tutorial_0.win());
        if(tutorial_0.win()) {
            tutorial_0.debugShow();
            System.out.println("You Win!");
        }
    }
}
