package com.example.wpx.framework.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.wpx.framework.ui.base.BaseFragment;

import java.util.List;


/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class XFragmentPagerAdapter extends FragmentPagerAdapter {

    public static int MAIN_VIEW_PAGER = 1;//主界面的ViewPager

    private int mViewPagerType = 0;
    public String[] mainViewPagerTitle = null;
    private List<BaseFragment> mFragments;

    public XFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    //根据传入的viewPagerType，在getTitle中返回不同的标题信息
    public XFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments, int viewPagerType) {
        this(fm, fragments);
        mViewPagerType = viewPagerType;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {

//        if (mViewPagerType == MAIN_VIEW_PAGER) {
//            if (mainViewPagerTitle == null) {
//                mainViewPagerTitle = UIUtils.getStringArr(R.array.main_view_pager_title);
//            }
//            return mainViewPagerTitle[position];
//        }

        //默认的ViewPager(不需要返回title)
        return super.getPageTitle(position);
    }

}
