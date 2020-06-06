package com.example.bca.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.bca.DailyExpense;
import com.example.bca.DailyExpenseList;
import com.example.bca.FairShare;
import com.example.bca.FairShareList;
import com.example.bca.HighExpense;
import com.example.bca.HighExpenseList;
import com.example.bca.MyMoney;
import com.example.bca.Profile;
import com.example.bca.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3,
            R.string.tab_text_4, R.string.tab_text_5, R.string.tab_text_6, R.string.tab_text_7, R.string.tab_text_8};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                FairShare fairShare = new FairShare();
                return fairShare;
            case 1:
                FairShareList fairShareList = new FairShareList();
                return fairShareList;
            case 2:
                DailyExpense dailyExpense = new DailyExpense();
                return dailyExpense;
            case 3:
                DailyExpenseList dailyExpenseList = new DailyExpenseList();
                return dailyExpenseList;
            case 4:
                HighExpense highExpense = new HighExpense();
                return highExpense;
            case 5:
                HighExpenseList highExpenseList = new HighExpenseList();
                return highExpenseList;
            case 6:
                MyMoney myMoney = new MyMoney();
                return myMoney;
            case 7:
                Profile profile = new Profile();
                return profile;
        }
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 8;
    }
}