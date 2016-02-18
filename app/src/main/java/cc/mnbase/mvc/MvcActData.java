package cc.mnbase.mvc;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-18
 * Time: 10:39
 * Version 1.0
 */

public class MvcActData {

    private OnDataListener mOnDataListener;

    private ApiUtil mApiUtil;

    private String url = "https://route.showapi.com/578-7" +
            "?showapi_appid=5203" +
            "&showapi_timestamp=20160118192302" +
            "&showapi_sign=12FF8AD136BF413FFF0D311DF1479F34";

    private String tag = MvcActData.class.getSimpleName();

    public MvcActData(OnDataListener onDataListener){
        mOnDataListener = onDataListener;
        mApiUtil = new ApiUtil();
    }

    public void sendData(){
        if(mOnDataListener == null) return;
        initData();
    }

    private void initData(){
        mApiUtil.getData(url, mOnDataListener);
    }

    public void setOnDataListener(OnDataListener mOnDataListener) {
        this.mOnDataListener = mOnDataListener;
    }

}
