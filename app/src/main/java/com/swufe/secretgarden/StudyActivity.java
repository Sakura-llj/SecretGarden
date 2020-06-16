package com.swufe.secretgarden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StudyActivity extends AppCompatActivity implements Runnable{

    private String TAG = "study";
    List<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
    SimpleAdapter listItemAdapter;
    Handler handler;
    ListView listView;
    ImageView ivDeleteText;
    EditText et_Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        ivDeleteText = findViewById(R.id.ivDeleteText);
        et_Search = findViewById(R.id.et_Search);
        listView = findViewById(R.id.noticeList);

        //点叉全删功能
        ivDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_Search.setText("");
            }
        });

        //EditText监听
        et_Search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0){
                    ivDeleteText.setVisibility(View.GONE);
                }else {
                    ivDeleteText.setVisibility(View.VISIBLE);
                }
            }
        });

        initListView();
        listView.setAdapter(listItemAdapter);
        listView.setEmptyView(findViewById(R.id.nodata));

        //开启子线程
        Thread t= new Thread(this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what==5){
                    listItems = (List<HashMap<String, String>>) msg.obj;
                    listItemAdapter = new SimpleAdapter(StudyActivity.this, listItems,//数据源
                            R.layout.list_item,
                            new String[] { "showTitle","showTime"},//数据项key
                            new int[] {R.id.showtitle,R.id.showtime});//控件
                    listView.setAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            }
        };

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

    private void initListView(){
        for (int i = 0; i < 10; i++){
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("showTitle","Title: " + i);
            map.put("showTime","time" + i);
            listItems.add(map);//布局文件的控件id和放map时的key可以不同
        }
        //生成适配器的Item和动态数组相对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                //有了数据&布局，确定数据和布局之间的对应关系
                new String[] { "showTitle","showTime"},//数据项key
                new int[] {R.id.showtitle,R.id.showtime});//控件
    }

    @Override
    public void run() {
        Log.i(TAG,"run: run()....");
        //获取网络数据
        List<HashMap<String, String>> retList = new ArrayList<HashMap<String, String>>();
        Document doc = null;
        try {
            doc = Jsoup.connect("https://it.swufe.edu.cn/index/tzgg.htm").get();
            Log.i(TAG,"run: " + doc.title());

            Elements uls = doc.getElementsByTag("ul");
            Element ul18 = uls.get(17);
            //Log.i(TAG,"run: ul18=" + ul18);

            Elements spans = ul18.getElementsByTag("span");
            //Log.i(TAG,"run: titles=" + spans);
            for (int i=0;i<spans.size();i=i+2){
                Element span1 = spans.get(i);//标题
                Element span2 = spans.get(i+1);//时间

                String strTitle = span1.text();
                String strTime = span2.text();
                Log.i(TAG,"run: " + strTitle + "" + strTime);

                HashMap<String,String> map = new HashMap<String,String>();
                map.put("showTitle",strTitle);
                map.put("showTime",strTime);
                retList.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        //获取msg对象，发送给handler
        Message msg = handler.obtainMessage(5);
        msg.obj = retList;
        handler.sendMessage(msg);

    }
}
