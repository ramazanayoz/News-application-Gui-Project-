

package com.example.android.newsfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.newsfeed.adapter.CategoryFragmentPagerAdapter;
import com.example.android.newsfeed.fragment.BaseArticlesFragment;
import com.example.android.newsfeed.fragment.SportFragment;
import com.example.android.newsfeed.utils.Constants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**toolbar kısmını set ediyoruz*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Tablayout ayarlaması yapıyoruz kısaca
        //fragmentlar arası swipe izin vermek için view pager tanımlıyoruz
        viewPager = findViewById(R.id.viewpager);
        // tabLayout tanımlıyoruz ve viewpager özelliğini tablayout kullanarak aktif ediyoruz swipe yapmak için
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        //ActionDrawerToogle  yani actionbarda 3lü simgeye basınca nagivationView açılmasını sağlıyoruzz
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //nagivation viev deen ilgili item tıklanınca ilgili fragment açılmasını sağlar
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //CategoryFragmentPagerAdapter sınıfından oluşturduğumuz adapteri alıyoruz
        CategoryFragmentPagerAdapter pagerAdapter = new CategoryFragmentPagerAdapter(this, getSupportFragmentManager());
        /**Viewpager'a pagerAdapter'ü set ediyoruz böylece, viewpager kısım artık ayarlanmış ve haberler gösterime hazır oluyor*/
        viewPager.setAdapter(pagerAdapter);
    }

    /**nagivationview'dan item seçildiğinde Viewpagerın o fragmentı göstermesi sağlanır*/
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        System.out.println("göster onNavigationItemSelected(@NonNull MenuItem item) fonk çağırıldı "+" //");

        //click yapılan itemin numarasını alır
        int id = item.getItemId();

        //Navigation Drawer'dan hangi iteme tıklandıysa, Viewpager'ın O Fragment'ına geçişi sağlanır
        //click yapılan itemin numarasına göre geçiş sağlanır
        if (id == R.id.nav_home) {
            viewPager.setCurrentItem(Constants.HOME); //0 == HOME
        } else if (id == R.id.nav_world) {
            viewPager.setCurrentItem(Constants.WORLD); //1 == WORLD
        } else if (id == R.id.nav_science) {
            viewPager.setCurrentItem(Constants.SCIENCE); //2 == SCIENCE
        } else if (id == R.id.nav_sport) {
            viewPager.setCurrentItem(Constants.SPORT); //3 == SPORT
        } else if (id == R.id.nav_environment) {
            viewPager.setCurrentItem(Constants.ENVIRONMENT); //4 == ENVIRONMENT
        } else if (id == R.id.nav_society) {
            viewPager.setCurrentItem(Constants.SOCIETY); //5 == SOCIETY
        } else if (id == R.id.nav_fashion) {
            viewPager.setCurrentItem(Constants.FASHION); //6 == FASHION
        } else if (id == R.id.nav_business) {
            viewPager.setCurrentItem(Constants.BUSINESS); //7 == BUSINESS
        } else if (id == R.id.nav_culture) {
            viewPager.setCurrentItem(Constants.CULTURE); //8 == CULTURE
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**geri tuşuna basıldığında*/
    @Override
    public void onBackPressed() {
        System.out.println("göster onBackPressed() fonk çağırıldı "+" //");
    }

    @Override
    /**Activity's options menu'nun contents'ini initialize ve inflate ediyoruz*/
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("göster onCreateOptionsMenu(Menu menu) fonk çağırıldı "+" //");

        //belirtilen xml dosyasındaki optins menu resourcesini inflate yaptırıyoruz
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    /**bu method options menu seçildiğinde çağırılıyor ve SettingsActivity intentine geçiş yapılıyor*/
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("göster onOptionsItemSelected(MenuItem item) fonk çağırıldı "+" //");
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
