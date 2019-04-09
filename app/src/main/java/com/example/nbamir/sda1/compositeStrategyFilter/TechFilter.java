package com.example.nbamir.sda1.compositeStrategyFilter;

import android.util.Log;

import com.example.nbamir.sda1.EventMaker.Event;

import java.util.ArrayList;

public class TechFilter extends FilterStrategy {

    @Override
    public ArrayList<Event> filter(ArrayList<Event> eventList) {
        Log.d("filteredList","initially"+filteredList.toString());
        if(filteredList!=null){
            filteredList.clear();

        }

        for(Event e:eventList){
            if(e.category.equals("Tech")){
                filteredList.add(e); //check this
                Log.d("filteredList","addedtotemp"+e.toString());

            }
        }

        return filteredList;
    }
}
