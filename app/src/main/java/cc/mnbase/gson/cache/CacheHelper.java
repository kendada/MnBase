package cc.mnbase.gson.cache;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-13
 * Time: 16:25
 * Version 1.0
 *
 * 缓存数据【json格式，目前只支持缓存文本】
 */

public class CacheHelper {

    private Context mContext;

    private final static String NAME = "dataCache"; // 默认缓存文件夹
    private File mFile;
    private int day = 10; // 过期时间：默认10天
    private int more = 5; // 过多文件阀值：默认5个

    private String tag = CacheHelper.class.getSimpleName();

    public CacheHelper(Context context){
        this(context, NAME);
    }

    public CacheHelper(Context context, String fileName){
        mContext = context;
        mFile = new File(getCachePath() + File.separator + fileName);
        createFile(mFile); // 创建缓存文件夹
        deleteMoreCache(); // 删除过多缓存文件
        deleteExpiedCache(); // 删除过期缓存
    }

    /**
     * 创建缓存文件
     * @param cacheName
     * @param cacheContent
     * */
    public void createCache(String cacheName, String cacheContent){
        File file = new File(mFile + File.separator + cacheName);
        FileOutputStream fis = null;
        try {
            fis = new FileOutputStream(file);
            byte[] bytes = cacheContent.getBytes();

            fis.write(bytes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取缓存文件：按时间排序之后的结果
     * */
    public File[] cacheDataList(){
        if(mFile != null){
            File[] files = mFile.listFiles();
            files = sortFile(files);
            return files;
        }
        return null;
    }

    /**
     * 根据创建时间对缓存文件进行排序
     * */
    private File[] sortFile(File[] files){
        if(files == null) return null;
        File tmpFile = null;
        int lenght = files.length;
        for(int i=0; i<lenght; i++){
            for(int j=i; j<lenght; j++){
                File file1 = files[i];
                long time1 = file1.lastModified();
                File file2 = files[j];
                long time2 = file2.lastModified();
                if(time2 > time1){ // 对比最后修改时间
                    tmpFile = file1;
                    files[i] = files[j];
                    files[j] = tmpFile;
                }
            }
        }

        return files;
    }

    /**
     * 获取缓存文件
     * @param cachePath
     * */
    public String getCacheData(String cachePath){
        File file = new File(cachePath);
        FileInputStream is = null;
        String res = null;
        try {
            is = new FileInputStream(file);
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    /**
     * 删除过期缓存
     * */
    private void deleteExpiedCache(){
        if(mFile != null){
            File[] files = mFile.listFiles();
            if(files == null) return;
            if(files.length <= 3) return; // 缓存文件不大于3时，不进行是否过期判断
            for(File file : files){
                long time = file.lastModified();
                int count = countDays(time);
                Log.i(tag, "距离今天已经" + count + "天了。。。");
                if(count > day){
                    file.delete(); // 过期删除
                }
            }
        }
    }

    /**
     * 删除过多的缓存，不管是否过期
     * */
    private void deleteMoreCache(){
        File[] files = cacheDataList();
        if(files == null) return;
        int length = files.length;
        if(length > more){
           for(int i=length-1; i>3; i--){
               File file = files[i];
               file.delete();
           }
        }
    }

    /**
     * 清除数据
     * */
    private void clearData(){

    }

    /**
     * 获取SD卡缓存路径:/storage/emulated/0/Android/data/xxx/cache
     * */
    private File getCachePath(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && contextIsNull()){
            File file = mContext.getExternalCacheDir();
            return file;
        }
        return null;
    }

    /**
     * 创建文件夹
     * */
    private void createFile(File file){
        if(file == null) return;
        if(!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 判断Context是否为空， Context为null时直接抛出异常
     * */
    private boolean contextIsNull(){
        if(mContext == null){
            throw new IllegalArgumentException("context is not null！");
        }
        return true;
    }

    /**
     * 距离今天的天数
     * */
    private int countDays(long date){
        long t = Calendar.getInstance().getTime().getTime();
        return (int)(t / 1000 - date / 1000) / 3600 / 24;
    }
}
