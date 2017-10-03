package com.example.yy.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yy on 2017/10/2.
 */

public class Now {
    @SerializedName("tmp")//添加注解：json属性映射到实体类属性
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;
    }
}
