package com.viet.news.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viet.news.R;


/**
 * @author null
 */
public class TwoFragment extends LazyFragment {
    private String tabName;
    private TextView mTabNameTv;
    private View mContainerView;

    public static TwoFragment newInstance(String tabTitle) {
        TwoFragment twoFragment = new TwoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tabName", tabTitle);
        twoFragment.setArguments(bundle);
        return twoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabName = getArguments().getString("tabName");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainerView = inflater.inflate(R.layout.fragment_two, container, false);
        return mContainerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTabNameTv = (TextView) mContainerView.findViewById(R.id.id_fragment_two);
        mTabNameTv.setText(String.valueOf("布局2" + tabName));
        initNet();
    }

    @Override
    public void initNet() {

    }
}
