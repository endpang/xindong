package com.yibaifen.endler.xindong;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.squareup.picasso.Picasso;
import com.yibaifen.endler.xindong.model.Constants;
import com.yibaifen.endler.xindong.model.Face;
import com.yibaifen.endler.xindong.model.HomeItem;
import com.yibaifen.endler.xindong.model.HomeAdapter;
import com.yibaifen.endler.xindong.model.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;


public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter hMyAdapter;
    OkHttpClient client = new OkHttpClient();
    private Handler handler=null;
    ArrayList<Image> imglist  = null;
    ArrayList<Image> newlist  = null;
    String[] urllist  = null;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;
    private EasyRefreshLayout easyRefreshLayout = null;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        handler =new Handler();
        getRequest("https://maps.cc/girl/api/mylist.php?page="+page,1);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv1);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        easyRefreshLayout = (EasyRefreshLayout) findViewById(R.id.easylayout);
        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                Log.i("loadMore","in");
            }

            @Override
            public void onRefreshing() {
                getRequest("https://maps.cc/girl/api/mylist.php?page=1",1);
                Log.i("Re","in");
                page = 1;
                easyRefreshLayout.refreshComplete();
            }
        });
    }

    protected void loadMore(){
        page ++;
        getRequest("https://maps.cc/girl/api/mylist.php?page="+page,2);
        Log.i("loadMore","in");
    }

    Runnable  runnableLoad = new  Runnable(){
        @Override
        public void run(){
            hMyAdapter.addData(hinitData(newlist));
            hMyAdapter.loadMoreComplete();
        }

    };
    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {

            mRecyclerView.setLayoutManager(new
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            hMyAdapter = new HomeAdapter(R.layout.rv_main_item,hinitData(imglist));
            hMyAdapter.openLoadAnimation();
            hMyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override public void onLoadMoreRequested() {
                    loadMore();
                }
            });

            mRecyclerView.setAdapter(hMyAdapter);
            hMyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Log.d(TAG, "onItemClick: ");
                    //Toast.makeText(ListActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListActivity.this, ImageActivity.class);

                    //Log.i("urllist", urllist.toString() );
                    Bundle bundle=new Bundle();
                    bundle.putStringArray("list", urllist);
                    bundle.putString("url", urllist[position]);
                    Log.i("url.position",urllist[position]);
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    //intent.putStringArrayListExtra("list",urllist);
                    startActivity(intent);
                }
            });


        }

    };
    private List<HomeItem> hinitData(ArrayList<Image> images) {
        List<HomeItem> mDataList = new ArrayList<HomeItem>();

        for(int i = 0;i < images.size(); i ++){
            Image img = images.get(i);
            HomeItem hi = new HomeItem();
            hi.setTitle(img.getColour() + "分\n@"+ img.getUser().getScreen_name());
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
    private void getRequest(final String url, final int i) {

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
                                Log.i("gson:",gson.toString());
                                list = gson.fromJson(content, new TypeToken<ArrayList<Image>>() {
                                }.getType());
                            }
                            //List<Image> strings = gson.fromJson(content, Image[].class);
                            Log.i("str",list.toString());

                            imglist = list;

                            urllist = new String[imglist.size()];
                            for(int i = 0;i < list.size(); i ++){
                                //System.out.println(list.get(i));
                                //urllist.add(list.get(i).getUrl());
                                urllist[i] = list.get(i).getUrl();
                            }
                            Log.i("urllist_1",urllist.toString());
                            handler.post(runnableUi);
                        }
                        if(i == 2 && content != ""){
                            Gson gson = new Gson();
                            ArrayList<Image> list  = null;
                            if (gson != null) {
                                Log.i("gson:",gson.toString());
                                list = gson.fromJson(content, new TypeToken<ArrayList<Image>>() {
                                }.getType());
                            }
                            //List<Image> strings = gson.fromJson(content, Image[].class);
                            //Log.i("str",content);

                            newlist = list;
                            //imglist.addAll(list);
                            //Log.i("imglist",newlist.size()+"");

                            String[] urllist2 = new String[newlist.size()];
                            //urllist = new String[imglist.size()];
                            for(int i = 0;i < list.size(); i ++){
                                //System.out.println(list.get(i));
                                //urllist.add(list.get(i).getUrl());
                                urllist2[i] = list.get(i).getUrl();
                            }
                            List l1 = new ArrayList(Arrays.asList(urllist));
                            l1.addAll(Arrays.asList(urllist2));
                            String[] str = new String[l1.size()];
                            l1.toArray(str);
                            urllist = str;
                            //String[] urlold = urllist;
                            //urllist= new String[urlold.length+urllist2.length];
                            //System.arraycopy(urlold, 0, urllist, 0, urlold.length);
                            //System.arraycopy(urllist2, 0, urllist, 0, urllist2.length);
                            //Log.i("urlold",urlold.length + "");
                            //Log.i("urllist",urllist.length+"");
                            handler.post(runnableLoad);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //调用inflate()方法创建菜单
        getMenuInflater().inflate(R.menu.menu_main,menu);
        //如果返回false，创建的菜单无法显示
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //通过调用item.getItemId()来判断菜单项
        switch (item.getItemId()){
            case R.id.action_login:
                //Toast.makeText(this,"You Clicked Add",Toast.LENGTH_SHORT).show();
                AuthInfo mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
                WbSdk.install(this,mAuthInfo);
                mSsoHandler = new SsoHandler(ListActivity.this);
                mSsoHandler.authorize(new SelfWbAuthListener());

                break;

            case R.id.action_flush:
                getRequest("https://maps.cc/girl/api/mylist.php",1);
                break;

            default:
        }
        return true;
    }
    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener{
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            ListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    Log.i("token",token.toString());
                    if (mAccessToken.isSessionValid()) {
                        // 保存 Token 到 SharedPreferences
                        postRequest(token.toString(),4);
                        AccessTokenKeeper.writeAccessToken(ListActivity.this, mAccessToken);
                    }
                }
            });
        }

        @Override
        public void cancel() {
            Toast.makeText(ListActivity.this,"canceled", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            Log.i("error",errorMessage.getErrorMessage());
            Toast.makeText(ListActivity.this, errorMessage.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void postRequest(String json,final int stat) {

        RequestBody formBody = null;
        String url = null;
//        if(stat == 1) {
//            url = "http://kan.msxiaobing.com/Api/ImageAnalyze/Process?service=yanzhi&tid=f877a031eac0406abbfc6e2260417fec";
//            Gson gson = new Gson();
//            Face user = gson.fromJson(json, Face.class);
//            Log.i("url",user.getHost() + user.getUrl());
//            formBody = new FormBody.Builder()
//                    .add("MsgId", String.valueOf(System.currentTimeMillis() * 1000))
//                    .add("CreateTime", String.valueOf(System.currentTimeMillis()))
//                    .add("Content[imageUrl]", user.getHost() + user.getUrl())
//                    .build();
//        }
//        if(stat == 2){
//            url = "http://kan.msxiaobing.com/Api/Image/UploadBase64";
//            formBody = RequestBody.create(JSON, json);
//        }
//        if(stat ==3 ){
//            url = "https://maps.cc/girl/postcolor.php?xxid="+xxid;
//            formBody = RequestBody.create(JSON, json);
//        }
        if(stat == 4){
            url = "https://maps.cc/girl/start.php";
            formBody = RequestBody.create(JSON,json);
        }
        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .header("Cookie", "cpid=GDJbTCOyPEnZMClJfzFRSTU0ajFlTVpIXjPDTFo1VU1KAA; salt=BA587393E03E5A3CAAAE915769AFD20C; ARRAffinity=3b10bc35c1c993d8238753ddf09d68ede973452b125c86290c8b860ea0532de0; ai_user=Jgf7S|2018-04-05T03:53:29.343Z; ai_session=92GN0|1522900409451.2|1522900409451.2")
                .header("Origin","http://kan.msxiaobing.com")
                .header("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36")
                .header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                .header("Referer","http://kan.msxiaobing.com/V3/Portal?task=yanzhi&ftid=3b769fdd8e184393a16df2cb931b5119")

                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String s =  response.body().string();
//                        if(stat == 2){
//                            postRequest(s,1);
//                        }
//                        if(stat == 1){
//
//                            //Log.i("maps.cc", s);
//                            postRequest(s,3);
//                            xxid = "";
//                        }
//                        if(stat == 3){
//                            Gson gson = new Gson();
//                            String[] strings = gson.fromJson(s, String[].class);
//                            if(strings.length > 0){
//                                if(strings[0].equals("200")){
//                                    sr = strings[1] + ":" + strings[2];
//                                    imgurl = strings[3];
//                                    handler.post(runnableUi);
//                                }
//
//                            }
//
//                        }
                        if(stat == 4){
                            Oauth2AccessToken token  = AccessTokenKeeper.readAccessToken(ListActivity.this);
                            Log.i("uid",token.getUid());
                        }
                        //String re = new String(s.getBytes("UTF-8"), "UTF-8");
                        Log.i("WY","打印POST响应的数据：" + s);
                        if(s.equals("admin")){
                            Log.i("intent","goto main");

                            Intent intent = new Intent(ListActivity.this, MainActivity.class);
                            startActivity(intent);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("requestCode:",String.valueOf(requestCode));
        Log.i("resultCode:",String.valueOf(resultCode));
        Log.i("data:",data.toString());
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            //Intent intent = new Intent(ListActivity.this, MainActivity.class);
            //startActivity(intent);
        }

    }
}
