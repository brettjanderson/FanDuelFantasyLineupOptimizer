package Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Brett Anderson.
 */
public class ManualDataEntryLong extends JFrame {

    private JTextArea input;
    private JScrollPane scrollPane;
    private JButton submit;
    private JLabel label;
    private String teamName;
    private boolean infoEntered;

    public ManualDataEntryLong(String data, String resourceNeeded){
        super(resourceNeeded);

        label = new JLabel(data);

        input = new JTextArea(50, 200);
        scrollPane = new JScrollPane(input);

        submit = new JButton();
        submit.setText("Enter");

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teamName = input.getText();
                setInfoEntered(true);
            }
        });

        add(label, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(submit, BorderLayout.SOUTH);

        infoEntered = false;

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