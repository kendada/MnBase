package cc.mn.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;


/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-28
 * Time: 20:59
 * Version 1.0
 *
 * 功能：清除缓存：1.清除数据库；2.清除SharedPreference；3.清除files和自定义目录文件
 */

public class CleanCacheDataManager {

    private Context mContext;

    public CleanCacheDataManager(Context context){
        mContext = context;
    }

    /**
     * 清除应用内部缓存(/data/data/xxx/cache：xxx->包名)
     * */
    public void cleanCache(){
        if(contextIsNull()){
            deleteFilesByDirectory(mContext.getCacheDir());
        }
    }

    /**
     * 清除内部所有的数据库(/data/data/xxx/databases：xxx->包名)
     * */
    public void cleanDatabases(){
        deleteFilesByDirectory(new File(getCompletePackageName() + "/databases"));
    }

    /**
     * 通过数据库名称删除数据库
     *
     * @param name 数据库名称
     * */
    public void deleteDatabasesByName(String name){
        if(contextIsNull()){
            mContext.deleteDatabase(name);
        }
    }

    /**
     * 清除 /data/data/xxx/files 下的所有文件
     * */
    public void cleanFiles(){
        if(contextIsNull()){
            deleteFilesByDirectory(mContext.getFilesDir());
        }
    }

    /**
     * 清除SD卡应用缓存文件(/mnt/sdcard/android/data/xxx/cache：xxx->包名)
     * */
    public void cleanExternalCache(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && contextIsNull()){
            deleteFilesByDirectory(mContext.getExternalCacheDir());
        }
    }

    /**
     * 清除其他路径下的文件
     *
     * @param filePath 完整路径
     * */
    public void cleanCustomCache(String filePath){
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 清除其他路径的文件
     *
     * @param file 完整路径
     * */
    public void cleanCustomCache(File file){
        deleteFilesByDirectory(file);
    }

    /**
     * 清除应用的所有的数据
     * */
    public void cleanAllData(String... files){
        cleanCache();
        cleanExternalCache();
        cleanDatabases();
        cleanSharedPreference();
        cleanFiles();
        for(String file : files){
            cleanCustomCache(file);
        }
    }

    /**
     * 清除SharedPreference
     * */
    public void cleanSharedPreference(){
        deleteFilesByDirectory(new File(getCompletePackageName() + "/shared_prefs"));
    }

    /**
     * 获取格式化之后文件大小
     * */
    public String getCacheSize(File file){
        return getFormSize(getFolderSize(file));
    }

    /**
     * 获取文件的大小
     * */
    public long getFolderSize(File file){
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for(File f : fileList){
                if(f.isDirectory()){
                    size += getFolderSize(f);
                } else {
                    size += f.length();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化文件大小
     * */
    public String getFormSize(double size){
        double kiloByte = size /1024;
        if(kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if(megaByte < 1){
            BigDecimal decimal = new BigDecimal(Double.toString(kiloByte));
            return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if(gigaByte < 1){
            BigDecimal decimal = new BigDecimal(Double.toString(megaByte));
            return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "MB";
        }

        double teraByte = gigaByte / 1024;
        if(teraByte < 1){
            BigDecimal decimal = new BigDecimal(Double.toString(gigaByte));
            return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "GB";
        }

        BigDecimal decimal = new BigDecimal(teraByte);
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "TB";
    }

    /**
     * 获取包名完整路径
     * */
    private String getCompletePackageName(){
        if(contextIsNull()){
            return mContext.getFilesDir().getParent() + mContext.getPackageName();
        }
        return null;
    }

    /**
     * 具体执行删除文件的方法
     * @param file 文件夹路径
     * */
    private void deleteFilesByDirectory(File file){
        if(file != null && file.exists() && file.isDirectory()){
            for(File itemFile : file.listFiles()){
                itemFile.delete();
            }
        }
    }

    /**
     * 判断Context是否为空
     * */
    private boolean contextIsNull(){
        if(mContext == null){
            throw new IllegalArgumentException("Context 不能为空！");
        }
        return true;
    }

}
