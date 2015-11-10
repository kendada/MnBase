package cc.mnbase.disklru;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.mnbase.disklru.libcore.io.DiskLruCache;
import cc.mnbase.image.FileUtils;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-10-23
 * Time: 14:57
 * Version 1.0
 */

public class ImageLoader {

    private FileUtils mFileUtils;

    private DiskLruCache mDiskLruCache;

    private ExecutorService mExecutorService;

    private String tag = ImageLoader.class.getSimpleName();

    public ImageLoader(Context context){
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        Log.i(tag, "--app的最大内存："+(maxMemory/(1024*1024)));
        int mCacheMemory = maxMemory / 8;
        mFileUtils = new FileUtils(context);
        try {
            mDiskLruCache = DiskLruCache.open(new File(mFileUtils.getStorageDirectory()), getAppVersion(context),
                    1, 10*1024*1024);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadImage(final String url, final ImageView imageView){
        Bitmap bitmap = getBitmapFromDisk(url);
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
            return;
        }
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                imageView.setImageBitmap(getBitmapFromDisk(url));
                Log.i(tag, "***下载完成***");
            }
        };
        getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                saveBitmap(url);
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
        });
    }

    private Bitmap getBitmapFromDisk(String url){
        String key = MD5.hashKeyForDisk(url);
        Bitmap bitmap = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if(snapshot!=null){
                InputStream is = snapshot.getInputStream(0);
                bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 获取线程池
     * */
    private ExecutorService getThreadPool(){
        if(mExecutorService==null){
            synchronized (ExecutorService.class){
                if(mExecutorService==null){
                    mExecutorService = Executors.newFixedThreadPool(3);
                }
            }
        }
        return mExecutorService;
    }

    /**
     * 下载并保存图片
     * */
    private void saveBitmap(String url){
        String key = MD5.hashKeyForDisk(url);
        Log.i(tag, "----85----"+key);
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if(editor!=null){
                OutputStream outputStream = editor.newOutputStream(0);
                if(downImage(url, outputStream)){
                    editor.commit();
                } else {
                    editor.abort();
                }
            }
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行下载图片
     * */
    private boolean downImage(String imgUrl, OutputStream outputStream){
        HttpURLConnection con = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            URL url = new URL(imgUrl);
            con = (HttpURLConnection)url.openConnection();
            in = new BufferedInputStream(con.getInputStream(), 1024);
            out = new BufferedOutputStream(outputStream, 1024);

            int b;
            while((b = in.read()) !=-1){
                out.write(b);
            }
            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(con != null){
                con.disconnect();
            }
            try {
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 获取app当前版本
     * @param context
     * @return app版本号
     * */
    public int getAppVersion(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }


}
