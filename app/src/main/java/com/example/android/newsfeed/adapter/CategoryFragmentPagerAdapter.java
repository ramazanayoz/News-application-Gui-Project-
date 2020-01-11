

package com.example.android.newsfeed.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.newsfeed.R;
import com.example.android.newsfeed.fragment.BusinessFragment;
import com.example.android.newsfeed.fragment.CultureFragment;
import com.example.android.newsfeed.fragment.EnvironmentFragment;
import com.example.android.newsfeed.fragment.FashionFragment;
import com.example.android.newsfeed.fragment.HomeFragment;
import com.example.android.newsfeed.fragment.ScienceFragment;
import com.example.android.newsfeed.fragment.SocietyFragment;
import com.example.android.newsfeed.fragment.SportFragment;
import com.example.android.newsfeed.fragment.WorldFragment;
import com.example.android.newsfeed.utils.Constants;

/***viewPager için Fragmentın uygun olması(hazır olması) sağlanır*/
public class CategoryFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public CategoryFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        System.out.println("göster mContext ---> CategoryFragmentPagerAdapter ---> "+mContext );
    }

    /***Tab hangi numaradaki sayfadaysa ona uygun fragment nesnesini oluşturtuyoruz*/
    @Override
    public Fragment getItem(int position) { //
        System.out.println("göster getItem(int position) fonk çağırıldı  ---> CategoryFragmentPagerAdapter " );
        System.out.println("göster position --> getItem(int position) ---> CategoryFragmentPagerAdapter ---> "+position );
        switch (position) {
            case Constants.HOME:  //0 == HOME
                return new HomeFragment();
            case Constants.WORLD:   //1 == WORLD
                return new WorldFragment();
            case Constants.SCIENCE: //2 == SCIENCE
                return new ScienceFragment();
            case Constants.SPORT:   //3 == SPORT
                return new SportFragment();
            case Constants.ENVIRONMENT: //4 == ENVIRONMENT
                 return new EnvironmentFragment();
            case Constants.SOCIETY: //5 == SOCIETY
                return new SocietyFragment();
            case Constants.FASHION: //6 == FASHION
                return new FashionFragment();
            case Constants.BUSINESS: //7 == BUSINESS
                return new BusinessFragment();
            case Constants.CULTURE: //8 == CULTURE
                return new CultureFragment();
            default:
                return null;
        }
    }

    /*** tabda gösterilmek istenen toplam sayfa sayısı*/
    @Override
    public int getCount() {
        return 9;
    } //kaç tane fragment tab  gösterilmesini istiyoruz

    /*** tap'in page title'ını döndürür*/
    @Override
    public CharSequence getPageTitle(int position) {
        System.out.println("göster position --> getPageTitle(int position) ---> CategoryFragmentPagerAdapter ---> "+position );

        int titleResId;
        switch (position) {
            case Constants.HOME:
                titleResId = R.string.ic_title_home;
                break;
            case Constants.WORLD:
                titleResId = R.string.ic_title_world;
                break;
            case Constants.SCIENCE:
                titleResId = R.string.ic_title_science;
                break;
            case Constants.SPORT:
                titleResId = R.string.ic_title_sport;
                break;
            case Constants.ENVIRONMENT:
                titleResId = R.string.ic_title_environment;
                break;
            case Constants.SOCIETY:
                titleResId = R.string.ic_title_society;
                break;
            case Constants.FASHION:
                titleResId = R.string.ic_title_fashion;
                break;
            case Constants.BUSINESS:
                titleResId = R.string.ic_title_business;
                break;
            default:
                titleResId = R.string.ic_title_culture;

                break;
        }
        System.out.println("göster ---> CategoryFragmentPagerAdapter ---> getPageTitle(int position) ----> R.string.ic_title_culture ---->"+R.string.ic_title_culture );

        return mContext.getString(titleResId);
    }
}