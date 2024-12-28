/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fitnessapp;

import static fitnessapp.User.readUsers;
import static fitnessapp.User.writeToFile;
import static java.lang.System.exit;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Nutritionist extends User {

    public Nutritionist(boolean gender, int age, String username, String password, String email) {
        super(gender, age, username, password, email);
    }

    public boolean addMeal(String name, ArrayList<String> ingrediants, double calories, String Mealtype) {
        ArrayList<Meal> meals = readMeals();

        for (Meal m : meals) {
            if (m.getName().equals(name)) {
                return false;
            }
        }

        Meal meal = new Meal(name, ingrediants, calories, Mealtype);
        meals.add(meal);
        writeToFile(meals, "meals.bin");
        return true;
    }

    public boolean editMeal(String name,String newname, ArrayList<String> ingrediants, double calories, String type) {
        ArrayList<Meal> Meals = readMeals();
        for (Meal u : Meals) {
            if (u.getName().equals(name)) {
                u.setName(newname);
                u.setCalories(calories);
                u.setIngrediants(ingrediants);
                u.setMealTpe(type);
                writeToFile(Meals, "meals.bin");
                return true;

            }
        }
        return false;

    }

    public boolean removeMeal(String name) {
        ArrayList<Meal> meals = readMeals();
        Iterator<Meal> iterator = meals.iterator();
        while (iterator.hasNext()) {
            Meal m = iterator.next();
            if (m.getName().equals(name)) {
                iterator.remove();
                writeToFile(meals, "meals.bin");
                return true;
            }
        }
        return false;
    }

    private HBox removeMealPane() {
        Label nameLabel = new Label("Enter name to Remove:");

        TextField nameField = new TextField();

        Button removeButton = new Button("Remove Meal");

        removeButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                GUI.PopUp.showError("Name cannot be empty");
                return;
            }

            if (removeMeal(name)) {
                GUI.PopUp.showMessage("Meal " + name + " removed successfully");
            } else {
                GUI.PopUp.showError("Meal " + name + " not found");
            }
        });

        HBox hbox = new HBox(10, nameLabel, nameField, removeButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-padding: 10px;");

        return hbox;
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

    private GridPane addMealPane() {
        Label nameLabel = new Label("Name:");
        Label caloriesLabel = new Label("Calories:");
        Label typeLabel = new Label("Type:");
        Label ingredientsLabel = new Label("Ingredients (comma-separated):");

        TextField nameField = new TextField();
        nameField.setPromptText("enter name");
        TextField caloriesField = new TextField();
        caloriesField.setPromptText("enter calories");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snack");
        typeComboBox.setValue("Lunch");

        TextField ingredientsField = new TextField();
        ingredientsField.setPromptText("enter ingredients");
        Button addMealButton = new Button("Add Meal");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(caloriesLabel, 0, 1);
        gridPane.add(caloriesField, 1, 1);
        gridPane.add(typeLabel, 0, 2);
        gridPane.add(typeComboBox, 1, 2);
        gridPane.add(ingredientsLabel, 0, 3);
        gridPane.add(ingredientsField, 1, 3);
        gridPane.add(addMealButton, 0, 4);

        addMealButton.setOnAction(e -> {
            String name = nameField.getText();
            String caloriesText = caloriesField.getText();
            String type = typeComboBox.getValue();
            String ingredientsText = ingredientsField.getText();

            if (name == null || name.trim().isEmpty()) {
                GUI.PopUp.showError("Name cannot be empty");
                return;
            }

            int calories;
            try {
                calories = Integer.parseInt(caloriesText);
                if (calories <= 0) {
                    GUI.PopUp.showError("Calories must be a positive number");
                    return;
                }
            } catch (NumberFormatException ex) {
                GUI.PopUp.showError("Calories must be a valid number");
                return;
            }

            if (type == null) {
                GUI.PopUp.showError("Please select a meal type");
                return;
            }

            ArrayList<String> ingredients = new ArrayList<>();
            if (ingredientsText != null && !ingredientsText.trim().isEmpty()) {
                String[] splitIngredients = ingredientsText.split(",");
                for (String ingredient : splitIngredients) {
                    ingredients.add(ingredient.trim());
                }
            }

            if (addMeal(name, ingredients, calories, type)) {
                GUI.PopUp.showMessage(name + " added successfully");
            } else {
                GUI.PopUp.showError("Meal with this name already exists");
            }
        });

        return gridPane;
    }

    private ArrayList<Meal> findMeal(String keyword) {

        ArrayList<Meal> meals = readMeals();
        ArrayList<Meal> MealWithKeyword = new ArrayList<>();

        for (Meal p : meals) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                MealWithKeyword.add(p);
            }
        }

        return MealWithKeyword;
    }

    private VBox list(BorderPane borderPane) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        TextField search = new TextField();
        search.setPromptText("Search");

        search.setOnKeyTyped((t) -> {

            String keyword = search.getText();

            try {
                ArrayList<Meal> meals = new ArrayList<>();
                meals = findMeal(keyword);

                vbox.getChildren().remove(1, vbox.getChildren().size());
                vbox.getChildren().add(table(meals, borderPane));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        vbox.getChildren().addAll(search, table(readMeals(), borderPane));

        return vbox;
    }

    public TableView<Meal> table(ArrayList<Meal> meals, BorderPane borderPane) {
        TableView<Meal> tableView = new TableView<>();

        // Create table columns
        TableColumn<Meal, String> MealTypeColumn = new TableColumn<>("Meal Type");
        TableColumn<Meal, String> MealnameColumn = new TableColumn<>("Mealname");
        TableColumn<Meal, String> MealcaloriesColumn = new TableColumn<>("calories");

        // Set up column mappings to User class properties
        MealTypeColumn.setCellValueFactory(new PropertyValueFactory<>("MealTpe"));
        MealnameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        MealcaloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));

        // Add the "More Details" and "Delete" columns to the table
        tableView.getColumns().addAll(MealTypeColumn, MealnameColumn, MealcaloriesColumn);

        // Add data to the table
        ObservableList<Meal> mealList = FXCollections.observableArrayList(meals);
        tableView.setItems(mealList);

        return tableView;
    }

    private TableView<Meal> listMeals() {
        TableView<Meal> tableView = new TableView<>();

        TableColumn<Meal, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Meal, String> caloriesColumn = new TableColumn<>("Calories");
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));

        TableColumn<Meal, String> typeColumn = new TableColumn<>("Meal Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("MealTpe"));

        TableColumn<Meal, String> ingredientsColumn = new TableColumn<>("Ingrediants");
        ingredientsColumn.setCellValueFactory(cellData -> {
            ArrayList<String> ingrediants = cellData.getValue().getIngrediants();
            return new SimpleStringProperty(String.join(", ", ingrediants));
        });

        tableView.getColumns().addAll(nameColumn, caloriesColumn, typeColumn, ingredientsColumn);

        ArrayList<Meal> mealsList = readMeals();
        tableView.getItems().addAll(mealsList);

        return tableView;
    }

    private GridPane EditMeal() {

        Label mealnameLabel = new Label("Enter Meal to edit:");

        TextField mealnameField = new TextField();
        mealnameField.setPromptText("enter meal name");

        Button editButton = new Button("Edit Meal");
        Label nameLabel = new Label("Name:");
        Label caloriesLabel = new Label("Calories:");
        Label typeLabel = new Label("Type:");
        Label ingredientsLabel = new Label("Ingredients (comma-separated):");

        TextField newnameField = new TextField();
        newnameField.setPromptText("enter new name");
        TextField caloriesField = new TextField();
        caloriesField.setPromptText("enter new calories");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snack");
        typeComboBox.setValue("Lunch");

        TextField ingredientsField = new TextField();
        ingredientsField.setPromptText("enter new ingredients");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(mealnameLabel, 0, 0);
        gridPane.add(mealnameField, 1, 0);

        gridPane.add(nameLabel, 0, 1);
        gridPane.add(newnameField, 1, 1);
        gridPane.add(caloriesLabel, 0, 3);
        gridPane.add(caloriesField, 1, 3);
        gridPane.add(typeLabel, 0, 4);
        gridPane.add(typeComboBox, 1, 4);
        gridPane.add(ingredientsLabel, 0, 5);
        gridPane.add(ingredientsField, 1, 5);
        gridPane.add(editButton, 1, 6);

        editButton.setOnAction(e -> {
            String name = mealnameField.getText();
            String newname = newnameField.getText();
            String caloriesText = caloriesField.getText();
            String type = typeComboBox.getValue();
            String ingredientsText = ingredientsField.getText();

            if (name == null || name.trim().isEmpty()) {
                GUI.PopUp.showError("Name cannot be empty");
                return;
            }

            int calories;
            try {
                calories = Integer.parseInt(caloriesText);
                if (calories <= 0) {
                    GUI.PopUp.showError("Calories must be a positive number");
                    return;
                }
            } catch (NumberFormatException ex) {
                GUI.PopUp.showError("Calories must be a valid number");
                return;
            }

            if (type == null) {
                GUI.PopUp.showError("Please select a meal type");
                return;
            }

            if (newname == null || newname.trim().isEmpty()) {
                GUI.PopUp.showError("New name cannot be empty");
                return;
            }
            Meal u = searchMeal(name);
            if (u == null) {
                GUI.PopUp.showError("Meal " + name + " not found");
            }

            ArrayList<String> ingredients = new ArrayList<>();
            if (ingredientsText != null && !ingredientsText.trim().isEmpty()) {
                String[] splitIngredients = ingredientsText.split(",");
                for (String ingredient : splitIngredients) {
                    ingredients.add(ingredient.trim());
                }
            }
            Meal m = searchMeal(newname);
            if (m != null) {
                GUI.PopUp.showError(name + " New name already taken");
                return;

            }
            if (editMeal(name,newname, ingredients, calories, type)) {
                GUI.PopUp.showMessage(name + " edited successfully");
             } else {
            GUI.PopUp.showError("Failed to edit meal. Please try again.");
        }
            
        }
        );

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

        Button addButton = new Button("Add Meal");
        addButton.setOnAction((t) -> {
            borderPane.setCenter(addMealPane());
        });

        Button saerchButton = new Button("Search Meals");
        saerchButton.setOnAction((t) -> {
            borderPane.setCenter(list(borderPane));

        });

        Button listButton = new Button("List Meals");
        listButton.setOnAction((t) -> {
            borderPane.setCenter(new VBox(listMeals(), removeMealPane()));
        });
        Button editButton = new Button("Edit Meals");
        editButton.setOnAction((t) -> {
            borderPane.setCenter(EditMeal());
        });
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction((t) -> {
            fitnessapp.FitnessApp.stage.setScene(new GUI.Login_Signup(new HBox()));
        });

        buttonBox.getChildren().addAll(addButton, saerchButton, listButton, editButton, logoutButton);
        borderPane.setLeft(buttonBox);

        Label centerLabel = new Label("Welcome to the Nutritionist Panel");
        centerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        borderPane.setCenter(centerLabel);

        return new Scene(borderPane, 600, 400);

    }

    @Override
    public String toString() {
        return "Nutritionist{" + "username=" + getUsername() + ", password=" + getPassword() + '}';
    }

}
