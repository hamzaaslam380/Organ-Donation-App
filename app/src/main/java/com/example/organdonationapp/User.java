package com.example.organdonationapp;

public class User {
    String Name;
    String Email;
    String Phone;
    String Password;
    String Type;

    public User(String name, String email, String phone, String password, String type) {
        Name = name;
        Email = email;
        Phone = phone;
        Password = password;
        Type = type;
    }

    public User() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
