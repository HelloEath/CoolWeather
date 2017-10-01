package com.example.yy.coolweather.util;

import android.text.TextUtils;

import com.example.yy.coolweather.db.City;
import com.example.yy.coolweather.db.County;
import com.example.yy.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yy on 2017/10/1.
 */

public class Utility {

    /*解析和处理服务器返回的省级数据*/
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

/*
    解析和处理服务器返回的市级数据
*/
    public static boolean handleCityResponse(String response,int provinceId){

        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCityes=new JSONArray(response);
                for (int i=0;i<allCityes.length();i++){
                    JSONObject cityObject=allCityes.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinced(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

/*
    解析和处理服务器反悔的县级数据
*/
    public static boolean handleCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){

            try {
                JSONArray allCity=new JSONArray(response);
                for (int i=0;i<allCity.length();i++){
                    JSONObject countyObject=allCity.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();//保存数据到数据库
                }

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;

    }



}
