package cc.mnbase.mvp3.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-02-15
 * Time: 14:16
 * Version 1.0
 */

public class Result {

    @SerializedName("PageIndex")
    public int pageIndex;

    @SerializedName("PageSize")
    public int pageSize;

    @SerializedName("Count")
    public int count;

    @SerializedName("List")
    public List<LoveData> loveDatas;

}
