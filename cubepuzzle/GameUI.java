package cubepuzzle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;

import java.io.FileNotFoundException;

import java.util.Scanner;

import javax.swing.*;

// the only use of GameUI is to show and playe a game
// the other function of the game is based on other part
public class GameUI extends JInternalFrame implements ActionListener {
    static DrawPanel drawPanel = null;
    private static GameUI uiInstance = null;

    static JLabel stepLabel     = new JLabel("");
    static JLabel msgLabel      = new JLabel("");

    GameUI() {
        Container conPane = getContentPane();

        // draw panel setting
        drawPanel = new DrawPanel();
        conPane.add(stepLabel, BorderLayout.NORTH);
        conPane.add(msgLabel, BorderLayout.SOUTH);
        conPane.add(drawPanel, BorderLayout.CENTER);

        // create basic UI interface
        this.setSize(Constants.UI_WIDTH, Constants.UI_HEIGHT);
        this.setVisible(true);
        this.setTitle(Constants.UI_TITLE);
        //this.setLocationRelativeTo(null);

        // event listeners
        // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		drawPanel.addKeyListener(new GameKeyListener());

        // without this, the key event listener will not be launched
        drawPanel.setFocusable(true);
    }

    // use single instance to create the object
    public static GameUI getInstance() {
        if(uiInstance == null) uiInstance = new GameUI();
        return uiInstance;
    }

    // after a new game is set the picture will be repainted
    @Deprecated
    public void setGame(Game gameNew) {
        DrawPanel.setGame(gameNew);
        MathTransform.setLength(gameNew.getLenX(), gameNew.getLenY());

        //! repaint after game is set
        drawPanel.repaint();
    }

    // action listener for all the buttons
    public void actionPerformed(ActionEvent e) {
        //System.err.println("actionPerformed [Checking]");
        if(e.getActionCommand().equals(Constants.BUTTON_RESTART)) { // restart the game
            DrawPanel.gameNow.restart();
            //System.err.println("actionPerformed " + Constants.BUTTON_RESTART);
            GameUI.drawPanel.repaint();
        }
    }

    // this is not the main app
    // this main function is just used to debug the class GameUI
    public static void main(String[] args) {
        GameUI myGameUI = new GameUI();
        myGameUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            myGameUI.setGame(new Game("games/tutorial.1.txt"));
        }catch(FileNotFoundException e) {
            e.printStackTrace(); // send error message when file not found
        }

        // input instruction from command line
        Scanner cin = new Scanner(System.in);
        while(true) {
            System.out.print(">>>");
            String ins = cin.nextLine().trim().toLowerCase();
            if(ins.equals("restart")) {
                DrawPanel.gameNow.restart();
                GameUI.drawPanel.repaint();
            }else
            if(ins.equals("exit")) {
                break;
            }else {
                System.err.println("instruction " + ins + " Unkown.");
            }
        }
        cin.close();
    }
}

class DrawPanel extends JPanel {
    static Game    gameNow   = null;
    static boolean animating = false;

    public DrawPanel() {
        super(true);
    }

    // each draw panel have a game
    // which means there many be more than one game on a frame
    public static void setGame(Game gameNew) {
        gameNow = gameNew;
        GameUI.msgLabel.setText(gameNew.getInformation());
        GameUI.stepLabel.setText("StepCnt = " + DrawPanel.gameNow.stepCnt);

        //? make sure that the full graph is able to be painted on the screen
        MathTransform.setLength(gameNew.getLenX(), gameNew.getLenY());
        GameUI.drawPanel.repaint();
    }

    private Image iBuffer = null;
    private Graphics gBuffer = null;

