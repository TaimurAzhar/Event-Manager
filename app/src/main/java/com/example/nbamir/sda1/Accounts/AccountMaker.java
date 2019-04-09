package com.example.nbamir.sda1.Accounts;

import com.google.firebase.database.DatabaseReference;

public class AccountMaker {
    Admin admin;
    Outsider outsider;
    Student student;

    public void makeAdmin(String key, String id, String name, String email, String password, DatabaseReference adminRef) {
        admin = new Admin(key, id, name,email,password);
        admin.addToDB(adminRef);
    }

    public void makeStudent(String key, String ID, String name, int batch, String school,String email,String password,DatabaseReference studRef) {
        Student student = new Student(key, ID, name, batch, school,email,password);
        student.addToDB(studRef);
    }

    public void makeOutsider(String key, String name,String email,String password,DatabaseReference outRef) {
        outsider = new Outsider(key,name,email,password);
        outsider.addToDB(outRef);
    }
}
/*
*
*
* package com.example.nbamir.sda1.TemplatePattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nbamir.sda1.Database.Database;
import com.example.nbamir.sda1.EventBoard.AndroidDataAdapter;
import com.example.nbamir.sda1.EventBoard.EventBoardSingleton;
import com.example.nbamir.sda1.EventMaker.AddEventActivity;
import com.example.nbamir.sda1.EventMaker.Event;
import com.example.nbamir.sda1.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public abstract class RequestedEventsTemplate {

    TextView pendingText;
    TextView pendingCount;
    TextView approvedText;
    TextView approvedCount;
    TextView disapprovedText;
    TextView disapprovedCount;
    TextView controlsText;
    RecyclerView pendingRecycler;
    RecyclerView approvedRecycler;
    RecyclerView disapprovedRecycler;
    FloatingActionButton addButton;
    FloatingActionButton deleteButton;
    FloatingActionButton postButton;
    EventBoardSingleton eventBoardSingleton;
    ArrayList<Event> pendingList=new ArrayList<>(),approvedList=new ArrayList<>(),disapprovedList=new ArrayList<>();;
    Activity activity;
    Database db;
    String uid;
    LinearLayout linearLayout;
    public String key;

    //	//hooks
    private boolean isApprovedEnabled = true; //enabled for students
    private boolean isDisApprovedEnabled = true; //enabled for students
    private boolean isPostEnabled = true; //enabled for admin
    private boolean isFeatureEnabled = true; //enabled for admin,student

    public RequestedEventsTemplate(){
        isApprovedEnabled=disableApproved();
        isDisApprovedEnabled=disableDisApproved();
        isPostEnabled=disablePost();
        isFeatureEnabled=disableFeature();
    }


    public final void run(Activity activity,String uid){
        getActivity(activity);
        getViews();
        showpending(uid);
        showapproved(uid);
        showdisapproved(uid);
        showAddButton();
        showDeleteButton(uid);
        showPostButton();
    }

    public void getActivity(Activity activity){
        this.activity=activity;
    }

    public abstract void getViews();//varies

    public abstract void showpending(String uid); //varies

    public void showapproved(String uid){
        if(isApprovedEnabled){
            approvedText.setVisibility(View.VISIBLE);
            approvedCount.setVisibility(View.VISIBLE);
            approvedRecycler.setVisibility(View.VISIBLE);

            if(approvedList!=null){
                approvedList.clear();
            }
            Database db = new Database();
            DatabaseReference mDatabase = db.getDatabase();
            mDatabase.child("Approved Events").child(uid).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Event event = dataSnapshot.getValue(Event.class);
                    Log.d("1", "getlist:" + event.name+event.category+event.venue+event.image);
                    approvedList.add(event);
                    EventBoardSingleton approvedBoard=EventBoardSingleton.getInstance();
                    approvedBoard.initEventPanels(activity,approvedRecycler,approvedList);
                    approvedCount.setText(approvedList.size()+" Results");
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void showdisapproved(String uid){
        if(isDisApprovedEnabled){
            System.out.println("showdisapproved.");
            disapprovedText.setVisibility(View.VISIBLE);
            disapprovedCount.setVisibility(View.VISIBLE);
            disapprovedRecycler.setVisibility(View.VISIBLE);

            if(disapprovedList!=null){
                disapprovedList.clear();
            }
            Database db = new Database();
            DatabaseReference mDatabase = db.getDatabase();
            mDatabase.child("Disapproved Events").child(uid).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Event event = dataSnapshot.getValue(Event.class);
                    Log.d("1", "getlist:" + event.name+event.category+event.venue+event.image);
                    disapprovedList.add(event);
                    EventBoardSingleton disapprovedBoard=EventBoardSingleton.getInstance();
                    disapprovedBoard.initEventPanels(activity,disapprovedRecycler,disapprovedList);
                    disapprovedCount.setText(disapprovedList.size()+" Results");
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void showAddButton(){
        if(isFeatureEnabled){
            System.out.println("showAddButton: Add to own pending list");
            addButton.show();
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                    Intent intent = new Intent(activity,AddEventActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
    }

    public abstract void showDeleteButton(String uid); //varies

    public void showPostButton(){
        if(isPostEnabled) {

            System.out.println("showPostButton.");
            postButton.show();
            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db = new Database();
                    final DatabaseReference mDatabase = db.getDatabase();
                    //Admin
                    mDatabase.child("Pending Events").child("admin").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            for(DataSnapshot data:dataSnapshot.getChildren()){
                                key=data.getKey();

                                Log.d("thispost1",""+data.getKey()+key);
                                Event event = data.getValue(Event.class);
                                if(event.name.equals(AndroidDataAdapter.name)){
                                    db.addEvent(event,mDatabase.child("Events"));
                                    Log.d("thispost2",data.getKey()+key +"event"+event.uid);

                                    mDatabase.child("Pending Events").child("admin").child(event.uid).child(key).removeValue();

                                }
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    mDatabase.child("Pending Events").child("student").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            for(DataSnapshot data:dataSnapshot.getChildren()){
                                Log.d("getevents",""+data);
                                Event event = data.getValue(Event.class);
                                if(event.name.equals(AndroidDataAdapter.name)){
                                    db.addEvent(event,mDatabase.child("Events"));
                                    mDatabase.child("Approved Events").child(event.uid).child(data.getKey()).setValue(event);
                                    mDatabase.child("Pending Events").child("student").child(event.uid).child(data.getKey()).removeValue();
                                }
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            });
        }}

    //hook methods
    public boolean disableApproved() { //override in admin, outsider
        return true;
    }

    public boolean disableDisApproved() { //override in admin, outsider
        return true;
    }

    public boolean disablePost() { //override in student, outsider
        return true;
    }

    public boolean disableFeature() { //override in outsider
        return true;
    }

}*/