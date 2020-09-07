package nemosofts.single.radio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Random;

import nemosofts.single.radio.Constant.Constant;
import nemosofts.single.radio.Receiver.Methods;
import nemosofts.single.radio.Receiver.Setting;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static Button play;
    public static RelativeLayout rl_loading;
    Methods methods;
    ImageView imageView;
    public static TextView titleWelcome;
    ImageView imageViewBackground;

    DrawerLayout drawer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.play);
        rl_loading =(RelativeLayout) findViewById(R.id.rl_loading);

        imageViewBackground = findViewById(R.id.image);

        if (Setting.isPlayed) {
            play.setBackgroundResource(R.drawable.ic_pause_white_24dp);

        } else {
            play.setBackgroundResource(R.drawable.ic_pause_white_24dp);

            Intent intent = new Intent(MainActivity.this, PlayerService.class);
            if (Constant.autoplay){
                if (Setting.isPlayed) {
                } else {
                    intent.setAction(PlayerService.ACTION_PLAY);
                    startService(intent);
                }
            }

        }


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.purchase_code.equals(Setting.itemAbout.getPurchase_code())){
                    playPause();
                }else {
                    methods.getVerify();
                }



            }
        });


        imageView = (ImageView)  findViewById(R.id.imagehome);
        titleWelcome = (TextView)  findViewById(R.id.title_welcome);
        titleWelcome.setText(getTimeOfTheDay());

        AdView mAdView3 = (AdView) findViewById(R.id.adView3);
        AdRequest adRequest3 = new AdRequest.Builder().build();
        mAdView3.loadAd(adRequest3);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu1);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toggle.setDrawerIndicatorEnabled(false);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    public void playPause() {
        Intent intent = new Intent(MainActivity.this, PlayerService.class);
        if (Setting.isPlayed) {
            intent.setAction(PlayerService.ACTION_TOGGLE);
            startService(intent);

        } else {
            intent.setAction(PlayerService.ACTION_PLAY);
            startService(intent);
        }
    }


    private String getTimeOfTheDay() {
        String message = getString(R.string.title_good_day);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        String[] images = new String[]{};
        if (timeOfDay >= 0 && timeOfDay < 6) {
            message = getString(R.string.title_good_night);
            images = getResources().getStringArray(R.array.night);
        } else if (timeOfDay >= 6 && timeOfDay < 12) {
            message = getString(R.string.title_good_morning);
            images = getResources().getStringArray(R.array.morning);
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            message = getString(R.string.title_good_afternoon);
            images = getResources().getStringArray(R.array.after_noon);
        } else if (timeOfDay >= 16 && timeOfDay < 20) {
            message = getString(R.string.title_good_evening);
            images = getResources().getStringArray(R.array.evening);
        } else if (timeOfDay >= 20 && timeOfDay < 24) {
            message = getString(R.string.title_good_night);
            images = getResources().getStringArray(R.array.night);
        }
        String day = images[new Random().nextInt(images.length)];
        loadTimeImage(day);
        return message;
    }

    private void loadTimeImage(String day) {
        Glide.with(this)
                .load(day)
                .placeholder(R.drawable.gradient_8)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        }else if (id == R.id.nav_facebook) {
            openFbUrl(Constant.facebook_name);
        }else if (id == R.id.nav_twitter) {
            openWebView(Constant.Twitter);
        }else if (id == R.id.nav_instagram) {
            openWebView(Constant.Instagram);
        } else if (id == R.id.nav_web) {
            openWebView(Constant.Web_Url);
        } else if (id == R.id.nav_abo) {
            Intent main = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(main);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void openWebView(String url){
        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
        intent.putExtra("URL",url);
        startActivity(intent);
    }
    protected void openFbUrl(String username){
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(username);
        facebookIntent.setData(Uri.parse(facebookUrl));
        startActivity(facebookIntent);
    }
    public String getFacebookPageURL(String username) {
        String FACEBOOK_URL = "https://www.facebook.com/"+username;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + username;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }
}
