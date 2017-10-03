package com.example.yy.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.yy.coolweather.gson.Weather;
import com.example.yy.coolweather.util.HttpUtil;
import com.example.yy.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
后台自动更新天气
*/
public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return  null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      updateWeather();
        updateBingPic();
        AlarmManager alarmClock= (AlarmManager) getSystemService(ALARM_SERVICE);

        int anHour=8*60*60*1000;//8个小时
        Long tringgerAtTime= SystemClock.elapsedRealtime()+anHour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        alarmClock.cancel(pi);
        alarmClock.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,tringgerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }
/*
    更新每日一图*/
    private void updateBingPic() {

        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String bingPic=response.body().string();
                SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bingPic",bingPic);
                editor.apply();
            }
        });
    }

/*
    更新天气信息
*/
    private void updateWeather() {
    SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
        String weatherString=prefs.getString("weather",null);
        if (weatherString!=null){
            //有缓存直接解析天气数据
            Weather weather= Utility.handleWeatherResponse(weatherString);
            String weatherId=weather.basic.weatherId;
            String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=7c6f7ed1b2e749a688a2f858294281cd";
            HttpUtil.sendHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseText=response.body().string();
                    Weather weather=Utility.handleWeatherResponse(responseText);
                    if (weather!=null & "ok".equals(weather.status)){
                        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }
    }
}
