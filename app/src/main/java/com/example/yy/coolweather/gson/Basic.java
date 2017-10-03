package com.example.yy.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yy on 2017/10/2.
 */

public class Basic {
    @SerializedName("city")
    public  String cityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;
    public class Update{

        @SerializedName("loc")
        public String updateTime;
    }
}
