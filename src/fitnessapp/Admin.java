/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fitnessapp;

import java.io.Serializable;
import static java.lang.System.exit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class Admin extends User implements Serializable {

    public Admin(boolean gender, int age, String username, String password, String email) {
        super(gender, age, username, password, email);
    }

    public boolean addUser(int type, boolean gender, int age, String username, String password, double currentWeight, String subscription, String email) {
        ArrayList<User> users = readUsers();

        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }

        User user = null;

        if (type == 1) {
            user = new Trainer(gender, age, username, password, email);
        } else if (type == 2) {
            user = new Nutritionist(gender, age, username, password, email);
        } else {
            user = new Client(new ArrayList<Program>(), currentWeight, gender, age, username, password, subscription, email);
        }

        users.add(user);
        writeToFile(users, "users.bin");
        return true;
    }

    public void edituser(User user, String password) {
        ArrayList<User> users = readUsers();
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                u.setPassword(password);
            }
        }
        User.writeToFile(users, "users.bin");
    }

    public boolean removeUser(String username) {
        ArrayList<User> users = readUsers();
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User u = iterator.next();
            if (u.getUsername().equals(username)) {
                iterator.remove();
                writeToFile(users, "users.bin");
                return true;
            }
        }
        
        return false;
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

    public void editUserPage() {
        Scanner sc = new Scanner(System.in);
        ArrayList<User> users = readUsers();
        for (User u : users) {
            System.out.println(u);
        }
        System.out.println("Enter the username of the user you want to edit : ");
        String username = sc.next();
        User u = searchUser(username);
        if (u == null) {
            System.out.println("Username not found");
        } else {
            System.out.println("enter the new password");
            String password = sc.next();
            edituser(u, password);
        }

    }

    private HBox removeUser() {
        Label usernameLabel = new Label("Enter Username to Remove:");

        TextField usernameField = new TextField();

        Button removeButton = new Button("Remove User");

        removeButton.setOnAction(e -> {
            String username = usernameField.getText();
            if (username == null || username.trim().isEmpty()) {
                GUI.PopUp.showError("Username cannot be empty");
                return;
            }

            if (removeUser(username)) {
                GUI.PopUp.showMessage("User " + username + " removed successfully");
            } else {
                GUI.PopUp.showError("User " + username + " not found");
            }
        });

        HBox hbox = new HBox(10, usernameLabel, usernameField, removeButton);
        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        hbox.setStyle("-fx-padding: 10px;");

        return hbox;
    }

    private TableView<User> listUsers() {
        TableView<User> tableView = new TableView<>();

        try {

            TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

            TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
            passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

            TableColumn<User, Integer> ageColumn = new TableColumn<>("Age");
            ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

            TableColumn<User, Boolean> genderColumn = new TableColumn<>("Gender");
            genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
            genderColumn.setCellFactory(column -> new TableCell<User, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item ? "Male" : "Female");
                    }
                }
            });

            TableColumn<User, String> emailColumn = new TableColumn<>("Email");
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

            tableView.getColumns().addAll(usernameColumn, passwordColumn, ageColumn, genderColumn, emailColumn);

            ArrayList<User> usersList = readUsers();
            tableView.getItems().addAll(usersList);
        } catch (Exception e) {
            System.out.println(e);
        }
        return tableView;
    }

   

    private GridPane addUser() {

        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label ageLabel = new Label("Age:");
        Label emailLabel = new Label("email:");
        Label genderLabel = new Label("Gender:");
        Label userTypeLabel = new Label("User Type:");
        Label weightLabel = new Label("Current Weight (kg):");
        Label subscriptionLabel = new Label("Subscription Duration:");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter UserName");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        TextField ageField = new TextField();
        ageField.setPromptText("Enter Age");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");

        TextField weightField = new TextField();
        weightField.setPromptText("Enter Weight(Kg)");
        ComboBox<String> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female");
        genderComboBox.setValue("Male");

        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("Client", "Trainer", "Nutritionist");

        userTypeComboBox.setValue("Trainer");

        ToggleGroup subscriptionGroup = new ToggleGroup();
        RadioButton oneMonth = new RadioButton("1 month");
        RadioButton threeMonths = new RadioButton("3 months");
        RadioButton sixMonths = new RadioButton("6 months");
        RadioButton nineMonths = new RadioButton("9 months");
        RadioButton twelveMonths = new RadioButton("12 months");
        oneMonth.setSelected(true);

        oneMonth.setToggleGroup(subscriptionGroup);
        threeMonths.setToggleGroup(subscriptionGroup);
        sixMonths.setToggleGroup(subscriptionGroup);
        nineMonths.setToggleGroup(subscriptionGroup);
        twelveMonths.setToggleGroup(subscriptionGroup);

        VBox subscriptionBox = new VBox(5, oneMonth, threeMonths, sixMonths, nineMonths, twelveMonths);

        Button addUserButton = new Button("Add User");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(ageLabel, 0, 3);
        gridPane.add(ageField, 1, 3);
        gridPane.add(emailLabel, 0, 4);
        gridPane.add(emailField, 1, 4);
        gridPane.add(genderLabel, 0, 5);
        gridPane.add(genderComboBox, 1, 5);
        gridPane.add(userTypeLabel, 0, 6);
        gridPane.add(userTypeComboBox, 1, 6);

        gridPane.add(weightLabel, 0, 7);
        gridPane.add(weightField, 1, 7);
        gridPane.add(subscriptionLabel, 0, 8);
        gridPane.add(subscriptionBox, 1, 8);

        gridPane.add(addUserButton, 0, 9);

        weightLabel.setVisible(false);
        weightField.setVisible(false);
        subscriptionLabel.setVisible(false);
        subscriptionBox.setVisible(false);

        userTypeComboBox.setOnAction(e -> {
            String selectedType = userTypeComboBox.getValue();
            boolean isClient = "Client".equals(selectedType);

            weightLabel.setVisible(isClient);
            weightField.setVisible(isClient);
            subscriptionLabel.setVisible(isClient);
            subscriptionBox.setVisible(isClient);
        });

        addUserButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String ageText = ageField.getText();
            String gender = genderComboBox.getValue();
            String email = emailField.getText();

            boolean isMale = gender.equals("Male");

            String userType = userTypeComboBox.getValue();
            int userTypeInt = 0;
            if ("Trainer".equals(userType)) {
                userTypeInt = 1;
            } else if ("Nutritionist".equals(userType)) {
                userTypeInt = 2;
            } else if ("Client".equals(userType)) {
                userTypeInt = 3;
            }

            if (username == null || username.trim().isEmpty()) {
                GUI.PopUp.showError("Username cannot be empty");
                return;
            }

            if (password == null || password.trim().isEmpty()) {
                GUI.PopUp.showError("Password cannot be empty");
                return;
            }

            if (email == null || email.trim().isEmpty()) {
                GUI.PopUp.showError("email cannot be empty");
                return;
            }

            if (ageText == null || ageText.trim().isEmpty()) {
                GUI.PopUp.showError("Age cannot be empty");
                return;
            }
            int age = 0;
            try {
                age = Integer.parseInt(ageText);
                if (age <= 0) {
                    GUI.PopUp.showError("Age must be a positive number");
                    return;
                }
            } catch (NumberFormatException ex) {
                GUI.PopUp.showError("Age must be a valid number");
                return;
            }

            if (userType == null) {
                GUI.PopUp.showError("Please select a user type");
                return;
            }

            double currentWeight = 0;
            LocalDateTime endOfSubscription = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String endOfSubscriptionString = endOfSubscription.format(formatter);

            if ("Client".equals(userType)) {
                String weightText = weightField.getText();
                RadioButton selectedRadioButton = (RadioButton) subscriptionGroup.getSelectedToggle();

                if (weightText == null || weightText.trim().isEmpty()) {
                    GUI.PopUp.showError("Current weight cannot be empty");
                    return;
                }
                try {
                    currentWeight = Double.parseDouble(weightText);
                    if (currentWeight <= 0) {
                        GUI.PopUp.showError("Weight must be a positive number");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    GUI.PopUp.showError("Weight must be a valid number");
                    return;
                }

                if (selectedRadioButton == null) {
                    GUI.PopUp.showError("Please select a subscription duration");
                    return;
                }

                int months = Integer.parseInt(selectedRadioButton.getText().split(" ")[0]);
                endOfSubscription = LocalDateTime.now().plusMonths(months);
            }
            if (addUser(userTypeInt, isMale, age, username, password, currentWeight, endOfSubscriptionString, email)) {
                GUI.PopUp.showMessage(username + " created successfully as " + userType);
            } else {
                GUI.PopUp.showError("Username is taken");
            }

        });

        return gridPane;
    }

    private ArrayList<User> findUsers(String keyword) {

        ArrayList<User> users = readUsers();
        ArrayList<User> UserWithKeyword = new ArrayList<>();

        for (User p : users) {
            if (p.getUsername().toLowerCase().contains(keyword.toLowerCase())
                    || p.getPassword().toLowerCase().contains(keyword.toLowerCase())) {
                UserWithKeyword.add(p);
            }
        }

        return UserWithKeyword;
    }

    private VBox list(BorderPane borderPane) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        TextField search = new TextField();
        search.setPromptText("Search");

        search.setOnKeyTyped((t) -> {

            String keyword = search.getText();

            try {
                ArrayList<User> users = new ArrayList<>();
                users = findUsers(keyword);

                vbox.getChildren().remove(1, vbox.getChildren().size());
                vbox.getChildren().add(table(users, borderPane));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        vbox.getChildren().addAll(search, table(User.readUsers(), borderPane));

        return vbox;
    }

    public TableView<User> table(ArrayList<User> users, BorderPane borderPane) {
        TableView<User> tableView = new TableView<>();

        // Create table columns
        TableColumn<User, String> userTypeColumn = new TableColumn<>("User Type");
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
        TableColumn<User, String> ageColumn = new TableColumn<>("Age");
        TableColumn<User, String> genderColumn = new TableColumn<>("gender");
        TableColumn<User, String> emailColumn = new TableColumn<>("email");

        // Set up column mappings to User class properties
        userTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClass().getSimpleName()));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));;
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("Age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));;
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));;
        
         genderColumn.setCellValueFactory(cellData -> {
        boolean isMale = cellData.getValue().getGender();
        String gender = isMale ? "Male" : "Female";
        return new SimpleStringProperty(gender);
    });
        

        // Add the "More Details" and "Delete" columns to the table
        tableView.getColumns().addAll(userTypeColumn, usernameColumn, passwordColumn,ageColumn,genderColumn,emailColumn);

        // Add data to the table
        ObservableList<User> userList = FXCollections.observableArrayList(users);
        tableView.setItems(userList);

        return tableView;
    }

    @Override
    public Scene homePage() {
        BorderPane borderPane = new BorderPane();

        Label welcomeLabel = new Label("Welcome " + getUsername());
        welcomeLabel.setStyle("-fx-font-size: 30px; -fx-padding: 10px;");

        StackPane topPane = new StackPane(welcomeLabel);
        topPane.setAlignment(Pos.CENTER);
        topPane.setStyle("-fx-padding: 10px; -fx-background-color: #e0e0e0;");
        borderPane.setTop(topPane);

        VBox buttonBox = new VBox(15);
        buttonBox.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0;");

        Button addButton = new Button("Add User");
        addButton.setOnAction((t) -> {
            borderPane.setCenter(addUser());
        });

        Button searchButton = new Button("Search Users");
        searchButton.setOnAction((t) -> {
            borderPane.setCenter(list(borderPane));
        });

        Button listButton = new Button("List Users");
        listButton.setOnAction((t) -> {
            borderPane.setCenter(new VBox(listUsers(), removeUser()));

        });

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction((t) -> {
            fitnessapp.FitnessApp.stage.setScene(new GUI.Login_Signup(new HBox()));
        });

        buttonBox.getChildren().addAll(addButton, searchButton, listButton, logoutButton);
        borderPane.setLeft(buttonBox);
        Label centerLabel = new Label("Welcome back");
        centerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        borderPane.setCenter(centerLabel);

        Scene scene = new Scene(borderPane, 600, 400);
        return scene;
    }

    @Override
    public String toString() {
        return "Admin{" + "username=" + getUsername() + ", password=" + getPassword() + '}';
    }
}
