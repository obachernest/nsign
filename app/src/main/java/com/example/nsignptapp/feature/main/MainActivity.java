package com.example.nsignptapp.feature.main;

import static com.example.nsignptapp.common.Constants.EVENTS_JSON;
import static com.example.nsignptapp.common.Constants.EVENTS_MODEL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nsignptapp.R;
import com.example.nsignptapp.common.Utils;
import com.example.nsignptapp.common.download.DownloadManager;
import com.example.nsignptapp.feature.display.presentation.activity.DisplayActivity;
import com.example.nsignptapp.feature.display.data.model.EventsJsonResponseModel;

public class MainActivity extends AppCompatActivity implements DownloadManager.DownloadCallbacks {
    private TextView statusTV;
    private ProgressBar downloadPB;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        startAsyncDownload();
    }

    private void bindViews(){
        statusTV = (TextView) findViewById(R.id.textInfo);
        downloadPB = (ProgressBar) findViewById(R.id.progressBarMain);
    }

    /**
     * Start downloading assets
     */
    @SuppressLint("SetTextI18n")
    private void startAsyncDownload(){
        downloadPB.setVisibility(View.VISIBLE);
        statusTV.setText("DOWNLOADING ASSETS");

        downloadManager = new DownloadManager(this);
        downloadManager.startDownload();
    }

    /**
     * Handles the result of the download despite being successful or not
     * @param msg message to show
     */
    @SuppressLint("SetTextI18n")
    public void onDownloadFinish(String msg) {
        if (msg.contains("Download Completed!")) {
            statusTV.setText("PARSING_JSON");
            try {
                EventsJsonResponseModel eventsModel = Utils.parseJson(EVENTS_JSON);
                showContent(eventsModel);
            } catch (Exception e) {
                new AlertDialog.Builder(this).setTitle("ERROR").setMessage("Error during parsing, cannot show content").show();
            }
        }else{
            new AlertDialog.Builder(this).setTitle("ERROR").setMessage(msg).show();
        }
    }

    /**
     * Used to update the progress bar for the download
     * @param from current progress
     * @param to end progress
     */
    @Override
    public void onDownloadProgress(int from, int to) {
        downloadPB.setMax(to);
        downloadPB.setProgress(from,true);
        downloadPB.setVisibility(View.VISIBLE);
    }

    /**
     * Download was successful so we send the content to display
     */
    @SuppressLint("SetTextI18n")
    private void showContent(EventsJsonResponseModel eventsModel){
        statusTV.setText("REPRODUCING");
        downloadPB.setVisibility(View.GONE);
        Intent intent = new Intent(this, DisplayActivity.class);
        intent.putExtra(EVENTS_MODEL,eventsModel);
        startActivityForResult(intent, 99);
        statusTV.setText("CLICK TO RESTART");
        statusTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAsyncDownload();
            }
        });
    }

    /**
     * Used to start again if the button restart was clicked
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == RESULT_OK ){
            startAsyncDownload();
        }
    }

}