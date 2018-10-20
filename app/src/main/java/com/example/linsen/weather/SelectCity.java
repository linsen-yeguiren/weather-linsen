package com.example.linsen.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by linsen on 2018/10/20.
 */

public class SelectCity extends Activity implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                Intent intent=new Intent();
                intent.putExtra("cityCode","101160101");
                setResult(RESULT_OK,intent);
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
        backView=(ImageView) findViewById(R.id.title_back);
        backView.setOnClickListener(this);
    }
}
