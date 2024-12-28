package GUI;

import static GUI.PopUp.showError;
import static fitnessapp.FitnessApp.stage;
import fitnessapp.User;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class Login_Signup extends Scene {

    private Parent parent;

    private GridPane login() {
        GridPane gridpane = new GridPane();
        Label login_l = new Label("Login ");
        login_l.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        Label username_l = new Label("Usernaeme: ");
        Label password_l = new Label("Password: ");

        TextField username_t = new TextField();
        username_t.setFocusTraversable(false);
        username_t.setPromptText("enter username");
        PasswordField password_t = new PasswordField();
        password_t.setFocusTraversable(false);
        password_t.setPromptText("enter password");

        Button login_b = new Button("Login");

        login_b.setOnAction((t) -> {

            String username = username_t.getText();
            String password = password_t.getText();
            User u = fitnessapp.User.login(username, password);
            if (u == null) {
                PopUp.showError("Wrong usernmae or password");
                return;

            }
            if (username == null || username.trim().isEmpty()) {
                GUI.PopUp.showError("Username cannot be empty");
                return;
            }

            if (password == null || password.trim().isEmpty()) {
                GUI.PopUp.showError("Password cannot be empty");
                return;
            }

            fitnessapp.User user = User.login(username, password);
            if (u == null) {
                showError("Wrong username or password");
            } else {
                stage.setScene(u.homePage());
            }
        });

        Button signup_b = new Button("Signup");
        signup_b.setOnAction((t) -> {
            TranslateTransition transition = new TranslateTransition(Duration.seconds(1), parent);
            transition.setFromX(0);
            transition.setToX(-895);
            transition.play();
        });

        gridpane.add(login_l, 1, 0);
        gridpane.add(username_l, 0, 1);
        gridpane.add(password_l, 0, 2);
        gridpane.add(username_t, 1, 1);
        gridpane.add(password_t, 1, 2);
        gridpane.add(login_b, 0, 3);
        gridpane.add(signup_b, 2, 3);

        gridpane.setHgap(10);
        gridpane.setVgap(20);
        gridpane.setAlignment(Pos.CENTER);
        gridpane.setMinWidth(400);
        gridpane.setPadding(new Insets(-100, 0, 0, 0));

        return gridpane;
    }

    private GridPane signup() {
        GridPane gridpane = new GridPane();
        Label signup_l = new Label("Signup ");
        signup_l.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        Label username_l = new Label("Username: ");
        Label password_l = new Label("Password: ");
        Label gender_l = new Label("Gender: ");
        Label email_l = new Label("Email: ");
        Label age_l = new Label("Age: ");
        Label subsctibtion_l = new Label("Subsctibtion: ");
        Label currentweight_l = new Label("Current weight (kg): ");

        TextField username_t = new TextField("");
        username_t.setPromptText("enter username");

        PasswordField password_t = new PasswordField();
        password_t.setPromptText("enter password");

        TextField age_t = new TextField();
        age_t.setPromptText("enter age");

        TextField email_t = new TextField("");
        email_t.setPromptText("enter email");
        
        
        ComboBox<String> gender_cb = new ComboBox<>();

        gender_cb.getItems().addAll("Male", "Female");
        gender_cb.setValue("Male");

        ComboBox<String> subscription_cb = new ComboBox<>();
        subscription_cb.getItems().addAll("1-month", "3-month", "6-month", "12-month");
        subscription_cb.setValue("1-month");

        TextField currentweight_t = new TextField();
        currentweight_t.setPromptText("enter weight");

        Button login_b = new Button("Login");
        login_b.setOnAction((t) -> {
            TranslateTransition transition = new TranslateTransition(Duration.seconds(1), parent);
            transition.setFromX(-895);
            transition.setToX(0);
            transition.play();
        });
        Button signup_b = new Button("Signup");
        signup_b.setOnAction((t) -> {
            String username = username_t.getText();
            String password = password_t.getText();
            String gender = gender_cb.getValue();
            String ageText = age_t.getText();
            String subscriptionText = subscription_cb.getValue();
            String email = email_t.getText();
            double currentweight = Double.parseDouble(currentweight_t.getText());

            if (username.isEmpty() || password.isEmpty() || gender == null || ageText.isEmpty() || subscriptionText == null) {
                PopUp.showError("Please fill in all fields.");
            } else {
                try {
                    int age = Integer.parseInt(ageText);
                    boolean isMale = gender.equals("Male");

                    boolean isRegistered = fitnessapp.Client.signup(isMale, age, username, password, currentweight, subscriptionText,email);
                    if (isRegistered) {
                        PopUp.showMessage("Signup successful!");
                    } else {
                        PopUp.showError("Username already taken or other registration error.");
                    }
                } catch (NumberFormatException e) {
                    PopUp.showError("Age must be a valid number.");
                }
            }
        });
        gridpane.add(signup_l, 1, 0);
        gridpane.add(username_l, 0, 1);
        gridpane.add(password_l, 0, 2);
        gridpane.add(age_l, 0, 3);
        gridpane.add(email_l, 0, 4);
        gridpane.add(gender_l, 0, 5);
        gridpane.add(currentweight_l, 0, 6);

        gridpane.add(subsctibtion_l, 0, 7);
        gridpane.add(username_t, 1, 1);
        gridpane.add(password_t, 1, 2);
        gridpane.add(age_t, 1, 3);
        gridpane.add(email_t, 1, 4);
        gridpane.add(gender_cb, 1, 5);
        gridpane.add(currentweight_t, 1, 6);

        gridpane.add(subscription_cb, 1, 7);
        gridpane.add(login_b, 0, 8);
        gridpane.add(signup_b, 2, 8);

        gridpane.setHgap(10);
        gridpane.setVgap(20);
        gridpane.setAlignment(Pos.CENTER);
        gridpane.setMinWidth(400);
        gridpane.setPadding(new Insets(-100, 0, 0, 0));

        return gridpane;
    }

    public Login_Signup(HBox parent) {
        super(parent);
        this.parent = parent;

        ImageView image = new ImageView("./img/login_signup.png");
        parent.getChildren().addAll(login(), image, signup());
    }

}
