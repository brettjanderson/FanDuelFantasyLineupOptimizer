package Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Brett Anderson.
 */
public class ManualDataEntry extends JFrame {

    private JTextField input;
    private JButton submit;
    private JLabel teamAbbrLabel;
    private String teamName;
    private boolean infoEntered;

    public ManualDataEntry(String teamAbbr, String resourceNeeded){
        super(resourceNeeded);
        teamAbbrLabel = new JLabel(teamAbbr);

        submit = new JButton();
        submit.setText("Enter");

        input = new JTextField();
        input.setColumns(20);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teamName = input.getText();
                setInfoEntered(true);
            }
        });

        add(teamAbbrLabel, BorderLayout.NORTH);
        add(input, BorderLayout.CENTER);
        add(submit, BorderLayout.SOUTH);

        pack();
        setLocationByPlatform(true);
        setVisible(true);
    }

    public synchronized String getEnteredInformation() throws InterruptedException {

        while(!infoEntered) wait();

        return teamName;
    }

    public void closeWindow(){
        this.dispose();
    }

    public synchronized void setInfoEntered(boolean infoEntered) {
        this.infoEntered = infoEntered;
        notify();
    }
}
