package cc.mn;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-11
 * Time: 15:47
 * Version 1.0
 */

public abstract class BaseCameraActivity extends BaseActivity {

    public static final int ACTION_CAMERA = 0;
    public static final int ACTION_ALBUM = 1;

    private String mPicName;

    private String savePath = Environment.getExternalStorageDirectory().getPath();

    private String tag = BaseCameraActivity.class.getSimpleName();

    /**
     * 打开摄像头拍摄图片
     * */
    public void openCamera(String picName){
        mPicName = picName;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(savePath, picName + ".jpg")));
        startActivityForResult(intent, ACTION_CAMERA);
    }

    /**
     * 打开相册选取图片
     * */
    public void openAlbum(){
        Intent albumIntent = new Intent();
        albumIntent.setType("image/*");
        albumIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(albumIntent, ACTION_ALBUM);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            Uri uri = data.getData();
        }
        String picPath = "";
        switch (requestCode){
            case ACTION_CAMERA:
                picPath = new File(savePath, mPicName+".jpg").getPath();
                selectSuccess(picPath);
                break;
            case ACTION_ALBUM:
                if(data == null){
                    return;
                }
                Uri uri = data.getData();
                picPath = getPath(this, uri);
                selectSuccess(picPath);
                break;
        }
    }

    public abstract void selectSuccess(String path);

    public String getPath(final Context context, final Uri uri){
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotoUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }


        return null;
    }

    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs){
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if(cursor != null && cursor.moveToFirst()){
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri){
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri){
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public boolean isMediaDocument(Uri uri){
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public boolean isGooglePhotoUri(Uri uri){
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

}
