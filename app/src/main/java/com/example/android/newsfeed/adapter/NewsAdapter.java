

package com.example.android.newsfeed.adapter;

import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.android.newsfeed.News;
import com.example.android.newsfeed.NewsDetailsActivity;
import com.example.android.newsfeed.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * NewsAdapter ile card item layouta newsloader'dan gelen Liste set ediliyor
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context mContext;
    private List<News> mNewsList;

    /**newsList parametresi newsLoadar --> QueryUtils ile Jsondan key value eşleşmesi ile dolduruluyor*/
    public NewsAdapter(Context context, List<News> newsList) {
        mContext = context;
        mNewsList = newsList;
    }

    /**news_card_item resource inflate ediliyor*/
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card_item, parent, false);
        return new ViewHolder(v);
    }

    /**Listedeki Toplam item(haber) sayısını return eder*/
    @Override
    public int getItemCount() {
        System.out.println("NewsAdapter --> getItemCount() --> mNewsList.size()"+mNewsList.size());
        return mNewsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView authorTextView;
        private TextView dateTextView;
        private ImageView thumbnailImageView;
        private ImageView shareImageView;
        private TextView trailTextView;
        private CardView cardView;
        private ProgressBar progressBar;

        //Viewleri tanımlıyoruz
        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_card);
            authorTextView = itemView.findViewById(R.id.author_card);
            dateTextView = itemView.findViewById(R.id.date_card);
            thumbnailImageView = itemView.findViewById(R.id.thumbnail_image_card);
            shareImageView = itemView.findViewById(R.id.share_image_card);
            trailTextView = itemView.findViewById(R.id.trail_text_card);
            cardView = itemView.findViewById(R.id.card_view);
            progressBar = itemView.findViewById(R.id.progress_load_photo);

        }
    }

    /**mNewsList listesi kullanılarak  her bir view için set işlemi yapılıyor*/
    @Override
    public void onBindViewHolder(ViewHolder holders, int position) {

        final ViewHolder holder = holders;




        //haber fragmentinde sayfayı aşşağı doğru kaydırdıkça yeni haber geliyor ve position 0 dan başlayıp  artıyor
        final News currentNews = mNewsList.get(position);
        System.out.println("NewsAdapter --> onBindViewHolder(ViewHolder holders, int position) --> currentNews and position-->" +currentNews.getTitle()+" "+ position);

        //titleTextView için set işlemi gerçekleşiyor
        holder.titleTextView.setText(currentNews.getTitle());

        // //authorTextView için set işlemi gerçekleşiyor
        //author null ise o authorTextView'u gizle
        if (currentNews.getAuthor() == null) {
            holder.authorTextView.setVisibility(View.GONE);
        } else {
            holder.authorTextView.setVisibility(View.VISIBLE);
            holder.authorTextView.setText(currentNews.getAuthor());
        }


        // şimdiki tarih ile yayınlanan tarif arasındaki farkı alıyoruz ve
        // dateTextView' a set yapıyoruz
        final CharSequence dateNews = getTimeDifference(formatDate(currentNews.getDate()));
        holder.dateTextView.setText(dateNews);

        //trailTextHTML  toString() fonk ile <br> gibi ifadeler yok ediliyor ve trailTextView bölümü için uygun hale getiriliyor
        //yani burada Html text convert to plain text yapıyoruz ve trailTextHTML'a set yapıyoruz
        String trailTextHTML = currentNews.getTrailTextHtml();
        System.out.println("NewsAdapter --> onBindViewHolder(ViewHolder holders, int position) --> trailTextHTML-->" +trailTextHTML);
        holder.trailTextView.setText(Html.fromHtml(trailTextHTML).toString());
        System.out.println("NewsAdapter --> onBindViewHolder(ViewHolder holders, int position) --> trailTextHTML-->" +trailTextHTML);
        System.out.println("NewsAdapter --> onBindViewHolder(ViewHolder holders, int position) --> Html.fromHtml(trailTextHTML)-->" +Html.fromHtml(trailTextHTML));


        /**cardview daki bir item'a tıklandığında NewsDetailsActivity intentine gitmesini söyledik*/
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext ,NewsDetailsActivity.class);

                intent.putExtra("url", currentNews.getUrl());
                intent.putExtra("title", currentNews.getTitle());
                intent.putExtra("img", currentNews.getThumbnail());
                intent.putExtra("date", dateNews);
                intent.putExtra("author", currentNews.getAuthor());

                mContext.startActivity(intent);


              /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mContext.startActivity(intent);
                }else {
                    mContext.startActivity(intent);
                } */


            }
        });

        //Thumbnail null değilse  thumbnailImageView  için  glide kullanarak set işlemi yapıyoruz
        if (currentNews.getThumbnail() == null) {
            holder.thumbnailImageView.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);

        } else {

            holder.thumbnailImageView.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.VISIBLE);

            System.out.println("NewsAdapter --> onBindViewHolder(ViewHolder holders, int position) --> currentNews.getThumbnail()-->" +currentNews.getThumbnail());
            // Load thumbnail with glide
            Glide.with(mContext.getApplicationContext())
                    .load(currentNews.getThumbnail())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.thumbnailImageView);


        }

        //share view için OnClickListener ayarlıyoruz paylaşım için
        holder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareData(currentNews);
            }
        });
    }

    //share işlemini gerçekleştiren fonksiyon
    private void shareData(News news) {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, news.getTitle() + " : " + news.getUrl());
        mContext.startActivity(Intent.createChooser(sharingIntent,mContext.getString(R.string.share_article)));

    }

    /**
     *  mNewsList listesindeki tüm datelar clear edilir bu sayede, set edilen View'lar boş olacak
     */
    public void clearAll() {
        mNewsList.clear();
        notifyDataSetChanged();
    }

    /**
     *mNewsList listesindeki tüm data'lar clear edilir, karışıklık olmaması için
     * sonra gelen data mNewsList listesine eklenir ve view'a set edilir
     */
    public void addAll(List<News> newsList) {
        mNewsList.clear();
        mNewsList.addAll(newsList);
        notifyDataSetChanged();
    }

    /**

     *
     * parametre olarak ( 2018-08-08T08:00:00Z)
     * yerel saate göre düzenler ve return olarak şeklinde çıkar ( "Jan 8, 2018  2:15 AM")
     */
    private String formatDate(String dateStringUTC) {
        // Parse the dateString into a Date object
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
        Date dateObject = null;
        try {
            dateObject = simpleDateFormat.parse(dateStringUTC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Initialize a SimpleDateFormat instance and configure it to provide a more readable
        // representation according to the given format, but still in UTC
        SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.ENGLISH);
        String formattedDateUTC = df.format(dateObject);
        // Convert UTC into Local time
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(formattedDateUTC);
            df.setTimeZone(TimeZone.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(date);
    }

    /**

     * şeklinde çıktı almak için ("9 hours ago")
     */
    private CharSequence getTimeDifference(String formattedDate) {
        long currentTime = System.currentTimeMillis();
        long publicationTime = getDateInMillis(formattedDate);
        return DateUtils.getRelativeTimeSpanString(publicationTime, currentTime,
                DateUtils.SECOND_IN_MILLIS);
    }

    /***/
    private static long getDateInMillis(String formattedDate) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("MMM d, yyyy  h:mm a");
        long dateInMillis;
        Date dateObject;
        try {
            dateObject = simpleDateFormat.parse(formattedDate);
            dateInMillis = dateObject.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
