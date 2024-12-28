/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fitnessapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

public abstract class User implements Serializable {

    private boolean gender;
    private int age;
    private String username;
    private String password;
    private String email;

    public User(boolean gender, int age, String username, String password , String email) {
        this.gender = gender;
        this.age = age;
        this.username = username;
        this.password = password;
        this.email=email;
    }

    
    
    
    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   

    public static User login(String username, String password) {
        ArrayList<User> users = readUsers();
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public static void writeToFile(Object object, String filePath) {
        try {
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(new File(filePath)));
            o.writeObject(object);
            o.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static Object readFromFile(String filePath) {
        Object object = null;

        try {
            ObjectInputStream i = new ObjectInputStream(new FileInputStream(new File(filePath)));
            object = i.readObject();
            i.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return object;
    }

    public static ArrayList<User> readUsers() {
        Object object = readFromFile("users.bin");

        if (object == null) {
            return new ArrayList<User>();
        } else {
            return (ArrayList<User>) object;
        }
    }

    public static ArrayList<Meal> readMeals() {
        Object object = readFromFile("meals.bin");

        if (object == null) {
            return new ArrayList<Meal>();
        } else {
            return (ArrayList<Meal>) object;
        }
    }

    public static ArrayList<Workout> readWorkouts() {
        Object object = readFromFile("workouts.bin");

        if (object == null) {
            return new ArrayList<Workout>();
        } else {
            return (ArrayList<Workout>) object;
        }
    }

    public static ArrayList<Program> readPrograms() {
        Object object = readFromFile("programs.bin");

        if (object == null) {
            return new ArrayList<Program>();
        } else {
            return (ArrayList<Program>) object;
        }
    }

    public static void saveUserUpdates(User user) {
        ArrayList<User> users = readUsers();
        ArrayList<User> updatedUsers = new ArrayList<>();
        for (User u : users) {
            if (!user.getUsername().equals(u.getUsername())) {
                updatedUsers.add(u);
            } else {
                updatedUsers.add(user);
            }
        }
        writeToFile(updatedUsers, "users.bin");
    }

    public abstract Scene homePage();

    @Override
    public String toString() {
         String genderString = gender ? "Male" : "Female";
        return "User{" + "gender=" + genderString + ", age=" + age + ", username=" + username + ", password=" + password + '}';
    }

 

}
