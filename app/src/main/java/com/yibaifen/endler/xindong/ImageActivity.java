package com.yibaifen.endler.xindong;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.VolumeProviderCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.yibaifen.endler.xindong.model.Image;

import java.util.ArrayList;

public class ImageActivity extends Activity {
    //private float mCurrentPosX;
    //private float mCurrentPosY;
    private float mPosX;
    //private float mPosY;
    private int ac;
    private int position;
    private String[] imglist;
    private ImageView iv;
    private AdView mAdView;
    private AdView mAdView2;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        MobileAds.initialize(this, "ca-app-pub-3189177796348267~8730436996");


        mAdView = findViewById(R.id.adView);
        mAdView.invalidate();
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(
                AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
        mAdView2 = findViewById(R.id.adView2);
        mAdView2.invalidate();
        AdRequest adRequest2 = new AdRequest.Builder().addTestDevice(
                AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView2.loadAd(adRequest2);


        Bundle b=this.getIntent().getExtras();
        imglist=b.getStringArray("list");
        String url = b.getString("url");
        position = b.getInt("position");

        iv = (ImageView) findViewById(R.id.iv);
        Glide.with(this).load(url).into(iv);
        FloatingActionButton del = (FloatingActionButton) findViewById(R.id.del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /**
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("click:","click");
                finish();
            }
        });
        /**/
        iv.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event){

                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:


                        ac = 0;
                        mPosX = event.getX();
                        float x = view.getWidth();
                        //mPosY = event.getY();
                        Log.i("width",x+"");
                        Log.i("x:",mPosX+"");
                        Log.i("action:","down");
                        if(mPosX > x/2){
                            ac = 2;
                        }else{
                            ac = 1;
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("action:","up");
                        if((ac == 1) || (ac == 3)){
                            Log.i("action","上一张");
                            position -= 1;
                            if(position < 0){
                                position = 0;
                                break;
                            }
                            Log.i("url",position+"");
                            Glide.with(view).load(imglist[position]).into(iv);
                            //AdRequest adRequest = new AdRequest.Builder().build();
                            //mAdView2.loadAd(adRequest);
                            //Glide.with(position).load(imglist[position]).into(iv);
                            //Glide.with(ImageView.class).load(url).into(iv);
                        }
                        if((ac == 2)|| (ac == 4)){
                            Log.i("action","下一张");
                            position += 1;
                            if(position > (imglist.length - 1)){
                                position = imglist.length;
                                break;
                            }else{

                            }
                            Log.i("position",position+"");
                            Glide.with(view).load(imglist[position]).into(iv);
                            //AdRequest adRequest = new AdRequest.Builder().build();
                            //mAdView.loadAd(adRequest);
                        }
                        break;
                        /**
                    case MotionEvent.ACTION_MOVE:
                        mCurrentPosX = event.getX();
                        mCurrentPosY = event.getY();
                        if (mCurrentPosX - mPosX > 0 && Math.abs(mCurrentPosY - mPosY) < 10) {
                            //Log.e("", "向右");
                            ac = 1;
                        }else if (mCurrentPosX - mPosX < 0 && Math.abs(mCurrentPosY - mPosY) < 10) {
                            //Log.e("", "向左");
                            ac = 2;
                        }else if (mCurrentPosY - mPosY > 0 && Math.abs(mCurrentPosX - mPosX) < 10) {
                            //Log.e("", "向下");
                            ac = 3;
                        }else if (mCurrentPosY - mPosY < 0 && Math.abs(mCurrentPosX - mPosX) < 10) {
                            //Log.e("", "向上");
                            ac = 4;
                        }
                        break;
                         //*/
                    default:
                        break;
                }
                return true;
            }
        });

    }
}
