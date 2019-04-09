package com.example.nbamir.sda1.Accounts;

import com.google.firebase.database.DatabaseReference;

public class Outsider implements User {

    public String name, key,email,password;

    public Outsider(String key, String name,String email,String password) {
        this.key = key;
        this.name = name;
        this.email=email;
        this.password=password;
    }

    @Override
    public void addToDB(DatabaseReference outRef) {
        outRef.child(this.key).child("name").setValue(this.name);
        outRef.child(this.key).child("email").setValue(this.email);
        outRef.child(this.key).child("password").setValue(this.password);
        outRef.child(this.key).child("type").setValue("outsider");

    }
}
