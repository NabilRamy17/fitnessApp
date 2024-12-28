/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fitnessapp;

import static fitnessapp.User.readMeals;
import static fitnessapp.User.writeToFile;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Trainer extends User {

    public Trainer(boolean gender, int age, String username, String password, String email) {
        super(gender, age, username, password, email);
    }

    public boolean addWorkout(String name, String type) {
        ArrayList<Workout> workouts = readWorkouts();

        for (Workout w : workouts) {
            if (w.getName().equals(name)) {
                return false;
            }
        }

        Workout workout = new Workout(name, type);
        workouts.add(workout);
        writeToFile(workouts, "workouts.bin");
        return true;
    }

    private GridPane addWorkout() {
        Label nameLabel = new Label("Workout Name:");
        TextField nameField = new TextField();

        nameField.setPromptText("Enter Woekout Name");

        Label typeLabel = new Label("Workout Type:");

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Cardio", "Strength", "Balance", "Flexibilty");
        typeComboBox.setValue("Strength");

        Button addWorkoutButton = new Button("Add Workout");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(nameLabel, 0, 1);
        gridPane.add(nameField, 1, 1);
        gridPane.add(typeLabel, 0, 2);
        gridPane.add(typeComboBox, 1, 2);
        gridPane.add(addWorkoutButton, 0, 3);

        addWorkoutButton.setOnAction(e -> {
            String name = nameField.getText();
            String type = typeComboBox.getValue();

            if (name == null || name.trim().isEmpty()) {
                GUI.PopUp.showError("Workout name cannot be empty");
                return;
            }

            if (type == null) {
                GUI.PopUp.showError("Please select a workout type");
                return;
            }

            if (addWorkout(name, type)) {
                GUI.PopUp.showMessage("Workout created successfully");
            } else {
                GUI.PopUp.showError("Workout with this name already exists");
            }
        });

        return gridPane;
    }

    public Workout searchWorkout(String name) {
        ArrayList<Workout> workouts = readWorkouts();

        for (Workout w : workouts) {
            if (w.getName().equals(name)) {
                return w;
            }
        }

        return null;
    }

    private HBox searchWorkout() {
        Label nameLabel = new Label("Enter Workout Name to Search:");

        TextField nameField = new TextField();

        Button searchButton = new Button("Search Workout");

        searchButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                GUI.PopUp.showError("Workout name cannot be empty");
                return;
            }
            Workout w = searchWorkout(name);
            if (w == null) {
                GUI.PopUp.showError("Workout " + name + " not found");
            } else {
                GUI.PopUp.showMessage(w.toString());
            }
        });

        HBox hbox = new HBox(10, nameLabel, nameField, searchButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-padding: 10px;");

        return hbox;
    }

    private TableView<Workout> listWorkouts(ArrayList<Workout> workoutList) {
        TableView<Workout> tableView = new TableView<>();

        TableColumn<Workout, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Workout, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        tableView.getColumns().addAll(nameColumn, typeColumn);

        // Clear existing items and add filtered workouts
        tableView.getItems().clear();
        tableView.getItems().addAll(workoutList);

        return tableView;
    }

    public boolean removeWorkout(String name) {
        ArrayList<Workout> workouts = readWorkouts();
        Iterator<Workout> iterator = workouts.iterator();
        while (iterator.hasNext()) {
            Workout u = iterator.next();
            if (u.getName().equals(name)) {
                iterator.remove();
                writeToFile(workouts, "workouts.bin");
                return true;
            }
        }

        return false;
    }

    private HBox removeWorkout() {
        Label nameLabel = new Label("Enter Workout Name to Remove:");

        TextField nameField = new TextField();
        nameField.setPromptText("enter workout to remove");
        Button removeButton = new Button("Remove Workout");

        removeButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                GUI.PopUp.showError("Workout name cannot be empty");
                return;
            }

            if (removeWorkout(name)) {
                GUI.PopUp.showMessage("Workout " + name + " removed successfully");
            } else {
                GUI.PopUp.showError("Workout " + name + " not found");
            }
        });

        HBox hbox = new HBox(10, nameLabel, nameField, removeButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-padding: 10px;");

        return hbox;
    }

    public boolean createProgram(String title, ArrayList<Workout> workouts, ArrayList<Meal> meals) {
        ArrayList<Program> programs = readPrograms();

        for (Program p : programs) {
            if (p.getTitle().equals(title)) {
                return false;
            }
        }
        Program program = new Program(title, workouts, meals);
        programs.add(program);
        writeToFile(programs, "programs.bin");
        return true;
    }

    private GridPane addProgram() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        Label nameLabel = new Label("Program Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Program name");
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);

        Label workoutsLabel = new Label("Select Workouts:");
        gridPane.add(workoutsLabel, 0, 1);

        VBox workoutsBox = new VBox();
        workoutsBox.setSpacing(5);
        ArrayList<Workout> workouts = User.readWorkouts();
        ArrayList<CheckBox> workoutCheckBoxes = new ArrayList<>();

        for (Workout workout : workouts) {
            CheckBox checkBox = new CheckBox(workout.getName());
            workoutCheckBoxes.add(checkBox);
            workoutsBox.getChildren().add(checkBox);
        }

        gridPane.add(workoutsBox, 1, 1);

        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        gridPane.add(separator, 0, 2, 2, 1);

        Label mealsLabel = new Label("Select Meals:");
        gridPane.add(mealsLabel, 0, 3);

        VBox mealsBox = new VBox();
        mealsBox.setSpacing(5);
        ArrayList<Meal> meals = User.readMeals();
        ArrayList<CheckBox> mealCheckBoxes = new ArrayList<>();

        for (Meal meal : meals) {
            CheckBox checkBox = new CheckBox(meal.getName());
            mealCheckBoxes.add(checkBox);
            mealsBox.getChildren().add(checkBox);
        }

        gridPane.add(mealsBox, 1, 3);

        Button submitButton = new Button("Add Program");
        gridPane.add(submitButton, 1, 4);

        submitButton.setOnAction(e -> {
            String programName = nameField.getText();
            if (programName == null || programName.trim().isEmpty()) {
                GUI.PopUp.showError("Program name cannot be empty");
                return;
            }
            ArrayList<Workout> selectedWorkouts = new ArrayList<>();
            ArrayList<Meal> selectedMeals = new ArrayList<>();

            for (int i = 0; i < workoutCheckBoxes.size(); i++) {
                if (workoutCheckBoxes.get(i).isSelected()) {
                    selectedWorkouts.add(workouts.get(i));
                }
            }

            for (int i = 0; i < mealCheckBoxes.size(); i++) {
                if (mealCheckBoxes.get(i).isSelected()) {
                    selectedMeals.add(meals.get(i));
                }
            }

            if (createProgram(programName, workouts, meals)) {
                GUI.PopUp.showMessage("Program created successfully");
            } else {
                GUI.PopUp.showError("Program with this name already exists");
            }

        });

        return gridPane;
    }

    public Program searchProgram(String title) {
        ArrayList<Program> programs = readPrograms();

        for (Program p : programs) {
            if (p.getTitle().equals(title)) {
                return p;
            }
        }

        return null;
    }

    private HBox searchProgram() {
        Label nameLabel = new Label("Enter Program Name to Search:");

        TextField nameField = new TextField();

        Button searchButton = new Button("Search Program");

        searchButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                GUI.PopUp.showError("Program name cannot be empty");
                return;
            }
            Program p = searchProgram(name);
            if (p == null) {
                GUI.PopUp.showError("Program " + name + " not found");
            } else {
                GUI.PopUp.showMessage(p.toString());
            }
        });

        HBox hbox = new HBox(10, nameLabel, nameField, searchButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-padding: 10px;");

        return hbox;
    }

    private ArrayList<Workout> findworkout(String keyword) {

        ArrayList<Workout> workout = User.readWorkouts();
        ArrayList<Workout> workoutWithKeyword = new ArrayList<>();

        for (Workout p : workout) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                workoutWithKeyword.add(p);
            }
        }

        return workoutWithKeyword;
    }

    private VBox list(BorderPane borderPane) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        TextField search = new TextField();
        search.setPromptText("Search");

        search.setOnKeyTyped((t) -> {

            String keyword = search.getText();

            try {
                ArrayList<Workout> workout = findworkout(keyword);

                vbox.getChildren().remove(1, vbox.getChildren().size());
                vbox.getChildren().add(listWorkouts(workout));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        vbox.getChildren().addAll(search, listWorkouts(User.readWorkouts()));

        return vbox;
    }

    private TableView<Program> listPrograms() {
        TableView<Program> tableView = new TableView<>();

        TableColumn<Program, String> titleColumn = new TableColumn<>("Program title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Program, String> workoutsColumn = new TableColumn<>("Workouts");
        workoutsColumn.setCellValueFactory(cellData -> {
            Program program = cellData.getValue();
            String workoutsNames = program.getWorkouts().stream()
                    .map(Workout::getName) // Assuming Workout has a getName() method
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(workoutsNames);
        });

        TableColumn<Program, String> mealsColumn = new TableColumn<>("Meals");
        mealsColumn.setCellValueFactory(cellData -> {
            Program program = cellData.getValue();
            String mealsNames = program.getMeals().stream()
                    .map(Meal::getName) // Assuming Meal has a getName() method
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(mealsNames);
        });

        tableView.getColumns().addAll(titleColumn, workoutsColumn, mealsColumn);

        ArrayList<Program> programList = User.readPrograms(); // Assuming a method to fetch programs
        tableView.getItems().addAll(programList);

        return tableView;
    }

    public boolean deleteprgram(String name) {
        ArrayList<Program> Programs = readPrograms();
        Iterator<Program> iterator = Programs.iterator();
        while (iterator.hasNext()) {
            Program u = iterator.next();
            if (u.getTitle().equals(name)) {
                iterator.remove();
                writeToFile(Programs, "programs.bin");
                return true;
            }
        }
        return false;

    }

    private HBox removeProgram() {
        Label nameLabel = new Label("Enter Program Name to Remove:");

        TextField nameField = new TextField();

        Button removeButton = new Button("Remove Program");

        removeButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                GUI.PopUp.showError("Program name cannot be empty");
                return;
            }

            if (deleteprgram(name)) {
                GUI.PopUp.showMessage("Program " + name + " removed successfully");
            } else {
                GUI.PopUp.showError("Program " + name + " not found");
            }
        });

        HBox hbox = new HBox(10, nameLabel, nameField, removeButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-padding: 10px;");

        return hbox;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean editWorkout(String name, String newname, String type) {
        ArrayList<Workout> Workouts = readWorkouts();

        for (Workout u : Workouts) {
            if (u.getName().equals(name)) {
                u.setName(newname);
                u.setType(type);

                writeToFile(Workouts, "workouts.bin");
                return true;

            }
        }
        return false;
    }

    public Meal searchMeal(String name) {
        ArrayList<Meal> meals = readMeals();

        for (Meal m : meals) {
            if (m.getName().equals(name)) {
                return m;
            }
        }

        return null;
    }

    public User searchUser(String username) {
        ArrayList<User> users = readUsers();

        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }

        return null;
    }

    public void addProgramToClinet(Client client, Program program) {
        boolean found = false;
        for (Program p : client.getPrograms()) {
            if (p.getTitle().equals(program.getTitle())) {
                found = true;
                break;
            }
        }

        if (!found) {
            client.getPrograms().add(program);
            saveUserUpdates(client);
        }
    }

    private ArrayList<Client> getClients() {
        ArrayList<Client> clients = new ArrayList<>();
        for (User u : User.readUsers()) {
            if (u instanceof Client) {
                clients.add((Client) u);
            }
        }
        return clients;
    }

    private GridPane addtoclient() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        // Label for Programs
        Label programsLabel = new Label("Select Program:");
        gridPane.add(programsLabel, 0, 0);

        // Program Checkboxes
        VBox workoutsBox = new VBox();
        workoutsBox.setSpacing(5);
        ArrayList<Program> programs = readPrograms();
        ArrayList<CheckBox> workoutCheckBoxes = new ArrayList<>();

        for (Program program : programs) {
            CheckBox checkBox = new CheckBox(program.getTitle());
            workoutCheckBoxes.add(checkBox);
            workoutsBox.getChildren().add(checkBox);
        }
        gridPane.add(workoutsBox, 1, 1);

        // Separator
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        gridPane.add(separator, 0, 2, 2, 1);

        // Label for Clients
        Label clientLabel = new Label("Select Client:");
        gridPane.add(clientLabel, 0, 3);

        // Client Checkboxes
        VBox clientsBox = new VBox();
        clientsBox.setSpacing(5);
        ArrayList<Client> users = getClients();

        ArrayList<CheckBox> clientCheckBoxes = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Client) {
                CheckBox checkBox = new CheckBox(user.getUsername());
                clientCheckBoxes.add(checkBox);
                clientsBox.getChildren().add(checkBox);
            }
        }
        gridPane.add(clientsBox, 1, 3);

        // Submit Button
        Button submitButton = new Button("Add Program");
        gridPane.add(submitButton, 1, 4);

        // Submit Button Action
        submitButton.setOnAction(e -> {
            // Get Selected Programs
            ArrayList<Program> selectedPrograms = new ArrayList<>();
            for (int i = 0; i < workoutCheckBoxes.size(); i++) {
                if (workoutCheckBoxes.get(i).isSelected()) {
                    selectedPrograms.add(programs.get(i));
                }
            }

            // Get Selected Clients
            ArrayList<Client> selectedClients = new ArrayList<>();
            for (int i = 0; i < clientCheckBoxes.size(); i++) {

                if (clientCheckBoxes.get(i).isSelected()) {
                    User user = users.get(i);
                    selectedClients.add((Client) user);

                }
            }

            // Validation
            if (selectedPrograms.isEmpty()) {
                GUI.PopUp.showError("No program selected. Please select at least one program.");
                return;
            }

            if (selectedClients.isEmpty()) {
                GUI.PopUp.showError("No client selected. Please select at least one client.");
                return;
            }

            // Assign Programs to Clients
            for (Client client : selectedClients) {
                for (Program program : selectedPrograms) {
                    addProgramToClinet(client, program);
                }
            }

            GUI.PopUp.showMessage("Programs successfully added to selected clients!");

            // Reset Checkboxes
            workoutCheckBoxes.forEach(checkBox -> checkBox.setSelected(false));
            clientCheckBoxes.forEach(checkBox -> checkBox.setSelected(false));
        });

        return gridPane;
    }

    private GridPane editworout() {

        Label workoutnameLabel = new Label("Enter old name:");
        TextField workoutnameField = new TextField();
        workoutnameField.setPromptText("enter wourkout name");

        Label nameLabel = new Label("Name:");
        TextField newnameField = new TextField();
        newnameField.setPromptText("enter new name");
        Label typeLabel = new Label("Type:");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Cardio", "Strength", "Balance", "Flexibilty");
        typeComboBox.setValue("Strength");
        Button editButton = new Button("Edit Workout");
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(workoutnameLabel, 0, 0);
        gridPane.add(workoutnameField, 1, 0);

        gridPane.add(nameLabel, 0, 1);
        gridPane.add(newnameField, 1, 1);
        gridPane.add(typeLabel, 0, 3);
        gridPane.add(typeComboBox, 1, 3);
        gridPane.add(editButton, 1, 5);

        editButton.setOnAction(e -> {
            String name = workoutnameField.getText();
            String newname = newnameField.getText();
            String type = typeComboBox.getValue();

            if (name == null || name.trim().isEmpty()) {
                GUI.PopUp.showError("Name cannot be empty");
                return;
            }

            if (newname == null || newname.trim().isEmpty()) {
                GUI.PopUp.showError("New name cannot be empty");
                return;
            }
            if (type == null) {
                GUI.PopUp.showError("Please select a Wourkout type");
                return;
            }

            Workout w = searchWorkout(newname);
            if (w != null) {
                GUI.PopUp.showError(newname + " New name already taken");
                return;

            }

            if (editWorkout(name, newname, type)) {
                GUI.PopUp.showMessage(name + " edited successfully");
            } else {
                GUI.PopUp.showError("Failed to edit Wourkout. Please try again.");
            }

        });
        return gridPane;
    }

    public void viewProgramPage() {
        Scanner sc = new Scanner(System.in);
        ArrayList<Program> programs = readPrograms();
        for (Program p : programs) {
            System.out.println(p);
        }
        System.out.println("Enter the title of the program you want to search : ");
        String name = sc.next();
        Program p = searchProgram(name);
        if (p == null) {
            System.out.println("program not found");
        } else {
            System.out.println(p);
        }
    }

    public void clientsPage() {
        Scanner sc = new Scanner(System.in);
        ArrayList<User> users = readUsers();
        for (User u : users) {
            if (u instanceof Client) {
                System.out.println(u);
            }
        }
        System.out.println("Enter the username of the user you want to see : ");
        String username = sc.next();
        User u = searchUser(username);
        if (u == null) {
            System.out.println("Username not found");
        } else {
            System.out.println(u);
            ArrayList<Program> programs = readPrograms();
            for (Program p : programs) {
                System.out.println(p);
            }
            System.out.println("Enter the title of the program you want to add : ");
            String name = sc.next();
            Program p = searchProgram(name);
            if (p == null) {
                System.out.println("program not found");
            } else {
                addProgramToClinet((Client) u, p);

            }
        }
    }

    public Scene homePage() {
        BorderPane borderPane = new BorderPane();

        Label welcomeLabel = new Label("Welcome, " + getUsername());
        welcomeLabel.setStyle("-fx-font-size: 30px; -fx-padding: 10px;");

        StackPane topPane = new StackPane(welcomeLabel);
        topPane.setAlignment(Pos.CENTER);
        topPane.setStyle("-fx-padding: 10px; -fx-background-color: #e0e0e0;");
        borderPane.setTop(topPane);

        VBox buttonBoxLeft = new VBox(10);
        buttonBoxLeft.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0;");

        Button addButton = new Button("Add Workout");
        addButton.setOnAction((t) -> {
            borderPane.setCenter(addWorkout());
        });

        Button searchButton = new Button("Search Workout");
        searchButton.setOnAction((t) -> {
            borderPane.setCenter(list(borderPane));
        });
        Button listButton = new Button("List Workouts");
        listButton.setOnAction((t) -> {
            borderPane.setCenter(new VBox(listWorkouts(User.readWorkouts()), removeWorkout()));
        });

        Button editButton = new Button("Edit Workout");
        editButton.setOnAction((t) -> {
            borderPane.setCenter(editworout());
        });

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction((t) -> {
            fitnessapp.FitnessApp.stage.setScene(new GUI.Login_Signup(new HBox()));
        });

        buttonBoxLeft.getChildren().addAll(addButton, searchButton, listButton, editButton, logoutButton);
        borderPane.setLeft(buttonBoxLeft);

        // Right side buttons for program
        VBox buttonBoxRight = new VBox(10);
        buttonBoxRight.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0;");

        Button addProgramButton = new Button("Add Program");
        addProgramButton.setOnAction((t) -> {
            borderPane.setCenter(addProgram());
        });

        Button searchProgramButton = new Button("Search Program");
        searchProgramButton.setOnAction((t) -> {
            borderPane.setCenter(searchProgram());
        });
        Button listProgramButton = new Button("List Programs");
        listProgramButton.setOnAction((t) -> {
            borderPane.setCenter(new VBox(listPrograms(), removeProgram()));
        });

        Button addtoclieantutton = new Button("Add to client");
        addtoclieantutton.setOnAction((t) -> {
            borderPane.setCenter(new VBox(addtoclient()));
        });

        buttonBoxRight.getChildren().addAll(addProgramButton, searchProgramButton, addtoclieantutton, listProgramButton);
        borderPane.setRight(buttonBoxRight);

        Label centerLabel = new Label("Welcome back");
        centerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        borderPane.setCenter(centerLabel);

        Scene scene = new Scene(borderPane, 800, 400);
        return scene;

    }

    @Override
    public String toString() {
        return "Trainer{" + "username=" + getUsername() + ", password=" + getPassword() + '}';
    }

}
