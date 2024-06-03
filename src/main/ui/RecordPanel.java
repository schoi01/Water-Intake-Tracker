package ui;

import model.Water;
import model.WaterRecord;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents the recording data panel
public class RecordPanel extends JPanel {

    WaterRecord wr;
    Water waterInput;
    JsonReader jsonReader;
    JsonWriter jsonWriter;
    static final String JSON_STORE = "./data/waterLog.json";

    JLabel goalLabel;
    JLabel status;
    String statVal;
    JTextField amount;
    JTextField hour;
    JTextField min;
    String time;
    JButton addButton;
    JLabel amtLabel;
    JLabel hrLabel;
    JLabel minLabel;
    JTable table = new JTable();
    JScrollPane pane;
    Object[] cols;
    DefaultTableModel model;
    JProgressBar progressBar;
    JButton saveButton;
    JButton loadButton;
    JButton removeButton;

    // constructor instantiates some fields and calls other detail-required methods
    public RecordPanel(String statVal) {
        this.statVal = statVal;

        wr = new WaterRecord(Integer.parseInt(statVal));

        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);

        setSize(600, 800);
        setLayout(null);
        initializeTableFields();
        createGoalStatus();
        createTable();
        createProgressBar(Integer.parseInt(statVal));
        createSaveButton();
        createLoadButton();
        createRemoveButton();
    }

    // EFFECTS: initializes fields for table
    //          amount, hour, time, and addButton
    private void initializeTableFields() {
        amtLabel = new JLabel("amount:");
        hrLabel = new JLabel("hour:");
        minLabel = new JLabel("minute:");

        amtLabel.setBounds(65, 50, 150, 20);
        hrLabel.setBounds(81, 80, 150, 20);
        minLabel.setBounds(68, 110, 150, 20);

        amount = new JTextField(10);
        hour = new JTextField(10);
        min = new JTextField(10);

        amount.setBounds(120, 50, 150, 20);
        hour.setBounds(120, 80, 150, 20);
        min.setBounds(120, 110, 150, 20);

        addButton = new JButton("ADD");
        addButton.setBounds(295, 80, 70, 25);

        cols = new String[]{"amount (mL)", "time"};

        model = (DefaultTableModel) table.getModel();
        model.setColumnIdentifiers(cols);
    }

    // MODIFIES: this
    // EFFECTS: creates goal status description labels and displays on this
    private void createGoalStatus() {
        goalLabel = new JLabel("Current Goal:");
        goalLabel.setFont(new Font("sans", Font.BOLD, 20));
        goalLabel.setBounds(100, 0, 150, 20);

        status = new JLabel(statVal);
        status.setFont(new Font("sans", Font.BOLD, 20));
        status.setBounds(270, 0, 200, 20);

        JLabel unit = new JLabel("mL");
        unit.setFont(new Font("sans", Font.BOLD, 20));
        unit.setBounds(370, 0, 100, 20);

        add(goalLabel);
        add(status);
        add(unit);
    }

    // EFFECTS: creates a table for logged data to be displayed
    //          calls ActionListeners when add button is pressed
    private void createTable() {
        pane = new JScrollPane(table);
        pane.setBounds(35, 150, 350, 380);

        setLayout(null);

        add(pane);
        add(amtLabel);
        add(amount);
        add(hrLabel);
        add(hour);
        add(minLabel);
        add(min);
        add(addButton);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDragEnabled(false);

        addButton.addActionListener(new ProgressActionListener());
        addButton.addActionListener(new AddingActionListener());
    }

    // MODIFIES: this
    // EFFECTS: creates progress bar with current status value besides the table and displays it
    private void createProgressBar(int statVal) {
        progressBar = new JProgressBar(SwingConstants.VERTICAL, 0, statVal);
        progressBar.setValue(statVal);
        progressBar.setStringPainted(true);

        progressBar.setBounds(450, 50, 70, 480);

        add(progressBar);
    }

    // MODIFIES: this
    // EFFECTS: creates save button and displays it
    //          calls an ActionListener when pressed
    private void createSaveButton() {
        saveButton = new JButton("SAVE");
        saveButton.setBounds(120, 550, 90, 30);

        saveButton.addActionListener(new SaveButtonHandler());

        add(saveButton);
    }

    // MODIFIES: this
    // EFFECTS: creates load button and displays it
    //          calls an ActionListener when pressed
    private void createLoadButton() {
        loadButton = new JButton("LOAD");
        loadButton.setBounds(240, 550, 90, 30);

        loadButton.addActionListener(new LoadButtonHandler());

        add(loadButton);
    }

    // MODIFIES: this
    // EFFECTS: creates remove button and displays it
    //          calls an ActionListener when pressed
    private void createRemoveButton() {
        removeButton = new JButton("REMOVE");
        removeButton.setBounds(360, 550, 90, 30);

        removeButton.addActionListener(new RemoveButtonHandler());

        add(removeButton);
    }

    // MODIFIES: this
    // EFFECTS: loads saved file and updates progress bar according to the loaded file
    protected void loadSavedFile() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        int waterTotal = 0;

        try {
            wr = jsonReader.read();

            status.setText(Integer.toString(wr.getGoal()));

            for (Water w : wr.getLog()) {
                String amount = Integer.toString(w.getAmount());
                String time = w.getTime();

                model.addRow(new Object[]{amount, time});

                waterTotal += w.getAmount();
            }

            progressBar = new JProgressBar(SwingConstants.VERTICAL, 0, wr.getGoal() + waterTotal);
            progressBar.setValue(wr.getGoal());
            progressBar.setStringPainted(true);

            progressBar.setBounds(450, 50, 70, 480);

            add(progressBar);
            progressBar.repaint();
            progressBar.revalidate();

            JOptionPane.showMessageDialog(new JPanel(), "Loaded Successfully!");

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    // REQUIRES: add button is pressed
    // MODIFIES: this, wr
    // EFFECTS: adds water input to wr, and displays it in the table
    private class AddingActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            waterInput = wr.waterLog(Integer.parseInt(amount.getText()), hour.getText(), min.getText());
            time = waterInput.getTime();
            wr.addWater(waterInput);

            model.addRow(new Object[]{amount.getText(), time});
        }
    }

    // REQUIRES: add button is pressed
    // MODIFIES: this
    // EFFECTS: updates status label with a new value (newly entered value subtracted from old status)
    //          updates the progress bar accordingly
    //          when status <= 0, displays a messagebox indicating the achievement
    private class ProgressActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (Integer.parseInt(status.getText()) > 0) {
                    status.setText(Integer.toString(
                            Integer.parseInt(status.getText()) - Integer.parseInt(amount.getText())));
                    progressBar.setValue(Integer.parseInt(status.getText()));

                    if (Integer.parseInt(status.getText()) <= 0) {
                        status.setText(Integer.toString(0));
                        progressBar.setValue(0);

                        JOptionPane.showMessageDialog(new JPanel(),
                                "Congratulations! \nYou have achieved your goal!");
                    }
                }
            } catch (NullPointerException n) {
                n.printStackTrace();
                System.out.println("Calculation invalid!");
            }
        }
    }

    // REQUIRES: save button is pressed
    // EFFECTS: saves current goal and log to file
    private class SaveButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            wr.setGoal(Integer.parseInt(status.getText()));

            try {
                jsonWriter.open();
            } catch (FileNotFoundException fe) {
                fe.printStackTrace();
            }
            jsonWriter.write(wr);
            jsonWriter.close();
            JOptionPane.showMessageDialog(new JPanel(), "Saved Successfully!");
        }
    }

    // REQUIRES: load button is pressed
    // EFFECTS: calls loadSavedFile (which loads up saved file and progress bar)
    private class LoadButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadSavedFile();
        }
    }

    // REQUIRES: remove button is pressed
    // MODIFIES: this, wr
    // EFFECTS: removes selected row from the table, removes it from wr
    //          updates status and progress bar with the deletion applied
    //          if no row is selected, displays a warning message
    private class RemoveButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (table.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(new JPanel(), "No Log Selected!");
            } else {
                int getSelectedRow = table.getSelectedRow();
                Water w = wr.getLog().get(getSelectedRow);
                model.removeRow(getSelectedRow);
                wr.removeWater(w);

                status.setText(Integer.toString(
                        Integer.parseInt(status.getText()) + w.getAmount()));
                status.repaint();
                status.revalidate();

                progressBar.setValue(Integer.parseInt(status.getText()));
                progressBar.setStringPainted(true);
                progressBar.repaint();
                progressBar.revalidate();

                JOptionPane.showMessageDialog(new JPanel(), "Removed Successfully!");
            }
        }
    }
}