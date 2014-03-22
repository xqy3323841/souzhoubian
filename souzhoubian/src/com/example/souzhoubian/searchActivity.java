package com.example.souzhoubian;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: x
 * Date: 14-3-19
 * Time: 上午9:44
 * To change this template use File | Settings | File Templates.
 */
public class searchActivity extends Activity {
    private int page = 10;
    private ImageButton goBack,search;
    private EditText editText;
    private String text;
    private String old;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private final String CITY_CODE = "0029";
    private double nowLongitude;
    private double nowLatitude;
    private HttpGet request = null;
    private final DefaultHttpClient client = new DefaultHttpClient();
    private ProgressDialog progressDialog;
    private String resultStr;
    private String name;
    private String telephone;
    private String address;
    private int a = 0;
    private SimpleAdapter adapter;
    private double shopLongitude;
    private double shopLatitude;
    private double distance;
    private static double DEF_PI = 3.14159265359; // PI
    private static double DEF_2PI = 6.28318530712; // 2*PI
    private static double DEF_PI180 = 0.01745329252; // PI/180.0
    private static double DEF_R = 6370693.5; // radius of earth
    private ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        goBack = (ImageButton) findViewById(R.id.search_go_back);
        search = (ImageButton) findViewById(R.id.getsearch);
        editText= (EditText) findViewById(R.id.search_editText);
        listView = (ListView) findViewById(R.id.search_listview);
        nowLongitude = Double.parseDouble(getIntent().getStringExtra("weidu"));
        nowLatitude = Double.parseDouble(getIntent().getStringExtra("jingdu"));
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(searchActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = editText.getText().toString();
                 if (text == null&&text.equals("")){
                     Toast.makeText(searchActivity.this,"请输入搜索内容",Toast.LENGTH_LONG).show();
                 }else{
                     search();
                 }
                adapter= new SimpleAdapter (searchActivity.this, list, R.layout.infolist,
                         new String[]{"name", "address", "distance"},
                         new int[]{R.id.textInfo1, R.id.textInfo2, R.id.textInfo3});
                listView.setAdapter(adapter);
                old = text;
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i > list.size()) {
                            Toast.makeText(searchActivity.this, "服务器繁忙,请重试..", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Map<String, Object> map = list.get(i - 1);
                        Intent intent = new Intent(searchActivity.this, baiduMapActivity.class);
                        intent.putExtra("shopLatitude", map.get("shopLatitude").toString());
                        intent.putExtra("shopLongitude", map.get("shopLongitude").toString());
                        intent.putExtra("nowLongitude", nowLongitude + "");
                        intent.putExtra("nowLatitude", nowLatitude + "");
                        intent.putExtra("size",list.size()+"");
                        intent.putExtra("address",map.get("address")+"");
                        intent.putExtra("myaddress",getIntent().getStringArrayExtra("myaddress")+"");
                        intent.putExtra("distance",map.get("distance")+"");
                        Log.d("", shopLatitude + " 商店 " + shopLongitude + " 自己 " + nowLatitude + "  " + nowLongitude);
                        startActivity(intent);
                    }
                });
            }
        });
    }
    public void search() {
        page = 5;
        try {
            text = URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("", e.getMessage().toString());
        }
        if (!text.equals(old)) {
            list.clear();
        } else {
            return;
        }
        final String url = "https://api.weibo.com/2/location/pois/search/by_geo.json?city="
                + CITY_CODE + "&" + "q=" + text + "&" +
                "access_token=2.003bqBkC0rLoqt8775fb7bffR7utyC" +
                "&" + "count=" + page + "&" + "range=" + "5000" + "&" + "coordinate="
                + nowLongitude + "" + "," + nowLatitude + "";
        Log.d("souzhoubianurl", url);
        request = new HttpGet(url);
        int timeout = 30000;
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(params, timeout);
        HttpConnectionParams.setConnectionTimeout(params, timeout);

        client.setParams(params);
        AsyncTask<Integer, Integer, Integer> task = new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                requestHttp(request);
                return 0;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                progressDialog.dismiss();
                Toast.makeText(searchActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                jsonbt();
            }

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(searchActivity.this);
                progressDialog.setMessage("加载中...");
                progressDialog.show();
                super.onPreExecute();
            }

        };
        task.execute(0);
    }
    public void requestHttp(HttpGet request) {
        try {
            HttpResponse response = client.execute(request);
            InputStream inputStream = response.getEntity().getContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int length;
            while ((length = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, length);
            }
            outputStream.close();
            inputStream.close();
            resultStr = outputStream.toString("UTF-8");
            Log.d("", resultStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void jsonbt() {
        a = 0;
        adapter.notifyDataSetChanged();
        try {
            JSONObject rootJsonObject = new JSONObject(resultStr);
            Log.d("", rootJsonObject.toString());
            JSONArray poilistJsonArray = rootJsonObject.optJSONArray("poilist");
            if (poilistJsonArray == null) {
                Toast.makeText(this, "查不到结果哦", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = a; i < poilistJsonArray.length(); i++) {
                JSONObject poiJsonObject = (JSONObject) poilistJsonArray.get(i);
                name = (String) poiJsonObject.get("name");
                address = (String) poiJsonObject.get("address");
                telephone = (String) poiJsonObject.get("tel");
                shopLongitude = Double.parseDouble((String) poiJsonObject.get("x"));
                shopLatitude = Double.parseDouble((String) poiJsonObject.get("y"));
                distance = GetShortDistance(shopLatitude, shopLongitude, nowLatitude, nowLongitude);

                Log.d("", name + address + telephone);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", name);
                map.put("address", address);
                if (distance > 1000) {
                    distance = distance / 1000;
                    map.put("distance", (int) distance + "km");
                } else {
                    map.put("distance", (int) distance + "m");
                }
                map.put("shopLongitude", shopLongitude);
                map.put("shopLatitude", shopLatitude);
                map.put("nowLongitude", nowLongitude);
                map.put("nowLatitude", nowLatitude);
                list.add(map);
            }

            a += 5;
        } catch (JSONException e) {
            Log.e("" + "error", e.getMessage().toString());
        }
    }
    public double GetShortDistance(double lon1, double lat1, double lon2, double lat2) {
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
            dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
            dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }
}
