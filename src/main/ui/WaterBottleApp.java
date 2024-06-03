package ui;

import model.Event;
import model.EventLog;
import model.Water;
import model.WaterRecord;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Online Water Bottle application
public class WaterBottleApp {
    private static final String JSON_STORE = "./data/waterLog.json";
    private Scanner input;
    private WaterRecord record;
    private int goal;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the water bottle application with a welcome message
    public WaterBottleApp() throws FileNotFoundException {

        System.out.println("Welcome to Online Water Bottle!");

        runWaterBottle();
    }

    // MODIFIES: this
    // EFFECTS: processes user's first input to start the app
    private void runWaterBottle() {
        init();

        displayMenu();
        String command = input.next();
        command = command.toLowerCase();

        if (command.equals("g")) {
            setUpGoal();
        } else if (command.equals("l")) {
            loadWaterRecord();
        } else {
            System.out.println("Selection invalid...Please try again.");
            runWaterBottle();
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes water log
    private void init() {
        record = new WaterRecord(0);
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: displays start instruction to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tg -> set up a goal for the day");
        System.out.println("\tl -> load water log from file");
    }

    // MODIFIES: this
    // EFFECTS: sets up a water-intake goal
    private void setUpGoal() {
        Scanner sc = new Scanner(System.in);
        String total;

        System.out.println("\nWhat is your goal for today? (Only write number in mL):");

        if (sc.hasNextInt()) {
            System.out.println("\nSuccessfully recorded!");
            total = sc.next();
            System.out.println("Your goal has been set to: " + total + " mL.");
            goal = Integer.parseInt(total);
            record = new WaterRecord(goal);
            beginLogging();
        } else {
            System.out.println("Invalid input! Please try again.");
            setUpGoal();
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user's input to start recording each water-intake data
    public void beginLogging() {
        while (record.getGoal() > 0) {
            loggingDisplayMenu();
            String command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                stopLogging();
            } else {
                loggingProcessCommand(command);
            }
        }
        achieved();
    }

    // EFFECTS: displays menu of options to user
    private void loggingDisplayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tr -> record a new water log");
        System.out.println("\ti -> check your goal");
        System.out.println("\tc -> check total water consumption amount so far");
        System.out.println("\ts -> save water log to file");
        System.out.println("\tl -> load water log from file");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: process user command
    private void loggingProcessCommand(String command) {
        if (command.equals("r")) {
            recordWater();
        } else if (command.equals("i")) {
            System.out.println("Your current goal is " + record.getGoal() + " mL.");
        } else if (command.equals("c")) {
            System.out.println("You have drank " + record.addTotalWaterAmount() + " mL so far!");
        } else if (command.equals("s")) {
            saveWaterRecord();
        } else if (command.equals("l")) {
            loadWaterRecord();
        } else {
            System.out.println("Selection invalid...Please try again.");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds water to the water log
    private void recordWater() {
        Scanner sc = new Scanner(System.in);
        int amount;

        System.out.println("\nEnter water amount (Only write number in mL):");

        if (sc.hasNextInt()) {
            amount = sc.nextInt();
            recordHour(amount);
        } else {
            System.out.println("\nInvalid, please try again.");
            recordWater();
        }
    }

    // EFFECTS: takes user's input for the hour time at which they drank water
    private void recordHour(int amount) {
        Scanner sc = new Scanner(System.in);
        String hour;

        System.out.println("\nEnter current time (hour -- only type number):");

        if (sc.hasNextInt()) {
            hour = sc.next();
            recordMinute(amount,hour);
        } else {
            System.out.println("\nInvalid, please try again.");
            recordHour(amount);
        }

    }

    // EFFECTS: takes user's input for the minute time at which they drank water
    private void recordMinute(int amount,String hour) {
        Scanner sc = new Scanner(System.in);
        String minute;

        System.out.println("\nEnter current time (minute -- only type number):");

        if (sc.hasNextInt()) {
            minute = sc.next();
            makeWater(amount, hour, minute);
        } else {
            System.out.println("\nInvalid, please try again.");
            recordMinute(amount,hour);
        }
    }

    // MODIFIES: this
    // EFFECTS: make new water log with the given inputs
    //          and add it to the list
    private void makeWater(int amount, String hour, String minute) {

        Water waterInput = record.waterLog(amount, hour, minute);
        record.addWater(waterInput);

        System.out.println("\nGreat!! You have successfully entered a new water log: \n\t"
                + waterInput);

        suggestRemoveRecent(waterInput);
    }

    // MODIFIES: this
    // EFFECTS: displays full water log to user and stops
    private void stopLogging() {
        System.out.println("\nYour Water log for today: \n\t");
        if (record.isEmpty()) {
            System.out.println("\tNo log to show.");
        } else {
            System.out.println(record.getLog());
        }
        saveWaterRecord();
        System.out.println("\nYour record has been automatically saved.\n");

        for (Event ev: EventLog.getInstance()) {
            System.out.println(ev.toString());
        }

        System.out.println("\nSee you again tomorrow!");
        System.exit(0);
    }

    // MODIFIES: this
    // EFFECTS: takes the current water log and processes user's input to decide whether the current water log
    //          should be kept or removed from the list
    private void suggestRemoveRecent(Water waterInput) {
        System.out.println("\nIf you would like to remove your recent water log, type 'remove'. "
                + "If not, type 'keep': ");
        String userInput = input.next();
        userInput = userInput.toLowerCase();

        if (userInput.equals("remove")) {
            removeLastWater(waterInput);
        } else if (userInput.equals("keep")) {
            showProgress(waterInput);
        } else {
            System.out.println("Invalid input.");
            suggestRemoveRecent(waterInput);
        }
    }

    // EFFECTS: shows the current amount of water left (the "new" goal amount)
    private void showProgress(Water waterInput) {
        System.out.println("\nGreat! The total amount of water left in your Water Bottle is: \n\t"
                + record.progress(waterInput) + " mL.");
    }

    // EFFECTS: displays the "CONGRATULATIONS" message
    private void achieved() {
        System.out.println("\nCONGRATUlATIONS! You have achieved your daily goal!");
        stopLogging();
    }

    // MODIFIES: this
    // EFFECTS: takes the current water log and removes it from the list
    //          then shows the goal set before the current water log
    private void removeLastWater(Water waterInput) {
        record.removeWater(waterInput);

        System.out.println("You have successfully removed your recent water log!");

        System.out.println("\nGreat! The total amount of water left in your Water Bottle is: \n\t"
                + goal + " mL.");
    }

    // EFFECTS: saves the water record to file
    private void saveWaterRecord() {
        try {
            jsonWriter.open();
            jsonWriter.write(record);
            jsonWriter.close();
            System.out.println("Saved the water log to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads water record from file
    private void loadWaterRecord() {
        try {
            record = jsonReader.read();
            System.out.println("Successfully loaded saved log from " + JSON_STORE);
            beginLogging();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

}