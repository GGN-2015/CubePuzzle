package cubepuzzle;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
//import javax.swing.JOptionPane;

public class GameUI extends JFrame {
    static DrawPanel drawPanel = null;
    public GameUI() {
        drawPanel = new DrawPanel();
        this.add(drawPanel);
        this.setSize(Constants.UI_WIDTH, Constants.UI_HEIGHT);
        this.setVisible(true);
        this.setTitle(Constants.UI_TITLE);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(new GameKeyListener());
    }

    // after a new game is set the picture will be repainted
    public void setGame(Game gameNew) {
        drawPanel.setGame(gameNew);
        MathTransform.setLength(gameNew.lenX, gameNew.lenY);
    }

    // this is not the main app
    // this main function is just used to debug the class GameUI
    public static void main(String[] args) {
        GameUI myGameUI = new GameUI();
        try {
            myGameUI.setGame(new Game("games/tutorial.1.txt"));
        }catch(FileNotFoundException e) {
            e.printStackTrace(); // send error message when file not found
        }
    }
}

class DrawPanel extends JPanel {
    static Game   gameNow = null;
    static JLabel jLabel  = new JLabel("");

    // each draw panel have a game
    // which means there many be more than one game on a frame
    public void setGame(Game gameNew) {
        gameNow = gameNew;
        this.add(jLabel);
        repaint();
    }

    // Paint Algorithm is based on Resources.java
    // Resources.java can be costomized by the user
    public void paint(Graphics g) {
        super.paint(g);
        if(gameNow != null) {
            try {
                Resources.drawChessboard(g, gameNow);
                Resources.drawWin(g, gameNow);
                Resources.drawCube(g, gameNow);
            } catch (Exception e) {
                // error of resource pack may occur
                e.printStackTrace();
            }
        }
    }
}

class GameKeyListener extends KeyAdapter {
	public void keyPressed(KeyEvent e){
		char charA=e.getKeyChar();
		System.out.println("key [" + charA + "] pressed");
        try{
            if(charA == 'a') DrawPanel.gameNow.moveLeft();
            if(charA == 'd') DrawPanel.gameNow.moveRight();
            if(charA == 'w') DrawPanel.gameNow.moveIn();
            if(charA == 's') DrawPanel.gameNow.moveOut();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        if(DrawPanel.gameNow.win()) {
            //JOptionPane.showMessageDialog(GameUI.drawPanel, "You Win!", "Cube Puzzle", JOptionPane.DEFAULT_OPTION);
            DrawPanel.jLabel.setText("You win! StepCnt = " + DrawPanel.gameNow.stepCnt);
        }else {
            DrawPanel.jLabel.setText("StepCnt = " + DrawPanel.gameNow.stepCnt);
        }
        GameUI.drawPanel.repaint();
	}
}
