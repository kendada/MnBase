package cc.mnbase.activity;

import android.os.Bundle;

import cc.mnbase.R;
import cc.mnbase.fragment.TestJianRongFragment;
import cc.mnbase.jianrong.AutoLayoutActivity;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-11
 * Time: 21:49
 * Version 1.0
 */

public class TestJianRongActivity2 extends AutoLayoutActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jian_rong_layout2);

        getSupportFragmentManager().beginTransaction().add(R.id.container, new TestJianRongFragment()).commit();
    }
}
