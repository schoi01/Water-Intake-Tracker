package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents the initial panel
public class InitialPanel extends JPanel {

    JButton goal;
    JButton load;
    JLabel label;

    // MODIFIES: this
    // EFFECTS: sets up label and goal/load buttons at appropriate location in the frame
    public InitialPanel() {
        setSize(600, 800);
        setLayout(null);
        setBackground(Color.white);

        label = new JLabel("Welcome to Virtual Water Bottle!");
        goal = new JButton("Set Up a New Goal");
        load =  new JButton("Load Saved File");

        label.setBounds(60,0,600,50);
        label.setFont(new Font("Sans",Font.BOLD,30));

        goal.setBounds(200,250,175,30);
        goal.setFont(new Font("Sans",Font.PLAIN,15));

        load.setBounds(200,300,175,30);
        load.setFont(new Font("Sans",Font.PLAIN,15));

        goal.addActionListener(new GoalButtonHandler());
        load.addActionListener(new LoadButtonHandler());

        add(label);
        add(goal);
        add(load);
    }

    // REQUIRES: goal button is pressed
    // MODIFIES: WaterBottleGUI
    // EFFECTS: switches displaying panel to GoalPanel
    private class GoalButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            GoalPanel goalPanel = new GoalPanel();
            WaterBottleGUI.buttonPanel.add(goalPanel,"link2");

            JButton button = (JButton) e.getSource();
            JPanel buttonPanel = (JPanel) button.getParent();
            JPanel cardLayoutPanel = (JPanel) buttonPanel.getParent();
            CardLayout layout = (CardLayout) cardLayoutPanel.getLayout();
            layout.show(cardLayoutPanel, "link2");
        }
    }

    // REQUIRES: load button is pressed
    // MODIFIES: WaterBottleGUI
    // EFFECTS: switches displaying panel to RecordPanel with loaded file
    private class LoadButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            RecordPanel recordPanel = new RecordPanel("0");
            WaterBottleGUI.buttonPanel.add(recordPanel,"link3");

            JButton button = (JButton) e.getSource();
            JPanel buttonPanel = (JPanel) button.getParent();
            JPanel cardLayoutPanel = (JPanel) buttonPanel.getParent();
            CardLayout layout = (CardLayout) cardLayoutPanel.getLayout();
            layout.show(cardLayoutPanel, "link3");

            recordPanel.loadSavedFile();
            recordPanel.progressBar.repaint();
            recordPanel.progressBar.revalidate();
        }
    }
}
