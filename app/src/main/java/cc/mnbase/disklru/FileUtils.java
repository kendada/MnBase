package cc.mnbase.disklru;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-10-22
 * Time: 11:49
 * Version 1.0
 */

public class FileUtils {

    public static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();

    private static String mDataRootPath = null;

    private final static String APP_PACKAGE_FOLDER = "/Android/data";

    private final static String FOLDER_NAME = "/image";

    private String appPackage = null;

    private String tag = FileUtils.class.getSimpleName();

    public FileUtils(Context context){
        appPackage = context.getPackageName();
        mDataRootPath = context.getCacheDir().getPath();
    }

    /**
     * 获取图片保存路径：优先保存在SD卡
     * */
    public String getStorageDirectory(){
        String path = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                mSdRootPath + APP_PACKAGE_FOLDER + File.separator + appPackage + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
        File folderFile = new File(path);
        if(!folderFile.exists()){ //目录不存在时，进行创建
            folderFile.mkdirs();
        }
        return path;
    }

    /**
     * 保存图片到SD卡，或者手机目录
     * */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void saveBitmap(String fileName, Bitmap bitmap) throws IOException{
        if(bitmap == null){
            return;
        }
        String path = getStorageDirectory();
        File folderFile = new File(path);
        if(!folderFile.exists()){ //目录不存在时，进行创建
            folderFile.mkdirs();
        }

        File file = new File(path + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        if(fileName.indexOf("jpg")!=-1|| fileName.indexOf("jpeg")!=-1){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } else if(fileName.indexOf("png")!=-1){
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } else {
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fos);
        }


        fos.flush();
        fos.close();
    }

    /**
     * 从SD卡或者手机目录获取图片
     * */
    public Bitmap getBitmap(String fileName){
        return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);
    }

    /**
     * 判断文件是否存在
     * */
    public boolean isFileExists(String fileName){
        return new File(getStorageDirectory() + File.separator + fileName).exists();
    }

    /**
     * 获取文件的大小
     * */
    public long getFileSize(String fileName){
        return new File(getStorageDirectory() + File.separator + fileName).length();
    }

    /**
     * 删除图片
     * */
    public void deleteFile(){
        String path = getStorageDirectory();
        File dirFile = new File(path);
        if(!dirFile.exists()){
            return;
        }
        if(dirFile.isDirectory()){
            String[] children = dirFile.list();
            for(String p:children){
                new File(path+File.separator+p).delete();
            }
        }
        dirFile.delete();
    }


}
