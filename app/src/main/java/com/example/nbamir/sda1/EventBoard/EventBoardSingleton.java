package com.example.nbamir.sda1.EventBoard;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.nbamir.sda1.EventMaker.Event;
import com.example.nbamir.sda1.R;

import java.util.ArrayList;

public class EventBoardSingleton extends AppCompatActivity {

    private static EventBoardSingleton instance;

    private ArrayList<Event> eventList=new ArrayList<>();

    private EventBoardSingleton(){}

    public static EventBoardSingleton getInstance(){
        if(instance==null){
            instance=new EventBoardSingleton();
        }
        return instance;
    }

    public ArrayList<Event> getEventList(){

        return eventList;
    }

    public void initEventPanels(Context context, RecyclerView mRecyclerView, ArrayList<Event> eventList) {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        AndroidDataAdapter mAdapter = new AndroidDataAdapter(context, eventList);
        mRecyclerView.setAdapter(mAdapter);
    }

}

