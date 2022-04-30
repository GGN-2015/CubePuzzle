package cubepuzzle;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class SelectGamePanel extends JPanel {
    SelectGamePanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, Constants.CP_SEPERATE, Constants.CP_SEPERATE));
        //setBounds(0, 0, Constants.CP_WIDTH, Constants.CP_HEIGHT);
        setMaximumSize(new Dimension(Constants.CP_WIDTH, Constants.CP_HEIGHT));
        setBackground(Color.DARK_GRAY);

        // instruction bottons
        File gamesDir = new File(Constants.DIR_GAME);
        if(!gamesDir.exists() || !gamesDir.isDirectory()) {
            System.err.println("ControllerPanel(): folder 'games' not found!");
            //! Error Dialog Window Here
            JOptionPane.showMessageDialog(null, "ControllerPanel(): folder 'games' not found!");
            System.exit(Constants.ERROR_NO_GAME_FOLDER);
        }
        for(File gameFile: gamesDir.listFiles()) {
            if(!gameFile.isDirectory()) { // all the file in games Dir is regarded as game file
                JButton jButton = new JButton(gameFile.getName());
                add(jButton);
                jButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        String gameFileName = "games/" + event.getActionCommand();
                        File gameFile = new File(gameFileName);

                        // check if the game file is ok to launch
                        if(gameFile.exists() && !gameFile.isDirectory()) { // ok to launch
                            try{
                                GameUI.drawPanel.requestFocus();
                                DrawPanel.setGame(new Game(gameFileName));
                            }catch(Exception err) {
                                err.printStackTrace();
                            }
                        }else {
                            JOptionPane.showMessageDialog(null, "ControllerPanel(): game '" + gameFileName + "' not found!");
                            //System.exit(Constants.ERROR_RUNTIME_NO_GAME_FILE);
                        }
                    }
                });
            }
        }

        // basic settings for the GUI windows
        // this.setSize(Constants.CP_WIDTH, Constants.CP_HEIGHT);
    }
}
