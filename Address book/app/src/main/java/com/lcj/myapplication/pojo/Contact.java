package com.lcj.myapplication.pojo;

import java.io.Serializable;

public class Contact implements Serializable {
    private String letter;
    private String name;
    private String number;

    public Contact() {
    }

    public Contact(String letter, String name, String number) {
        this.letter = letter;
        this.name = name;
        this.number = number;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "letter='" + letter + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
