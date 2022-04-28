package cubepuzzle;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

// Controller Panel is a single instance
public class ControllerPanel extends JFrame {
    public static ControllerPanel cpInstance = null;

    GameUI gameUI = null;
    JTextArea insInput = null;
    JButton runCommand = null;

    // use this static method to get the single instance
    static public ControllerPanel getInstance() {
        if(cpInstance == null) cpInstance = new ControllerPanel();
        return cpInstance;
    }

    // private constructor of ControllerPanel
    private ControllerPanel() {
        Container container = this.getContentPane();
        
        // apperance setting
        container.setLayout(new FlowLayout(FlowLayout.LEFT, Constants.CP_SEPERATE, Constants.CP_SEPERATE));
        container.setBounds(0, 0, Constants.CP_WIDTH, Constants.CP_HEIGHT);
        container.setBackground(Color.DARK_GRAY);
        this.setTitle(Constants.CP_TITLE);

        // instruction bottons
        File gamesDir = new File("games");
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
                            GameUI gameUI = GameUI.getInstance();
                            gameUI.setVisible(true);
                            try{
                                gameUI.setGame(new Game(gameFileName));
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
        this.setSize(Constants.CP_WIDTH, Constants.CP_HEIGHT);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        getInstance();
    }
}
