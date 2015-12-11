package cc.mnbase.activity;

import android.os.Bundle;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cc.mn.BaseActivity;
import cc.mnbase.R;
import cc.mnbase.adapter.SwipeCard2Adapter;
import cc.mnbase.adapter.WaterPullAdapter;
import cc.mnbase.model.Emm;
import cc.mnbase.model.Girl;
import cc.mnbase.util.api.ApiUtil;
import cc.mnbase.util.json.JsonUtil;
import cc.mnbase.view.list.abs.CardItem;
import cc.mnbase.view.list.abs.SwipeCard2View;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-07
 * Time: 09:48
 * Version 1.0
 */

public class SwipeCardActivity extends BaseActivity {

    private SwipeCard2View swipe_card_view;

    private String imagePaths[] = {"assets://wall01.jpg",
            "assets://wall02.jpg", "assets://wall03.jpg",
            "assets://wall04.jpg", "assets://wall05.jpg",
            "assets://wall06.jpg", "assets://wall07.jpg",
            "assets://wall08.jpg", "assets://wall09.jpg",
            "assets://wall10.jpg", "assets://wall11.jpg",
            "assets://wall12.jpg", "assets://wall01.jpg",
            "assets://wall02.jpg", "assets://wall03.jpg",
            "assets://wall04.jpg", "assets://wall05.jpg",
            "assets://wall06.jpg", "assets://wall07.jpg",
            "assets://wall08.jpg", "assets://wall09.jpg",
            "assets://wall10.jpg", "assets://wall11.jpg", "assets://wall12.jpg"}; // 24个图片资源名称

    private String names[] = {"郭富城", "刘德华", "张学友", "李连杰", "成龙", "谢霆锋", "李易峰",
            "霍建华", "胡歌", "曾志伟", "吴孟达", "梁朝伟", "周星驰", "赵本山", "郭德纲", "周润发", "邓超",
            "王祖蓝", "王宝强", "黄晓明", "张卫健", "徐峥", "李亚鹏", "郑伊健"}; // 24个人名

    private List<CardItem> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card_layout);


        swipe_card_view = (SwipeCard2View)findViewById(R.id.swipe_card_view);


        initData();
    }

    private void initData(){
        prepareDataList();
        swipe_card_view.setData(dataList);
    }

    private void prepareDataList() {
        int num = imagePaths.length;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < num; i++) {
                CardItem dataItem = new CardItem(names[i], imagePaths[i], (int) (Math.random() * 10), (int) (Math.random() * 6));
                dataList.add(dataItem);
            }
        }
    }


}