    // Paint Algorithm is based on Resources.java
    // Resources.java can be costomized by the user
    @Override
    public void paint(Graphics g) {
        if(gameNow != null) {
            try {
                if (iBuffer == null) {
                    iBuffer = createImage(this.getSize().width, this.getSize().height);
                    gBuffer = iBuffer.getGraphics();
                }
                gBuffer.setColor(getBackground());
                gBuffer.fillRect(0, 0, this.getSize().width, this.getSize().height);
                super.paint(gBuffer);

                Resources.drawChessboard(gBuffer, gameNow);
                Resources.drawWin       (gBuffer, gameNow);
                Resources.drawCube      (gBuffer, gameNow);
            } catch (Exception e) {
                // error of resource pack may occur
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, Constants.MSG_GAMEERR);
            }
            g.drawImage(iBuffer, 0, 0, this);
        }
    }

    @Override
    public void update(Graphics g) {
        if(!DrawPanel.animating) {
            g.clearRect(0, 0, this.getWidth(), this.getHeight());
            paint(g);
        }
    }

    // create an animation
    // and show win message and quit
    public void animate(Graphics g, Animation animation) throws Exception {
        animating = true;

        int frameCnt = (int)(Constants.ANIME_DURATION * Constants.ANIME_FPS);
        double frameLen = 1.0 / Constants.ANIME_FPS; // time for one frame
        for(int i = 0; i < frameCnt; i ++) {
            animation.paint(g, frameLen * i);
            Thread.sleep((long)(frameLen * 1000 + 0.5));; // wait this long and make next paint
        }
        this.repaint();

        animating = false;
    }
}

class GameKeyListener extends KeyAdapter {
	public void keyPressed(KeyEvent e){
		char charA=Character.toLowerCase(e.getKeyChar());
        try{
            // these are the keys for view point
            // you can use these keys even when the game is finishe.

            //! attention, in the new version, all the angle was set on the scroll bar
            int nowValue = SelectGamePanel.jViewScrollBar.getValue();
            if(charA == '[') {
                SelectGamePanel.jViewScrollBar.setValue((nowValue + 180 - Constants.MATH_MIN_DEG + 360) % 360 - 180);
            }
            if(charA == ']') {
                SelectGamePanel.jViewScrollBar.setValue((nowValue + 180 + Constants.MATH_MIN_DEG + 360) % 360 - 180);
            }
            if(charA == 'm') {
                SelectGamePanel.jViewScrollBar.setValue(0);
            }
            
            // only when you are not win can you move by the keyboard
            if(!DrawPanel.gameNow.win() && !DrawPanel.animating) {
                int animation = Constants.ANIME_NONE;

                // -------------------- KEY BOARD ACTION -------------------- //
                if(charA == 'a') {
                    boolean flag = DrawPanel.gameNow.moveLeft();
                    if(flag) {
                        animation = Constants.ANIME_LEFT;
                    }
                }
                if(charA == 'd') {
                    boolean flag = DrawPanel.gameNow.moveRight();
                    if(flag) {
                        animation = Constants.ANIME_RIGHT;
                    }
                }
                if(charA == 'w') {
                    boolean flag = DrawPanel.gameNow.moveIn();
                    if(flag) {
                        animation = Constants.ANIME_IN;
                    }
                }
                if(charA == 's') {
                    Boolean flag = DrawPanel.gameNow.moveOut();
                    if(flag) {
                        animation = Constants.ANIME_OUT;
                    }
                }

                // -------------------- ANIMATION PLAYER -------------------- //
                if(animation != Constants.ANIME_NONE) {
                    Graphics g = GameUI.drawPanel.getGraphics();
                    if(animation == Constants.ANIME_LEFT)  GameUI.drawPanel.animate(g, new AnimationLeft());
                    if(animation == Constants.ANIME_RIGHT) GameUI.drawPanel.animate(g, new AnimationRight());
                    if(animation == Constants.ANIME_IN)    GameUI.drawPanel.animate(g, new AnimationIn());
                    if(animation == Constants.ANIME_OUT)   GameUI.drawPanel.animate(g, new AnimationOut());
                }

                //! change the text before the dialog
                if(DrawPanel.gameNow.win()) {
                    GameUI.stepLabel.setText("You win! StepCnt = " + DrawPanel.gameNow.stepCnt);
                }else {
                    GameUI.stepLabel.setText("StepCnt = " + DrawPanel.gameNow.stepCnt);
                }

                //! this is not good, make sure the dialog is after the animation
                // this message will only show once
                if(DrawPanel.gameNow.win()) {
                    JOptionPane.showMessageDialog(null, "You win!");
                    //! hide when the game is win
                    //GameUI.getInstance().setVisible(false);
                }
            }
        }
        catch(Exception ex) {
            //! if gameNow is null, ignore it
            //ex.printStackTrace();
        }
        GameUI.drawPanel.repaint();
	}
}
