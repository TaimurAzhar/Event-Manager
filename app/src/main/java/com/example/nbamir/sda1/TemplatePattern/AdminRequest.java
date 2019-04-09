package com.example.nbamir.sda1.TemplatePattern;

import android.app.Activity;
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
import com.example.nbamir.sda1.EventBoard.RecyclerItemClickListener;
import com.example.nbamir.sda1.EventMaker.Event;
import com.example.nbamir.sda1.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class AdminRequest extends RequestedEventsTemplate {

    @Override
    public void getViews() {
        pendingText = (TextView) activity.findViewById(R.id.Pending);
        pendingCount  = (TextView) activity.findViewById(R.id.pendingCount);
        pendingRecycler =(RecyclerView) activity.findViewById(R.id.recycler_pending);

        addButton = (FloatingActionButton) activity.findViewById(R.id.add);
        deleteButton = (FloatingActionButton) activity.findViewById(R.id.delete);
        postButton = (FloatingActionButton) activity.findViewById(R.id.post);
        linearLayout =(LinearLayout) activity.findViewById(R.id.Controls);
        linearLayout.setVisibility(View.VISIBLE);

        pendingRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(activity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        AndroidDataAdapter.position=i;
                    }
                })
        );
    }

    @Override
    public void showpending(String uid) {
        System.out.println("showpendingvaries: All pending student events and admin's events displayed.");
        pendingText.setVisibility(View.VISIBLE);
        pendingCount.setVisibility(View.VISIBLE);
        pendingRecycler.setVisibility(View.VISIBLE);
        //filter list by pending of students and this admin;
        if(pendingList!=null){
            pendingList.clear();
        }
        Database db = new Database();
        DatabaseReference mDatabase = db.getDatabase();
        mDatabase.child("Pending Events").child("admin").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Event event = dataSnapshot.getValue(Event.class);
                Log.d("1", "getlist:" + event.name+event.category+event.venue+event.image);
                pendingList.add(event);

                EventBoardSingleton pendingBoard=EventBoardSingleton.getInstance();
                pendingBoard.initEventPanels(activity,pendingRecycler,pendingList);
                pendingCount.setText(pendingList.size()+" Results");
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

                    Event event = data.getValue(Event.class);
                    Log.d("1", "getliststudent:" + data);
                    pendingList.add(event);
                    EventBoardSingleton pendingBoard=EventBoardSingleton.getInstance();
                    pendingBoard.initEventPanels(activity,pendingRecycler,pendingList);
                    pendingCount.setText(pendingList.size()+" Results");
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

    @Override
    public void showDeleteButton(String id) {
        uid=id;
        System.out.println("showDeleteButtonvaries: Remove pending event from database and add to disapproved.");
        deleteButton.show();
        //change its onclick

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db = new Database();
                final DatabaseReference mDatabase = db.getDatabase();
                Log.d("thisworks",""+uid);
                mDatabase.child("Pending Events").child("admin").child(uid).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Event event = dataSnapshot.getValue(Event.class);
                        Log.d("thisworks2",""+uid+dataSnapshot.getKey());

                        if(event.name.equals(AndroidDataAdapter.name)){
                            mDatabase.child("Pending Events").child("admin").child(uid).child(dataSnapshot.getKey()).removeValue();
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

                            Event event = data.getValue(Event.class);
                            if(event.name.equals(AndroidDataAdapter.name)){
                                mDatabase.child("Pending Events").child("student").child(event.uid).child(data.getKey()).removeValue();
                                mDatabase.child("Disapproved Events").child(event.uid).child(data.getKey()).setValue(event);
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
    }

    //Hook Methods
    @Override
    public boolean disableApproved() { //override in admin, outsider
        return false;
    }

    @Override
    public boolean disableDisApproved() { //override in admin, outsider
        return false;
    }
}