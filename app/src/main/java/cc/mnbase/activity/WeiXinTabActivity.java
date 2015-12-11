package cc.mnbase.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cc.mn.widget.tab.MnTabGroupLayout;
import cc.mn.widget.tab.MnTabLayout;
import cc.mnbase.R;
import cc.mnbase.fragment.WeiXinFragment;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-25
 * Time: 15:50
 * Version 1.0
 */

public class WeiXinTabActivity extends AppCompatActivity implements MnTabGroupLayout.OnItemClickListener{

    private MnTabGroupLayout group_tab_layout;

    private ViewPager view_pager;

    private List<WeiXinFragment> list = new ArrayList<>();

    private int mPosition = 0;

    private String tag = WeiXinTabActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wixin_tab_layout);

        list.add(new WeiXinFragment("微信"));
        list.add(new WeiXinFragment("通讯录"));
        list.add(new WeiXinFragment("发现"));
        list.add(new WeiXinFragment("我"));

        view_pager = (ViewPager)findViewById(R.id.view_pager);
        group_tab_layout = (MnTabGroupLayout)findViewById(R.id.group_tab_layout);
        group_tab_layout.setOnItemClickListener(this);

        view_pager.setAdapter(pagerAdapter);

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                group_tab_layout.onPageScrolling(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                group_tab_layout.setCurrentItem(mPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) { //状态

            }
        });

    }

    //创建适配器
    private FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    };

    @Override
    public void onClick(int position, MnTabLayout tabLayout) {
        Log.i(tag, "选中的tablayout="+position);
        view_pager.setCurrentItem(position, false); //取消动画
    }
}
