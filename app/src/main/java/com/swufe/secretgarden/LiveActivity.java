package com.swufe.secretgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LiveActivity extends AppCompatActivity {

    private final String TAG = "Live";
    private RadioGroup radioGroup;

    EditText edHeight;
    EditText edWeight;
    TextView mybmi;
    RadioButton cnstd;
    RadioButton itstd;
    TextView result;
    float height;
    float weight;
    float BMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        edHeight = findViewById(R.id.edit_height);
        edWeight = findViewById(R.id.edit_weight);
        mybmi = findViewById(R.id.show_bmi);
        result = findViewById(R.id.result);
        radioGroup = findViewById(R.id.Groupstd);
        cnstd = findViewById(R.id.cnstd);
        itstd = findViewById(R.id.itstd);

        //获取SP里保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("myBMI", Activity.MODE_PRIVATE);
        height = sharedPreferences.getFloat("height_his", 0.0f);
        weight = sharedPreferences.getFloat("weight_his",0.0f);
        BMI = sharedPreferences.getFloat("BMI",0.0f);

        Log.i(TAG,"onCreate: sp height=" + height);
        Log.i(TAG,"onCreate: sp weight=" + weight);
        Log.i(TAG,"onCreate: sp BMI=" + BMI);

        //是否显示历史数据
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("是否显示历史数据").setPositiveButton("是",
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG,"onClick: 显示历史数据否");
                        edHeight.setText(String.valueOf(height));
                        edWeight.setText(String.valueOf(weight));
                        mybmi.setText("你上一次的BMI是：" + String.valueOf(BMI));
                    }
                }).setNegativeButton("否",null);
        builder.create().show();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (cnstd.getId()==checkedId){
                    Toast.makeText(LiveActivity.this, "中国标准", Toast.LENGTH_SHORT).show();
                    cnBMI(BMI);
                }else if (itstd.getId()==checkedId){
                    Toast.makeText(LiveActivity.this, "国际标准", Toast.LENGTH_SHORT).show();
                    itBMI(BMI);
                }
            }
        });

    }

    public void calbmi(View btn){
        Log.i(TAG,"onClick calcu...");

        //获取用户输入
        String hgt = edHeight.getText().toString();
        String wgt = edWeight.getText().toString();
        //异常处理
        if (hgt.length()>0 && wgt.length()>0){
            height = Float.parseFloat(hgt);
            weight = Float.parseFloat(wgt);
            //计算
            if (height>0 && height<2.72f && weight>0 && weight<100f){
                BMI = weight/(height*height);
                //按下计算按钮时，是否保存
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示").setMessage("请确认是否保存体征数据").setPositiveButton("是",
                        new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i(TAG,"onClick: 保存身高体重否");
                                SharedPreferences sharedPreferences = getSharedPreferences("myBMI", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putFloat("height_his",height);
                                editor.putFloat("weight_his",weight);
                                editor.putFloat("BMI",BMI);
                                editor.apply();
                            }
                        }).setNegativeButton("否",null);
                builder.create().show();

                mybmi.setText("你的BMI是：" + String.format("%.1f",BMI));
            }else {
                Toast.makeText(this,"请检查体征信息是否输入正确！",Toast.LENGTH_SHORT).show();
            }
        }else if (hgt.length()>0 && wgt.length()==0){
            Toast.makeText(this,"请输入体重信息（KG）",Toast.LENGTH_SHORT).show();
        }else if (hgt.length()==0 && wgt.length()>0){
            Toast.makeText(this,"请输入身高信息（M）",Toast.LENGTH_SHORT).show();
        }else if (hgt.length()==0 && wgt.length()==0){
            Toast.makeText(this,"请输入体征信息",Toast.LENGTH_SHORT).show();
        }

    }
    private void cnBMI(float BMI){
        if (BMI<18.5){
            result.setText("你有些瘦了");
        }else if (BMI<24 && BMI>=18.5){
            result.setText("你是个体重正常的宝宝^_^");
        }else if (BMI<28 && BMI>=24){
            result.setText("你有些重量了！");
        }else {
            result.setText("你不是很健康了！");
        }
    }
    private void itBMI(float BMI){
        if (BMI<18.5){
            result.setText("你有些瘦了！");
        }else if (BMI<25 && BMI>=18.5){
            result.setText("你是个体重正常的宝宝^_^");
        }else if (BMI<30 && BMI>=25){
            result.setText("你有些重量了！");
        }else {
            result.setText("你不是很健康了！");
        }
    }

}
