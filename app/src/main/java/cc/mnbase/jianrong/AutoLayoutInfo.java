package cc.mnbase.jianrong;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cc.mnbase.jianrong.attr.AutoAttr;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-11
 * Time: 17:28
 * Version 1.0
 */

public class AutoLayoutInfo {

    private List<AutoAttr> autoAttrs = new ArrayList<>();

    public void addAttr(AutoAttr autoAttr){
        autoAttrs.add(autoAttr);
    }

    public void fillAttrs(View view){
        for(AutoAttr autoAttr : autoAttrs){
            autoAttr.apply(view);
        }
    }

}
