package com.example.souzhoubian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created with IntelliJ IDEA.
 * User: x
 * Date: 14-3-19
 * Time: 上午9:44
 * To change this template use File | Settings | File Templates.
 */
public class searchActivity extends Activity {
    private ImageButton goBack,search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        goBack = (ImageButton) findViewById(R.id.search_go_back);
        search = (ImageButton) findViewById(R.id.getsearch);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(searchActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
