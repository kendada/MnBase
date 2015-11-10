package cc.mnbase.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cc.mnbase.R;
import cc.mnbase.util.queue.Test;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-27
 * Time: 11:14
 * Version 1.0
 */

public class TestQueueFragment extends BaseFragment{

    private Button btn = null;
    private Test test = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test_queue_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        test = new Test();
        btn = (Button)view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test.add();
            }
        });

    }
}
