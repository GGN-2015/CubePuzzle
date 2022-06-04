package cubepuzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EditFrame extends JFrame {
    private JComboBox<String> gameComboBoxRow = null;
    private JComboBox<String> gameComboBoxCol = null; // row, column

    private JTextField blockName = new JTextField(); // the name of a position of the chessboard

    EditFrame() {
        setTitle(Constants.CP_EDIT_TITLE);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false); //! you can still resize it in some certain way
        setMinimumSize(new Dimension(Constants.UI_WIDTH / 2, Constants.UI_HEIGHT / 6));
        setComboList(); // get the combo list
        setLayout(new GridLayout(4, 1, Constants.CP_SEPERATE, Constants.CP_SEPERATE)); // 4 lines, 1 column

        JPanel pSelect = new JPanel(new GridLayout(1, 4, Constants.CP_SEPERATE, Constants.CP_SEPERATE));
        add(pSelect); // first line, two combo box

        // Row/Column id
        pSelect.add(new JLabel("Row Id:"));
        pSelect.add(gameComboBoxRow);
        pSelect.add(new JLabel("Column Id:"));
        pSelect.add(gameComboBoxCol);

        add(blockName);

        JPanel pButton = new JPanel(new GridLayout(1, 2, Constants.CP_SEPERATE, Constants.CP_SEPERATE));
        JButton jGetValue = new JButton(Constants.BUTTON_GET_VALUE);
        jGetValue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { // set the value of blockName
                int i = gameComboBoxRow.getSelectedIndex();
                int j = gameComboBoxCol.getSelectedIndex();
                blockName.setText(DrawPanel.gameNow.chessboard[i][j]);
            }
        });
        pButton.add(jGetValue);
        JButton jSetValue = new JButton(Constants.Button_SET_VALUE);
        jSetValue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { // save the value of the blockName into chessboard
                int i = gameComboBoxRow.getSelectedIndex();
                int j = gameComboBoxCol.getSelectedIndex();
                DrawPanel.gameNow.chessboard[i][j] = blockName.getText();
                GameUI.drawPanel.repaint();
            }
        });
        pButton.add(jSetValue);
        add(pButton);

        JButton jSaveFile = new JButton(Constants.BUTTON_SAVE_FILE); // save the chessboard to a file
        jSaveFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int flag = JOptionPane.showConfirmDialog(null, "Are you sure to save to temp file?");
                //System.out.println("flag = " + flag);
                if(flag == 0) { // Confirm the save operation
                    try {
                        DrawPanel.gameNow.saveGame(Constants.DIR_GAME + "/" + "tempGame.txt");
                    }
                    catch(Exception exp) {
                        JOptionPane.showMessageDialog(null, "Error when save file.");
                    }
                }
            }
        });
        add(jSaveFile);

        setVisible(true);
    }

    public final void setComboList() {
        if(DrawPanel.gameNow != null) {
            gameComboBoxRow = new JComboBox<String>(Utils.getNumberList(DrawPanel.gameNow.lenX));
            gameComboBoxCol = new JComboBox<String>(Utils.getNumberList(DrawPanel.gameNow.lenY));
        }
    }
}
