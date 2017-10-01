package com.example.yy.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by yy on 2017/10/1.
 */

public class City extends DataSupport {
    private int id;
    private String cityName;//城市名字
    private int cityCode;//城市代号
    private int provinced;//市所属省id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinced() {
        return provinced;
    }

    public void setProvinced(int provinced) {
        this.provinced = provinced;
    }
}
