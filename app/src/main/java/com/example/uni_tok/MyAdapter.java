/*
package com.example.uni_tok;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                VideoFragment videoFragment = new VideoFragment();
                return videoFragment;
            case 1:
                SearchFragment searchFragment = new SearchFragment();
                return searchFragment;
            case 2:
                UploadFragment uploadFragment = new UploadFragment();
                return uploadFragment;
            case 3:
                ChannelFragment channelFragment = new ChannelFragment();
                return channelFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
 */