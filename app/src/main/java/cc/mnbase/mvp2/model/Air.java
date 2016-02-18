package cc.mnbase.mvp2.model;

import com.google.gson.annotations.SerializedName;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-20
 * Time: 10:36
 * Version 1.0
 */

public class Air {

  //  "Ranking": "137",
   //         "CityName": "东莞",
    //        "ProvinceName": "广东空气质量pm2.5",
  //          "AQI": "79",
  //          "Quality": "良",
  //          "PM25": "59μg/m³",
  //          "UpdateTime": "2016-01-20 10:34"

    @SerializedName("Ranking")
    public String ranking;

    @SerializedName("CityName")
    public String cityName;

    @SerializedName("ProvinceName")
    public String provinceName;

    @SerializedName("AQI")
    public String aqi;

    @SerializedName("Quality")
    public String quality;

    @SerializedName("PM25")
    public String pm25;

    @SerializedName("UpdateTime")
    public String updateTime;

    @Override
    public String toString() {
        return "[ranking = " + ranking + ", cityName = " + cityName + ", provinceName = " + provinceName
                + ", aqi = " + aqi + ", quality = " + quality + ", pm25 = " + pm25 + ", updateTime = " + updateTime + "]";
    }
}
