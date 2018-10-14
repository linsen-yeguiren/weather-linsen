package com.example.linsen.weather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linsen.weather.bean.TodayWeather;
import com.example.linsen.weather.netUtil.NetUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.String;


/**
 * Created by linsen on 2018/9/26.
 */

public class Weather extends Activity implements View.OnClickListener {

    private static final int UPDATE_TODAY_WEATHER = 1;
    private static final int QULITY_GOOD = 50;
    private static final int QULITY_LITTLE = 100;
    private static final int QULITY_MIDDLE = 250;
    private static final int QULITY_SERIOUS = 400;
    private ImageView imageView;
    private TextView cityTv, timeTv, tempTv, humidityTv, weekTv, pmDataTv, pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;

    private Handler handler = new Handler() {
        //@Override
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodaweather((TodayWeather) message.obj);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        imageView = (ImageView) findViewById(R.id.title_update_btn);
        imageView.setOnClickListener(this);
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("weather", "网络接通");
            Toast.makeText(this, "网络接通", Toast.LENGTH_LONG).show();
        } else {
            Log.d("weather", "网络不通");
            Toast.makeText(this, "网络不通", Toast.LENGTH_LONG).show();
        }
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_update_btn) {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("weather", cityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("weather", "网络连通");
                Toast.makeText(Weather.this, "网络接通", Toast.LENGTH_LONG).show();
                queryWeatherCode(cityCode);
            } else {
                Log.d("weather", "网络不通");
                Toast.makeText(Weather.this, "网络不通", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("weather", address);

        new Thread(new Runnable() {
            @Override
            public void run() {
                TodayWeather todayWeather;
                HttpURLConnection connection = null;
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
                    while ((string = reader.readLine()) != null) {
                        response.append(string);
                        Log.d("weather", string);
                    }
                    String resonseStr = response.toString();
                    Log.d("weather", resonseStr);
                    todayWeather = parseXML(resonseStr);
                    if (todayWeather != null) {
                        Log.d("weather", todayWeather.toString());
                        Message message = new Message();
                        message.what = UPDATE_TODAY_WEATHER;
                        message.obj = todayWeather;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection == null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private TodayWeather parseXML(String xmlData) {
        TodayWeather todayWeather = new TodayWeather();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            Log.d("weather", "parserXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("city")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setCity(xmlPullParser.getText());
                            Log.d("weather", "city" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("updatetime")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setUpdatetime(xmlPullParser.getText());
                            Log.d("weather", "updatetime" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("shidu")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setHumid(xmlPullParser.getText());
                            Log.d("weather", "humidity" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("pm25")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setPm25(xmlPullParser.getText());
                            Log.d("weather", "pm2.5" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("wendu")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setTemp(xmlPullParser.getText());
                            Log.d("weather", "wedu" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("date_1")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setDate(xmlPullParser.getText());
                            Log.d("weather", "date" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("high")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setHigh(xmlPullParser.getText());
                            Log.d("weather", "highTem" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("low")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setLow(xmlPullParser.getText());
                            Log.d("weather", "highTem" + xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("quality")){
                            eventType=xmlPullParser.next();
                            todayWeather.setQuality(xmlPullParser.getText());
                            Log.d("weather","quality"+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("type_1")){
                            eventType=xmlPullParser.next();
                            todayWeather.setType(xmlPullParser.getText());
                            Log.d("weather","type"+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("fx_1")){
                            eventType=xmlPullParser.next();
                            todayWeather.setWinderection(xmlPullParser.getText());
                            Log.d("weather","fengxiang"+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("fl_1")){
                            eventType=xmlPullParser.next();
                            todayWeather.setWindpow(xmlPullParser.getText());
                            Log.d("weather","fengli"+xmlPullParser.getText());
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;

    }

    void initView() {
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        tempTv = (TextView) findViewById(R.id.temper);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
        tempTv.setText("N/A");
    }

    void updateTodaweather(TodayWeather todayWeather) {
        city_name_Tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度:" + todayWeather.getHumid());
        pmDataTv.setText(todayWeather.getPm25());
//        int PM25 = Integer.parseInt(todayWeather.getPm25());
//        if (PM25 <= QULITY_GOOD) {
//            pmQualityTv.setText("空气良好");
//        } else if (PM25 <= QULITY_LITTLE) {
//            pmQualityTv.setText("轻度污染");
//        } else if (PM25 <= QULITY_MIDDLE) {
//            pmQualityTv.setText("中度污染");
//        } else if (PM25 <= QULITY_SERIOUS) {
//            pmQualityTv.setText("重度污染");
//        } else {
//            pmQualityTv.setText("严重污染");
//        }
        pmQualityTv.setText(todayWeather.getQuality());
        temperatureTv.setText(todayWeather.getLow().substring(3, todayWeather.getLow().length()) + "～" + todayWeather.getHigh().substring(3, todayWeather.getLow().length()));
        Log.d("weather",temperatureTv.getText().toString());
        tempTv.setText("温度:" + todayWeather.getTemp() + "℃");
        weekTv.setText(todayWeather.getDate());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:"+todayWeather.getWindpow());
        Toast.makeText(Weather.this, "更新成功！", Toast.LENGTH_SHORT).show();
    }

}


