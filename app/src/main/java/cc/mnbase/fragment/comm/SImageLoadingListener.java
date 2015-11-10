package cc.mnbase.fragment.comm;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * User: 靳世坤(1203596603@qq.com)
 * Date: 2015-10-26
 * Time: 15:03
 * Version 1.0
 */

public abstract class SImageLoadingListener implements ImageLoadingListener {

    @Override
    public void onLoadingStarted(String s, View view) {

    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingCancelled(String s, View view) {

    }
}
