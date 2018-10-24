package com.example.linsen.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.linsen.weather.app.MyApplication;
import com.example.linsen.weather.bean.CityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linsen on 2018/10/20.
 */
// Context是个抽象类,通过类的结构可以看到:Activity、Service、Application都是Context的子类
public class SelectCity extends Activity implements View.OnClickListener {
    private ListView mList;
    private TextView barTv;
    private List<CityBean> cityList;
    private ArrayAdapter<String> myAdapter;
    private List<String> filterDataList;
    private MyApplication myApplication;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
//                Intent intent = new Intent();
//                intent.putExtra("cityCode", "101160101");
//                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

    ImageView backView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        backView = (ImageView) findViewById(R.id.title_back);
        backView.setOnClickListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        String cityName = sharedPreferences.getString("main_city_name", "北京");
        barTv = (TextView) findViewById(R.id.title_name);
        barTv.setText("当前城市：" + cityName);
        initViews();
    }

    private void initViews() {
        mList = (ListView) findViewById(R.id.title_list);
        Button button = (Button) findViewById(R.id.search);
        myApplication = MyApplication.getInstance();
        cityList = myApplication.getCityList();
        filterDataList = new ArrayList<>();
        for (CityBean cityBean : cityList) {
            filterDataList.add(cityBean.getCity());
        }
        //myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filterDataList);
        myAdapter = new ArrayAdapter<String>(this, R.layout.list_layout, filterDataList);
        mList.setAdapter(myAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CityBean cityBean = cityList.get(position);
                SharedPreferences settings = getSharedPreferences("shared", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("main_city_name", cityBean.getCity());
                editor.commit();
                Intent intent = new Intent();
                intent.putExtra("cityCode", cityBean.getNumber());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.search_text);
                String serchText = editText.getText().toString();
                serchData(serchText);
            }
        });
    }

    private void serchData(String serchText) {
        List<CityBean> searchList = new ArrayList<>();
        cityList = myApplication.getCityList();
        List<String> nameList = new ArrayList<>();
        for (CityBean city : cityList) {
            if (city.getCity().contains(serchText)) {
                searchList.add(city);
                nameList.add(city.getCity());
            }
        }
        //myAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, nameList);
        myAdapter = new ArrayAdapter<>(this, R.layout.list_layout, nameList);
        mList.setAdapter(myAdapter);

//        mList.setOnItemClickListener(new AcityList = searchList;dapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
    }
}
