/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fitnessapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Program implements Serializable {

    private String title;
    private ArrayList<Workout> workouts;
    private ArrayList<Meal> meals;

    public Program(String title, ArrayList<Workout> workouts, ArrayList<Meal> meals) {
        this.title = title;
        this.workouts = workouts;
        this.meals = meals;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
    }

    @Override
    public String toString() {
        return "Program{" + "title=" + title + ", workouts=" + workouts + ", meals=" + meals + '}';
    }

}
