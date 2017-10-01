package com.example.yy.coolweather;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yy.coolweather.db.City;
import com.example.yy.coolweather.db.County;
import com.example.yy.coolweather.db.Province;
import com.example.yy.coolweather.util.HttpUtil;
import com.example.yy.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yy on 2017/10/1.
 */

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backBtn;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList=new ArrayList<>();
    //省列表
    private List<Province> provinceList;

    //市列表
    private List<City> cityList;

    //县列表
    private List<County> countyList;

    //选中的省份
    private Province setlectPro;

    //选中的城市
    private City selectedCity;

    //当前选中的级别
    private int currentLevel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.choose_area,container,false);
        titleText= (TextView) view.findViewById(R.id.title_text);
        backBtn= (Button) view.findViewById(R.id.back_btn);
        listView= (ListView) view.findViewById(R.id.list_view);
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_expandable_list_item_1,dataList);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel==LEVEL_PROVINCE){
                    setlectPro=provinceList.get(position);
                    queryCity();
                }else if (currentLevel==LEVEL_CITY){
                    
                    selectedCity=cityList.get(position);
                    queryCounties();
                }
            }
        });
        
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel==LEVEL_COUNTY){
                    queryCity();
                }else if (currentLevel==LEVEL_CITY){
                    queryPros();
                }
            }
        });
        queryPros();

    }


/*
    查询全国所有的省，优先从数据库查询，如果没有再到服务器上查询
*/
    private void queryPros() {
        titleText.setText("中国");
        backBtn.setVisibility(View.GONE);
        provinceList= DataSupport.findAll(Province.class);
        if (provinceList.size()>0){
            dataList.clear();
            for (Province Province : provinceList){
                dataList.add(Province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }else{
            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    /*
查询选中省内所有的市，优先从数据库查询，如果没有再从服务器查询
*/
    private void queryCity() {
titleText.setText(setlectPro.getProvinceName());
        backBtn.setVisibility(View.GONE);
        /*从数据区查询数据【帶參數】*/
        cityList=DataSupport.where("provinceid=?",String.valueOf(setlectPro.getId())).find(City.class);
        if (cityList.size()>0){
            dataList.clear();
            for (City city: cityList){
                dataList.add(city.getCityName());//把市名字加入集合
            }
            adapter.notifyDataSetChanged();//通知listview數據改變
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else{
            int provinceCode=setlectPro.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }

    }


    /*
        查询选中市内所有的县，有线从数据库查询，没有再从服务器查寻
    */
    private void queryCounties() {

        titleText.setText(selectedCity.getCityName());
        backBtn.setVisibility(View.GONE);
        countyList=DataSupport.where("cityId=?",String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size()>0){
            dataList.clear();
            for (County county:countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }else{

            int provinceCode=setlectPro.getProvinceCode();
            int cityCode=selectedCity.getCityCode();
            String address="http://guoilin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }

    }



/*
    根据传入的地址和类型从服务器上查询 省 市 县数据
*/
    private void queryFromServer(String address,final String type) {
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加載失敗！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText=response.body().string();
                //Log.d("responseText",responseText);
                Log.d("服務器：responseText",responseText);
                boolean result=false;
                if ("province".equals(type)){
                    result= Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,setlectPro.getId());
                }else if ("county".equals(type)){
                    result=Utility.handleCountyResponse(responseText,selectedCity.getId());

                }
                if (result){

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryPros();
                            }else if ("city".equals(type)){
                                queryCity();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });

    }

    /*
    显示进度条
*/


    private void showProgressDialog() {
        if (progressDialog==null){

            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载----");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
/*
    關閉进度条
*/

    private void closeProgressDialog() {
    if (progressDialog!=null){
        progressDialog.dismiss();
    }

    }




}
