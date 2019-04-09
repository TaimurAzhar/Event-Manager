package com.example.nbamir.sda1.Accounts;

import com.google.firebase.database.DatabaseReference;

public class Student implements User {
    public String name, school, ID, key,email,password;
    public int batch;

    public Student(String key, String ID, String name, int batch, String school,String email,String password) {
        this.key = key;
        this.ID = ID;
        this.name = name;
        this.batch = batch;
        this.school = school;
        this.email=email;
        this.password=password;
    }

    public void addToDB(DatabaseReference studRef) {
        studRef.child(this.key).child("ID").setValue(this.ID);
        studRef.child(this.key).child("name").setValue(this.name);
        studRef.child(this.key).child("batch").setValue(this.batch);
        studRef.child(this.key).child("school").setValue(this.school);
        studRef.child(this.key).child("email").setValue(this.email);
        studRef.child(this.key).child("password").setValue(this.password);
        studRef.child(this.key).child("type").setValue("student");
    }
}
