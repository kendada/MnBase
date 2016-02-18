package cc.mnbase.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;

import cc.mn.BaseActivity;
import cc.mn.util.ScreenUtils;
import cc.mn.util.DateUtil;
import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-29
 * Time: 15:43
 * Version 1.0
 */

public class DateTimeActivity extends BaseActivity {

    private ImageView image;

    private ScreenUtils screenUtils;

    private String tag = DateTimeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_layout);

        screenUtils = new ScreenUtils(this);

        int statusHeight =  screenUtils.getStatusHeight();
        Log.i(tag, "---状态栏高度 ：" + statusHeight);

        image = (ImageView)findViewById(R.id.image);


        Calendar c = Calendar.getInstance();
        c.set(2015, 11, 29, 15, 40, 0);
        Date date = c.getTime();

        String res1 =  DateUtil.dateToStr(date);
        Log.i(tag, "---- res1 = " + res1);

        String res2 = DateUtil.getCurDateStr();
        Log.i(tag, "---- res2 = " + res2);

        String res3 = DateUtil.getSMillon(date.getTime());
        Log.i(tag, "---- res3 = " + res3);

        int res4 = DateUtil.getHour(date);
        Log.i(tag, "---- res4 = " + res4);

        int res5 = DateUtil.countDays("2015-11-28 15:40:00"); // x
        Log.i(tag, "---- res5 = " + res5);

        String res6 = DateUtil.dateToStr(c.getTime(), DateUtil.FROMAT_YMDHMS_CN);
        Log.i(tag, "---- res6 = " + res6);

    }

    public void screen(View view){
        image.setImageBitmap(screenUtils.snapShotWithoutStatusBar());
    }

}
