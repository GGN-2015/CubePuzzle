package cubepuzzle;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;

import javax.swing.*;


public class SelectGamePanel extends JPanel {
    private JComboBox<String> gameComboBox = null;

    public SelectGamePanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, Constants.CP_SEPERATE, Constants.CP_SEPERATE));
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
        add(gameComboBox);

        JButton startGame = new JButton("start game");
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
                } catch(FileNotFoundException exp) {
                    JOptionPane.showMessageDialog(null, "game file '" + gameFileName + "' not found!");
                }
            }
        });

        JButton getTips = new JButton("get tips");
        add(getTips);
        getTips.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    FindWayAlgo findWayAlgo = new FindWayAlgo(DrawPanel.gameNow);
                    JOptionPane.showMessageDialog(null, findWayAlgo.getNextMove());
                } catch(Exception exp) {
                    JOptionPane.showMessageDialog(null, Constants.MSG_GAMEERR);
                }
                //! you need to focus back to the game after a tip
                //! to promote the game playing experience
                GameUI.drawPanel.requestFocus();
            }
        });
    }
}
