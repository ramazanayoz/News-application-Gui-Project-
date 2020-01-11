

package com.example.android.newsfeed.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.newsfeed.News;
import com.example.android.newsfeed.NewsLoader;
import com.example.android.newsfeed.NewsPreferences;
import com.example.android.newsfeed.R;
import com.example.android.newsfeed.adapter.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *uygylama ilk kaçıldığında --> onCreateView --> initializeLoader(isonnected) --> onCreateLoader -->  restartLoader
 *uygulama açıldıktan sonra fragment değiştirirken yada back butona basıldığında --> onCreateView --> initializeLoader(isonnected) --> onLoadFinished()
 *refresh yapıldığında --> onRefresh() --> initiateRefresh(isConnected) -->   --> restartLoader() --> onLoadFinished()
 */

public class BaseArticlesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<News>>{


    /** news loader ID. için değer */
    private static final int NEWS_LOADER_ID = 1;

    private NewsAdapter mAdapter;

    /** no news fond yazdıracağımız view  mEmptyStateTextView*/
    private TextView mEmptyStateTextView;

    /** NewLoader tamamlanıncaya kadar gözüken ProgressBar   */
    private View mLoadingIndicator;


    /**swipe yapacağımız view, bu viewle newsLoader işlemi tekrar yapılır*/
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**ilk bu fonksiyon çalışır*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("göster BaseArticlesFragment ----> onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) fonk");

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        /**RecyclerView'ü tanımlıyoruz*/
        RecyclerView mRecyclerView = rootView.findViewById(R.id.recycler_view);
       /**
        *LayoutManager Recyclerview da öğelerin yerleşimini yapmamız için gereklidir, Kendi layoutManager’imizi RecyclerView.LayoutManager’i extend ederek kullanabiliriz ya da hali hazırda olan LayoutManager’i da kullanabiliriz.
        *LinearLayoutManager : Alt alta tek sıra olacak şekilde. */
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // Recyclerview’in boyutunu değiştirmeyeceğimiz için performansı arttırmak için boyutunu sabitledik.
        mRecyclerView.setHasFixedSize(true);

        /** mRecyclerView'a layoutMenager set ediyoruz*/
        mRecyclerView.setLayoutManager(layoutManager);

        /**  SwipeRefreshLayout tanımlıyoruz*/
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);

        /**Kullanıcı bir yenileme için kaydırma hareketi gerçekleştirdiğinde ne olacağını ayrlıyoruz*/
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("göster BaseArticlesFragment --- > onRefresh() fonk");
                /**initiateRefresh() fonksiyonu ile  restartLoader işlemi gerçekleştirir*/
                initiateRefresh();
            }
        });

        /** layout'dan processing bar tanımlıyoruz*/
        mLoadingIndicator = rootView.findViewById(R.id.loading_indicator);

        /** bağlantı olmadığında göstereceiğimiz boş bir textViewvi tanımladık*/
        mEmptyStateTextView = rootView.findViewById(R.id.empty_Textview);

        /** adapter tanımladık şimdilik parametre olarak boş bir List gönderdik */
        mAdapter = new NewsAdapter(getActivity(), new ArrayList<News>());

        /** recylerview'a adapter'ü set ediyoruz*/
        mRecyclerView.setAdapter(mAdapter);

        /** internet bağlantısı varsa initializeLoader fonk çağırılır */
        initializeLoader(isConnected());

        return rootView;
    }


    /**loader işlemi gerçekleştirilmeden  önce hazırlık*/
    private void initializeLoader(boolean isConnected) {
        System.out.println("göster BaseArticlesFragment  ----> initializeLoader(boolean isConnected) fonk");

        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader with the NEWS_LOADER_ID
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }
    }




    /**bu kısmın temel amacı json olan link parametre eklenerek search uygun hale getirilir ve
    * json keyler ile value yakalanılarak stringler arrayliste kaydedilir
     *
    * diğer fragmentlarda override ediliyor çünkü sadece o sectionu search edebilmek için
    * bu kısım özellikle home fragmentı için gerekli çünkü homede section belirtmiyoruz */
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        System.out.println("göster BaseArticlesFragment ----> onCreateLoader(int i, Bundle bundle) fonk");


        Uri.Builder uriBuilder = NewsPreferences.getPreferredUri(getContext());


        /** parametre olarak işlemlerden geçirdiğimiz urlyi Newsloader'da kullanıyoruz */
        return new NewsLoader(getActivity(), uriBuilder.toString());
    }


    /**Loader işlemi finished olduunda en son yapılacak işlemlerimiz*/
    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> newsData) {
        System.out.println("göster BaseArticlesFragment ---> onLoadFinished(@NonNull Loader<List<News>> loader, List<News> newsData) fonk");


        // progress bar hide yapıyoruz  çünkü işleminiz biti
        mLoadingIndicator.setVisibility(View.GONE);





        /**newsData Listesi boş değilse adapter'e eklenir adapterden'de viewlara set yapılır adapter de recylerviewa set edilir*/
        if (newsData != null && !newsData.isEmpty()) {
            mEmptyStateTextView.setVisibility(View.GONE);
            mAdapter.addAll(newsData);
        }
        else{// News data null ise "No news found. set ederiz"
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText("No news found. \n Please Check Your Internet");
            // adapterdeki mNewsList clear edilecek bu sayede, set edilen View'lar boş olacak
            mAdapter.clearAll();
        }

        // Swiperefresh layout hide edilir bu kısımda
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**onLoaderReset ile  loader işlemleri tekrarlanır */
    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        System.out.println("göster BaseArticlesFragment ---> onLoaderReset(@NonNull Loader<List<News>> loader) fonk");

        System.out.println();
        //LoaderReset yapıyoruz bu yüzden adapterü clear yapıyoruz
        mAdapter.clearAll();
    }


    /**
     * swipe ile Refresh yaptığımızda burası çalışıyor ve burasıda yeniden loader yapmayı tetikliyor
     */
    private void initiateRefresh() {
        System.out.println("göster BaseArticlesFragment  ----> initiateRefresh() fonk ");
        restartLoader(isConnected());
    }

    /**
     swipe ile Refresh yapıldığında onLoaderReset tarafından bu fonk çağırılır
     */
    private void restartLoader(boolean isConnected) {
        System.out.println("göster BaseArticlesFragment  ----> restartLoader(boolean isConnected) fonk ");

        if (isConnected) { //interent bağlantısı varsa
            LoaderManager loaderManager = getLoaderManager();
            // bu kısım onCreateLoader fonk çalışmasını sağlar
            loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
        }
    }



    /**
     *  Internet bağlantısı kontrol eder
     */
    private boolean isConnected() {
        System.out.println("göster BaseArticlesFragment  ----> isConnected() fonk");
        //İNTERNET BĞLANTISINI KONTROL EDER
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }
}
