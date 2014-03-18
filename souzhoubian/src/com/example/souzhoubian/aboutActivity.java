package com.example.souzhoubian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created with IntelliJ IDEA.
 * User: x
 * Date: 14-3-18
 * Time: 下午4:44
 * To change this template use File | Settings | File Templates.
 */
public class aboutActivity extends Activity {
    private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        imageButton = (ImageButton) findViewById(R.id.go_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(aboutActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
