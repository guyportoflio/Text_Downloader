package com.presly.textdownloader;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Created by presly on 06/02/2017.
 */

public class TextDownloader extends AsyncTask<String, Void, String>{

    private Callbacks callbacks;
    private int httpStatusCode;
    private  String errorMessage;

    public TextDownloader(Callbacks callbacks){
        this.callbacks = callbacks;
    }

    @Override
    protected void onPreExecute() {
        callbacks.onAboutToBegin();
    }

    @Override
    protected String doInBackground(String... params) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            String link = params[0];

            URL url = new URL(link);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            httpStatusCode = connection.getResponseCode();

            if(httpStatusCode != HttpURLConnection.HTTP_OK) {
                errorMessage = connection.getResponseMessage();
                return null;
            }

            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String downloadedText = "";
            String oneLine = bufferedReader.readLine();
            while(oneLine != null){
                downloadedText += oneLine + "\n";
                oneLine = bufferedReader.readLine();
            }

            return downloadedText;
        }

        catch (Exception ex) {
          errorMessage = ex.getMessage();
            return null;
        }
        finally {
            if(bufferedReader != null){
                try { bufferedReader.close(); } catch (Exception ex) {}
            }
            if(inputStreamReader != null){
                try{inputStreamReader.close(); } catch (Exception ex) {}
            }
            if(inputStream != null){
                try { inputStream.close();} catch (Exception ex) {}
            }
        }
    }

       protected void onPostExecute(String downloadedText){
           if(downloadedText != null){
               callbacks.onSuccess(downloadedText);
           }
           else  {
               callbacks.onError(httpStatusCode, errorMessage);
           }
       }

    public interface Callbacks {
        void onAboutToBegin();
        void onSuccess(String downloadedText);
        void onError(int httpStatusCode, String errorMessage);
    }
}
