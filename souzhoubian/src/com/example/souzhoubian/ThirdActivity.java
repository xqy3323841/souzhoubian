package com.example.souzhoubian;

import android.app.Activity;
import android.content.Intent;
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
 * Time: 下午3:47
 * To change this template use File | Settings | File Templates.
 */
public class ThirdActivity extends Activity {
      private HashMap<String,String> item;
      private int id;
      private TextView textView;
      private ImageButton go_backbt;
      private ListView listView;
      private String title;
      private String[] Strings;
      private List<HashMap<String,Object>> list;
     private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.third);

        textView= (TextView) findViewById(R.id.third_text);
        go_backbt= (ImageButton) findViewById(R.id.third_go_back);

        listView = (ListView) findViewById(R.id.third_listview);
        list = new ArrayList<HashMap<String,Object>>();
        id = getIntent().getIntExtra("id",0);
        item = (HashMap<String,String>)getIntent().getSerializableExtra("title");
        title = item.get("name");
        textView.setText(title);
        getStrings();
        list = getData("name",Strings,list);

        simpleAdapter = new SimpleAdapter(this,list,R.layout.thirdlistview,new String[]{"name"},new int[]{R.id.third_listview_text});
        listView.setAdapter(simpleAdapter);
        go_backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getStrings(){
        switch (id)//判断上一级点击的是哪一个选项
        {
            case 0:
                if(title.equals("中餐厅")){
                    Strings   = new String[]{"中餐厅","综合酒楼","四川菜（川菜）","广东菜（粤菜）","山东菜（鲁菜)","江苏菜","浙江菜","上海菜","湖南菜（湘菜）"};
                }else if(title.equals("商场")){
                    Strings =new String[]{"购物中心","普通商场","免兑品点"};
                }else if(title.equals("运动场所")){
                    Strings =new String[]{"综合体育馆","保龄球馆","网球场","篮球场","足球场","滑雪场","溜冰场","屋外健身场","海滨浴场","游泳场","健身中心","乒乓球场","台球馆","壁球场","马术俱乐部","赛马场","橄榄球场","羽毛球场","跆拳道场"};
                }else if(title.equals("综合医院")){
                    Strings =new String[]{"三级甲等医院","卫生院"};
                }else if(title.equals("宾馆酒店")){
                    Strings =new String[]{"六星级酒店","五星级酒店","四星级酒店","三星级酒店","经济连锁酒店"};
                }else if(title.equals("博物馆")){
                    Strings =new String[]{"奥迪博物馆","奥迪文化中心"};
                }

                break;
            case 1:
                if(title.equals("外国餐厅")){
                    Strings = new String[]{"西餐厅","日本料理","韩国料理","法式餐厅","意式餐厅","泰国餐厅","地中海风情餐厅","美式餐厅","印度餐厅","英式餐厅","牛扒店","俄国餐厅","德国餐厅","巴西餐厅","墨西哥餐厅","其他"};
                }else if(title.equals("便利店")){
                    Strings =new String[]{"7ELEVEN便利店","OK便利店"};
                }else if(title.equals("高尔夫相关")){
                    Strings =new String[]{"高尔夫球场","高尔夫练习场"};
                }else if(title.equals("专科医院")){
                    Strings =new String[]{"整形美容","口腔医院","眼科医院","耳鼻喉医院","胸科医院","骨科医院","肿瘤医院","脑科医院","妇科医院","精神病医院","传染病医院"};
                }else if(title.equals("旅馆招待所")){
                    Strings =new String[]{"青年旅社"};
                }

                break;
            case 2:
                if(title.equals("快餐厅")){
                    Strings = new String[]{"肯德基","麦当劳","必胜客","永和豆浆","茶餐厅","大家乐","大快活"};
                }else if(title.equals("家电电子卖场")){
                    Strings =new String[]{"综合家电商场","国美","大中","苏宁","手机销售","数码电子","丰泽","镭射"};
                }else if(title.equals("娱乐场所")){
                    Strings =new String[]{"夜总会","KTV","迪厅","酒吧","游戏厅","棋牌室","博采中心","网吧"};
                }else if(title.equals("售票处")){
                    Strings = new String[]{"飞机票代售点","火车票代售点","长途汽车票代售点","船票代售点","公交卡代售点","公园景点售票处"};
                }else if(title.equals("港口码头")){
                    Strings =new String[]{"客运港","车渡口","人渡口"};
                }

                break;
            case 3:
                if(title.equals("超级市场")){
                    Strings =new String[]{"家乐福","沃尔玛","华润","北京华联","上海华联","麦德龙","万客隆","华堂","易初莲花","好又多","屈臣氏","乐购","惠康超市","百佳超市","万宁超市"};
                }else if(title.equals("度假疗养场所")){
                    Strings =new String[]{"度假村","疗养院"};
                }else if(title.equals("邮局")){
                    Strings =new String[]{"邮政快递"};
                }

                break;
            case 4:
                if(title.equals("咖啡厅")){
                    Strings = new String[]{"星巴克","上岛咖啡","巴黎咖啡"};
                }else if(title.equals("花鸟鱼虫市场")){
                    Strings =new String[]{"花卉市场","鸟狗市场"};
                }else if(title.equals("休闲场所")){
                    Strings =new String[]{"游乐场","垂钓园","采摘园","露营地","水上活动中心"};
                }

                break;
            case 5:
                if(title.equals("家居建材市场")){
                    Strings =new String[]{"家具建材综合市场","家具城","建材五金市场","厨卫市场","布艺市场","灯具瓷器市场"};
                }else if(title.equals("影剧院")){
                    Strings =new String[]{"电影院","音乐厅","剧场"};
                }else if(title.equals("电讯营业厅")){
                    Strings =new String[]{"中国电信营业厅","中国网通营业厅","中国移动营业厅","中国联通营业厅","中国铁通营业厅","中国卫通营业厅","和记电讯","数码通电讯","电讯盈科","中国移动万众/Peoples"};
                }else if(title.equals("医药保健相关")){
                    Strings =new String[]{"药房","医疗保健用品"};
                }

                break;
            case 6:
                if(title.equals("综合市场")){
                    Strings =new String[]{"小商品市场","旧货市场","农副产品市场","果品市场","蔬菜市场","水产海鲜市场"};
                }else if(title.equals("事务所")){
                    Strings =new String[]{"律师事务所","会计事务所","评估事务所","审计事务所","认证事务所","专利事务所"};
                }else if(title.equals("动物医疗场所")){
                    Strings =new String[]{"宠物诊所","兽医站"};
                }else if(title.equals("公交站")){
                    Strings =new String[]{"旅游专线车站","普通公交站"};
                }

                break;
            case 8:
                if(title.equals("体育用品店")){
                    Strings =new String[]{"李宁专卖店","耐克专卖店","阿迪达斯专卖店","锐步专卖店","彪马专卖店","高尔夫用品店","户外用品店"};
                }else if(title.equals("停车场")){
                    Strings =new String[]{"室内停车场","室外停车场","停车换乘"};
                }

                break;
            case 9:
                if(title.equals("特色商业街")){
                    Strings =new String[]{"步行街"};
                }

                break;
            case 10:
                if(title.equals("服装鞋帽皮具店")){
                    Strings =new String[]{"品牌服饰店","品牌鞋店","品牌皮具店"};
                }else if(title.equals("传媒机构")){
                    Strings =new String[]{"电视台","电台","报社","杂志社","出版社"};
                }

                break;
            case 11:
                if(title.equals("专卖店")){
                    Strings =new String[]{"古玩字画","珠宝首饰工艺品","钟表店","眼镜店","书店","音像店","儿童用品店","自行车专卖店","礼品手势店","烟酒专卖店","宠物用品店","摄影器材店","宝马生活方式"};
                }else if(title.equals("学校")){
                    Strings =new String[]{"高等院校","中学","小学","幼儿园","成人教育","职业技术学校"};
                }

                break;
            case 12:
                if(title.equals("特殊买卖场所")){
                    Strings =new String[]{"拍卖行","典当行"};
                }

                break;
            case 13:
                if(title.equals("个人用品")){
                    Strings =new String[]{"莎莎"};
                }

                break;
            default:

        }
    }
    private List<HashMap<String,Object>> getData(String Key,String[] Strings,List<HashMap<String,Object>> list){
        for(int i = 0;i<Strings.length;i++){
            HashMap<String,Object> item = new HashMap<String, Object>();
            item.put(Key,Strings[i]);

            list.add(item);
        }
        return list;
    }
}


