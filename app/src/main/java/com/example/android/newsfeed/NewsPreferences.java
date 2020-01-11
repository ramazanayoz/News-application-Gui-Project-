

package com.example.android.newsfeed;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.android.newsfeed.utils.Constants;

import static com.example.android.newsfeed.utils.Constants.API_KEY;
import static com.example.android.newsfeed.utils.Constants.API_KEY_PARAM;
import static com.example.android.newsfeed.utils.Constants.FORMAT;
import static com.example.android.newsfeed.utils.Constants.FORMAT_PARAM;
import static com.example.android.newsfeed.utils.Constants.FROM_DATE_PARAM;
import static com.example.android.newsfeed.utils.Constants.ORDER_BY_PARAM;
import static com.example.android.newsfeed.utils.Constants.ORDER_DATE_PARAM;
import static com.example.android.newsfeed.utils.Constants.PAGE_SIZE_PARAM;
import static com.example.android.newsfeed.utils.Constants.QUERY_PARAM;
import static com.example.android.newsfeed.utils.Constants.SECTION_PARAM;
import static com.example.android.newsfeed.utils.Constants.SHOW_FIELDS;
import static com.example.android.newsfeed.utils.Constants.SHOW_FIELDS_PARAM;
import static com.example.android.newsfeed.utils.Constants.SHOW_TAGS;
import static com.example.android.newsfeed.utils.Constants.SHOW_TAGS_PARAM;

public final class NewsPreferences {

    /**
     *  context parametresini SharedPreferences erişmek için kullandık
     *  Uri.Builder return yapar
     *urlye parametreler ekleme kısmı shared prefence kullanarak settingsdeki ayarları okuyoruz ve
     *query için kullanacağımız urlye parametreler ekliyoruz
     */
    public static Uri.Builder getPreferredUri(Context context) {


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        /**getstring ile sharedPrefs'den belirtilen key'in ilişkilendirilmiş value'sini alıyoruz
         *1. parametrede key bulunamazsa 2. parametredeki default değer alınır
         */
        // itemSize key ile ilişkilendirilen value alınıyor

        String numOfItems = sharedPrefs.getString(
                context.getString(R.string.settings_number_of_items_key),
                context.getString(R.string.settings_number_of_items_default));

        // orderBy key ile ilişkilendirilen value alınıyor
        String orderBy = sharedPrefs.getString(
                context.getString(R.string.settings_order_by_key),
                context.getString(R.string.settings_order_by_default));

        // ordeDdate key ile ilişkilendirilen value alınıyor
        String orderDate = sharedPrefs.getString(
                context.getString(R.string.settings_order_date_key),
                context.getString(R.string.settings_order_date_default));

        // fromDate key ile ilişkilendirilen value alınıyor
        String fromDate = sharedPrefs.getString(
                context.getString(R.string.settings_from_date_key),
                context.getString(R.string.settings_from_date_default));


        //temel parametelerin ekleneceği url
        Uri baseUri = Uri.parse(Constants.NEWS_REQUEST_URL);
        System.out.println("NewsPreferences--> getPreferredUri(Context context)   -->baseUri --> "+baseUri);

        //baseUri buildUpon ile Uri.Builder nesnesine dönüştürülerek query parametreleri eklemeye hazır hale gelir
        Uri.Builder uriBuilder = baseUri.buildUpon();
        System.out.println("NewsPreferences--> getPreferredUri(Context context)   -->uriBuilder --> "+uriBuilder);

        //urlye belirtilen  query parametreleri ve sharedpref'den çekilen ayarların parametreleri ekleniyor urlbuilder nesnesine
        // query paremetresi = value şeklinde uribuilder'e  ekleme yapılıyor (örneğin 'page-size=22')
        uriBuilder.appendQueryParameter(QUERY_PARAM, "");
        System.out.println("NewsPreferences --> getPreferredUri(Context context) --> uriBuilder.appendQueryParameter(QUERY_PARAM, \"\") --> "+uriBuilder.appendQueryParameter(QUERY_PARAM, ""));
        uriBuilder.appendQueryParameter(ORDER_BY_PARAM, orderBy);
        System.out.println("NewsPreferences --> getPreferredUri(Context context) --> uriBuilder.appendQueryParameter(ORDER_BY_PARAM, orderBy) --> "+uriBuilder.appendQueryParameter(ORDER_BY_PARAM, orderBy));
        uriBuilder.appendQueryParameter(PAGE_SIZE_PARAM, numOfItems);
        uriBuilder.appendQueryParameter(ORDER_DATE_PARAM, orderDate);
        uriBuilder.appendQueryParameter(FROM_DATE_PARAM, fromDate);
        uriBuilder.appendQueryParameter(SHOW_FIELDS_PARAM, SHOW_FIELDS);
        uriBuilder.appendQueryParameter(FORMAT_PARAM, FORMAT);
        uriBuilder.appendQueryParameter(SHOW_TAGS_PARAM, SHOW_TAGS);
        uriBuilder.appendQueryParameter(API_KEY_PARAM, API_KEY); // The guardian sitesinden aldığımız api'yi ekliyoruz
        System.out.println("NewsPreferences --> getPreferredUri(Context context) --> uriBuilder --> "+uriBuilder);



        return uriBuilder;
    }

    /**
     * bu fonksyonda linke section dan gelen section query parametresi ekleniyor, örneğin http://link...&section=sport gibi
     *  getPreferredUri methoduna ulaşmak için context paremtresi kullanılır
     * sectionu parametresinin haber kategorisini belirlemek için kullandık
     */
    public static String getPreferredUrl(Context context, String section) { //
        Uri.Builder uriBuilder = getPreferredUri(context);
        System.out.println("NewsPreferences --> getPreferredUrl(Context context, String section) --> getPreferredUri(context) --> "+getPreferredUri(context));

        System.out.println("NewsPreferences --> getPreferredUrl(Context context, String section) --> uriBuilder.appendQueryParameter(SECTION_PARAM, section) --> "+uriBuilder.appendQueryParameter(SECTION_PARAM, section));

        return uriBuilder.appendQueryParameter(SECTION_PARAM, section).toString();
    }
}
