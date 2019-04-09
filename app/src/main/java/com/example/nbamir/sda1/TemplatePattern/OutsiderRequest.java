package com.example.nbamir.sda1.TemplatePattern;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.nbamir.sda1.R;

public class OutsiderRequest extends RequestedEventsTemplate{

    @Override
    public void getViews() {
        pendingText = (TextView) activity.findViewById(R.id.Pending);
        deleteButton = (FloatingActionButton) activity.findViewById(R.id.delete);
        controlsText = (TextView) activity.findViewById(R.id.Unavaiable);

    }

    @Override
    public void showpending(String uid) {
        pendingText.setVisibility(View.VISIBLE);
        pendingText.setText("This Feature is not available for NonNustians.");
    }

    @Override
    public void showDeleteButton(String uid) {
        deleteButton.hide();
        controlsText.setVisibility(View.VISIBLE);
    }

    //Hook methods
    @Override
    public boolean disableApproved() { //override in admin, outsider
        return false;
    }

    @Override
    public boolean disableDisApproved() { //override in admin, outsider
        return false;
    }

    @Override
    public boolean disablePost() { //override in student, outsider
        return false;
    }

    @Override
    public boolean disableFeature() { //override in outsider
        return false;
    }

}