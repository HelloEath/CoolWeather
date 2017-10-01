package com.example.yy.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by yy on 2017/10/1.
 */

public class County extends DataSupport {
private int id;//县id
    private String countyName;//县名
    private String weatherId;//县天气
    private int cityId;//县所属市id

}
