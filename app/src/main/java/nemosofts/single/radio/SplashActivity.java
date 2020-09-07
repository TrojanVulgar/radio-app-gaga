package nemosofts.single.radio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import nemosofts.single.radio.Receiver.LoadNemosofts;
import nemosofts.single.radio.Receiver.NemosoftsListener;
import nemosofts.single.radio.Receiver.Setting;
import nemosofts.single.radio.SharedPref.SharedPref;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1500;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPref = new SharedPref(this);


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();

        if (Setting.isPlayed) {
            Intent main = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(main);
            finish();
        }else {
            if (isNetworkAvailable(SplashActivity.this)) {
                Loadnemosofts();
            }
        }
    }

    public void Loadnemosofts() {
        if (sharedPref.getIsFirstPurchaseCode()) {
            Toast.makeText(SplashActivity.this, "load Settings", Toast.LENGTH_SHORT).show();
            LoadNemosofts loadAbout = new LoadNemosofts(SplashActivity.this, new NemosoftsListener() {
                @Override
                public void onStart() {
                }
                @Override
                public void onEnd(String success, String verifyStatus, String message) {
                    if (success.equals("1")) {
                        if (!verifyStatus.equals("-1")) {
                            if (BuildConfig.APPLICATION_ID.equals(Setting.itemAbout.getPackage_name())) {
                                sharedPref.setIsFirstPurchaseCode(false);
                                sharedPref.setPurchaseCode(Setting.itemAbout);
                                thiva();
                            } else {
                                errorDialog(getString(R.string.error_nemosofts_key), getString(R.string.create_nemosofts_key));
                            }
                        } else {
                            errorDialog(getString(R.string.error_nemosofts_key), message);
                        }
                    } else {
                        errorDialog(getString(R.string.err_internet_not_conn), getString(R.string.error_connect_net_tryagain));
                    }
                }
            });
            loadAbout.execute();
        } else {
            sharedPref.getPurchaseCode();
            thiva();
        }
    }

    private void errorDialog(String title, String message) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this, R.style.ThemeDialog);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        if (title.equals(getString(R.string.err_internet_not_conn)) || title.equals(getString(R.string.server_error))) {
            alertDialog.setNegativeButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Loadnemosofts();
                }
            });
        }

        alertDialog.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void thiva() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(main);
                finish();
            }
        },SPLASH_TIME_OUT);

    }

    public static boolean isNetworkAvailable(Context activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }




}