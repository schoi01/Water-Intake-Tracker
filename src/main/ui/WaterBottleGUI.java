package ui;

import model.Event;
import model.EventLog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


// Represents application's window frame with the title
public class WaterBottleGUI extends JFrame {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 800;
    protected static JPanel buttonPanel;

    CardLayout cl;
    InitialPanel initialPanel;

    // Constructor initializes fields and sets up window frame layout
    // sets up window frame title
    public WaterBottleGUI() {
        super("Water Intake Tracker");
        initializeFields();
        initializeGraphics();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets frame layout to CardLayout and instantiates initialPanel and buttonPanel
    //          called by the WaterBottleGUI constructor
    private void initializeFields() {
        cl = new CardLayout(0,40);
        initialPanel = new InitialPanel();
        buttonPanel = new JPanel();
    }

    // MODIFIES: this
    // EFFECTS: initializes the frame layout
    private void initializeGraphics() {
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        welcomeTitle();
        initializeButtonPanel();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (Event ev: EventLog.getInstance()) {
                    System.out.println(ev.toString());
                }
                System.exit(0);
            }
        });
        setLocationRelativeTo(null);
    }

    // EFFECTS: adds initialPanel to buttonPanel set to CardLayout,
    //          then display initialPanel
    //          other panels will be added to the buttonPanel later on
    protected void initializeButtonPanel() {
        buttonPanel.setLayout(cl);
        buttonPanel.add(initialPanel,"link1");
        add(buttonPanel);
        cl.show(buttonPanel,"link1");
    }

    // MODIFIES: this
    // EFFECTS: panel with the title and water bottle image
    private void welcomeTitle() {
        JPanel welcomePanel = new JPanel();
        JLabel label = new JLabel();
        welcomePanel.setBackground(Color.white);

        welcomePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("Virtual Water Bottle");
        welcomeLabel.setFont(new Font("sans",Font.BOLD,35));

        BufferedImage wbImage = null;
        try {
            wbImage = ImageIO.read(new File("./Water_Bottle.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Image not found!");
        }
        Image img = wbImage.getScaledInstance(40, (int) 75.7,Image.SCALE_SMOOTH);
        ImageIcon imgIcon = new ImageIcon(img);
        label.setIcon(imgIcon);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(label);

        add(welcomePanel,BorderLayout.NORTH);
        pack();
    }

    // EFFECTS: starts the application
    public static void main(String[] args) {
        new WaterBottleGUI();
    }
}
