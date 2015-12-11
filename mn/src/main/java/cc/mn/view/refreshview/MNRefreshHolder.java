package cc.mn.view.refreshview;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-09
 * Time: 14:20
 * Version 1.0
 */

public class MNRefreshHolder {

    public int mOffsetY;

    public void move(int deltaY){
        mOffsetY += deltaY;
    }

    public boolean hasHeaderPullDown(){
        return mOffsetY > 0;
    }

    public boolean hasFooterPullUp(){
        return  mOffsetY < 0;
    }

    public boolean isOverHeader(int deltaY){
        return mOffsetY < - deltaY;
    }
}
