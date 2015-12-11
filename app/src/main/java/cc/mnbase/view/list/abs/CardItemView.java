package cc.mnbase.view.list.abs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-08
 * Time: 10:25
 * Version 1.0
 */

public class CardItemView extends LinearLayout {

    private ImageView imageView;
    private TextView userNameTv;
    private TextView imageNumTv;
    private TextView likeNumTv;

    public CardItemView(Context context) {
        this(context, null);
    }

    public CardItemView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CardItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.card_item, this);
        imageView = (ImageView)findViewById(R.id.card_image_view);
        userNameTv = (TextView)findViewById(R.id.card_user_name);
        imageNumTv = (TextView)findViewById(R.id.card_pic_num);
        likeNumTv = (TextView)findViewById(R.id.card_like);
    }

    public void setData(CardItem itemData) {
        ImageLoader.getInstance().displayImage(itemData.imagePath, imageView);
        userNameTv.setText(itemData.userName);
        imageNumTv.setText(itemData.imageNum + "");
        likeNumTv.setText(itemData.likeNum + "");
    }



}
