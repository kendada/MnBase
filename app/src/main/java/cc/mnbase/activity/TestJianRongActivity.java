package cc.mnbase.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import cc.mnbase.R;
import cc.mnbase.jianrong.AutoLayoutActivity;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-08
 * Time: 11:41
 * Version 1.0
 */

public class TestJianRongActivity extends AutoLayoutActivity {

    private TextView text;
   // private ImageView f_toobar;

    private int initX = 720;
    private int initY = 1080;

    private String tag = TestJianRongActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jian_rong_layout);

        text = (TextView)findViewById(R.id.text);
    //    f_toobar = (ImageView)findViewById(R.id.f_toobar);

        getScreenSize();

    }

    private void getScreenSize(){
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = getResources().getDisplayMetrics();


        StringBuffer sb = new StringBuffer();
        sb.append("point X = " + point.x + " point Y = " + point.y);
        sb.append("\r\n");
        sb.append("dm xdpi = " + dm.xdpi + " dm ydpi = " + dm.ydpi);


        double x = Math.pow(point.x/dm.xdpi, 2);
        double y = Math.pow(point.y/dm.ydpi, 2);

        double screenInches = Math.sqrt(x + y); // 屏幕尺寸

        sb.append("\r\n");
        sb.append("screenInches = " + screenInches);

        // dpi 计算方法：屏幕分辨率宽高的平方和，开方根，除以屏幕尺寸
        double dpi = Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2)) / screenInches;

        sb.append("\r\n");
        sb.append("dpi = " + dpi);

        sb.append("\r\n");
        sb.append("===========================================");

        double multX = (double)point.x / (double)initX;
        double multY = (double)point.y / (double)initY;

        sb.append("\r\n");
        sb.append("multX = " + multX + ", multY = " + multY);

        sb.append("\r\n");
        sb.append("===========================================\r\n");

    //    LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) f_toobar.getLayoutParams();

    //    int imgW = (int) (llp.width * multX);
    //    int imgH = (int) (llp.height * multY);
    //    LinearLayout.LayoutParams llp1 =
    //            new LinearLayout.LayoutParams(imgW, imgH);

    //    f_toobar.setLayoutParams(llp1);

        sb.append("\r\n");
    //    sb.append("imgW = " + imgW + ", imgH = " + imgH);

        int screenW = (int) (initX * multX);
        int screenH = (int) (initY * multY);

        sb.append("\r\n");
        sb.append("screenW = " + screenW + ", screenH = " + screenH);

        text.setText(sb.toString());
    }


}
