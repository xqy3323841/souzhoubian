package com.example.souzhoubian;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: x
 * Date: 14-3-20
 * Time: 上午9:25
 * To change this template use File | Settings | File Templates.
 */
public class SecondActivity extends Activity {
    private int id;
    private HashMap<String,String> item;
    private TextView textView;
    private ImageButton go_backbt;
    private String[] Strings;
    private List<HashMap<String,Object>> list;
    private ListViewAdapterSecond listViewAdapterSecond;
    private ListView listView;
    private boolean[] flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        textView = (TextView) findViewById(R.id.second_text);
        go_backbt = (ImageButton) findViewById(R.id.second_go_back);
        listView = (ListView) findViewById(R.id.second_listview);
        list = new ArrayList<HashMap<String, Object>>();
        list.clear();
        id = getIntent().getIntExtra("id",0);
        item = (HashMap<String, String>) getIntent().getSerializableExtra("title");
        textView.setText(item.get("name"));
        getStrings();
        list = getData("name",Strings,"flag",flag,list);
        listViewAdapterSecond = new ListViewAdapterSecond(this,list,R.layout.listview,new String[]{"name"},new int[]{R.id.listview_text});
        listView.setAdapter(listViewAdapterSecond);
        go_backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
       //To change body of overridden methods use File | Settings | File Templates.
    }

    private void getStrings(){
             switch(id){
                 case 0: Strings = new String[]{"中餐厅","外国餐厅","快餐厅","休闲餐饮场所","咖啡厅","茶艺馆","冷饮店","糕饼店","甜品店"};
                         flag = new boolean[]{true,true,true,false,true,false,false,false,false};break;
                 case 1: Strings = new String[]{"商场","便利店","家电电子卖场","超级市场","花鸟鱼虫市场","家居建材市场","综合市场","文化用品店","体育用品店","特色商业街","服装鞋帽皮具店","专卖店","特殊买卖场所","个人用品"};
                         flag = new boolean[]{true,true,true,true,true,true,true,false,true,true,true,true,true,true};break;
                 case 2: Strings = new String[]{"旅行社","信息咨询中心","售票处","邮局","物流速递","电讯营业厅","事务所","人才市场","自来水营业厅","电力营业厅","美容美发店","维修站点","摄影冲印店","洗浴推拿场所","洗衣店","中介机构","搬家公司","彩票投注站"};
                         flag = new boolean[]{false,false,true,true,false,true,true,false,false,false,false,false,false,false,false,false,false,false};break;
                 case 3: Strings = new String[]{"运动场所","高尔夫相关","娱乐场所","度假疗养场所","休闲场所","影剧院"};
                         flag = new boolean[]{true,true,true,true,true,true};break;
                 case 4: Strings = new String[]{"综合医院","专科医院","诊所","急救中心","疾病预防控制中心","医药保健相关","动物医疗场所"};
                         flag = new boolean[]{true,true,false,false,false,true,true};break;
                 case 5: Strings = new String[]{"宾馆酒店","旅馆招待所"};
                         flag = new boolean[]{true,true};break;
                 case 6: Strings = new String[]{"博物馆","展览馆","会展中心","美术馆","图书馆","科技馆","天文馆","文化馆","档案馆","文艺团体","传媒机构","学校","科研机构","培训机构","驾校"};
                         flag = new boolean[]{true,false,false,false,false,false,false,false,false,false,true,true,false,false,false};break;
                 case 7: Strings = new String[]{"飞机场","火车站","港口码头","长途汽车站","地铁站","轻轨站","公交站","班车站","停车场","过境口岸"};
                         flag = new boolean[]{false,false,true,false,false,false,true,false,true,false};break;
                 case 8: Strings = new String[]{"报刊亭","公用电话","公共厕所","紧急避难场所"};
                         flag = new boolean[]{false,false,false,false};break;
             }
    }
    private List<HashMap<String,Object>> getData(String Key,String[] Strings,String flag,boolean[] flags,List<HashMap<String,Object>> list){
              for(int i = 0;i<Strings.length;i++){
                   HashMap<String,Object> item = new HashMap<String, Object>();
                   item.put(Key,Strings[i]);
                   item.put(flag,flags[i]);
                   list.add(item);
              }
        return list;
    }
}
