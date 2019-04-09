package com.example.nbamir.sda1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.nbamir.sda1.Accounts.SignInActivity;
import com.example.nbamir.sda1.Database.Database;
import com.example.nbamir.sda1.EventBoard.EventBoardSingleton;
import com.example.nbamir.sda1.EventMaker.Event;
import com.example.nbamir.sda1.NavigationDrawer.NavigationDrawer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class BookmarksActivity extends NavigationDrawer {

    ArrayList<Event> eventList;
    DatabaseReference mDatabase;
    RecyclerView mRecyclerView;
    EventBoardSingleton eventBoardSingleton;
    Context context;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_bookmarks, contentFrameLayout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_bookmarks);

        Database db = new Database();
        mDatabase=db.getDatabase();

        eventBoardSingleton = EventBoardSingleton.getInstance();
        eventList=eventBoardSingleton.getEventList();

        context=getApplicationContext();
        eventBoardSingleton.initEventPanels(getApplicationContext(),mRecyclerView,eventList);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Log.d("UserFound",""+currentUser);
        }
        else{
            Log.d("NoUserFound",""+currentUser);
        }
    }
}

