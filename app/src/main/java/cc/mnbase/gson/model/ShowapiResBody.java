package cc.mnbase.gson.model;

import java.util.List;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-13
 * Time: 14:32
 * Version 1.0
 */

public class ShowapiResBody {

    private List<Movies> datalist ;

    private String remark;

    private String ret_code;

    public void setDatalist(List<Movies> datalist){
        this.datalist = datalist;
    }
    public List<Movies> getDatalist(){
        return this.datalist;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
    public String getRemark(){
        return this.remark;
    }
    public void setRet_code(String ret_code){
        this.ret_code = ret_code;
    }
    public String getRet_code(){
        return this.ret_code;
    }
}
