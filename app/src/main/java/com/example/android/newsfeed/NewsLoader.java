
package com.example.android.newsfeed;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.newsfeed.utils.QueryUtils;

import java.util.List;

/**
 * burda asyncTask kullandık çünkü urlye openconnection işlemi gerçekleştiriliyor
 * Loads a list of news by using an AsyncTask to perform the network request to the given URL.
 * Verilen URL'ye network request  gerçekleştirmek için bir AsyncTask kullanarak haber listesini yükler.
 * çalışma sırası  NewsLoader() fonk --> onStartLoading() fonk  -->loadInBackground() fonk
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {


    /** Query URL */
    private String mUrl;
    private Context mContext;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        this.mContext = context;
        mUrl = url;
        System.out.println("göster -->  NewsLoader --> NewsLoader(Context context, String url) ---> mUrl --> "+ mUrl );
        System.out.println("göster -->  NewsLoader --> NewsLoader(Context context, String url) fonk  --> " );

    }

    /** loadInBackground() forceLoad() fonksiyonun çalışmasını  tetikltyoruz*/
    @Override
    protected void onStartLoading() {
        forceLoad();
        System.out.println("göster -->  NewsLoader --> onStartLoading() fonk  --> " );

    }

    /*** This is on a background thread.*/
    /** bu fonksiyondaki işlemler arkaplanda tread açılarak arka planda yapılıyor*/
    @Override
    public List<News> loadInBackground() {
        System.out.println("göster -->  NewsLoader --> loadInBackground() fonk  --> " );
        if (mUrl == null) {
            return null;
        }

        //network request gerçekleştriyoruz ve urlyi parse yapıyoruz ve parse edilen verileri  List<News> newsList listesine ekliyoruz
        // json seklinde gelen url'yi  quiryutils den işleme geçirip parse yapıyoruz
        List<News> newsData = QueryUtils.fetchNewsData(mUrl,mContext);
    //    QueryUtils queryUtils= new QueryUtils(mContext);
//        System.out.println("göster -->  NewsLoader --> loadInBackground()  --> newsData --> " + newsData.get(1).getTitle()  );
        System.out.println("göster -->  NewsLoader --> loadInBackground()  --> newsData --> " + newsData  );
        return newsData;
    }


}
