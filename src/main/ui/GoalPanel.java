package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents the initial goal enter panel
public class GoalPanel extends JPanel implements ActionListener {

    JLabel label;
    JButton enter;
    JTextField goal;
    RecordPanel recordPanel;

    // constructor initializes fields
    public GoalPanel() {
        setSize(600, 800);
        setLayout(null);
        initializeFields();
    }

    // MODIFIES: this
    // EFFECTS: instantiates used label/textfield/button with descriptions related to entering goal
    //          and displays them with appropriate location
    private void initializeFields() {
        JLabel title = new JLabel("Set Up Your Water Goal");
        label = new JLabel("Enter your water goal for today:");
        goal = new JTextField(10);
        enter = new JButton("ENTER");

        title.setBounds(120,0,600,40);
        title.setFont(new Font("Sans",Font.BOLD,30));

        label.setBounds(130,150,600,25);
        label.setFont(new Font("Sans",Font.BOLD,20));

        goal.setBounds(180,200,120,25);
        goal.setFont(new Font("Sans",Font.PLAIN,13));

        enter.setBounds(310,200,83,25);
        enter.setFont(new Font("Sans",Font.PLAIN,13));

        enter.addActionListener(this);

        add(title);
        add(label);
        add(goal);
        add(enter);
    }

    // REQUIRES: enter button is pressed
    // MODIFIES: WaterBottleGUI and RecordPanel
    // EFFECTS: switches displaying panel to RecordPanel with recorded goal value
    //          passed in as a parameter
    @Override
    public void actionPerformed(ActionEvent e) {
        String statVal = goal.getText();

        recordPanel = new RecordPanel(statVal);
        WaterBottleGUI.buttonPanel.add(recordPanel,"link3");

        JButton button = (JButton) e.getSource();
        JPanel buttonPanel = (JPanel) button.getParent();
        JPanel cardLayoutPanel = (JPanel) buttonPanel.getParent();
        CardLayout layout = (CardLayout) cardLayoutPanel.getLayout();
        layout.show(cardLayoutPanel, "link3");
    }
}
