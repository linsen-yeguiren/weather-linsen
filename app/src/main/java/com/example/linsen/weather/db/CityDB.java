package com.example.linsen.weather.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.linsen.weather.bean.CityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linsen on 2018/10/20.
 */

public class CityDB {
    private SQLiteDatabase db;
    public static final String CITY_DB_NAME = "city.db";
    private static final String CITY_TABLE_NAME = "city";

    public CityDB(Context context, String path) {
        db = context.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
    }

    public List<CityBean> getAllCity() {
        List<CityBean> list = new ArrayList<CityBean>();
        Cursor c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME, null);
        while (c.moveToNext()) {
            String province = c.getString(c.getColumnIndex("province"));
            String city = c.getString(c.getColumnIndex("city"));
            String number = c.getString(c.getColumnIndex("number"));
            String allPY = c.getString(c.getColumnIndex("allpy"));
            String allFirstPY = c.getString(c.getColumnIndex("allfirstpy"));
            String firstPY = c.getString(c.getColumnIndex("firstpy"));
            CityBean item = new CityBean(province, city, number, firstPY, allPY, allFirstPY);
            list.add(item);
        }
        return list;
    }
}
