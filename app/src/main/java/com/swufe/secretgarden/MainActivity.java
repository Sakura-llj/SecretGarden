package com.swufe.secretgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void openlive(View btn){
        Log.i(TAG,"onClick btn_live...");
        //打开LiveActivity
        Intent live = new Intent(this,LiveActivity.class);
        startActivity(live);
    }
    public void openstudy(View btn){
        Log.i(TAG,"onClick btn_study...");
        //打开StudyActivity
        Intent study = new Intent(this,StudyActivity.class);
        startActivity(study);
    }
    public void openplay(View btn){
        Log.i(TAG,"onClick btn_play...");
        //打开PlayActivity
        Intent play = new Intent(this,PlayActivity.class);
        startActivity(play);
    }
}
