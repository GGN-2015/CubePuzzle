package cubepuzzle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    int lenX, lenY, beginX, beginY, endX, endY;
    int posX, posY, stepCnt = 0;
    String chessboard[][];
    public String[] information;

    public Cube myCube;
    public Cube myLastCube;
    
    // getter, setter for private properties
    public int getLenX() {
        return lenX;
    }
    public int getLenY() {
        return lenY;
    }
    public int getEndX() {
        return endX;
    }
    public int getEndY() {
        return endY;
    }

    // create an empty game
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
                chessboard[i][j] = Constants.RESOURCE_BASIC + "." + Constants.RESOURCE_NONE;
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

    // save a game status to a file as a new game
    public void saveGame(String fileName) throws IOException{
        FileWriter      fw = new FileWriter(fileName);
        BufferedWriter out = new BufferedWriter(fw);
        out.write(this.lenX + " ");
        out.write(this.lenY + "\n");   // output the size of the game
        out.write(this.beginX + " ");
        out.write(this.beginY + "\n"); // output the beginning position
        out.write(this.endX + " ");
        out.write(this.endY + "\n");   // output the end position
        out.write("\n\n");
        // output the message of chessboard to the file after two empty line
        for(int i = 0; i < this.chessboard.length; i ++) {
            for(int j = 0; j  < this.chessboard[i].length; j ++) {
                out.write(String.format("%-20s ", this.chessboard[i][j]));
            }
            out.write("\n");
        }
        out.write("\n\n");
        for(int i = 0; i < information.length; i ++) {
            out.write(information[i] + "\n"); //? information is trimed when readin
        }
        out.close();
    }

    // when you want to restart the game from the beginning
    public void restart() {
        posX = beginX; 
        posY = beginY;
        stepCnt = 0;
        myCube     = new Cube(); // remember to get a new cube
        myLastCube = new Cube();
    }

    // when you come to endX, endY and step > 0, you win
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
                //! this feature will be updated by polymorphism in later versions
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

    public String getInformation() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < information.length; i ++) {
            sb.append(information[i] + "<br>");
        }
        return "<html><body>" + sb.toString() + "</body></html>";
    }

    // deep copy for game object
    public Game clone() {
        Game newGame = new Game(lenX, lenY, beginX, beginY, endX, endY);
        newGame.posX = posX;
        newGame.posY = posY;
        newGame.chessboard  = chessboard;
        newGame.myCube      = myCube;
        newGame.myLastCube  = myLastCube;
        newGame.stepCnt     = stepCnt;
        newGame.information = information;
        return newGame;
    }
}
