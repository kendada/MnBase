package cc.mnbase.mvc.model;

import com.google.gson.annotations.SerializedName;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-13
 * Time: 14:31
 * Version 1.0
 */

public class Movies {

    @SerializedName("MovieName")
    private String name; // 成员变量名和json数据中得键名不同，可以通过注解来设置

    private String RowNum;

    private String amount;

    public void setMovieName(String MovieName){
        this.name = MovieName;
    }
    public String getMovieName(){
        return this.name;
    }
    public void setRowNum(String RowNum){
        this.RowNum = RowNum;
    }
    public String getRowNum(){
        return this.RowNum;
    }
    public void setAmount(String amount){
        this.amount = amount;
    }
    public String getAmount(){
        return this.amount;
    }

}
