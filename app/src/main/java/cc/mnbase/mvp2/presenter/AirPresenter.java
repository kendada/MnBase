package cc.mnbase.mvp2.presenter;

import com.google.gson.Gson;

import cc.mnbase.mvc.util.http.OnNetWorkDataListener;
import cc.mnbase.mvp2.model.Air;
import cc.mnbase.mvp2.model.Root;
import cc.mnbase.mvp2.util.ApiUtil;
import cc.mnbase.mvp2.view.IAirView;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-20
 * Time: 10:44
 * Version 1.0
 */

public class AirPresenter {

    private IAirView mAirView;
    private ApiUtil mApiUtil;

    public AirPresenter(IAirView airView){
        mAirView = airView;
        mApiUtil = new ApiUtil(); //
    }

    public void getData(){
        String url = "http://apis.haoservice.com/air/cityair" +
                "?city=" + mAirView.getCitName() +
                "&key=137d40682afa4661bd1d251a63713204";

        mApiUtil.getAirData(url, new OnNetWorkDataListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object res) {
                String result = String.valueOf(res);
                Gson gson = new Gson();  // Gson 解析
                Root root = gson.fromJson(result, Root.class);
                if(root != null){
                    Air air = root.result;
                    mAirView.toResult(air);
                }
            }

            @Override
            public void onError(Object error) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

}
