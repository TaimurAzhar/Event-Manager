package com.example.nbamir.sda1.Accounts;

import com.google.firebase.database.DatabaseReference;

public class Admin implements User {
    public String ID, name, key,email,password;

    public Admin(String key, String ID, String name,String email,String password) {
        this.ID = ID;
        this.key = key;
        this.name = name;
        this.email=email;
        this.password=password;
    }

    @Override
    public void addToDB(DatabaseReference adminRef) {
        adminRef.child(this.key).child("ID").setValue(this.ID);
        adminRef.child(this.key).child("name").setValue(this.name);
        adminRef.child(this.key).child("email").setValue(this.name);
        adminRef.child(this.key).child("password").setValue(this.password);
        adminRef.child(this.key).child("type").setValue("admin");
    }
}
