package com.example.souzhoubian;

/**
 * Created with IntelliJ IDEA.
 * User: x
 * Date: 14-3-22
 * Time: 上午11:29
 * To change this template use File | Settings | File Templates.
 */


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wzg on 14-3-18.
 */
public class baiduMapActivity extends Activity {

    private MapView mapView;
    private MapController controller;
    private Double nowLongitude;
    private Double nowLatitude;
    private final String TAG = "wawawawa";
    private Double shopLatitude;
    private Double shopLongitude;
    private MKSearch mkSearch;
    private RouteOverlay routeOverlay;
    private MapController mapController;
    private int maxLat = 0;
    private int minLat = 0;
    private int maxLon = 0;
    private int minLon = 0;
    private HttpGet request;
    private DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
    private String resultStr;
    private int a = 0;
    private List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
    private PoiOverlay poiOverlay;
    private String name;
    private Button btWalk;
    private Button btBus;
    private Button btCar;
    private MKPlanNode startNode;
    private MKPlanNode endNode;
    private String address;
    private String myAddress;
    private LayoutInflater layoutInflater;
    private PopupOverlay popupOverlay;
    private int selectedPoiItemIndex;
    private String tel;
    private int size = 0;
    private TextView distance;

    private static double DEF_PI = 3.14159265359; // PI
    private static double DEF_2PI = 6.28318530712; // 2*PI
    private static double DEF_PI180 = 0.01745329252; // PI/180.0
    private static double DEF_R = 6370693.5; // radius of earth
    private Double time;
    private TextView telphont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.baiduview);

        layoutInflater = LayoutInflater.from(this);
        init();
        doReceviceLocation();
    }

    public void init() {
        Log.d(TAG, getIntent().getStringExtra("nowLatitude") + "");
        btWalk = (Button) findViewById(R.id.btWalk);
        btBus = (Button) findViewById(R.id.btBus);
        btCar = (Button) findViewById(R.id.btCar);
//        size = Integer.parseInt(getIntent().getStringExtra("size"));
        size = 10;
        telphont = (TextView)findViewById(R.id.tel);
        distance = (TextView)findViewById(R.id.distance);

        mkSearch = new MKSearch();
        //获取自己所在的位置
        nowLatitude = Double.parseDouble(getIntent().getStringExtra("nowLatitude"));
        nowLongitude = Double.parseDouble(getIntent().getStringExtra("nowLongitude"));
        //获取已选择商户的位置
        shopLatitude = Double.parseDouble(getIntent().getStringExtra("shopLatitude"));
        shopLongitude = Double.parseDouble(getIntent().getStringExtra("shopLongitude"));
        mapView = (MapView) findViewById(R.id.mapView1);
        GeoPoint startPoint = new GeoPoint((int) (nowLatitude * 1E6), (int) (nowLongitude * 1E6));
        GeoPoint endPoint = new GeoPoint((int) (shopLatitude * 1E6), (int) (shopLongitude * 1E6));

        time  = GetShortDistance(nowLongitude,nowLatitude,shopLongitude,shopLatitude);



        //添加覆盖物涂层
//        OverlayItem item1 = new OverlayItem(startPoint,"item1","item1");
//        OverlayItem item2 = new OverlayItem(endPoint,"item2","item2");
//        Drawable mark1 = getResources().getDrawable(R.drawable.ic_loc_from);
//        Drawable mark2 = getResources().getDrawable(R.drawable.ic_loc_to);
//        item2.setMarker(mark2);
//        PoiOverlay myPoiOverlay = new PoiOverlay(mark1,mapView);
//        mapView.getOverlays().add(myPoiOverlay);
//        myPoiOverlay.addItem(item1);
//        myPoiOverlay.addItem(item2);
        controller = mapView.getController();
        controller.setZoom(12);

        startNode = new MKPlanNode();
        startNode.pt = startPoint;

        endNode = new MKPlanNode();
        endNode.pt = endPoint;
        controller.setCenter(endPoint);

        mapController = mapView.getController();

        if (nowLatitude > shopLatitude) {
            maxLat = (int) (nowLatitude * 1E6);
            minLat = (int) (shopLatitude * 1E6);
        } else {
            minLat = (int) (nowLatitude * 1E6);
            maxLat = (int) (shopLatitude * 1E6);
        }

        if (nowLongitude > shopLongitude) {
            maxLon = (int) (nowLongitude * 1E6);
            minLon = (int) (shopLongitude * 1E6);
        } else {
            maxLon = (int) (shopLongitude * 1E6);
            minLon = (int) (nowLongitude * 1E6);
        }

        mkSearch.init(BaiduMapApplication.getInstance().mBMapManager, new MKSearchListener() {
            @Override
            public void onGetPoiResult(MKPoiResult mkPoiResult, int i, int i2) {

            }

            @Override
            public void onGetTransitRouteResult(MKTransitRouteResult mkTransitRouteResult, int i) {

            }

            @Override
            public void onGetDrivingRouteResult(MKDrivingRouteResult mkDrivingRouteResult, int i) {
                displayDrivingRoute(mkDrivingRouteResult,i);
            }

            @Override
            public void onGetWalkingRouteResult(MKWalkingRouteResult mkWalkingRouteResult, int i) {
                Log.d(TAG, "onGetWalkingRouteResult " + mkWalkingRouteResult);
                displayRoute(mkWalkingRouteResult, i);
            }

            @Override
            public void onGetAddrResult(MKAddrInfo mkAddrInfo, int i) {

            }

            @Override
            public void onGetBusDetailResult(MKBusLineResult mkBusLineResult, int i) {
                displayBus(mkBusLineResult, i);
            }

            @Override
            public void onGetSuggestionResult(MKSuggestionResult mkSuggestionResult, int i) {

            }

            @Override
            public void onGetPoiDetailSearchResult(int i, int i2) {

            }

            @Override
            public void onGetShareUrlResult(MKShareUrlResult mkShareUrlResult, int i, int i2) {

            }
        });

        address = getIntent().getStringExtra("address");
        myAddress = getIntent().getStringExtra("myaddress");
        btWalk.setBackgroundColor(Color.CYAN);
        mkSearch.walkingSearch("西安", startNode, "西安", endNode);
        if(time<1000){
            distance.setText("你距离目的地还有"+((int)((time*1000)/1000))+"m,"+"大约需要"+(int)(time/50)+"分钟");
        }else if (time>1000){
            distance.setText("你距离目的地还有"+(int)(time/1000)+"km"+"大约需要"+(int)((time/50)/60)+"小时");
        }

        btWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(baiduMapActivity.this,"步行模式",Toast.LENGTH_SHORT).show();
                btWalk.setBackgroundColor(Color.CYAN);
                btBus.setBackgroundColor(Color.WHITE);
                btCar.setBackgroundColor(Color.WHITE);
                mkSearch.walkingSearch("西安", startNode, "西安", endNode);
                if(time<1000){
                    distance.setText("你距离目的地还有"+((int)((time*1000)/1000))+"m,"+"大约需要"+(int)(time/50)+"分钟");
                }else if (time>1000){
                    distance.setText("你距离目的地还有"+(int)(time/1000)+"km"+"大约需要"+(int)((time/50)/60)+"小时");
                }
            }
        });

        btCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(baiduMapActivity.this,"驾车模式",Toast.LENGTH_SHORT).show();
                btCar.setBackgroundColor(Color.CYAN);
                btWalk.setBackgroundColor(Color.WHITE);
                btBus.setBackgroundColor(Color.WHITE);
                mkSearch.drivingSearch("西安",startNode,"西安",endNode);
                if(time<1000){
                    distance.setText("你距离目的地还有"+((int)((time*1000)/1000))+"m,"+"大约需要"+(int)(time/500)+"分钟");
                }else if (time>1000){
                    distance.setText("你距离目的地还有"+(int)(time/1000)+"km"+"大约需要"+(int)((time/500)/60)+"小时");
                }
            }
        });

        btBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(baiduMapActivity.this,"公交模式",Toast.LENGTH_SHORT).show();
                btWalk.setBackgroundColor(Color.WHITE);
                btBus.setBackgroundColor(Color.CYAN);
                btCar.setBackgroundColor(Color.WHITE);
                mkSearch.busLineSearch(myAddress,address);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mapController.zoomToSpan(Math.abs(maxLat - minLat), Math.abs(maxLon - minLon));
                mapController.animateTo(new GeoPoint((maxLat + minLat) / 2, (maxLon + minLon) / 2));
            }
        }, 100);

        popupOverlay = new PopupOverlay(mapView, new PopupClickListener() {
            @Override
            public void onClickedPopup(int i) {
                //Toast.makeText(MainActivity.this, "You click popup", Toast.LENGTH_SHORT).show();
                PoiItem item = (PoiItem) poiOverlay.getItem(selectedPoiItemIndex);

                Log.d(getClass().getName(), "Click item at " + i + ", title " + item.getTitle());

//                GeoPoint startPoint = new GeoPoint((int) (currentLocation.getLatitude() * 1E6), (int) (currentLocation.getLongitude() * 1E6));
//                MKPlanNode startNode = new MKPlanNode();
//                startNode.pt = startPoint;
//
//                GeoPoint endPoint = item.getPoint();
//                MKPlanNode endNode = new MKPlanNode();
//                endNode.pt = endPoint;
//
//
//                search.walkingSearch(currentLocation.getCity(), startNode, currentLocation.getCity(), endNode);
            }
        });

        mapView.refresh();
    }

    private void displayBus(MKBusLineResult mkBusLineResult,int i ){
        if (routeOverlay != null) {
            mapView.getOverlays().remove(routeOverlay);
        }

        routeOverlay = new RouteOverlay(baiduMapActivity.this, mapView);
        // 此处仅展示一个方案作为示例
        MKRoute route = mkBusLineResult.getBusRoute();
        //route.getArrayPoints();
        routeOverlay.setData(mkBusLineResult.getBusRoute());
        //清除其他图层
        //mMapView.getOverlays().clear();

        //添加路线图层
        mapView.getOverlays().add(routeOverlay);
        //执行刷新使生效
        mapView.refresh();
    }

    private void displayRoute(MKWalkingRouteResult mkWalkingRouteResult, int i) {
        if (routeOverlay != null) {
            mapView.getOverlays().remove(routeOverlay);
        }
        routeOverlay = new RouteOverlay(baiduMapActivity.this, mapView);
        // 此处仅展示一个方案作为示例
        MKRoute route = mkWalkingRouteResult.getPlan(0).getRoute(0);
        //route.getArrayPoints();
        routeOverlay.setData(mkWalkingRouteResult.getPlan(0).getRoute(0));
        //清除其他图层
        //mMapView.getOverlays().clear();

        //添加路线图层
        mapView.getOverlays().add(routeOverlay);
        //执行刷新使生效
        mapView.refresh();
        // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
        //mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
        //移动地图到起点
        //mapView.getController().animateTo(res.getStart().pt);
    }

    private void displayDrivingRoute(MKDrivingRouteResult mkDrivingRouteResult, int i){
        if (routeOverlay != null) {
            mapView.getOverlays().remove(routeOverlay);
        }

        routeOverlay = new RouteOverlay(baiduMapActivity.this, mapView);
        // 此处仅展示一个方案作为示例
        MKRoute route = mkDrivingRouteResult.getPlan(0).getRoute(0);
        //route.getArrayPoints();
        routeOverlay.setData(mkDrivingRouteResult.getPlan(0).getRoute(0));
        //清除其他图层
        //mMapView.getOverlays().clear();

        //添加路线图层
        mapView.getOverlays().add(routeOverlay);
        //执行刷新使生效
        mapView.refresh();
    }

    public void doReceviceLocation() {
        final String url = "https://api.weibo.com/2/location/pois/search/by_geo.json?coordinate="
                + nowLongitude + "" + "," + nowLatitude + "&access_token=2.003bqBkC0rLoqt8775fb7bffR7utyC&count="+size;
        Log.d(TAG, url);
        request = new HttpGet(url);
        int timeout = 30000;
        HttpParams params = new BasicHttpParams();

        HttpConnectionParams.setSoTimeout(params, timeout);
        HttpConnectionParams.setConnectionTimeout(params, timeout);

        defaultHttpClient.setParams(params);
        AsyncTask<Integer, Integer, Integer> task = new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                requestHttp(request);
                return 0;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                Toast.makeText(baiduMapActivity.this, "请稍等", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"onPostExecute is finish");
                jsonbt();
                displayPoi();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

        };
        task.execute(0);
        Log.d(TAG,"you is doReceviceLocation");
    }

    public void jsonbt() {
        try {
            Log.d(TAG,"BaiduActivity jsonbt");
            JSONObject rootJsonObject = new JSONObject(resultStr);
            Log.d(TAG, rootJsonObject.toString());
            JSONArray poilistJsonArray = rootJsonObject.optJSONArray("poilist");
            if (poilistJsonArray == null) {
                Toast.makeText(this, "查不到结果哦", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = a; i < poilistJsonArray.length(); i++) {
                JSONObject poiJsonObject = (JSONObject) poilistJsonArray.get(i);
                shopLongitude = Double.parseDouble((String) poiJsonObject.get("x"));
                shopLatitude = Double.parseDouble((String) poiJsonObject.get("y"));
                tel = poiJsonObject.getString("tel");
                Map<String, Object> map = new HashMap<String, Object>();
                name = (String) poiJsonObject.get("name");
                map.put("shopLongitude", shopLongitude);
                map.put("shopLatitude", shopLatitude);
                map.put("nowLongitude", nowLongitude);
                map.put("nowLatitude", nowLatitude);
                map.put("tel",tel);
                map.put("name", name);
                list.add(map);
            }
            Log.d(TAG, list.size() + "  BaiduActivity的list的大小  ");
            a += 5;
        } catch (JSONException e) {
            Log.e(TAG + "error", e.getMessage().toString());
        }
    }


    public void requestHttp(HttpGet request) {
        try {
            HttpResponse response = defaultHttpClient.execute(request);
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
            Log.d(TAG,"" + resultStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        mapView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    public void displayPoi() {
        Drawable mark1 = getResources().getDrawable(R.drawable.ic_loc_normal);
        poiOverlay = new PoiOverlay(mark1,mapView);
        for (int i = 0; i < list.size(); i++) {
            GeoPoint p = new GeoPoint((int) (Double.parseDouble(list.get(i).get("shopLatitude")+"") * 1E6),
                    (int) ((Double.parseDouble(list.get(i).get("shopLongitude")+"") * 1E6)));
            Log.d(TAG,p.getLatitudeE6()+" and "+ p.getLongitudeE6());
            PoiItem item1 = new PoiItem(p,"item1","item1");
            item1.setMarker(mark1);
            poiOverlay.addItem(item1);
        }
        mapView.getOverlays().add(poiOverlay);
        mapView.refresh();
    }


    class PoiOverlay extends ItemizedOverlay {

        public PoiOverlay(Drawable drawable, MapView mapView) {
            super(drawable, mapView);
        }

        @Override
        public boolean onTap(GeoPoint geoPoint, MapView mapView) {
            Log.d(TAG,geoPoint.getLatitudeE6()+"  this is onTap");
            return super.onTap(geoPoint, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            Log.d(TAG,i+"  点");
            selectedPoiItemIndex = i;
            PoiItem item = (PoiItem) poiOverlay.getItem(i);
            View popup = layoutInflater.inflate(R.layout.popup1,null);
            TextView titleTextView = (TextView) popup.findViewById(R.id.titleTextView);
            titleTextView.setText((String)list.get(i).get("name"));
            TextView snippetTextView = (TextView) popup.findViewById(R.id.snippetTextView);
            snippetTextView.setText((String)list.get(i).get("address"));
            if(list.get(i).get("tel")==null||list.get(i).get("tel").equals("")){
                telphont.setText("暂无电话");
            }else {
                telphont.setText(list.get(i).get("tel") + "");
            }

            popupOverlay.showPopup(popup,item.getPoint(),item.getMarker().getIntrinsicHeight());

            return super.onTap(i);
        }
    }

    class PoiItem extends OverlayItem {
        public static final int TYPE_AIRPORT = 1;
        public static final int TYPE_ATTRACTION = 2;
        public static final int TYPE_ENTERTAINMENT = 3;
        public static final int TYPE_RESTAURANT = 4;
        public static final int TYPE_SHOPPING = 5;
        public static final int TYPE_TRANSPORTATION = 6;

        private int type;

        public PoiItem(GeoPoint geoPoint, String s, String s2) {
            super(geoPoint, s, s2);
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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
