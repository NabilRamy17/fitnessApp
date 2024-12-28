/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fitnessapp;

import java.io.Serializable;
import java.util.ArrayList;
public class Meal implements Serializable {

    private String name;
    private ArrayList<String> ingrediants;
    private double calories;
    private String MealType;

    public Meal(String name, ArrayList<String> ingrediants, double calories, String MealTpe) {
        this.name = name;
        this.ingrediants = ingrediants;
        this.calories = calories;
        this.MealType = MealTpe;
    }

    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getIngrediants() {
        return ingrediants;
    }

    public void setIngrediants(ArrayList<String> ingrediants) {
        this.ingrediants = ingrediants;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public String getMealTpe() {
        return MealType;
    }

    public void setMealTpe(String MealTpe) {
        this.MealType = MealTpe;
    }

    @Override
    public String toString() {
        return "Meal{" + "name=" + name + ", ingrediants=" + ingrediants + ", calories=" + calories + ", MealTpe=" + MealType + '}';
    }
    
    
    

   

   
}
