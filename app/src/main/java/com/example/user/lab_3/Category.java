package com.example.user.lab_3;

/**
 * Created by User on 22.11.2016.
 */

public class Category {
    private int id;
    private String name;

    public Category(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
