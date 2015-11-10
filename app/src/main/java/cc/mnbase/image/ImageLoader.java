package cc.mnbase.image;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.mnbase.image.libcore.io.DiskLruCache;

/**
 * Date: 2015-10-22
 * Time: 10:22
 * Version 1.0
 */

public class ImageLoader {

    private LruCache<String, Bitmap> mMemoryCache; //内存缓存

    private DiskLruCache mDiskLruCache; //google官方推荐处理本地缓存库

    private ExecutorService mDownlodThreadPool;  //下载图片线程池

    private int maxDiskMemory = 20*1024*1024; //本地最大缓存:20M

    private FileUtils fileUtils;

    private Context mContext;

    private String tag = ImageLoader.class.getSimpleName();

    public ImageLoader(Context context){
        mContext = context;
        //获取系统为每个应用分配的最大内存
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        int mCacheSize = maxMemory / 8;
        //分配最大内存的1/8
        mMemoryCache = new LruCache<String, Bitmap>(mCacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                //可以进行强制回收oldValue
                //oldValue.recycle();
                //oldValue = null;
            }
        };
        fileUtils = new FileUtils(context);
        try {
            mDiskLruCache = DiskLruCache.open(new File(fileUtils.getStorageDirectory()), getAppVersion(context),
                    1, maxDiskMemory);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取线程池
     * */
   // private ExecutorService getThreadPool(){
   //     if(mDownlodThreadPool==null){
   //         synchronized (ExecutorService.class){
   //             if(mDownlodThreadPool==null){
   //                 mDownlodThreadPool = Executors.newFixedThreadPool(1);
   //             }
   //         }
   //     }
  //      return mDownlodThreadPool;
  //  }
    /**
     * 获取线程池
     * */
    private ExecutorService getThreadPool(){
        if(mDownlodThreadPool==null){
            mDownlodThreadPool = Executors.newFixedThreadPool(5);
        }
        return mDownlodThreadPool;
    }

    /**
     * 添加Bitmap到内存缓存中
     * */
    private void addBitmapToMemoryCache(String key, Bitmap value){
        if(getBitmapFromMemoryCache(key) == null && value!=null){
            mMemoryCache.put(key, value);
        }
    }

    /**
     * 从内存缓存中获取Bitmap
     * */
    private Bitmap getBitmapFromMemoryCache(String key){
        return mMemoryCache.get(key);
    }

    /**
     * 下载图片
     * @param imageView 图片控件
     * @param listener  回调方法
     * @param url 图片地址
     * */
    public void displayImage(String url, ImageView imageView, onImageLoaderListener listener){
        downLoadImage(url, null, imageView, null, listener, null);
    }

    /**
     * 下载图片
     * @param imageSize 设置尺寸
     * @param listener  回调方法
     * @param url 图片地址
     * */
    public void displayImage(String url, ImageSize imageSize, onImageLoaderListener listener){
        downLoadImage(url, imageSize, null, null, listener, null);
    }


    /**
     * 下载图片
     * @param imageSize 设置图片尺寸
     * @param imageView 图片控件
     * @param listener 回调方法
     * @param url 图片地址
     * */
    public void loadImage(String url, ImageSize imageSize, ImageView imageView, DisplayImageOptions options, onImageLoaderListener listener,
                          onImageLoadingProgressListener progressListener){
        downLoadImage(url, imageSize, imageView, options, listener, progressListener);
    }

    /**
     * 下载图片
     * @param imageSize 设置图片尺寸
     * @param imageView 图片控件
     * @param listener 回调方法
     * @param url 图片地址
     * */
    public void loadImage(String url, ImageSize imageSize, ImageView imageView, onImageLoaderListener listener,
                          onImageLoadingProgressListener progressListener){
        loadImage(url, imageSize, imageView, null, listener, progressListener);
    }

    /**
     * 下载图片
     * @param imageSize 设置图片尺寸
     * @param imageView 图片控件
     * @param listener 回调方法
     * @param url 图片地址
     * */
    public void loadImage(String url, ImageSize imageSize, ImageView imageView, onImageLoaderListener listener){
        loadImage(url, imageSize, imageView, listener, null);
    }

    /**
     * 下载图片
     * @param imageSize 设置图片尺寸
     * @param imageView 图片控件
     * @param url 图片地址
     * */
    public void loadImage(String url, ImageSize imageSize, ImageView imageView){
        loadImage(url, imageSize, imageView, null);
    }

    /**
     * 下载图片
     * @param imageView 图片控件
     * @param url 图片地址
     * */
    public void loadImage(String url, ImageView imageView){
        loadImage(url, null, imageView);
    }

    /**
     * 下载图片
     * */
    private void downLoadImage(final String url, final ImageSize imageSize, final ImageView imageView, final DisplayImageOptions options,
                               final onImageLoaderListener listener, final onImageLoadingProgressListener progressListener){
        if(options!=null){
            imageView.setImageDrawable(options.getImageOnLoading(mContext.getResources()));
        }
        final String key = MD5.hashKeyForDisk(url);
        Bitmap bitmap = showCacheBitmap(key);
        if(bitmap!=null){
            if(imageSize!=null){
                bitmap = createNewBitmap(bitmap, imageSize);
            }
            if(imageView!=null){
                imageView.setImageBitmap(bitmap);
            }
            if(listener!=null){
                listener.onImageLoader(bitmap, url);
            }
            return;
        } else {
            final int[] fileLength = {0};
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case 0:
                            if(imageView!=null){
                                Bitmap tmpBitmap = (Bitmap)msg.obj;
                                if(tmpBitmap!=null){
                                    imageView.setImageBitmap(tmpBitmap);
                                    if(listener!=null){
                                        listener.onImageLoader(tmpBitmap, url);
                                    }
                                } else {
                                    if(options!=null){
                                        imageView.setImageDrawable(options.getImageOnFail(mContext.getResources()));
                                    }
                                }
                            }
                            break;
                        case 1:
                            if(progressListener!=null){
                                progressListener.onProgressUpdate(url, imageView, (int)msg.obj, fileLength[0]);
                            }
                            break;
                        case 2:
                            fileLength[0] = (int)msg.obj;
                            break;
                    }
                }
            };

            getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = saveBitmapToDisk(url, key, handler);  //下载并保存在储存卡中
                    if(bitmap!=null && imageSize!=null){
                        bitmap = createNewBitmap(bitmap, imageSize);
                    }
                    Message msg = handler.obtainMessage(0, bitmap);
                    handler.sendMessage(msg);

                    addBitmapToMemoryCache(key, bitmap); //添加Bitmap到手机内存里
                }
            });

        }
    }

    /**
     * 根据ImageSize重新设置图片尺寸
     * */
    private Bitmap createNewBitmap(Bitmap bitmap, ImageSize imageSize){
        //系统提供的图片缩放方法
        Bitmap tmp = ThumbnailUtils.extractThumbnail(bitmap, imageSize.getWidth(), imageSize.getHeight());
        return tmp;
    }

    /**
     * 从内存或者SD卡中获取图片
     * */
    private Bitmap showCacheBitmap(String key){
        if(getBitmapFromMemoryCache(key) != null){
            return getBitmapFromMemoryCache(key);
        } else {
            Bitmap bitmap = getBitmapFromDisk(key);
            addBitmapToMemoryCache(key, bitmap);
            return bitmap;
        }
    }

    /**
     * 从储存卡缓存中获取Bitmap
     * */
    private Bitmap getBitmapFromDisk(String key){
        Bitmap bitmap = null;
        try{
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if(snapshot!=null){
                InputStream is = snapshot.getInputStream(0);
                bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 获取储存卡已缓存图片的大小
     * */
    public int getDiskMemory(){
        return mMemoryCache.size();
    }

    /**
     * 保存Bitmap到储存卡中
     * */
    private Bitmap saveBitmapToDisk(String url, String key, Handler handler){
        Bitmap bitmap = null;
        try{
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if(editor!=null){
                OutputStream os = editor.newOutputStream(0);
                boolean succ = getBitmapFromUrl(url, key, handler, os);
                if(succ){
                    editor.commit(); //一定要执行此方法否则不会保存图片
                    bitmap = getBitmapFromDisk(key); //从SD卡获取
                } else {
                    editor.abort();
                }
            }
            mDiskLruCache.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 从网络中获取图片
     * */
    private boolean getBitmapFromUrl(String url, String key,  Handler handler, OutputStream fos){
        boolean succ = false;
        HttpURLConnection con = null;
        try{
            URL imgUrl = new URL(url);
            con = (HttpURLConnection)imgUrl.openConnection();
            con.setConnectTimeout(10 * 1000);
            con.setReadTimeout(10 * 1000);

            InputStream is = con.getInputStream();

            int fileLength = con.getContentLength();
            Message message = handler.obtainMessage(2, fileLength);
            handler.sendMessage(message);
            byte buf[] = new byte[1024];
            int downLoadFileSize = 0;
            do{
                int numread = is.read(buf);
                if(numread == -1){
                    break; //终止循环
                }
                fos.write(buf, 0, numread);
                downLoadFileSize += numread;
                Message msg = handler.obtainMessage(1, downLoadFileSize);
                handler.sendMessage(msg);
            } while (true);

            succ = true; //下载完成

        } catch (Exception e){
            e.printStackTrace();
            succ = false;
        } finally {
            if(con != null){
                con.disconnect();
            }
        }

        return succ;
    }

    /**
     * 取消下载任务
     * */
    //public synchronized void cancelTask(){
    //    if(mDownlodThreadPool != null){
    //        mDownlodThreadPool.shutdownNow();
    //        mDownlodThreadPool = null;
    //    }
    //}
    /**
     * 取消下载任务
     * */
    public void cancelTask(){
        if(mDownlodThreadPool != null){
            mDownlodThreadPool.shutdownNow();
            mDownlodThreadPool = null;
        }
    }

    /**
     * 获取app当前版本号
     * */
    private int getAppVersion(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            return info.versionCode;
        } catch (Exception e){
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 图片异步下载的回调方法
     * */
    public interface onImageLoaderListener{
        void onImageLoader(Bitmap bitmap, String url);
    }

    /**
     * 图片异步下载的进度回调方法
     * */
    public interface onImageLoadingProgressListener{
        void onProgressUpdate(String imageUri, View view, int current, int total);
    }

}
