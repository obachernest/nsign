package com.example.nsignptapp.feature.display.data.map;

import com.example.nsignptapp.feature.display.data.model.EventsJsonResponseModel;
import com.example.nsignptapp.feature.display.domain.model.DisplayModel;
import com.example.nsignptapp.feature.display.domain.model.PlayListModel;
import com.example.nsignptapp.feature.display.domain.model.ZoneModel;

import java.util.ArrayList;
import java.util.Comparator;

public class EventsMapper {

    private static String TAG = "EventsMapper";

    public static ArrayList<PlayListModel> map (EventsJsonResponseModel eventsModel) throws Exception {

        ArrayList<PlayListModel> data = new ArrayList<>();

        for (EventsJsonResponseModel.Playlist playlist : eventsModel.getPlaylists()) {

            int playlistWidth = playlist.getWidth();
            int playlistHeight = playlist.getHeigh();

            ArrayList<ZoneModel> zoneModelArrayList = new ArrayList<>();

            for (EventsJsonResponseModel.Zone zone : playlist.getZones()) {

                int posX = zone.getX();
                int posY = zone.getY();
                int zoneWidth = zone.getWidth();
                int zoneHeight = zone.getHeigh();

                ArrayList<DisplayModel> preOrderElements = new ArrayList<>();

                for (EventsJsonResponseModel.Resource resource : zone.getResources()) {

                    String fileName = resource.getName();
                    int duration = resource.getDuration();
                    int order = resource.getOrder();

                    preOrderElements.add(new DisplayModel(posX, posY, playlistWidth, playlistHeight, zoneWidth, zoneHeight, duration, fileName, order));
                }
                //Sort resources by "order"
                preOrderElements.sort(new Comparator<DisplayModel>() {
                    public int compare(DisplayModel o1, DisplayModel o2) {
                        return Integer.compare(o1.getOrder(), o2.getOrder());
                    }
                });
                zoneModelArrayList.add(new ZoneModel(preOrderElements));
            }
            data.add(new PlayListModel(zoneModelArrayList, playlist.getDuration()));
        }

        return data;
    }

}
