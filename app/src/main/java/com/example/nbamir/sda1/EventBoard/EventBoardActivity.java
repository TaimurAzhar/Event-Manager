package com.example.nbamir.sda1.EventBoard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nbamir.sda1.Accounts.SignInActivity;
import com.example.nbamir.sda1.Database.Database;
import com.example.nbamir.sda1.EventMaker.Event;
import com.example.nbamir.sda1.NavigationDrawer.NavigationDrawer;
import com.example.nbamir.sda1.R;
import com.example.nbamir.sda1.compositeStrategyFilter.AfterUniversityFilter;
import com.example.nbamir.sda1.compositeStrategyFilter.BusinessFilter;
import com.example.nbamir.sda1.compositeStrategyFilter.CompositeFilter;
import com.example.nbamir.sda1.compositeStrategyFilter.DayFilter;
import com.example.nbamir.sda1.compositeStrategyFilter.FilterButton;
import com.example.nbamir.sda1.compositeStrategyFilter.OutsiderFilter;
import com.example.nbamir.sda1.compositeStrategyFilter.RecreationalFilter;
import com.example.nbamir.sda1.compositeStrategyFilter.TechFilter;
import com.example.nbamir.sda1.compositeStrategyFilter.UniversityHoursFilter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class EventBoardActivity extends NavigationDrawer {

    ArrayList<Event> eventList,filteredList;
    DatabaseReference mDatabase;
    Event event1,event2,event3,event4;
    RecyclerView mRecyclerView;
    EventBoardSingleton eventBoardSingleton;
    Context context;
    Dialog dialog1,dialog2,dialog3;
    ArrayList categoriesSelected,timeSelected;
    CompositeFilter categoryFilter,timeFilter,mixedFilter;
    BusinessFilter businessFilter;
    TechFilter techFilter;
    OutsiderFilter outsiderFilter;
    RecreationalFilter recreationalFilter;
    AfterUniversityFilter afterUniversityFilter;
    UniversityHoursFilter universityHoursFilter;
    DayFilter dayFilter;
    FilterButton filterButton;
    TextView resultsView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_event_board, contentFrameLayout);

        createDialogs();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_event_board);
        Database db = new Database();
        mDatabase=db.getDatabase();

        eventBoardSingleton = EventBoardSingleton.getInstance();
        eventList=eventBoardSingleton.getEventList();

        context=getApplicationContext();
        getEvents(mDatabase);

        categoryFilter = new CompositeFilter();
        timeFilter = new CompositeFilter();
        mixedFilter = new CompositeFilter();

        resultsView = (TextView) findViewById(R.id.resultCount);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {

//                        Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                        AndroidDataAdapter.position=i;
                }
                })
        );
    }

    public void createDialogs(){
        String[] categories = {" Business", " Tech", " Recreational","Outsider"};
        String[] time = {" AfterUniversity", " UniversityHours", " Today"};

        //categoriesSelected = new ArrayList();
        timeSelected = new ArrayList();

        //Choice Dialogue
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Filter:");

        builder1.setPositiveButton("CATEGORY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Your logic when OK button is clicked
                dialog2.show();
            }
        })
                .setNegativeButton("TIME", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog3.show();
                    }
                })
                .setNeutralButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        filterButton=new FilterButton(mixedFilter);
                        filteredList=filterButton.executeStrategy(eventList);
                        eventBoardSingleton.initEventPanels(context,mRecyclerView,filteredList);
                        resultsView.setText(filteredList.size()+" Results");
                    }
                });

        dialog1 = builder1.create();


        //Categories Dialogue
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("Filter by Category:");

        builder2.setMultiChoiceItems(categories, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            //categoriesSelected.add(selectedItemId);
                            switch (selectedItemId){
                                case 0:{
                                    businessFilter = new BusinessFilter();
                                    categoryFilter.addFilter(businessFilter);
                                    Log.d("filteredList","business"+selectedItemId);
                                    break;
                                }
                                case 1:{
                                    techFilter = new TechFilter();
                                    categoryFilter.addFilter(techFilter);
                                    Log.d("filteredList","tech"+selectedItemId);
                                    break;
                                }
                                case 2:{
                                    recreationalFilter = new RecreationalFilter();
                                    categoryFilter.addFilter(recreationalFilter);
                                    Log.d("filteredList","recreational"+selectedItemId);
                                    break;
                                }
                                case 3:{
                                    outsiderFilter = new OutsiderFilter();
                                    categoryFilter.addFilter(outsiderFilter);
                                    Log.d("filteredList","outsider"+selectedItemId);
                                    break;
                                }
                            }
                        } else{
                            switch (selectedItemId){
                                case 0:{
                                    categoryFilter.removeFilter(businessFilter);
                                    Log.d("filteredList","business removed");

                                    break;
                                }
                                case 1:{
                                    categoryFilter.removeFilter(techFilter);
                                    Log.d("filteredList","tech removed");

                                    break;
                                }
                                case 2:{
                                    categoryFilter.removeFilter(recreationalFilter);
                                    Log.d("filteredList","recreational removed");

                                    break;
                                }
                                case 3:{
                                    categoryFilter.removeFilter(outsiderFilter);
                                    Log.d("filteredList","outsider removed");

                                    break;
                                }
                            }
                        }
                    }
                })
                .setNeutralButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Your logic when OK button is clicked
                        filterButton=new FilterButton(categoryFilter);
                        filteredList=filterButton.executeStrategy(eventList);
                        eventBoardSingleton.initEventPanels(context,mRecyclerView,filteredList);
                        resultsView.setText(filteredList.size()+" Results");
                    }
                })
                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mixedFilter.removeFilter(categoryFilter);

                        //Your logic when OK button is clicked
                        if(categoryFilter.filterList.size()!=0){
                            mixedFilter.addFilter(categoryFilter);
                        }
                        dialog1.show();
                    }
                });
        dialog2 = builder2.create();

        //Time Dialogue
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        builder3.setTitle("Filter by Time:");

        builder3.setMultiChoiceItems(time, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            switch (selectedItemId){
                                case 0:{
                                    afterUniversityFilter = new AfterUniversityFilter();
                                    timeFilter.addFilter(afterUniversityFilter);
                                    Log.d("filteredList","after"+selectedItemId);
                                    break;
                                }
                                case 1:{
                                    universityHoursFilter = new UniversityHoursFilter();
                                    timeFilter.addFilter(universityHoursFilter);
                                    Log.d("filteredList","uni"+selectedItemId);
                                    break;
                                }
                                case 2:{
                                    dayFilter = new DayFilter();
                                    timeFilter.addFilter(dayFilter);
                                    Log.d("filteredList","day"+selectedItemId);
                                    break;
                                }
                            }}
                        else {
                            switch (selectedItemId){
                                case 0:{
                                    timeFilter.removeFilter(afterUniversityFilter);
                                    Log.d("filteredList","afterremoved"+selectedItemId);
                                    break;
                                }
                                case 1:{
                                    timeFilter.removeFilter(universityHoursFilter);
                                    Log.d("filteredList","uniremoved"+selectedItemId);
                                    break;
                                }
                                case 2:{
                                    timeFilter.removeFilter(dayFilter);
                                    Log.d("filteredList","dayremoved"+selectedItemId);
                                    break;
                                }
                            }
                        }
                    }
                })
                .setNeutralButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Your logic when OK button is clicked
                        filterButton=new FilterButton(timeFilter);
                        filteredList=filterButton.executeStrategy(eventList);
                        eventBoardSingleton.initEventPanels(context,mRecyclerView,filteredList);
                        resultsView.setText(filteredList.size()+" Results");

                    }
                })
                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mixedFilter.removeFilter(timeFilter);
                        //Your logic when OK button is clicked
                        if(timeFilter.filterList.size()!=0){
                            mixedFilter.addFilter(timeFilter);
                        }
                        dialog1.show();
                    }
                });

        dialog3 = builder3.create();
    }

    private void getEvents(DatabaseReference mDatabase){
        eventList.clear();
        mDatabase.child("Events").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Event event = dataSnapshot.getValue(Event.class);
                Log.d("1", "getlist:" + event.startTime+event.endTime+event.venue+event.image);
                eventList.add(event);
                eventBoardSingleton.initEventPanels(context,mRecyclerView,eventList);
                resultsView.setText(eventList.size()+" Results");
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
