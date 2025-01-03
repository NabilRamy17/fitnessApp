/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fitnessapp;

import java.io.Serializable;


public class Workout implements Serializable {

    private String name;
    private String type;

    public Workout(String name, String type) {
        this.name = name;
        this.type = type;
    }

  
    public String getName() {
        return name;
    }
   

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Workout{" + "name=" + name + ", type=" + type + '}';
    }

    
    


}
