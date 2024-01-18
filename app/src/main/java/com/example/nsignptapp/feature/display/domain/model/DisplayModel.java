package com.example.nsignptapp.feature.display.domain.model;

public class DisplayModel {
    private int posX;
    private int posY;
    private int zoneWidth;
    private int zoneHeight;
    private int playlistWidth;
    private int playlistHeight;
    private int duration;
    private String srcFile;
    private int order;

    public DisplayModel(int posX, int posY, int playlistWidth, int playlistHeight, int width, int height, int duration, String srcFile, int order) {
        this.posX = posX;
        this.posY = posY;
        this.playlistWidth = playlistWidth;
        this.playlistHeight = playlistHeight;
        this.zoneWidth = width;
        this.zoneHeight = height;
        this.duration = duration;
        this.srcFile = srcFile;
        this.order = order;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getPlaylistWidth() {
        return playlistWidth;
    }

    public int getPlaylistHeight() {
        return playlistHeight;
    }

    public int getZoneWidth() {
        return zoneWidth;
    }

    public int getZoneHeight() {
        return zoneHeight;
    }

    public int getDuration() {
        return duration;
    }

    public String getSrcFile() {
        return srcFile;
    }

    public int getOrder() {
        return order;
    }
}
