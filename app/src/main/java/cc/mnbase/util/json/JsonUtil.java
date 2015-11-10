package cc.mnbase.util.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cc.mnbase.model.Girl;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-08-17
 * Time: 21:53
 * Version 1.0
 */

public class JsonUtil {

    public static List<Girl> girlList(String json){
        List<Girl> list = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONObject body = jsonObject.getJSONObject("showapi_res_body");
            JSONObject pagebean = body.optJSONObject("pagebean");

            JSONArray array = pagebean.optJSONArray("contentlist");
            int count = array.length();
            for(int i=0; i<count; i++){
                JSONObject jb = array.optJSONObject(i);
                String avatarUrl = jb.optString("avatarUrl");
                String cardUrl = jb.optString("cardUrl");
                String city = jb.optString("city");
                int height = jb.optInt("height");
                String link = jb.optString("link"); //主页
                String realName = jb.optString("realName"); //名字
                int totalFanNum = jb.optInt("totalFanNum"); //粉丝数
                int totalFavorNum = jb.optInt("totalFavorNum");
                String type = jb.optString("type");
                int userId = jb.optInt("userId");
                int weight = jb.optInt("weight"); //体重
                JSONArray imgs = jb.optJSONArray("imgList");
                List<String> imgList = new ArrayList<>();
                for(int j=0; j<imgList.size(); j++){
                    String imgUrl = imgs.optString(j);
                    imgList.add(imgUrl);
                }
                list.add(new Girl(avatarUrl, cardUrl, city, height, imgList, link, realName, totalFanNum,
                        totalFavorNum, type, userId, weight));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
