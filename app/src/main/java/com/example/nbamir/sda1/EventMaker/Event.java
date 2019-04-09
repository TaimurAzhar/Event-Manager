package com.example.nbamir.sda1.EventMaker;

import android.graphics.drawable.shapes.Shape;
import android.support.constraint.solver.widgets.Rectangle;

import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;


abstract class events implements Cloneable {

    public String date; //-
    public long startTime;//-
    public long endTime;//-
    public String name; //-
    public String venue; //-
    public String category; //-
    public String image; //-
    public String time; //-
    public String poster; //published by name
    public String description; //-
    public String heldBy;//-
    public String uid;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeldBy() {
        return heldBy;
    }

    public void setHeldBy(String heldBy) {
        this.heldBy = heldBy;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Object clone() {
        Object clone = null;

        try {
            clone = super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return clone;
    }
}

public class Event extends events{

    public Event() {
    }

    public Event(String date, long startTime, long endTime, String name, String venue, String category, String image, String time, String poster, String description, String heldBy, String uid) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.venue = venue;
        this.category = category;
        this.image = image;
        this.time = time;
        this.poster = poster;
        this.description = description;
        this.heldBy = heldBy;
        this.uid = uid;
    }

    public void addEvent(DatabaseReference myRef) { //add to database package
        myRef.child("Events").push().setValue(this);
    }


}


class eventsCache {

    private static Hashtable<String, events> eventMap  = new Hashtable<String, events>();

    public static events getEvent(String eventId) {
        events cachedEvents = eventMap.get(eventId);
        return (events) cachedEvents.clone();
    }

    // for each shape run database query and create shape
    // shapeMap.put(shapeKey, shape);
    // for example, we are adding three shapes

    public static void loadCache() {
        Event event = new Event();
        event.setCategory("Business");
        eventMap.put(event.getCategory(),event);
        Event event1 = new Event();
        event1.setCategory("Recreational");
        eventMap.put(event1.getCategory(),event1);
        Event event2 = new Event();
        event2.setCategory("Outsiders");
        eventMap.put(event2.getCategory(),event2);
        Event event3 = new Event();
        event3.setCategory("Tech");
        eventMap.put(event3.getCategory(),event3);
    }
}
