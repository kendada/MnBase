package cc.mnbase.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.mnbase.R;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-10-20
 * Time: 09:44
 * Version 1.0
 */

public class MenuLinearFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu_linear_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
