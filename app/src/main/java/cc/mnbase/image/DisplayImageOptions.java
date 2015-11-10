package cc.mnbase.image;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Date: 2015-10-22
 * Time: 21:55
 * Version 1.0
 */

public final class DisplayImageOptions {

    private final int imageResOnLoading;

    private final int imageResForEmptyUri;

    private final int imageResFail;

    private final Drawable imageLoading;

    private final Drawable imageForEmptyUri;

    private final Drawable imageOnFail;

    private DisplayImageOptions(DisplayImageOptions.Builder builder){
        this.imageResOnLoading = builder.imageResOnLoading;
        this.imageResForEmptyUri = builder.imageResForEmptyUri;
        this.imageResFail = builder.imageResOnFail;
        this.imageLoading = builder.imageLoading;
        this.imageForEmptyUri = builder.imageForEmptyUri;
        this.imageOnFail = builder.imageOnFail;
    }

    public Drawable getImageOnLoading(Resources res){
        return this.imageResOnLoading != 0 ?  res.getDrawable(this.imageResOnLoading):this.imageLoading;
    }

    public Drawable getImageForEmptyUri(Resources res){
        return this.imageResForEmptyUri != 0 ? res.getDrawable(this.imageResForEmptyUri):this.imageForEmptyUri;
    }

    public Drawable getImageOnFail(Resources res){
        return this.imageResFail != 0 ? res.getDrawable(this.imageResFail):this.imageOnFail;
    }

    public static class Builder{
        private int imageResOnLoading = 0;
        private int imageResForEmptyUri = 0;
        private int imageResOnFail = 0;
        private Drawable imageLoading = null;
        private Drawable imageForEmptyUri = null;
        private Drawable imageOnFail = null;

        public Builder(){

        }

        /**
         * 设置正在加载时显示图片
         * */
        public DisplayImageOptions.Builder showImageOnLoading(int imageRes){
            this.imageResOnLoading = imageRes;
            return this;
        }

        /**
         *
         * */
        public DisplayImageOptions.Builder showImageForEmptyUri(int imageRes){
            this.imageResForEmptyUri = imageRes;
            return this;
        }

        /**
         * 设置图片加载失败时显示的图片
         * */
        public DisplayImageOptions.Builder showImageOnFail(int imageRes){
            this.imageResOnFail = imageRes;
            return this;
        }

        /**
         * 设置正在加载时显示图片
         * */
        public DisplayImageOptions.Builder showImageOnLoading(Drawable drawable){
            this.imageLoading = drawable;
            return this;
        }

        /**
         *
         * */
        public DisplayImageOptions.Builder showImageForEmptyUri(Drawable drawable){
            this.imageForEmptyUri = drawable;
            return this;
        }

        /**
         * 设置图片加载失败时显示的图片
         * */
        public DisplayImageOptions.Builder showImageOnFail(Drawable drawable){
            this.imageOnFail = drawable;
            return this;
        }

        public DisplayImageOptions build(){
            return new DisplayImageOptions(this);
        }

    }

}
