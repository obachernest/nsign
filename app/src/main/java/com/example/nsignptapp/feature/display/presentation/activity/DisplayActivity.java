package com.example.nsignptapp.feature.display.presentation.activity;

import static com.example.nsignptapp.common.Constants.EVENTS_MODEL;
import static com.example.nsignptapp.common.Constants.LOCAL_URI;

import android.app.Activity;
import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.nsignptapp.R;
import com.example.nsignptapp.common.Utils;
import com.example.nsignptapp.common.component.ProgressBarAnimation;
import com.example.nsignptapp.feature.display.data.map.EventsMapper;
import com.example.nsignptapp.feature.display.data.model.EventsJsonResponseModel;
import com.example.nsignptapp.feature.display.domain.model.DisplayModel;
import com.example.nsignptapp.feature.display.domain.model.PlayListModel;

import java.util.ArrayList;

public class DisplayActivity extends Activity {
    private EventsJsonResponseModel eventsModel;
    private ConstraintLayout playlistCL, zoneCL;
    private ImageView imageIV;
    private VideoView videoVV;
    private ProgressBar totalDurationPB, playlistDurationPB, resourceDurationPB;
    private Button restartButton;
    private DisplayModel selectedResource;
    private ArrayList<PlayListModel> playlistList;
    private Handler handlerPlaylist, handlerResource;
    private int indexR, indexZ, indexP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_content_activity);
        bindViews();
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey(EVENTS_MODEL)){
            eventsModel = (EventsJsonResponseModel) getIntent().getExtras().get(EVENTS_MODEL);
        }
        initViews();
    }

    private void bindViews(){
        playlistCL = findViewById(R.id.layout_playlist);
        zoneCL = findViewById(R.id.layout_zone);
        imageIV = findViewById(R.id.imageDisplay);
        videoVV = findViewById(R.id.videoView);
        totalDurationPB = findViewById(R.id.progressBar);
        playlistDurationPB = findViewById(R.id.progressBarPlaylist);
        resourceDurationPB = findViewById(R.id.progressBarResource);
        restartButton = findViewById(R.id.restartButton);
    }
    private void initViews(){

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        setUpPlayList();
    }

    /**
     * Set's up the playlist
     */
    private void setUpPlayList(){
        playlistList = new ArrayList<>();
        int totalDuration = 0;
        try {
            playlistList = EventsMapper.map(eventsModel);
            for (PlayListModel item: playlistList) {
                totalDuration += item.getDuration();
            }
            startProgressBar(totalDuration, totalDurationPB);
            startProcess();
        }catch (Exception e){
            restartButton.setVisibility(View.VISIBLE);
            new AlertDialog.Builder(this).setTitle("ERROR").setMessage("Data error, cannot show content").show();
            //Toast.makeText(this, "Data error, cannot show content", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     * @param model show on screen the specified resource
     */
    private void setDisplayResource(DisplayModel model){

        //Set playlist dimensions (black border)
        playlistCL.getLayoutParams().width = Utils.pxToDp(this, model.getPlaylistWidth());
        playlistCL.getLayoutParams().height = Utils.pxToDp(this, model.getPlaylistHeight());

        //Set display zone dimensions (red border)
        zoneCL.getLayoutParams().width = Utils.pxToDp(this, model.getZoneWidth());
        zoneCL.getLayoutParams().height = Utils.pxToDp(this, model.getZoneHeight());
        zoneCL.setX(Utils.pxToDp(this, model.getPosX()));
        zoneCL.setY(Utils.pxToDp(this, model.getPosY()));

        //Check if Video or Image
        if (model.getSrcFile().contains("mp4")){
            videoVV.setVisibility(View.VISIBLE);
            imageIV.setVisibility(View.GONE);
            Uri video = Uri.parse(LOCAL_URI+model.getSrcFile());
            videoVV.setVideoURI(video);
            videoVV.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(false);
                    videoVV.start();
                }
            });
        }else {
            videoVV.setVisibility(View.GONE);
            imageIV.setVisibility(View.VISIBLE);
            imageIV.setImageURI(Uri.parse(LOCAL_URI+model.getSrcFile()));
        }
        startProgressBar(model.getDuration(), resourceDurationPB);
    }

    /**
     * Initializes and starts to reproduce the playlist
     */
    private void startProcess(){
        indexR = 0;
        indexZ = 0;
        indexP = 0;

        handlerPlaylist = new Handler();
        handlerResource = new Handler();

        startPlayList(playlistList.get(indexP));
    }

    /**
     * Checks the next resource, changes indexR for "resource", indexZ for "zone" and indexP for "playlist"
     */
    public void checkNextResource() {
        indexR++;
        //Check resource index
        if (indexR < playlistList.get(indexP).getZones().get(indexZ).getResources().size()){
            handleNextResource();
        }else{
            indexR = 0;
            indexZ++;
            //Chez zone index
            if (indexZ < playlistList.get(indexP).getZones().size()){
                handleNextResource();
            }else{
                indexZ = 0;
                indexP++;
                //Check playlist index
                if (indexP < playlistList.size()){
                    startPlayList(playlistList.get(indexP));
                }else{
                    //Finish
                    restartButton.setVisibility(View.VISIBLE);
                    new AlertDialog.Builder(this).setTitle("FINISH").setMessage("The playlist has finished successfully").show();
                }
            }
        }
    }

    /**
     * @param playlist play specific playlist
     */
    private void startPlayList(PlayListModel playlist){

        //Update playlist progress bar
        startProgressBar(playlist.getDuration(), playlistDurationPB);

        //Handle playlist changes
        handlerPlaylist.postDelayed(new Runnable() {
            @Override
            public void run() {
                handlerResource.removeCallbacksAndMessages(null);
                checkNextResource();
            }
        }, playlist.getDuration() * 1000L);

        //Handle resource changes
        handleNextResource();
    }

    /**
     * Handle next resource to display
     */
    private void handleNextResource(){
        //Update resource
        selectedResource = playlistList.get(indexP).getZones().get(indexZ).getResources().get(indexR);
        setDisplayResource(selectedResource);
        handlerResource.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkNextResource();
            }
        },selectedResource.getDuration() * 1000L);
    }

    /**
     * @param duration in Seconds
     * @param bar instance of the progressBar to start
     */
    private void startProgressBar(int duration, ProgressBar bar){
        ProgressBarAnimation anim = new ProgressBarAnimation(bar, 0, 100);
        anim.setDuration(duration * 1000L);
        bar.startAnimation(anim);
    }

}
