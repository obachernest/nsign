package com.example.nsignptapp.common;

import static com.example.nsignptapp.common.Constants.LOCAL_URI;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.nsignptapp.feature.display.data.model.EventsJsonResponseModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
    public static EventsJsonResponseModel parseJson(String fileName) throws RuntimeException{
        Gson g = new Gson();
        File file = new File(LOCAL_URI+fileName);
        String jsonString = null;
        try {
            jsonString = inputStreamToString(new FileInputStream(file));
            return g.fromJson(jsonString, EventsJsonResponseModel.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String inputStreamToString(InputStream is) throws IOException {
        if (is == null) {
            throw new IllegalArgumentException("The InputStream cannot be null");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder text = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            text.append(line).append('\n');
        }
        reader.close();

        return text.toString();
    }

    public static int dpToPx(Context context, int dp)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / 160f));
    }

    public static int pxToDp(Context context, int px)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (px / (metrics.densityDpi / 160f));
    }
}
