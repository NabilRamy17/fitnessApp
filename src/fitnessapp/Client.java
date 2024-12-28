/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fitnessapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Client extends User {

    private ArrayList<Program> programs;
    private LocalDate subscriptionEndDate;
    private double currentWeight;

    public Client(ArrayList<Program> programs, double currentWeight, boolean gender, int age, String username, String password, String subscriptionEndDate, String email) {
        super(gender, age, username, password, email);
        this.programs = programs;
        this.currentWeight = currentWeight;
        LocalDate currentDate = LocalDate.now();

        if (subscriptionEndDate.equals("1-month")) {
            this.subscriptionEndDate = currentDate.plusMonths(1);
        } else if (subscriptionEndDate.equals("3-month")) {
            this.subscriptionEndDate = currentDate.plusMonths(3);
        } else if (subscriptionEndDate.equals("6-month")) {
            this.subscriptionEndDate = currentDate.plusMonths(6);
        } else { // Default to 12-month subscription
            this.subscriptionEndDate = currentDate.plusMonths(12);
        }

    }

    public ArrayList<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(ArrayList<Program> programs) {
        this.programs = programs;
    }

    public LocalDate getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public void setSubscriptionEndDate(LocalDate subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public static boolean signup(boolean gender, int age, String username, String password, double currentweight, String subscriptionEndDate, String email) {
        ArrayList<User> users = readUsers();
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }

        Client client = new Client(new ArrayList<Program>(), currentweight, gender, age, username, password, subscriptionEndDate, email);
        users.add(client);
        writeToFile(users, "users.bin");
        return true;
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

    private GridPane viewProfile() {

        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label ageLabel = new Label("Age:");
        Label emailLabel = new Label("Email:");
        Label genderLabel = new Label("Gender:");
        Label weightLabel = new Label("Current Weight:");
        Label subscriptionLabel = new Label("End of Subscription:");

        Label usernameValue = new Label(this.getUsername());
        Label passwordValue = new Label(this.getPassword());
        Label ageValue = new Label(String.valueOf(this.getAge()));
        Label emailValue = new Label(this.getEmail());
        String gender;
        if (this.getGender()) {
            gender = "Male";
        } else {
            gender = "female";
        }

        Label genderValue = new Label(gender);

        Label weightValue = new Label(String.format("%.2f kg", this.getCurrentWeight()));
        Label subscriptionValue = new Label(this.getSubscriptionEndDate().toString());

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameValue, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordValue, 1, 2);
        gridPane.add(ageLabel, 0, 3);
        gridPane.add(ageValue, 1, 3);
        gridPane.add(emailLabel, 0, 4);
        gridPane.add(emailValue, 1, 4);
        gridPane.add(genderLabel, 0, 5);
        gridPane.add(genderValue, 1, 5);
        gridPane.add(weightLabel, 0, 6);
        gridPane.add(weightValue, 1, 6);
        gridPane.add(subscriptionLabel, 0, 7);
        gridPane.add(subscriptionValue, 1, 7);

        return gridPane;
    }

    private GridPane viewPrograms() {
    // Title Label
    Label titleLabel = new Label("My Programs");
    titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

    // Check if the client has any programs
    if (programs.isEmpty()) {
        Label noProgramsLabel = new Label("You have not enrolled in any programs yet.");
        noProgramsLabel.setStyle("-fx-font-size: 14px;");

        GridPane noProgramsPane = new GridPane();
        noProgramsPane.setVgap(10);
        noProgramsPane.setAlignment(Pos.CENTER);
        noProgramsPane.add(titleLabel, 0, 0);
        noProgramsPane.add(noProgramsLabel, 0, 1);
        return noProgramsPane;
    }

    // Create a grid to display programs
    GridPane programsPane = new GridPane();
    programsPane.setVgap(10);
    programsPane.setHgap(20);
    programsPane.setAlignment(Pos.TOP_CENTER);

    // Add title
    programsPane.add(titleLabel, 0, 0, 3, 1);

    // Add headers
    Label programNameHeader = new Label("Program Name");
    programNameHeader.setStyle("-fx-font-weight: bold;");
    Label workoutsHeader = new Label("Workouts");
    workoutsHeader.setStyle("-fx-font-weight: bold;");
    Label mealsHeader = new Label("Meals");
    mealsHeader.setStyle("-fx-font-weight: bold;");

    programsPane.add(programNameHeader, 0, 1);
    programsPane.add(workoutsHeader, 1, 1);
    programsPane.add(mealsHeader, 2, 1);

    // Add each program to the grid
    int row = 2;
    for (Program program : programs) {
        // Program name
        Label programName = new Label(program.getTitle());
        programsPane.add(programName, 0, row);

        // Workouts
        VBox workoutsBox = new VBox();
        workoutsBox.setSpacing(5);
        ArrayList<Workout> workouts = program.getWorkouts();
        if (workouts != null && !workouts.isEmpty()) {
            for (Workout workout : workouts) {
                Label workoutLabel = new Label("• " + workout.getName()); // Assuming `Workout` has a `getName()` method
                workoutsBox.getChildren().add(workoutLabel);
            }
        } else {
            workoutsBox.getChildren().add(new Label("No workouts available"));
        }
        programsPane.add(workoutsBox, 1, row);

        // Meals
        VBox mealsBox = new VBox();
        mealsBox.setSpacing(5);
        ArrayList<Meal> meals = program.getMeals();
        if (meals != null && !meals.isEmpty()) {
            for (Meal meal : meals) {
                Label mealLabel = new Label("• " + meal.getName()); // Assuming `Meal` has a `getName()` method
                mealsBox.getChildren().add(mealLabel);
            }
        } else {
            mealsBox.getChildren().add(new Label("No meals available"));
        }
        programsPane.add(mealsBox, 2, row);

        row++;
    }

    return programsPane;
}

    private GridPane myProgress() {

        Label currentWeightLabel = new Label("Current Weight:");
        Label newWeightLabel = new Label("Enter New Weight:");
        Label DietplanLabel = new Label("Diet plan");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Bulk", "Cut");
        typeComboBox.setValue("Cut");
        Label currentWeightValue = new Label(String.format("%.2f kg", this.getCurrentWeight()));
        TextField newWeightField = new TextField();
        newWeightField.setPromptText("enter new wieght");
        Button updateButton = new Button("Update");

        updateButton.setOnAction(e -> {
            String newWeightText = newWeightField.getText();

            if (newWeightText == null || newWeightText.trim().isEmpty()) {
                GUI.PopUp.showError("Weight cannot be empty");
                return;
            }

            try {
                double newWeight = Double.parseDouble(newWeightText);
                if (newWeight <= 0) {
                    GUI.PopUp.showError("Weight must be a positive number");
                    return;
                }

                String selectedPlan = typeComboBox.getValue(); // Get the selected plan (Cut or Bulk)
                double currentWeight = this.getCurrentWeight();

                if ("Cut".equals(selectedPlan)) {
                    if (newWeight >= currentWeight) {
                        GUI.PopUp.showError("You need to stick more to the diet plan");
                        this.setCurrentWeight(newWeight);
                        currentWeightValue.setText(String.format("%.2f kg", newWeight));
                        return;
                    } else {
                        GUI.PopUp.showMessage("Good job");
                        this.setCurrentWeight(newWeight);
                        currentWeightValue.setText(String.format("%.2f kg", newWeight));
                        newWeightField.clear();
                    }
                } else if ("Bulk".equals(selectedPlan)) {
                    if (newWeight <= currentWeight) {
                        GUI.PopUp.showError("You need to stick more to the diet plan");
                        this.setCurrentWeight(newWeight);
                        currentWeightValue.setText(String.format("%.2f kg", newWeight));
                        newWeightField.clear();

                        return;
                    } else {
                        GUI.PopUp.showMessage("Good job");
                        this.setCurrentWeight(newWeight);
                        currentWeightValue.setText(String.format("%.2f kg", newWeight));
                        newWeightField.clear();

                    }
                }

            } catch (NumberFormatException ex) {
                GUI.PopUp.showError("Please enter a valid number");
            }
        });

        // Main Layout
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(currentWeightLabel, 0, 0);
        gridPane.add(currentWeightValue, 1, 0);
        gridPane.add(newWeightLabel, 0, 1);
        gridPane.add(newWeightField, 1, 1);
        gridPane.add(DietplanLabel, 0, 3);
        gridPane.add(typeComboBox, 1, 3);

        gridPane.add(updateButton, 0, 4);

        return gridPane;
    }

    public Scene homePage() {

        BorderPane borderPane = new BorderPane();

        Label welcomeLabel = new Label("Welcome, " + getUsername());
        welcomeLabel.setStyle("-fx-font-size: 30px; -fx-padding: 10px;");

        StackPane topPane = new StackPane(welcomeLabel);
        topPane.setAlignment(Pos.CENTER);
        topPane.setStyle("-fx-padding: 10px; -fx-background-color: #e0e0e0;");
        borderPane.setTop(topPane);

        VBox buttonBox = new VBox(10);
        buttonBox.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0;");

        Button viewProfileButton = new Button("My Profile");
        viewProfileButton.setOnAction((t) -> {
            borderPane.setCenter(viewProfile());
        });

        Button viewProgramsButton = new Button("My Programs");
        viewProgramsButton.setOnAction((t) -> {
            borderPane.setCenter(viewPrograms());
        });

        Button myProgressButton = new Button("My Progress");
        myProgressButton.setOnAction((t) -> {
            borderPane.setCenter(myProgress());

        });

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction((t) -> {
            fitnessapp.FitnessApp.stage.setScene(new GUI.Login_Signup(new HBox()));
        });

        buttonBox.getChildren().addAll(viewProfileButton, myProgressButton, viewProgramsButton, logoutButton);
        borderPane.setLeft(buttonBox);

        Label centerLabel = new Label("Welcome back");
        centerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        borderPane.setCenter(centerLabel);

        Scene scene = new Scene(borderPane, 600, 400);
        return scene;
    }

    @Override
    public String toString() {
        return "Client{" + "programs=" + programs + ", subscriptionEndDate=" + subscriptionEndDate + ", currentWeight=" + currentWeight + '}';
    }

}
