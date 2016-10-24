package net.diskroom.weather;

import java.net.URLEncoder;
import java.security.Provider;
import java.util.ArrayList;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import android.location.Criteria;
import android.location.LocationManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int foreacastLength = 4;       //天气预报面板下 预报天气的最大天数
    private static final int UPDATE_WEATHER = 1;        //修改天气主面板的消息类型

    private TextView todayDateInfo;

    private TextView todayWeekday;
    private TextView todayWindDirection;
    private TextView todayRainRate;
    private TextView todayDate;

    private TextView tomorrow;
    private TextView SW;
    private TextView EightPercent;
    private TextView date;

    private TextView tomorrow2;
    private TextView SW2;
    private TextView EightPercent2;
    private TextView date2;

    private TextView tomorrow3;
    private TextView SW3;
    private TextView EightPercent3;
    private TextView date3;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_WEATHER) {
                todayDateInfo = (TextView) findViewById(R.id.todayDateInfo);

                todayWeekday = (TextView) findViewById(R.id.todayWeekday);
                todayWindDirection = (TextView) findViewById(R.id.todayWindDirection);
                todayRainRate = (TextView) findViewById(R.id.todayRainRate);
                todayDate = (TextView) findViewById(R.id.todayDate);

                tomorrow = (TextView) findViewById(R.id.tomorrow);
                SW = (TextView) findViewById(R.id.SW);
                EightPercent = (TextView) findViewById(R.id.EightPercent);
                date = (TextView) findViewById(R.id.date);

                tomorrow2 = (TextView) findViewById(R.id.tomorrow2);
                SW2 = (TextView) findViewById(R.id.SW2);
                EightPercent2 = (TextView) findViewById(R.id.EightPercent2);
                date2 = (TextView) findViewById(R.id.date2);

                tomorrow3 = (TextView) findViewById(R.id.tomorrow3);
                SW3 = (TextView) findViewById(R.id.SW3);
                EightPercent3 = (TextView) findViewById(R.id.EightPercent3);
                date3 = (TextView) findViewById(R.id.date3);

                ArrayList<weatherCast> forecast = (ArrayList<weatherCast>) msg.obj;

                todayDateInfo.setText(forecast.get(0).getWeekdayInfo() + " " + forecast.get(0).getDate() + ", " + forecast.get(0).getYear());

                todayWeekday.setText(forecast.get(0).getWeekday());
                todayWindDirection.setText(forecast.get(0).getDir());
                todayRainRate.setText(forecast.get(0).getPop());
                todayDate.setText(forecast.get(0).getDate());

                tomorrow.setText(forecast.get(1).getWeekday());
                SW.setText(forecast.get(1).getDir());
                EightPercent.setText(forecast.get(1).getPop());
                date.setText(forecast.get(1).getDate());

                tomorrow2.setText(forecast.get(2).getWeekday());
                SW2.setText(forecast.get(2).getDir());
                EightPercent2.setText(forecast.get(2).getPop());
                date2.setText(forecast.get(2).getDate());

                tomorrow3.setText(forecast.get(3).getWeekday());
                SW3.setText(forecast.get(3).getDir());
                EightPercent3.setText(forecast.get(3).getPop());
                date3.setText(forecast.get(3).getDate());

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_percent);
        //LogUtils.v(this.getWindowManager().getDefaultDisplay().getHeight());
        //LogUtils.v(this.getWindowManager().getDefaultDisplay().getWidth());
        //DisplayMetrics dm = new DisplayMetrics();
        //this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //LogUtils.v(dm.widthPixels);
        //LogUtils.v(dm.heightPixels);
        //LogUtils.v("the threadId is "+Thread.currentThread().getId());
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //获取provider
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);       // 高精度
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
        String provider = lm.getBestProvider(criteria, true);

        //获取location
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(MainActivity.this, "请开启GPS设置以便进行定位", Toast.LENGTH_LONG).show();
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location == null){
            //首先使用gps进行定位 如果获取location失败 则检查wifi 使用network进行定位
            if (!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Toast.makeText(MainActivity.this, "GPS无法正常定位，请在设置中打开基于网络的位置服务进行更精确的定位", Toast.LENGTH_LONG).show();
                return;
            }
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if(location == null){
            Toast.makeText(MainActivity.this,"无法进行定位",Toast.LENGTH_LONG).show();
            return;
        }
        //Toast.makeText(MainActivity.this,location.getLatitude()+":"+location.getLongitude(),Toast.LENGTH_LONG).show();
        //lm.requestLocationUpdates(provider, 1, 1, new wLocationListener());

        final Location finalLocation = location;
        new Thread() {
            public void run() {
                String geoUrl = "http://api.map.baidu.com/geocoder/v2/?ak=846pey6G0prSi7UZQ8NneYAjLwGvj2tf&location="+ finalLocation.getLatitude()+","+ finalLocation.getLongitude()+"&output=json&pois=1&mcode=1&mcode=1F:B9:16:01:95:2B:71:4E:FD:F8:E0:B8:09:07:2E:80:1A:77:6F:A2;net.diskroom.weather";
                String geoInfo = HttpUtils.get(geoUrl);
                try {
                    JSONObject geoObject = new JSONObject(geoInfo);
                    String city = String.valueOf(geoObject.getJSONObject("result").getJSONObject("addressComponent").get("city").getClass());
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

        }.start();

        new Thread() {
            public void run() {
                try {
                    //yahoo weather api
                    String baseUrl = "http://query.yahooapis.com/v1/public/yql";
                    String querySql = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text='London,UK')";
                    String yahooApiUrl = baseUrl + "?q=" + URLEncoder.encode(querySql, "utf-8") + "&format=json";
                    String yahooWeatherInfo = HttpUtils.get(yahooApiUrl);
                    JSONObject yahooJsonObject = new JSONObject(yahooWeatherInfo);
                    JSONArray yahooForecast = yahooJsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast");
                    //LogUtils.v(yahooForecast);

                    //和风天气api
                    String city = "london";
                    String key = "913fead73e804e599aec5aabb63862db";
                    String heApiUrl = "https://api.heweather.com/x3/weather?city=" + city + "&key=" + key;

                    String heWeatherInfo = HttpUtils.get(heApiUrl);
                    JSONObject heJsonObject = new JSONObject(heWeatherInfo);
                    JSONArray heWeather = heJsonObject.getJSONArray("HeWeather data service 3.0");
                    JSONObject heWeather_ = (JSONObject) heWeather.get(0);
                    JSONArray heForecast = heWeather_.getJSONArray("daily_forecast");

                    ArrayList<weatherCast> forecastList = new ArrayList<weatherCast>();         //存放天气预报对象的数组
                    for (int i = 0; i <= foreacastLength; i++) {
                        JSONObject yahooForecastJSONObject = yahooForecast.getJSONObject(i);
                        JSONObject heForecastJSONObject = heForecast.getJSONObject(i);
                        //定义一个 天气预报对象 从Yahoo获取星期和日期 从和风天气获取风向和降水概率
                        weatherCast weatherCastObj = new weatherCast();

                        weatherCastObj.setDate(yahooForecastJSONObject.getString("date"));
                        weatherCastObj.setWeekDay(yahooForecastJSONObject.getString("day"));
                        JSONObject WIND = heForecastJSONObject.getJSONObject("wind");           //风向
                        weatherCastObj.setDir(WIND.getString("dir"));
                        weatherCastObj.setPop(heForecastJSONObject.getString("pop"));

                        forecastList.add(weatherCastObj);
                    }
                    Message msg = new Message();
                    msg.what = UPDATE_WEATHER;
                    msg.obj = forecastList;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


}
