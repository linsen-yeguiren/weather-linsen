package com.example.linsen.weather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.linsen.weather.netUtil.NetUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by linsen on 2018/9/26.
 */

public class Weather extends Activity implements View.OnClickListener{

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        imageView=(ImageView)findViewById(R.id.title_update_btn);
        imageView.setOnClickListener(this);
        if(NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
            //log.d("weather","网络接通");
            Toast.makeText(this,"网络接通",Toast.LENGTH_LONG).show();
        }else{
            //log.d("weather","网络不通");

            Toast.makeText(this,"网络不通",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onClick(View view){
        if(view.getId()==R.id.title_update_btn){
            SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
            String cityCode=sharedPreferences.getString("main_city_code","101010100");
            Log.d("djsjjdkk","ksdfjakfjlaerrifoaer");
            if(NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
                queryWeatherCode(cityCode);
            }else
            {
                Toast.makeText(Weather.this,"网络不通",Toast.LENGTH_LONG).show();
            }

        }
    }
    private void queryWeatherCode(String cityCode){
        final String address="http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String string;
                    while((string = reader.readLine()) != null) {
                        response.append(string);
                    }
                    String resonseStr = response.toString();
                    Log.d("weather",resonseStr);
                    parseXML(resonseStr);

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(connection==null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

}


