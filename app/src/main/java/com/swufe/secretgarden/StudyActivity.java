package com.swufe.secretgarden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class StudyActivity extends AppCompatActivity implements Runnable{

    private String TAG = "study";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        //开启子线程
        Thread t = new Thread(this);
        t.start();
    }

    //More info菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.study,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.info_CET){
            Intent webcet = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cet.neea.edu.cn/"));
            startActivity(webcet);
        }else if (item.getItemId()==R.id.info_IELTS){
            Intent webielts = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.chinaielts.org/"));
            startActivity(webielts);
        }else if (item.getItemId()==R.id.info_NCRE){
            Intent webncre = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ncre.neea.edu.cn/"));
            startActivity(webncre);
        }else if (item.getItemId()==R.id.info_JWC){
            Intent webjwc = new Intent(Intent.ACTION_VIEW, Uri.parse("https://jwc.swufe.edu.cn/"));
            startActivity(webjwc);
        }else if (item.getItemId()==R.id.info_IT){
            Intent webjwc = new Intent(Intent.ACTION_VIEW, Uri.parse("https://it.swufe.edu.cn/"));
            startActivity(webjwc);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {
        Log.i(TAG,"run: run().....");
    }
}
