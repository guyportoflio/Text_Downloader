package com.presly.textdownloader;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TextDownloader.Callbacks {

    private EditText linkText;
    private TextView textView1;
    private ProgressDialog progressDialog1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView)findViewById(R.id.textView1);
        linkText = (EditText)findViewById(R.id.linkText);
    }

    public void sendToTextDownloader(View view){
        TextDownloader textDownloader = new TextDownloader(this);
        String link = "http://android-demo-apis.appspot.com/simple/zen";
        String convertToString = linkText.getText().toString();
        if(convertToString.equals(link) || convertToString.equals("") ) {
            textDownloader.execute(link);
        }
       else {

            textDownloader.execute(convertToString);
        }
    }

    @Override
    public void onAboutToBegin() {
    progressDialog1 = new ProgressDialog(this);
        progressDialog1.setTitle("Downloading");
        progressDialog1.setMessage("Please wait...");
        progressDialog1.setIcon(R.drawable.loadingball);
        progressDialog1.show();
    }

    @Override
    public void onSuccess(String downloadedText) {
        progressDialog1.dismiss();
        textView1.setText(downloadedText);
    }

    @Override
    public void onError(int httpStatusCode, String errorMessage) {
   progressDialog1.dismiss();
        textView1.setText("Error: " + httpStatusCode + " happened.  full message:" + errorMessage);
    }


}
