package com.example.nbamir.sda1.EventMaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nbamir.sda1.Accounts.SignInActivity;
import com.example.nbamir.sda1.Database.Database;
import com.example.nbamir.sda1.EventBoard.EventBoardSingleton;
import com.example.nbamir.sda1.NavigationDrawer.NavigationDrawer;
import com.example.nbamir.sda1.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class EventPageActivity extends NavigationDrawer {

    private TextView nameView; //-
    private TextView ownerView; //-
    private TextView dateView; //-
    private TextView durationView;//-
    private TextView categoryTimeView;//-
    private TextView venueView; //-
    private TextView descriptionView; //-
    private TextView publishedByView; //-
    private ImageView imageView;

    Calendar calendar1,calendar2;
    DateFormat df;
    EventBoardSingleton eventBoardSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_event_page, contentFrameLayout);

        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");
        long startTime = intent.getLongExtra("startTime",0);
        long endTime = intent.getLongExtra("endTime",0);
        String category = intent.getStringExtra("category");
        String time = intent.getStringExtra("time");
        String heldBy = intent.getStringExtra("heldBy");
        String poster = intent.getStringExtra("poster");
        String venue = intent.getStringExtra("venue");
        String description = intent.getStringExtra("description");

        eventBoardSingleton= EventBoardSingleton.getInstance();

        nameView = findViewById(R.id.Name);
        ownerView= findViewById(R.id.eventOwner);
        dateView = findViewById(R.id.Date);
        durationView = findViewById(R.id.duration);
        categoryTimeView = findViewById(R.id.CategoryTime);
        venueView = findViewById(R.id.Venue);
        descriptionView = findViewById(R.id.Description);
        imageView = findViewById(R.id.Image);

        calendar1 = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        calendar1.setTimeInMillis(startTime);
        calendar2.setTimeInMillis(endTime);


        df =  new SimpleDateFormat("HH:mm");

        Picasso.get().load(image).into(imageView);
        nameView.setText(name);
        dateView.setText("Date: "+date);
        ownerView.setText("Held By: "+heldBy);
        durationView.setText("Duration: "+df.format(calendar1.getTime())+" - "+df.format(calendar2.getTime()));
        categoryTimeView.setText(category+" - "+time);
        venueView.setText(venue);
        descriptionView.setText(description);
    }
}

