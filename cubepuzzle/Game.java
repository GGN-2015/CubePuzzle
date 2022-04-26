package cubepuzzle;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    public final int lenX, lenY, beginX, beginY, endX, endY;
    private int posX, posY, stepCnt = 0;
    private String chessboard[][];
    public  String[] information;

    public Cube myCube;

    public Game(int lenX, int lenY, int beginX, int beginY, int endX, int endY) {
        this.lenX   = lenX;
        this.lenY   = lenY;
        this.beginX = beginX;
        this.beginY = beginY;
        this.endX   = endX;
        this.endY   = endY;
        this.myCube = new Cube();
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
        myCube = new Cube();
    }

    // when you come to endX, endY and step > 0
    // which means you can not win if you don't move
    public boolean win() {
        return posX == endX && posY == endY && stepCnt > 0;
    }

    // you have enter a new grid on board, check whether this move is valid
    public boolean checkPosEnter() {
        // for debug, we say every move is valid
        return true;
    }

    // there are four directions to move the cube
    // basic idea: move in ; check ; move out on failure
    public void moveLeft() {
        if(posY > 0) {
            stepCnt ++;
            posY --;
            myCube.rollLeft();
            if(!checkPosEnter()) {
                stepCnt --;
                posY ++;
                myCube.rollRight();
            }
        }
    }
    public void moveRight() {
        if(posY < lenY - 1) {
            stepCnt ++;
            posY ++;
            myCube.rollRight();
            if(!checkPosEnter()) {
                stepCnt --;
                posY --;
                myCube.rollLeft();
            }
        }
    }
    public void moveIn() {
        if(posX > 0) {
            stepCnt ++;
            posX --;
            myCube.rollIn();
            if(!checkPosEnter()) {
                stepCnt --;
                posX ++;
                myCube.rollOut();
            }
        }
    }
    public void moveOut() {
        if(posX < lenX - 1) {
            stepCnt ++;
            posX ++;
            myCube.rollOut();
            if(!checkPosEnter()) {
                stepCnt --;
                posX --;
                myCube.rollIn();
            }
        }
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
    public void debugShowAndMove(Scanner cin) {
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
    public static void main(String[] args) throws FileNotFoundException, IOException {
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
