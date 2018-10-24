package com.example.linsen.weather.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.example.linsen.weather.bean.CityBean;
import com.example.linsen.weather.db.CityDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by linsen on 2018/10/20.
 */
// Context是个抽象类,通过类的结构可以看到:Activity、Service、Application都是Context的子类
public class MyApplication extends Application {
    private static final String TAG="MyAPP";
    private static MyApplication myApplication;
    private CityDB cityDB;
    private List<CityBean>cityList;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"myApplicatin->onCreate()");
        myApplication=this;
        cityDB=openCityDB();
        initCityList();
    }
    public static MyApplication getInstance(){
        return myApplication;
    }
    private CityDB openCityDB(){
        String pathfolder="/data"+ Environment.getDataDirectory().getAbsolutePath()+File.separator+getPackageName()+File.separator+"database1"+File.separator;
        String path=pathfolder+CityDB.CITY_DB_NAME;
        File db=new File(path);
        Log.d(TAG,path);
        if(!db.exists()){
            File dirFirstFolder=new File(pathfolder);
            if(!dirFirstFolder.exists()){
                dirFirstFolder.mkdir();
                Log.i("MyApp","mkdirs");
            }
            Log.i("MyApp","db is not exists");
            try {
                InputStream inputStream=getAssets().open("city.db");
                //getAssets()返回AssetManager对象，Android 应用程序内置资源放到assets文件夹下，assets是专用目录
                FileOutputStream fileOutputStream=new FileOutputStream(db);
                int len=-1;
                byte[] buffer=new byte[1024];
                while((len=inputStream.read(buffer))!=-1){
                    fileOutputStream.write(buffer,0, len);
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this,path);
    }
    private void initCityList(){
        cityList=new ArrayList<CityBean>();
        new Thread(new Runnable(){
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }
    private boolean prepareCityList(){
        cityList=cityDB.getAllCity();
        int i=0;
        for(CityBean cityBean:cityList){
            i++;
            String cityName=cityBean.getCity();
            String cityCode=cityBean.getNumber();
            Log.d(TAG,cityCode+":"+cityName);
        }
        Log.d(TAG,"i="+i);
        return true;
    }

    public List<CityBean> getCityList() {
        return cityList;
    }
}
