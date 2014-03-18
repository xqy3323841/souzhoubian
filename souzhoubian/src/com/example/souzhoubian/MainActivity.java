package com.example.souzhoubian;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
    private SimpleAdapter simpleAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        search= (ImageButton) findViewById(R.id.search);
        setting= (ImageButton) findViewById(R.id.setting);
        locationBt= (ImageButton) findViewById(R.id.locationBt);
        locationText= (TextView) findViewById(R.id.locationText);
        listView= (ListView) findViewById(R.id.listview);
        getData();
        simpleAdapter = new SimpleAdapter(this,data,R.layout.listview,new String[]{"name"},new int[]{R.id.id1});
        listView.setAdapter(simpleAdapter);
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
}
