package com.example.nsignptapp.feature.display.domain.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayListModel implements Serializable {
    private ArrayList<ZoneModel> zones;
    private int duration;

    public PlayListModel(ArrayList<ZoneModel> zones, int duration) {
        this.zones = zones;
        this.duration = duration;
    }

    public ArrayList<ZoneModel> getZones() {
        return zones;
    }

    public int getDuration() {
        return duration;
    }
}
