# Online Water Bottle

## Proposal:

### What will the application do?  
- ***Online Water Bottle*** will help you set a goal of how much water you want to drink per day, and track how much 
you drink water every day. It has other useful functions, such as setting a personal daily goal, and seeing the total 
amount of water left.

### Who will use it?
- It can be used by anyone. Especially, it will be useful for those who intend to stay healthy for various reasons -
 weight loss or weight maintenance, improving sleep quality/mood, etc.

### Why is this project of interest to you?
- I have personally been trying to drink a certain amount of water every day, however it has been really difficult to
make it a habit of mine and put it into practice. Therefore, I thought developing a water intake tracker would be very 
useful and helpful for myself as well as for many others who may also struggle with keeping themselves hydrated.

## User Stories:

### In *Virtual Water Bottle* Application:
- As a user, I want to be able to input my daily water consumption goal into the program.
- As a user, I want to be able to be record the amount of water I drink with the current time.
- As a user, I want to be able to subtract the amount of water I drink each time from the total amount of water intake.
- As a user, I want to be able to see a full log of my record when I quit the application.
- As a user, I want to be able to manually save my record after each water log I input.
- As a user, I want to be able to automatically save my water log when I quit the application.
- As a user, I want to be able to reload my saved water log with current water goal when I start the application.

## Phase 4: Task 2

### Representative Sample of the EventLog:
- Fri Nov 26 10:26:08 PST 2021 <br />
---> Added water input: [ 200 mL at 3:30 ] <br />
- Fri Nov 26 10:26:12 PST 2021 <br />
---> Removed water input: [ 200 mL at 3:30 ]

## Phase 4: Task 3

### Reflection on the Design of My Application:
- All of my panel classes have buttons and/or labels that I have manually set the bounds of, which means that the 
location of each button is set based on one another. If I changed one component's location, the others would have to be
manually changed accordingly. Therefore, If I had more time to work on my project, I would create a static location
value and design all components using that static value (i.e., width of a button = static value +- #). This will improve
the consistency of my application.
- Looking at my UML class diagram, I understand that the RecordPanel has way too many functions that I think could be 
put separately into different classes. For instance, I have multiple inner classes that implement ActionListener in the
RecordPanel, which tend to be a bit long and make the class look complicated. These inner classes are all designed to be
called on button click. Instead, I could create a separate class for these classes, for instance, and pass in the
constructor appropriate information from the RecordPanel (or create methods that takes values from the RecordPanel as a
parameter and call it in the RecordPanel) so that the required values and variables can be used in different classes.
- Additionally, if I had more time to work on my project, I would add try/catch exceptions for entering invalid values
  (i.e., entering letters instead of numbers) to increase robustness of my application.