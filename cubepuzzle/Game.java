package cubepuzzle;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    public final int lenX, lenY, beginX, beginY, endX, endY;
    private int posX, posY, stepCnt = 0;
    private String chessboard[][];
    public  String[] information;

    public Cube myCube;

    //read in game information from a file
    public Game(String fileName) throws FileNotFoundException{
        Scanner fin  = new Scanner(new FileReader(fileName));
        lenX   = fin.nextInt();
        lenY   = fin.nextInt();
        beginX = fin.nextInt();
        endX   = fin.nextInt();
        beginY = fin.nextInt();
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

    public void debugShow() {
        System.out.println("Game.debugShow:");
        myCube.debugShow();
        for(int i = 0; i < lenX; i ++) {
            for(int j = 0; j < lenY; j ++) {
                if(i == posX && j == posY) {
                    System.out.print("basic.cube");
                }else {
                    System.out.print(chessboard[i][j] + "\t"); 
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void debugShowAndMove(Scanner cin) {
        debugShow();
        System.out.println(">>>");
        String ope = cin.next().toLowerCase();
        for(int i = 0; i < ope.length(); i ++) {
            if(win()) {
                break;
            }
            if(ope.charAt(i) == 'w') moveIn();
            if(ope.charAt(i) == 'a') moveLeft();
            if(ope.charAt(i) == 's') moveOut();
            if(ope.charAt(i) == 'd') moveRight();
        }
    }

    public static boolean readfile(String filepath) throws FileNotFoundException, IOException {
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("文件");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());
 
            } else if (file.isDirectory()) {
                System.out.println("文件夹");
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        System.out.println("path=" + readfile.getPath());
                        System.out.println("absolutepath="
                                + readfile.getAbsolutePath());
                        System.out.println("name=" + readfile.getName());
                        System.out.println();
                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "\\" + filelist[i]);
                    }
                }
 
            }
 
        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return true;
    }

    // this main fucntion is only used to debuge class Game
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String path = System.getProperty("java.class.path");
        System.out.println(path);
        readfile(path);
        Game tutorial_0 = new Game("turorial.0.txt");
        Scanner cin = new Scanner(System.in);
        while(!tutorial_0.win()) {
            tutorial_0.debugShowAndMove(cin);
        }
        if(tutorial_0.win()) {
            System.out.println("You Win!");
        }
    }
}
