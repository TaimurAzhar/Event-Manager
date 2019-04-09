package com.example.nbamir.sda1.compositeStrategyFilter;

import com.example.nbamir.sda1.EventMaker.Event;

import java.util.ArrayList;

public abstract class FilterStrategy {

    ArrayList<Event> filteredList = new ArrayList<Event>();

    public abstract ArrayList<Event> filter(ArrayList<Event> eventList);

}
