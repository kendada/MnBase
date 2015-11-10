package cc.mnbase.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import cc.mnbase.R;
import cc.mnbase.image.FileUtils;
import cc.mnbase.image.ImageLoader;

/**
 * User: 靳世坤(1203596603@qq.com)
 * Date: 2015-10-22
 * Time: 10:16
 * Version 1.0
 */

public class DiskLruFragment extends BaseFragment {

    private ImageView img;
    private Button load_btn, delete_btn;
    private ProgressBar progressBar;

    private ImageLoader imageLoader;

    private FileUtils fileUtils;

    private String tag = DiskLruFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lru2_image_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageLoader = new ImageLoader(getActivity());
        fileUtils = new FileUtils(getActivity());

        img = (ImageView)view.findViewById(R.id.img);
        load_btn = (Button)view.findViewById(R.id.load_btn);
        delete_btn = (Button)view.findViewById(R.id.delete_btn);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        load_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    loadMore();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileUtils.deleteFile();
            }
        });

    }

    private void loadMore(){
        String url = "http://mnsfz-img.xiuna.com/pic/qingchun/2015-10-22/1/6631274473886133994.jpg";

        imageLoader.loadImage(url, img);
    }

}
