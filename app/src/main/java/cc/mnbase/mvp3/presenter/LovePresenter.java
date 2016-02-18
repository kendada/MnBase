package cc.mnbase.mvp3.presenter;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import cc.mnbase.mvp3.model.Data;
import cc.mnbase.mvp3.view.ILoveView;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-02-15
 * Time: 14:22
 * Version 1.0
 */

public class LovePresenter {

    private ILoveView mLoveView;

    private String url = "http://apis.haoservice.com/lifeservice/JingDianYulu";

    private int pageIndex = 0, pageSize = 40;

    public LovePresenter(ILoveView loveView){
        mLoveView = loveView;

        url = url + "?key=" + mLoveView.getKey() + "&typeId=" + mLoveView.getTypeId() + "&pageIndex="
                + pageIndex +
                "&pageSize=" + pageSize;
    }

    public void getData(){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                Gson gson = new Gson();
                Data data = gson.fromJson(s, Data.class);
                mLoveView.toResult(data);
            }
        });
    }


}
