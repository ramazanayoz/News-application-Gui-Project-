
package com.example.android.newsfeed.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.android.newsfeed.News;
import com.example.android.newsfeed.NewsLoader;
import com.example.android.newsfeed.NewsPreferences;
import com.example.android.newsfeed.R;

import java.sql.SQLOutput;
import java.util.List;


public class BusinessFragment extends BaseArticlesFragment {


    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        String businessUrl = NewsPreferences.getPreferredUrl(getContext(), getString(R.string.business));
        System.out.println("BusinessFragment ---> onCreateLoader(int i, Bundle bundle) ---> businessUrl"+businessUrl);

        // Create a new loader for the given URL
        return new NewsLoader(getActivity(),businessUrl);
    }
}
