package com.example.linsen.weather.lisener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by linsen on 2018/10/30.
 */

public class EditChangedListener implements TextWatcher{
    private CharSequence temp; // 监听前的文本

    private int editStart; // 光标开始位置

    private int editEnd; // 光标结束位置
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
