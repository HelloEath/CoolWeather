package com.example.yy.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by yy on 2017/10/1.
 */

public class Province extends DataSupport{
    public Province() {
        super();
    }

    private int id;//省id
    private String provinceName;//省名
    private int provinceCode;//省代号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
