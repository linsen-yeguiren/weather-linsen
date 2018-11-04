package com.example.linsen.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import static com.example.linsen.weather.R.id.search;

/**
 * Created by linsen on 2018/10/20.
 */
// Context是个抽象类,通过类的结构可以看到:Activity、Service、Application都是Context的子类
public class SelectCity extends Activity implements View.OnClickListener {
    private ListView mList;
    private TextView barTv;
    private EditText editText;
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
        editText = (EditText) findViewById(R.id.search_text);
        mList = (ListView) findViewById(R.id.title_list);
        Button button = (Button) findViewById(search);
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

                String serchText = editText.getText().toString();
                serchData(serchText);
            }
        });
        editText.addTextChangedListener(new EditChangedListener());
        //editText.addTextChangedListener(new );
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
        cityList=searchList;
//        mList.setOnItemClickListener(new AcityList = searchList;dapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
    }
    public class EditChangedListener implements TextWatcher {
        private CharSequence temp; // 监听前的文本

        private int editStart; // 光标开始位置

        private int editEnd; // 光标结束位置
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp=s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String searchtext=editText.getText().toString();
            serchData(searchtext);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
