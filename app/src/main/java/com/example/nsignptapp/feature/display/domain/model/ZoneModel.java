package com.example.nsignptapp.feature.display.domain.model;

import java.util.ArrayList;

public class ZoneModel {
    private ArrayList<DisplayModel> resources;
    public ZoneModel(ArrayList<DisplayModel> resources) {
        this.resources = resources;
    }

    public ArrayList<DisplayModel> getResources() {
        return resources;
    }
}
