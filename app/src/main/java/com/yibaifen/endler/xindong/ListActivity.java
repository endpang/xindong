package com.yibaifen.endler.xindong;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.yibaifen.endler.xindong.model.HomeItem;
import com.yibaifen.endler.xindong.model.HomeAdapter;
import com.yibaifen.endler.xindong.model.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter hMyAdapter;
    OkHttpClient client = new OkHttpClient();
    private Handler handler=null;
    ArrayList<Image> imglist  = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        handler =new Handler();
        getRequest("https://maps.cc/girl/api/mylist.php",1);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {


            hMyAdapter = new HomeAdapter(R.layout.rv_main_item,hinitData(imglist));
            hMyAdapter.openLoadAnimation();
            mRecyclerView.setAdapter(hMyAdapter);
            hMyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Log.d(TAG, "onItemClick: ");
                    Toast.makeText(ListActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
                }
            });


        }

    };
    private List<HomeItem> hinitData(ArrayList<Image> images) {
        List<HomeItem> mDataList = new ArrayList<HomeItem>();

        for(int i = 0;i < images.size(); i ++){
            Image img = images.get(i);
            HomeItem hi = new HomeItem();
            hi.setTitle(img.getUser().getScreen_name());
            hi.setImage(img.getUrl());
            hi.setAva(img.getImage());
            mDataList.add(hi);
        }
        /**
        for (int i = 0; i <= 100; i++) {
            HomeItem hi = new HomeItem();
            hi.setTitle("item:" + i);
            hi.setImage("https://maps.cc/girl/thumb/eac62f8333c741dc0892125f73c7a3b2.jpg");
            mDataList.add(hi);
        }
         //*/
        return  mDataList;
    }
    private void getRequest(String url, final int i) {

        final Request request=new Request.Builder()
                .get()
                .tag(this)
                .url(url)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String content= response.body().string();
                        //Log.i("android","content:" + content);
                        if(i == 1 && content != ""){
                            Gson gson = new Gson();
                            ArrayList<Image> list  = null;
                            if (gson != null) {
                                list = gson.fromJson(content, new TypeToken<ArrayList<Image>>() {
                                }.getType());
                            }
                            //List<Image> strings = gson.fromJson(content, Image[].class);
                            Log.i("str",list.toString());
                            imglist = list;
                            handler.post(runnableUi);
                        }

                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
