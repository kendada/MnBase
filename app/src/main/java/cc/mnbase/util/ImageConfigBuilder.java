package cc.mnbase.util;

import cc.mnbase.R;
import cc.mnbase.image.DisplayImageOptions;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-10-22
 * Time: 23:24
 * Version 1.0
 */

public class ImageConfigBuilder {

    private static final int LODING_PHOTO = R.mipmap.pic_bg;
    private static final int EMPTY_PHOTO = R.mipmap.empty_picture;
    private static final int FAIL_PHOTO = R.mipmap.empty_failed;

    public final static DisplayImageOptions USER_HEAD_HD_OPTIONS = new DisplayImageOptions.Builder()
            .showImageOnLoading(LODING_PHOTO)
            .showImageForEmptyUri(EMPTY_PHOTO)
            .showImageOnFail(FAIL_PHOTO)
            .build();


}
