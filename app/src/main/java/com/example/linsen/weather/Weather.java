package com.example.linsen.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linsen.weather.adapter.ViewPageAdaper;
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
import java.util.ArrayList;
import java.util.List;


/**
 * Created by linsen on 2018/9/26.
 */
// Context是个抽象类,通过类的结构可以看到:Activity、Service、Application都是Context的子类
public class Weather extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final int UPDATE_TODAY_WEATHER = 1;
    private static final int QULITY_GOOD = 50;
    private static final int QULITY_LITTLE = 100;
    private static final int QULITY_MIDDLE = 150;
    private static final int QULITY_HIGH = 150;
    private static final int QULITY_SERIOUS = 300;
    private List<View> views;
    private ImageView[] dots;
    private int[] ids = {R.id.lv1, R.id.lv2, R.id.lv3};
    private ViewPager viewPager;
    private ImageView imageView, cityView;
    private TextView cityTv, timeTv, tempTv, humidityTv, weekTv, pmDataTv, pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;
    private ProgressBar progressBar;
    private List<TextView> tlistweek;
    private List<TextView> tlisttem;
    private List<TextView> tlistweather;
    private List<TextView> tlistwind;
    private List<ImageView> imagelist;
    private List<List> weekList;

    void initDots() {
        dots = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) findViewById(ids[i]);
        }
    }

    private Handler handler = new Handler() {
        //@Override
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case UPDATE_TODAY_WEATHER:
                    updateweather((List) message.obj);
                    break;
                default:
                    break;
            }

        }
    };

    private void updateweather(List list) {
        progressBar.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        int i = 0;
        updateTodaweather((TodayWeather) list.get(1));
        for (Object obj : list) {
            Log.d("yyyxxx", "i");
            updateweekweather((TodayWeather) obj, i);
            i++;
        }
    }

    private void updateweekweather(TodayWeather obj, int i) {
        Log.d("xxxxyyy", "" + i);
        tlistweek.get(i).setText(obj.getDate());
        tlisttem.get(i).setText(obj.getLow().substring(3, obj.getLow().length()) + "～" + obj.getHigh().substring(3, obj.getHigh().length()));
        tlistweather.get(i).setText(obj.getType());
        tlistwind.get(i).setText(obj.getWinderection());
        switch (obj.getType()) {
            case "晴":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_qing);
                break;
            case "阴":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_yin);
                break;
            case "多云":
                Log.d("tianqi", "" + i);
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_duoyun);
                break;
            case "雾":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_wu);
                break;
            case "沙尘暴":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_shachenbao);
                break;
            case "雷阵雨":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
                break;
            case "雷阵雨冰雹":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
                break;
            case "小雨":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
                break;
            case "大雨":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_dayu);
                break;
            case "暴雨":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_baoyu);
                break;
            case "大暴雨":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
                break;
            case "特大暴雨":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
                break;
            case "大雪":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_daxue);
                break;
            case "暴雪":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_baoxue);
                break;
            case "阵雪":
                imagelist.get(i).setImageResource(R.drawable.biz_plugin_weather_zhenxue);
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        imageView = (ImageView) findViewById(R.id.title_update_btn);
        imageView.setOnClickListener(this);
        cityView = (ImageView) findViewById(R.id.title_city_manager);
        cityView.setOnClickListener(this);

        Log.d("hhhh", "===================");
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("weather", "网络接通");
            Toast.makeText(this, "网络接通", Toast.LENGTH_LONG).show();
        } else {
            Log.d("weather", "网络不通");
            Toast.makeText(this, "网络不通", Toast.LENGTH_LONG).show();
        }
        initView();
        initDots();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_update_btn) {
            imageView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_NO_LOCALIZED_COLLATORS);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("weather", cityCode);
            Log.d("xxxxxxx", cityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("weather", "网络连通");
                Toast.makeText(Weather.this, "网络接通", Toast.LENGTH_LONG).show();
                queryWeatherCode(cityCode);
            } else {
                Log.d("weather", "网络不通");
                Toast.makeText(Weather.this, "网络不通", Toast.LENGTH_LONG).show();
            }

        } else if (view.getId() == R.id.title_city_manager) {
            Intent intent = new Intent(this, SelectCity.class);
            Log.d("...", "===================");
            //startActivity(intent);
            startActivityForResult(intent, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode = data.getStringExtra("cityCode");
            SharedPreferences settings = getSharedPreferences("config", MODE_NO_LOCALIZED_COLLATORS);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("main_city_code", newCityCode);
            editor.commit();
            Log.d("myWeather", "选择的城市代码为" + newCityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(newCityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(Weather.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("weather", address);
        Log.d("xxxx", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                TodayWeather yestodayWeather, todayWeather, weather1, weather2, weather3, weather4;
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
                    List<TodayWeather> list = new ArrayList();
                    todayWeather = parseXML(resonseStr, 0);
                    weather1 = parseXML(resonseStr, 1);
                    weather2 = parseXML(resonseStr, 2);
                    weather3 = parseXML(resonseStr, 3);
                    weather4 = parseXML(resonseStr, 4);
                    Log.d("ceshi", weather1.toString());
                    Log.d("ceshi", weather2.toString());
                    Log.d("ceshi", weather3.toString());
                    Log.d("ceshi", weather4.toString());
                    yestodayWeather = parseXMLForYestoday(resonseStr);
                    list.add(yestodayWeather);
                    list.add(todayWeather);
                    list.add(weather1);
                    list.add(weather2);
                    list.add(weather3);
                    list.add(weather4);
                    if (todayWeather != null) {
                        Log.d("weather", todayWeather.toString());
                        Message message = new Message();
                        message.what = UPDATE_TODAY_WEATHER;
                        message.obj = list;
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

    private TodayWeather parseXMLForYestoday(String xmlData) {
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
                        if (xmlPullParser.getName().equals("city_1")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setCity(xmlPullParser.getText());
                            Log.d("weather", "city" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("updatetime_1")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setUpdatetime(xmlPullParser.getText());
                            Log.d("weather", "updatetime" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("shidu_1")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setHumid(xmlPullParser.getText());
                            Log.d("weather", "humidity" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("pm25_1")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setPm25(xmlPullParser.getText());
                            Log.d("weather", "pm2.5" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("wendu_1")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setTemp(xmlPullParser.getText());
                            Log.d("weather", "wedu" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("date_1"))
                        //else if (xmlPullParser.getName().equals("date"))
                        {
                            eventType = xmlPullParser.next();
                            todayWeather.setDate(xmlPullParser.getText());
                            Log.d("weather", "date" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("high_1")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setHigh(xmlPullParser.getText());
                            Log.d("weather", "highTem" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("low_1")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setLow(xmlPullParser.getText());
                            Log.d("weather", "highTem" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("quality_1")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setQuality(xmlPullParser.getText());
                            Log.d("weather", "quality" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("type_1")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setType(xmlPullParser.getText());
                            Log.d("weather", "type" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fx_1")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setWinderection(xmlPullParser.getText());
                            Log.d("weather", "fengxiang" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fl_1")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setWindpow(xmlPullParser.getText());
                            Log.d("weather", "fengli" + xmlPullParser.getText());
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

    private TodayWeather parseXML(String xmlData, int i) {
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
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
                        } else if (xmlPullParser.getName().equals("date"))
                        //else if (xmlPullParser.getName().equals("date"))
                        {
                            if (dateCount == i) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                Log.d("weather", "date" + xmlPullParser.getText());
                            }
                            dateCount++;

                        } else if (xmlPullParser.getName().equals("high")) {
                            if (highCount == i) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText());
                                Log.d("weather", "highTem" + xmlPullParser.getText());
                            }
                            highCount++;

                        } else if (xmlPullParser.getName().equals("low")) {
                            if (lowCount == i) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText());
                                Log.d("weather", "highTem" + xmlPullParser.getText());
                            }
                            lowCount++;

                        } else if (xmlPullParser.getName().equals("quality")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setQuality(xmlPullParser.getText());
                            Log.d("weather", "quality" + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("type")) {
                            if (typeCount == i) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                Log.d("weather", "type" + xmlPullParser.getText());
                            }
                            typeCount++;
                        } else if (xmlPullParser.getName().equals("fengxiang")) {
                            if (fengliCount == i) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWinderection(xmlPullParser.getText());
                                Log.d("weather", "fengxiang" + xmlPullParser.getText());
                            }
                            fengliCount++;

                        } else if (xmlPullParser.getName().equals("fengli")) {
                            if (fengxiangCount == i) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWindpow(xmlPullParser.getText());
                                Log.d("weather", "fengli" + xmlPullParser.getText());
                            }
                            fengxiangCount++;

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
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList();
        views.add(inflater.inflate(R.layout.page1, null));
        views.add(inflater.inflate(R.layout.page2, null));
        //views.add(inflater.inflate(R.layout.page3, null));
        PagerAdapter pagerAdapter = new ViewPageAdaper(this, views);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(this);
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
        progressBar=(ProgressBar) findViewById(R.id.updateProgress);
        //for(int i=0;i<6;i++)
        tlistweek = new ArrayList<>();
        tlistweek.add((TextView) views.get(0).findViewById(R.id.weekday1));
        tlistweek.add((TextView) views.get(0).findViewById(R.id.weekday2));
        tlistweek.add((TextView) views.get(0).findViewById(R.id.weekday3));
        tlistweek.add((TextView) views.get(1).findViewById(R.id.weekday4));
        tlistweek.add((TextView) views.get(1).findViewById(R.id.weekday5));
        tlistweek.add((TextView) views.get(1).findViewById(R.id.weekday6));
        imagelist = new ArrayList<>();
        imagelist.add((ImageView) views.get(0).findViewById(R.id.image1));
        imagelist.add((ImageView) views.get(0).findViewById(R.id.image2));
        imagelist.add((ImageView) views.get(0).findViewById(R.id.image3));
        imagelist.add((ImageView) views.get(1).findViewById(R.id.image4));
        imagelist.add((ImageView) views.get(1).findViewById(R.id.image5));
        imagelist.add((ImageView) views.get(1).findViewById(R.id.image6));
        tlisttem = new ArrayList<>();
        tlisttem.add((TextView) views.get(0).findViewById(R.id.tem1));
        tlisttem.add((TextView) views.get(0).findViewById(R.id.tem2));
        tlisttem.add((TextView) views.get(0).findViewById(R.id.tem3));
        tlisttem.add((TextView) views.get(1).findViewById(R.id.tem4));
        tlisttem.add((TextView) views.get(1).findViewById(R.id.tem5));
        tlisttem.add((TextView) views.get(1).findViewById(R.id.tem6));
        tlistweather = new ArrayList<>();
        tlistweather.add((TextView) views.get(0).findViewById(R.id.weather1));
        tlistweather.add((TextView) views.get(0).findViewById(R.id.weather2));
        tlistweather.add((TextView) views.get(0).findViewById(R.id.weather3));
        tlistweather.add((TextView) views.get(1).findViewById(R.id.weather4));
        tlistweather.add((TextView) views.get(1).findViewById(R.id.weather5));
        tlistweather.add((TextView) views.get(1).findViewById(R.id.weather6));
        tlistwind = new ArrayList<>();
        tlistwind.add((TextView) views.get(0).findViewById(R.id.wind1));
        tlistwind.add((TextView) views.get(0).findViewById(R.id.wind2));
        tlistwind.add((TextView) views.get(0).findViewById(R.id.wind3));
        tlistwind.add((TextView) views.get(1).findViewById(R.id.wind4));
        tlistwind.add((TextView) views.get(1).findViewById(R.id.wind5));
        tlistwind.add((TextView) views.get(1).findViewById(R.id.wind6));
        weekList = new ArrayList<>();
        weekList.add(tlistweek);
        weekList.add(imagelist);
        weekList.add(tlisttem);
        weekList.add(tlistweather);
        weekList.add(tlistwind);
    }

    void updateTodaweather(TodayWeather todayWeather) {
        city_name_Tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度:" + todayWeather.getHumid());
        pmDataTv.setText(todayWeather.getPm25());
        if (todayWeather.getPm25() != null) {
            int PM25 = Integer.parseInt(todayWeather.getPm25());
            if (PM25 <= QULITY_GOOD) {
                pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
            } else if (PM25 <= QULITY_LITTLE) {
                pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
            } else if (PM25 <= QULITY_MIDDLE) {
                pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
            } else if (PM25 <= QULITY_HIGH) {
                pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
            } else if (PM25 <= QULITY_SERIOUS) {
                pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
            } else {
                pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
            }
        }

        pmQualityTv.setText(todayWeather.getQuality());
        if (todayWeather.getLow() != null) {
            Log.d("cccccc", todayWeather.getLow() + todayWeather.getHigh());
            temperatureTv.setText(todayWeather.getLow().substring(3, todayWeather.getLow().length()) + "～" + todayWeather.getHigh().substring(3, todayWeather.getHigh().length()));
        } else {
            temperatureTv.setText(null);
        }
        Log.d("weather", temperatureTv.getText().toString());
        tempTv.setText("温度:" + todayWeather.getTemp() + "℃");
        weekTv.setText(todayWeather.getDate());
        climateTv.setText(todayWeather.getType());
        windTv.setText(todayWeather.getWinderection() + ":" + todayWeather.getWindpow());
        switch (todayWeather.getType()) {
            case "晴":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
                break;
            case "多云":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
                break;
            case "雾":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
                break;
            case "沙尘暴":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
                break;
            case "雷阵雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
                break;
            case "雷阵雨冰雹":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
                break;
            case "小雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
                break;
            case "大雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
                break;
            case "暴雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
                break;
            case "大暴雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
                break;
            case "特大暴雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
                break;
            case "大雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
                break;
            case "暴雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
                break;
            case "阵雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
            default:
                break;

        }
        Toast.makeText(Weather.this, "更新成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int a = 0; a < views.size(); a++) {
            if (a == position) {
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            } else {
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}


