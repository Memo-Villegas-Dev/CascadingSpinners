package com.androidcodefinder.cascadingspinners;

/**
 * Created by Sumeet Jain on 18-12-2017.
 */

public class Nombre {

    private String nombre;

    public Nombre(){}

    public Nombre(String nombre){
        this.nombre = nombre;
    }

    public void setModel(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return this.nombre;
    }

}