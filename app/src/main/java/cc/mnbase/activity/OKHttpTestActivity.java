package cc.mnbase.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import cc.mn.BaseActivity;
import cc.mnbase.R;
import cc.mnbase.util.okhttp.OKHttpClientStringListener;
import cc.mnbase.util.okhttp.OKHttpCookie;
import cc.mnbase.util.okhttp.OKHttpParam;
import cc.mnbase.util.okhttp.OKHttpUtil;
import cc.mnbase.util.okhttp.ProgressRequestBody;
import cc.mnbase.util.okhttp.ProgressResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-14
 * Time: 09:41
 * Version 1.0
 */

public class OKHttpTestActivity extends BaseActivity{

    Button btn, btn1, btn2, btn3;
    TextView text;

    private OKHttpUtil mOkHttpUtil;

    private String url = "http://snsapp.xzw.com/index.php?app=public&mod=Passport&act=doLogin";
    private String url1 = "http://snsapp.xzw.com/index.php?app=public&mod=xzwindex&act=UserInformation";
    private String url11 = "http://snsapp.xzw.com/index.php?app=public&mod=Xzwindex&act=following&ssex=1&p=1;";
    private String url22 = "http://snsapp.xzw.com/index.php?app=public&mod=xzwother&act=gettips&star=3&type=4";
    private String url2 = "http://snsapp.xzw.com/index.php?app=photo&mod=Xzwindex&act=upload";

    private String tag = OKHttpTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp_test_layout);

        mOkHttpUtil = OKHttpUtil.getInstance();
        //获取Cookie
        OKHttpCookie okHttpCookie = new OKHttpCookie(getContext());
        //设置Cookie
        mOkHttpUtil.setOkHttpCookie(okHttpCookie);

        btn = (Button)findViewById(R.id.btn);
        text = (TextView)findViewById(R.id.text);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePhoto();
            }
        });

    }

    private void init(){
        try{
            OKHttpParam param = new OKHttpParam();
            param.putParam("login_email", "1203596603@qq.com");
            param.putParam("login_password", "jin123");
            param.putParam("login_remember", "1");
            mOkHttpUtil.post(url, param, new OKHttpClientStringListener(){
                @Override
                public void onSuccess(int code, Object response) {
                    Log.i(tag, "----response="+response);
                    text.setText("----response="+response);

                    OkHttpClient client = mOkHttpUtil.getOkHttpClient();
                    OKHttpCookie okHttpCookie = (OKHttpCookie) client.getCookieHandler();
                    Log.i(tag, "------cookie=" + okHttpCookie.getCookieStore().getCookies());
                }

                @Override
                public void onFalure(int code, Object error) {
                    Log.i(tag, "----error="+error);
                }
            }, new ProgressResponseBody.ProgressResponseListener() {
                        @Override
                        public void update(long byteRead, long contentLength, boolean done) {
                            Log.i(tag, "--byteRead=" + byteRead + "----contentLength=" + contentLength);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void userInfo(){
        mOkHttpUtil._get(url11, new OKHttpClientStringListener() {
            @Override
            public void onSuccess(int code, Object response) {
                Log.i(tag, "----response=" + response);
                text.setText("--" + response);
            }

            @Override
            public void onFalure(int code, Object error) {
                Log.i(tag, "----error=" + error);
            }
        }, new ProgressResponseBody.ProgressResponseListener() {
            @Override
            public void update(long byteRead, long contentLength, boolean done) {
             //   Log.i(tag, "--byteRead=" + byteRead + "----contentLength=" + contentLength + "---百分比=" +
            //            ((float)byteRead / (float)contentLength)*100);
                Log.i(tag, "---百分比=" + ((float)byteRead / (float)contentLength)*100);
            }
        });
    }

    private void getPhoto(){
        mOkHttpUtil.get(url22, new OKHttpClientStringListener() {
            @Override
            public void onSuccess(int code, Object response) {
                Log.i(tag, "----response=" + response);
                text.setText("--" + response);
            }

            @Override
            public void onFalure(int code, Object error) {
                Log.i(tag, "----error=" + error);
            }
        });
    }

    /**
     *
     * */
    //测试，上传照片
    private void updatePhoto(){
        final OKHttpParam param = new OKHttpParam();
        param.putParam("Filedata[0]", new File("/mnt/sdcard/Download/mm_icon.png"));
        param.putParam("Filedata[1]", new File("/mnt/sdcard/Download/avatar_icon.png"));
        param.putParam("Filedata[2]", new File("/mnt/sdcard/Download/img01.jpg"));
        param.putParam("Filedata[3]", new File("/mnt/sdcard/Download/img02.jpg"));

        mOkHttpUtil.post(url2, param, new OKHttpClientStringListener() {
                    @Override
                    public void onSuccess(int code, Object response) {
                        Log.i(tag, "----response=" + response);
                        text.setText("--" + response);
                    }

                    @Override
                    public void onFalure(int code, Object error) {
                        Log.i(tag, "----error=" + error);
                    }
                },
                new ProgressResponseBody.ProgressResponseListener() {
                    @Override
                    public void update(long byteRead, long contentLength, boolean done) {
                        Log.i(tag, "--byteRead=" + byteRead + "----contentLength=" + contentLength);
                    }
                });

    }

}
