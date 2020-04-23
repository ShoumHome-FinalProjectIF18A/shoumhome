package id.dwichan.shoumhome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private void setActTheme() {
        String theme = sharedPreferences.getString("theme", "light");
        switch(theme) {
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "auto":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setActTheme();
        super.onCreate(savedInstanceState);
        int waktu_load = 4000;
        setContentView(R.layout.activity_splash);
        TextView version = findViewById(R.id.version);
        String getVersion = getString(R.string.app_version);
        version.setText("Versi " + getVersion);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home=new Intent(SplashActivity.this, MainActivity.class);
                startActivity(home);
                finish();
            }
        },waktu_load);
    }
}
