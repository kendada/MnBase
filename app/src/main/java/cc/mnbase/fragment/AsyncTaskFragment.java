package cc.mnbase.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import cc.mnbase.R;
import cc.mnbase.task.AsyncTask;
import cc.mnbase.task.ThreadPoolManager;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-29
 * Time: 14:53
 * Version 1.0
 */

public class AsyncTaskFragment extends BaseFragment {

    private ThreadPoolManager poolManager;

    private Button start_btn, stop_btn;
    private ImageView img;

    private String tag = AsyncTaskFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_async_task_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        poolManager = new ThreadPoolManager(ThreadPoolManager.TYPE_FIFO, 5);

        img = (ImageView)view.findViewById(R.id.img);

        start_btn = (Button)view.findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testTask();
            }
        });
        stop_btn = (Button)view.findViewById(R.id.stop_btn);
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poolManager.stop();
            }
        });

    }

    private void testTask(){
        AsyncTask task = new AsyncTask() {
            @Override
            public void updata(Object obj) {
                Log.i(tag, "***加载完成***"+obj);
                if(obj instanceof Bitmap){
                    img.setImageBitmap((Bitmap)obj);
                }
            }

            @Override
            public Object loadData() {
                Log.i(tag, "***加载数据开始***");
                Bitmap bitmap = null;
                try {
                    InputStream is = getActivity().getAssets().open("img01.jpg");
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }
        };

        poolManager.addAsyncTask(task);

        for(int i=1; i<20; i++){
            final int finalI = i;
            poolManager.addAsyncTask(new AsyncTask() {
                @Override
                public void updata(Object obj) {
                    Log.i(tag, "***加载完成***"+obj);
                }

                @Override
                public Object loadData() {
                    Log.i(tag, "***加载数据开始***");
                    return "|||---"+ finalI;
                }
            });
        }

        poolManager.start();
    }

}
