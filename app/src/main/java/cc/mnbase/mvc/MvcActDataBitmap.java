package cc.mnbase.mvc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-18
 * Time: 14:26
 * Version 1.0
 */

public class MvcActDataBitmap {

    private OnDataBitmapListener onDataBitmapListener;

    public MvcActDataBitmap(OnDataBitmapListener onDataBitmapListener) {
        this.onDataBitmapListener = onDataBitmapListener;
    }

    public void sendBitmap(Context context){
        if(onDataBitmapListener == null || context == null) return;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.empty_failed);
        onDataBitmapListener.onBitmap(bitmap);
    }

    public void setOnDataBitmapListener(OnDataBitmapListener onDataBitmapListener) {
        this.onDataBitmapListener = onDataBitmapListener;
    }
}
