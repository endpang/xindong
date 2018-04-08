package com.yibaifen.endler.xindong;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import okhttp3.FormBody;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.net.URL;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.squareup.picasso.Picasso;
import com.yibaifen.endler.xindong.model.*;

import okhttp3.RequestBody;


public class MainActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private  String xxid = "";
    private TextView tv=null;
    private ImageView iv = null;
    private Handler handler=null;
    private String sr = null;
    private String imgurl = null;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handler =new Handler();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton login = (FloatingActionButton) findViewById(R.id.login);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                tv=(TextView) findViewById(R.id.t1);
                iv = (ImageView) findViewById(R.id.i1);
                //final ImageView iv = (ImageView) findViewById(R.id.i1);
                if(xxid == ""){
                    getRequest("https://maps.cc/girl/android.php",1);
                }
                //tv.setText("hello");
            }

        });
        AuthInfo mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        WbSdk.install(this,mAuthInfo);
        mSsoHandler = new SsoHandler(MainActivity.this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSsoHandler.authorize(new SelfWbAuthListener());
            }
        });

        //mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        //WbSdk.install(this,mAuthInfo);


    }
    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener{
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    Log.i("token",token.toString());
                    if (mAccessToken.isSessionValid()) {
                        // 显示 Token
                        //updateTokenView(false);
                        Log.i("token","false");
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(MainActivity.this, mAccessToken);
                        Toast.makeText(MainActivity.this,
                                "success", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void cancel() {
            Toast.makeText(MainActivity.this,"canceled", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            Log.i("error",errorMessage.getErrorMessage());
            Toast.makeText(MainActivity.this, errorMessage.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void xiaobing(String content){
        Log.i("xiaobing",content);
        postRequest(content,2);
        //Gson gson = new Gson();
        //Face user = gson.fromJson(content, Face.class);
    }
    private void callback(String content){

    }

    private void postRequest(String json,final int stat) {

        RequestBody formBody = null;
        String url = null;
        if(stat == 1) {
            url = "http://kan.msxiaobing.com/Api/ImageAnalyze/Process?service=yanzhi&tid=f877a031eac0406abbfc6e2260417fec";
            Gson gson = new Gson();
            Face user = gson.fromJson(json, Face.class);
            Log.i("url",user.getHost() + user.getUrl());
            formBody = new FormBody.Builder()
                .add("MsgId", String.valueOf(System.currentTimeMillis() * 1000))
                .add("CreateTime", String.valueOf(System.currentTimeMillis()))
                .add("Content[imageUrl]", user.getHost() + user.getUrl())
                .build();
        }
        if(stat == 2){
            url = "http://kan.msxiaobing.com/Api/Image/UploadBase64";
            formBody = RequestBody.create(JSON, json);
        }
        if(stat ==3 ){
            url = "https://maps.cc/girl/postcolor.php?xxid="+xxid;
            formBody = RequestBody.create(JSON, json);
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
                        if(stat == 2){
                            postRequest(s,1);
                        }
                        if(stat == 1){

                            //Log.i("maps.cc", s);
                            postRequest(s,3);
                            xxid = "";
                        }
                        if(stat == 3){
                            Gson gson = new Gson();
                            String[] strings = gson.fromJson(s, String[].class);
                            if(strings.length > 0){
                                if(strings[0].equals("200")){
                                    sr = strings[1] + ":" + strings[2];
                                    imgurl = strings[3];
                                    handler.post(runnableUi);
                                }

                            }

                        }
                        //String re = new String(s.getBytes("UTF-8"), "UTF-8");
                        Log.i("WY","打印POST响应的数据：" + s);

                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            tv.setText(sr);
            try{
                Log.i("imgurl",imgurl);
                Picasso.get().load(imgurl).into(iv);
                //URL picUrl = new URL(imgurl);
                //iv.setImageURI(imgurl);
                //Bitmap pngBM = BitmapFactory.decodeStream(picUrl.openStream());
                //iv.setImageBitmap(pngBM);
            }catch (Exception e){
                Log.i("showimg","没显示出来" + e.toString());
            }


        }

    };
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
                        Log.i("android","content:" + content);
                        if(i == 1 && content != ""){
                            Gson gson = new Gson();
                            String[] strings = gson.fromJson(content, String[].class);
                            if(strings.length > 0){
                                xxid = strings[0];
                                xiaobing(strings[1]);
                            }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("requestCode:",String.valueOf(requestCode));
        Log.i("resultCode:",String.valueOf(resultCode));
        Log.i("data:",data.toString());
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}


