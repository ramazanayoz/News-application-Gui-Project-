package com.example.android.newsfeed;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.newsfeed.adapter.NewsAdapter;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NewsDetailsActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    private ImageView imageView;
    private TextView appbar_title, appbar_subtitle, time,title;
    private boolean isHideTolbarView = false;
    private LinearLayout titleAppBar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private String mUrl, mImg, mTitle, mAuthor;
    private CharSequence mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        /**toolbarı tanımlıyoruz*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**CollapsingToolbarLayout tanımlıyoruz*/
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        /**vieawlar için tanımlama yapıyoruz*/
        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        titleAppBar = findViewById(R.id.title_appbar);
        imageView = findViewById(R.id.backdrop);
        appbar_title = findViewById(R.id.title_on_appbar);
        appbar_subtitle = findViewById(R.id.subtitle_on_appbar);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);

        /**putextradan gönderdiklerimizi alıyoruz*/
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mImg = intent.getStringExtra("img");
        mTitle = intent.getStringExtra("title");
        mDate = intent.getStringExtra("date");
        mAuthor = intent.getStringExtra("author");


        RequestOptions requestOptions = new RequestOptions();


        //thumnail'İ glide ile set işlemi yapıyoruz view'a
        Glide.with(this)
                .load(mImg)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);


        //title için set yapıyoruz
        appbar_subtitle.setText(mUrl);
        title.setText(mTitle);

        //author işlemi için set yapıyoruz
        String author = null;
        if (mAuthor !=null || mAuthor != ""){
            mAuthor = " \u2022 " + mAuthor;
        } else {
            author = "";
        }

        time.setText(mAuthor + " \u2022 " + mDate);


        /**web view özelliklerini ayarlıyoruz*/
        initWebView(mUrl);


    }
    /**web view özelliklerini ayarlıyoruz*/
    private void initWebView(String url){
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }


    /**web view activityde iken geri tuşuna basıldığında*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }



    //scroll kaydırdığımızda  appbar kısmının nasıl görüneceğini belirtiyoruz
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        if (percentage == 1f && isHideTolbarView) {
            titleAppBar.setVisibility(View.VISIBLE);
            isHideTolbarView= !isHideTolbarView;
        }

        else if (percentage < 1f && !isHideTolbarView) {
            titleAppBar.setVisibility(View.GONE);
            isHideTolbarView = !isHideTolbarView;
        }

    }

    //option menu infalte ediyoruz
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newsdetails, menu);
        return true;
    }

    //options menuden item seçilme işleminde
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //webview seçildiyse
        if (id ==R.id.view_web ){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mUrl));
            startActivity(i);
            return true;
        }

        //share seçildiyse
        else if (id == R.id.share){
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plan");
                // i.putExtra(Intent.EXTRA_SUBJECT, mTitle);
                String body = mTitle + "\n" + mUrl + "\n" + "Share from the News App" + "\n";
                i.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(i, "Share with:"));
            }catch(Exception e){
                Toast.makeText(this, "Hmm.. Sorry, \nCannot be share", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }


}