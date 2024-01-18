package com.example.nsignptapp.feature.display.data.model;

import java.io.Serializable;
import java.util.ArrayList;

public class EventsJsonResponseModel implements Serializable {
    public Schedule schedule;
    public ArrayList<Playlist> playlists;

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    public class Playlist implements Serializable{
        public String id;
        public int width;
        public int heigh;
        public int duration;
        public ArrayList<Zone> zones;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeigh() {
            return heigh;
        }

        public void setHeigh(int heigh) {
            this.heigh = heigh;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public ArrayList<Zone> getZones() {
            return zones;
        }

        public void setZones(ArrayList<Zone> zones) {
            this.zones = zones;
        }
    }

    public class Resource implements Serializable{
        public String id;
        public int order;
        public String name;
        public int duration;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }

    public class Schedule implements Serializable{
        public String id;
        public String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class Zone implements Serializable{
        public String id;
        public int x;
        public int y;
        public int width;
        public int heigh;
        public ArrayList<Resource> resources;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeigh() {
            return heigh;
        }

        public void setHeigh(int heigh) {
            this.heigh = heigh;
        }

        public ArrayList<Resource> getResources() {
            return resources;
        }

        public void setResources(ArrayList<Resource> resources) {
            this.resources = resources;
        }
    }
}



