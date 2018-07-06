package com.example.user.ocv;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import android.os.Environment;

//import Java.Lang.Object;

import static org.opencv.imgproc.Imgproc.cvtColor;


public class MainActivity extends AppCompatActivity {

    int permissionCheck1= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    int permissionCheck2= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    int permissionCheck3= ContextCompat.checkSelfPermission(this, Manifest.permission.KILL_BACKGROUND_PROCESSES);


    String root = Environment.getExternalStorageDirectory().toString();

    // Used to load the 'native-lib' library on application startup.
    //static {
        //System.loadLibrary("native-lib");
    //}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modifySettings=new Intent(MainActivity.this,MyPreferencesActivity.class);
                startActivity(modifySettings);
            }
        });
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());
        //Mat m = Imgcodecs.imread(root+"img.jpg");
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
}
