package com.example.souzhoubian;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: x
 * Date: 14-3-24
 * Time: 上午8:56
 * To change this template use File | Settings | File Templates.
 */
public class ListViewAdapterThird extends SimpleAdapter{
    LayoutInflater inflater = null;
    Context context;
    List list;

    public ListViewAdapterThird(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);


        TextView text= (TextView) view.findViewById(R.id.third_listview_text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,resultActivity.class);
                intent.putExtra("weidu",MainActivity.lng+"");
                intent.putExtra("jingdu",MainActivity.lat+"");
                intent.putExtra("title",(HashMap)list.get(position));
                context.startActivity(intent);
                Log.d("跳转到",""+position);
            }
        });
        //To change body of overridden methods use File | Settings | File Templates.
        return view;
    }
}
