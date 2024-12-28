/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package fitnessapp;

import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FitnessApp extends Application {

    private static Scanner sc = new Scanner(System.in);
    public static Stage stage;

  

    public static void main(String[] args) {
        System.out.println(User.readUsers());
        System.out.println(User.readMeals());
        System.out.println(User.readWorkouts());
        System.out.println(User.readPrograms());

        Admin a = new Admin(true, 20 ,"a", "a","admin@gmail.com");
        ArrayList<User> users = User.readUsers();
        boolean found = false;
        for (User u : users) {
            if (u.getUsername().equals(a.getUsername())) {
                found = true;
            }
        }
        if (!found) {
            users.add(a);
            User.writeToFile(users, "users.bin");
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setWidth(900);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.getIcons().add(new Image("./img/badge.jpg"));
        stage.setTitle("POPEYE'S GYM");
        stage.setScene(new GUI.Login_Signup(new HBox()));
        stage.show();
    }

}
