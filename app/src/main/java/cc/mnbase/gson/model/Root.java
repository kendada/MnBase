package cc.mnbase.gson.model;

import com.google.gson.annotations.SerializedName;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-13
 * Time: 14:33
 * Version 1.0
 */

public class Root {
    private int showapi_res_code;

    private String showapi_res_error;

    @SerializedName("showapi_res_body")
    private ShowapiResBody showapiResBody; // 利用注解完成

    public void setShowapi_res_code(int showapi_res_code){
        this.showapi_res_code = showapi_res_code;
    }
    public int getShowapi_res_code(){
        return this.showapi_res_code;
    }
    public void setShowapi_res_error(String showapi_res_error){
        this.showapi_res_error = showapi_res_error;
    }
    public String getShowapi_res_error(){
        return this.showapi_res_error;
    }

    public ShowapiResBody getShowapiResBody() {
        return showapiResBody;
    }

    public void setShowapiResBody(ShowapiResBody showapiResBody) {
        this.showapiResBody = showapiResBody;
    }
}
