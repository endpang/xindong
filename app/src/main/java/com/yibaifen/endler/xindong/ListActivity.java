package com.yibaifen.endler.xindong;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
    }

    private List<HomeItem> hinitData() {
        List<HomeItem> mDataList = new ArrayList<HomeItem>();

        for (int i = 0; i <= 100; i++) {
            HomeItem hi = new HomeItem();
            hi.setTitle("item:" + i);
            mDataList.add(hi);
        }
        return  mDataList;
    }
}
