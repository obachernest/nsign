package com.example.nsignptapp.common.download;

import static com.example.nsignptapp.common.Constants.LOCAL_URI;
import static com.example.nsignptapp.common.Constants.URL_ZIP;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownloadManager {
    public interface DownloadCallbacks {
        void onDownloadFinish(String msg);
        void onDownloadProgress(int from, int to);
    }

    private DownloadCallbacks mCallbacks;
    private DownloadTask mTask;

    /**
     * @param mCallbacks needed to notify the download status
     */
    public DownloadManager(DownloadCallbacks mCallbacks) {
        this.mCallbacks = mCallbacks;
    }

    /**
     * Public method to start downloading
     */
    public void startDownload(){

        //Creates the directories to dump the files
        File filesDirectory = new File(LOCAL_URI);
        filesDirectory.mkdirs();

        //Executes download in another thread
        mTask = new DownloadTask();
        mTask.execute(URL_ZIP, LOCAL_URI+"resources.zip");
    }

    /**
     * Unpacks the downloaded ZIP file
     * @param filePath path of the desired file to unpack
     * @return true for OK and false for KO
     */
    private boolean unpackZip(String filePath) {
        InputStream is;
        ZipInputStream zis;
        try {

            File zipfile = new File(filePath);
            String parentFolder = zipfile.getParentFile().getPath();
            String filename;

            is = new FileInputStream(filePath);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();

                if (ze.isDirectory()) {
                    File fmd = new File(parentFolder + "/" + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(parentFolder + "/" + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Async task to download the specified file
     */
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... args) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            String destinationFilePath = "";
            try {
                URL url = new URL(args[0]);
                destinationFilePath = args[1];

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
                }
                int lenghtOfFile = connection.getContentLength();

                // initializa download progress bar
                mCallbacks.onDownloadProgress(0,lenghtOfFile);

                // download the file
                input = connection.getInputStream();

                Log.d("DownloadFragment ", "destinationFilePath=" + destinationFilePath);
                new File(destinationFilePath).createNewFile();
                output = new FileOutputStream(destinationFilePath);

                byte data[] = new byte[4096];
                int count;
                int total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // update download progress bar
                    mCallbacks.onDownloadProgress(total,lenghtOfFile);
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }

            File f = new File(destinationFilePath);

            Log.d("DownloadFragment ", "f.getParentFile().getPath()=" + f.getParentFile().getPath());
            Log.d("DownloadFragment ", "f.getName()=" + f.getName().replace(".zip", ""));
            unpackZip(destinationFilePath);
            return "Download Completed!";
        }

        /**
         * Executed when the process has finished
         * @param s The result of the operation computed by {@link #doInBackground}.
         *
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("DownloadFragment ", s);
            mCallbacks.onDownloadFinish(s);
        }
    }
}
