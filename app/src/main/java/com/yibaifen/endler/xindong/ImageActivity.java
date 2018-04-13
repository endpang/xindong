package com.yibaifen.endler.xindong;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        String url =getIntent().getStringExtra("url");

        ImageView iv = (ImageView) findViewById(R.id.iv);
        Glide.with(this).load(url).into(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("click:","click");
                finish();
            }
        });

    }
}
