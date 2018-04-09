package com.yibaifen.endler.xindong;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yibaifen.endler.xindong.model.HomeItem;
import com.yibaifen.endler.xindong.model.HomeAdapter;
import java.util.ArrayList;
import java.util.List;



public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter hMyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        hMyAdapter = new HomeAdapter(R.layout.rv_main_item,hinitData());
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

    private List<HomeItem> hinitData() {
        List<HomeItem> mDataList = new ArrayList<HomeItem>();

        for (int i = 0; i <= 100; i++) {
            HomeItem hi = new HomeItem();
            hi.setTitle("item:" + i);
            hi.setImage("https://maps.cc/girl/thumb/eac62f8333c741dc0892125f73c7a3b2.jpg");
            mDataList.add(hi);
        }
        return  mDataList;
    }
}
