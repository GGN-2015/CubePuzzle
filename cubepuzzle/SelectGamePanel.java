package cubepuzzle;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;

import javax.swing.*;


public class SelectGamePanel extends JPanel {
    static JComboBox<String> gameComboBox = null;

    //! all the operation of angle rotation is set on the Scroll Bar
    static JScrollBar jViewScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 1, -180, 179);
    public SelectGamePanel() {
        setLayout(new GridLayout(Constants.CP_ROWS, Constants.CP_COLS, Constants.CP_SEPERATE, Constants.CP_SEPERATE));
        //setBounds(0, 0, Constants.CP_WIDTH, Constants.CP_HEIGHT);
        setMaximumSize(new Dimension(Constants.CP_WIDTH, Constants.CP_HEIGHT));
        setBackground(Color.DARK_GRAY);

        // instruction bottons
        if(!Utils.gameDirExist()) {
            //! Error Dialog Window Here
            JOptionPane.showMessageDialog(null, "ControllerPanel(): folder 'games' not found!");
            System.exit(Constants.ERROR_NO_GAME_FOLDER);
        }
        
        gameComboBox = new JComboBox<>(Utils.getGames());
        gameComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });
        add(gameComboBox);

        JButton startGame = new JButton(Constants.BUTTON_RESTART);
        add(startGame);
        startGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String gameNow = (String)gameComboBox.getSelectedItem();
                String gameFileName = Constants.DIR_GAME + "/" + gameNow;
                try {
                    //! you need to get focus on the drawPanel before the game
                    //! 1. because KeyAdapter is set on the drawPanel
                    //! 2. the focus will cause a repaint action for the first time so the double buffer will init
                    //! 3. this BAD issue will be fixed in the future COMMITS.
                    GameUI.drawPanel.requestFocus();
                    DrawPanel.setGame(new Game(gameFileName));
                    jViewScrollBar.setValue(0); // initially set the main aspect
                } catch(FileNotFoundException exp) {
                    JOptionPane.showMessageDialog(null, "game file '" + gameFileName + "' not found!");
                }
            }
        });

        newGame();

        JButton getTips = new JButton(Constants.BUTTON_GET_TIPS);
        add(getTips);
        getTips.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    if(DrawPanel.gameNow.win()) {
                        JOptionPane.showMessageDialog(null, Constants.MSG_WIN);
                    }else {
                        FindWayAlgo findWayAlgo = new FindWayAlgo(DrawPanel.gameNow);
                        JOptionPane.showMessageDialog(null, findWayAlgo.getTips());
                    }
                } catch(Exception exp) {
                    JOptionPane.showMessageDialog(null, Constants.MSG_GAMEERR);
                }
                //! you need to focus back to the game after a tip
                //! to promote the game playing experience
                GameUI.drawPanel.requestFocus();
            }
        });

        jViewScrollBar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                // rotate to the direction
                try {
                    MathTransform.returnToMainAngle();
                    MathTransform.rotateWatchPoint(((JScrollBar)e.getSource()).getValue() / 180.0 * Math.PI);
                    GameUI.drawPanel.repaint();  
                }
                catch(Exception ex) {
                    //! every pixel matters
                    //JOptionPane.showMessageDialog(null, Constants.GAME_NOT_START);
                    ((JScrollBar)e.getSource()).setValue(0);
                }
            }
        });
        add(jViewScrollBar);
    }

    static public void newGame() {
        if(gameComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(null, Constants.GAME_NO_SOURCE);
            System.exit(1);
        }
        try {
            String gameNow = (String)gameComboBox.getSelectedItem(); //! maybe no item
            String gameFileName = Constants.DIR_GAME + "/" + gameNow;
            GameUI.drawPanel.requestFocus();
            DrawPanel.setGame(new Game(gameFileName)); //! maybe file not found
            jViewScrollBar.setValue(0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
