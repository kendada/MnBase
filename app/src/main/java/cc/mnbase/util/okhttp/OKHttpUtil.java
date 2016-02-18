package cc.mnbase.util.okhttp;

import android.os.Handler;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-14
 * Time: 14:34
 * Version 1.0
 */

public class OKHttpUtil {

    private static OKHttpUtil mOkHttpUtil;

    private OkHttpClient mOkHttpClient;

    private Handler mHandler;

    private OKHttpCookie mOkHttpCookie;


    private static final String tag = OKHttpUtil.class.getSimpleName();


    private OKHttpUtil(){
        mOkHttpClient = new OkHttpClient();
        mHandler = new Handler();
    }

    public static OKHttpUtil getInstance(){
        if(mOkHttpUtil == null){
            synchronized (OKHttpUtil.class){
                if(mOkHttpUtil == null){
                    mOkHttpUtil = new OKHttpUtil();
                }
            }
        }
        return mOkHttpUtil;
    }

    /**
     * 设置Cookie
     * */
    public void setOkHttpCookie(OKHttpCookie okHttpCookie) {
        this.mOkHttpCookie = okHttpCookie;
        mOkHttpClient.setCookieHandler(mOkHttpCookie);
    }

    /**
     * 测试
     * */
    public OkHttpClient getOkHttpClient(){
        return mOkHttpClient;
    }

    /**
     * 同步的Get请求
     * */
    private Response doGet(String url) throws IOException{
        final Request mRequest = buildRequest(url).build();
        Call call = mOkHttpClient.newCall(mRequest);
        Response response = call.execute();
        return response;
    }


    /**
     * 异步的Get请求
     * */
    public void _get(String url, final OKHttpClientListener listener,
                    final ProgressResponseBody.ProgressResponseListener progressResponseListener){
        final  Request request = buildRequest(url).build();
        OkHttpClient client = mOkHttpClient.clone(); //克隆，不影响mOkHttpClient
        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(),
                        progressResponseListener)).build();
            }
        });
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendMessageFailureCallback(-1, e, listener);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                sendMessageSuccessCallback(response.code(), response.body().string(), listener);
            }
        });
    }

    /**
     * 对外部公开的Get请求方法
     * */
    public void get(String url, final OKHttpClientListener listener){
        if(listener!=null) listener.onStart();
        final  Request request = buildRequest(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendMessageFailureCallback(-1, e, listener);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                sendMessageSuccessCallback(response.code(), response.body().string(), listener);
            }
        });
    }

    /**
     * 根据要不要上传文件确定是哪一个
     * */
    private Request stringOrFileParam(String url, OKHttpParam params){
        Map<String, File> fileFaramMap = params.getFileParamMap();
        Request request = null;
        if(fileFaramMap != null && fileFaramMap.size() > 0){ //是否包含文件
            request = getMultipartFormRequset(url, params);
        } else {
            request = getPostRequest(url, params);
        }
        return request;
    }

    /**
     * 同步的Post请求，不能发送文件
     * */
    private Response doPost(String url, OKHttpParam param) throws IOException{
        Request request = stringOrFileParam(url, param);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * 同步的Post请求，不能发送文件
     * */
    private String doPoatAsString(String url, OKHttpParam param) throws IOException{
        Response response = doPost(url, param);
        return response.body().string();
    }

    public Response post(String url, OKHttpParam param) throws IOException{
        return doPost(url, param);
    }

    private void _post(String url, OKHttpParam param, final OKHttpClientListener listener,
                     final ProgressResponseBody.ProgressResponseListener progressListener){
        Request request = stringOrFileParam(url, param);
        OkHttpClient client = mOkHttpClient.clone(); //克隆，不影响mOkHttpClient
        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(),
                        progressListener)).build();
            }
        });
        request.newBuilder().put(new ProgressRequestBody(request.body(), new ProgressRequestBody.ProgressRequestListener() {
            @Override
            public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                Log.i(tag, "--bytesWritten="+bytesWritten+"----contentLength="+contentLength);
            }
        }));
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendMessageFailureCallback(-1, e, listener);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                sendMessageSuccessCallback(response.code(), response.body().string(), listener);
            }
        });
    }

    /**
     * 对外公开的Post请求
     * */
    public void post(String url, OKHttpParam param, OKHttpClientListener listener,
                      ProgressResponseBody.ProgressResponseListener progressListener){
        _post(url, param, listener, progressListener);
    }

    /**
     * 一般请求不包含文件上传
     * */
    private Request getPostRequest(String url, OKHttpParam params){
        if(params == null){
            params = new OKHttpParam();
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Map<String, String> paramMap = params.getParamMap();
        if(paramMap != null && paramMap.size() > 0){
            Set<String> keys = paramMap.keySet();
            for(String key:keys){
                builder.add(key, paramMap.get(key));
            }
        }
        RequestBody requestBody = builder.build();
        return buildRequest(url).post(requestBody).build();
    }

    /**
     * 上传文件请求
     * */
    private Request getMultipartFormRequset(String url, OKHttpParam params){
        if(params==null){
            params = new OKHttpParam();
        }

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        //一般数据
        Map<String, String> paramMap = params.getParamMap();
        if(paramMap != null && paramMap.size()>0){
            Set<String> keys = paramMap.keySet();
            for(String key:keys){
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, paramMap.get(key)));
            }
        }
        //添加文件
        Map<String, File> fileParamMap = params.getFileParamMap();
        if(fileParamMap!=null && fileParamMap.size()>0){
            RequestBody fileBody = null;
            Set<String> keys = fileParamMap.keySet();
            for(String key:keys){
                File file = fileParamMap.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(getMimeType(fileName)), file);
                Log.i(tag, "----"+fileBody.contentType());
                //添加请求头和请求体
                builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return buildRequest(url).post(requestBody).build();
    }

    /**
     * 创建Request并根据条件添加Cookie信息
     * @param  url 请求地址
     * */
    private Request.Builder buildRequest(String url){
        return new Request.Builder()
                .addHeader("Cookie", mOkHttpCookie != null ? mOkHttpCookie.getCookie() : "")
                .url(url);
    }

    /**
     * 获取文件类型
     * */
    private String getMimeType(String path){
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path); //获取文件类型
        if(contentTypeFor == null){
            contentTypeFor = "application/octet-stream";
        }
        Log.i(tag, "-----="+contentTypeFor);
        return contentTypeFor;
    }

    /**
     * 发送请求失败的消息
     * */
    private void sendMessageFailureCallback(final int code, final IOException e, final OKHttpClientListener listener) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onFalure(code, e);
                }
            }
        });
    }

    /**
     * 发送请求成功的消息
     * */
    private void sendMessageSuccessCallback(final int code, final Object result, final OKHttpClientListener listener){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(listener != null){
                    listener.onSuccess(code, result);
                }
            }
        });
    }

}
