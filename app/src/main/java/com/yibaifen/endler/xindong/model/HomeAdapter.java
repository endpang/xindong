package com.yibaifen.endler.xindong.model;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yibaifen.endler.xindong.R;

import java.util.List;

/**
 * Created by pangzhiwei on 2018/4/9.
 */

public class HomeAdapter extends BaseQuickAdapter<HomeItem, BaseViewHolder> {
    public HomeAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeItem item) {
        helper.setText(R.id.text, item.getTitle());
        //helper.setImageResource(R.id.icon, item.getImageResource());
        Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.icon));
        Glide.with(mContext).load(item.getAva()).into((ImageView) helper.getView(R.id.ava));


    }
}