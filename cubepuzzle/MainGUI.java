package cubepuzzle;

import java.awt.*;
import java.io.FileNotFoundException;
import javax.swing.*;

public class MainGUI extends JFrame{
    private MainGUI() throws FileNotFoundException {
        setTitle(Constants.CP_TITLE);
        JSplitPane hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

        GameUI gameUI = new GameUI();
        gameUI.setMinimumSize(new Dimension(Constants.UI_WIDTH, Constants.UI_HEIGHT));
        hSplitPane.setLeftComponent(gameUI);
        hSplitPane.setRightComponent(new JScrollPane(new SelectGamePanel()));
        add(hSplitPane);

        // settings 
        setVisible(true);
        hSplitPane.setDividerLocation(Constants.UI_WIDTH); // must after setVisible true
        hSplitPane.setEnabled(false);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
    
    public static MainGUI getInstance() throws FileNotFoundException {
        return new MainGUI();
    }

    public static void main(String[] args) throws FileNotFoundException {
        getInstance();
    }
}
