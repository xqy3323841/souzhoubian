package com.example.souzhoubian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private ImageButton search,setting,locationBt;
    private TextView locationText;
    private ListView listView;
    private List data;
    private ListViewAdapter listViewAdapter;
    private LocationClient locationClient;
    private String getAppstr;
    public  static double lng;//经度
    public  static double lat;//纬度
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        search= (ImageButton) findViewById(R.id.search);
        setting= (ImageButton) findViewById(R.id.setting);
        locationBt= (ImageButton) findViewById(R.id.locationBt);
        locationText= (TextView) findViewById(R.id.locationText);
        listView= (ListView) findViewById(R.id.main_listview);
        getData();
        listViewAdapter = new ListViewAdapter(this,data,R.layout.listview,new String[]{"name"},new int[]{R.id.listview_text});
        listView.setAdapter(listViewAdapter);
        getLocation();


            //跳转至设置
            setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(getAppstr!=null&&!getAppstr.equals("")){
                    Intent intent = new Intent(MainActivity.this, aboutActivity.class);
                    startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this,"正在定位中，请稍候......",Toast.LENGTH_LONG).show();
                    }
                }
            });
            //跳转至搜索
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getAppstr!=null&&!getAppstr.equals("")){
                    Intent intent = new Intent(MainActivity.this,searchActivity.class);
                    intent.putExtra("weidu",lng+"");
                    intent.putExtra("jingdu",lat+"");
                    startActivity(intent);
                    }else{
                    Toast.makeText(MainActivity.this,"正在定位中，请稍候......",Toast.LENGTH_LONG).show();
                    }
                }
            });
            locationBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationText.setText("定位中....");
                    getLocation();
                }
            });


    }

    private void getData(){
        data = new ArrayList();
        HashMap<String,String> item;
        item= new HashMap<String, String>();
        item.put("name","餐饮服务");

        data.add(item);
        item= new HashMap<String, String>();
        item.put("name","购物服务");
        data.add(item);
        item= new HashMap<String, String>();
        item.put("name","生活服务");
        data.add(item);
        item= new HashMap<String, String>();
        item.put("name","体育休闲服务");
        data.add(item);
        item= new HashMap<String, String>();
        item.put("name","医疗保健服务");
        data.add(item);
        item= new HashMap<String, String>();
        item.put("name","住宿服务");
        data.add(item);
        item= new HashMap<String, String>();
        item.put("name","科教文化服务");
        data.add(item);
        item= new HashMap<String, String>();
        item.put("name","交通设施服务");
        data.add(item);
        item= new HashMap<String, String>();
        item.put("name","公交设施");
        data.add(item);


    }
    private void getLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(0);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向

        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                getAppstr=bdLocation.getAddrStr();
                locationText.setText(getAppstr);
                Log.d("souzhoubian", "onReceiveLocation " + getAppstr);
                lng = bdLocation.getLongitude();
                lat = bdLocation.getLatitude();
                BaseApplication.locData.latitude = lat;
                BaseApplication.locData.longitude = lng;
                Log.d("jingweidu",lat+""+lng);
                if (lng==0||lat==0)
                {
                    locationText.setText("定位失败请重新定位");
                }else
                {
                    locationText.setText(getAppstr);
                }
            }

            @Override
            public void onReceivePoi(BDLocation bdLocation) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        locationClient.setLocOption(option);
        locationClient.start();
    }
}
