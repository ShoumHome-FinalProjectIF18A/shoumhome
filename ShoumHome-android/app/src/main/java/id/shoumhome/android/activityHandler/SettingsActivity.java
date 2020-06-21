package id.shoumhome.android.activityHandler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import id.shoumhome.android.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }

    Context getAppContext() {
        return this.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        switch(sp.getString("theme", "auto")) {
            case "auto":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }

        super.onBackPressed();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference defaultLocation = (Preference) findPreference("defaultLocation");
            Preference logout = (Preference) findPreference("logout");
            logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(getActivity().getApplicationContext());
                    ab.setTitle("Keluar?");
                    ab.setMessage("Yakin ingin keluar dari ShoumHome?");
                    ab.setPositiveButton("Ya, saya ingin keluar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Context context;
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            sp.edit().putString("username", "");
                            sp.edit().putString("password", "");
                            sp.edit().commit();

                            Intent i = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(i);
                        }
                    });
                    ab.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // nothing
                        }
                    });
                    ab.create().show();
                    return false;
                }
            });
            defaultLocation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SharedPreferences pf = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    String i = pf.getString("defaultLocation", "-7.0,109.0");

                    // Buat parmeter untuk dilemparkan ke MapsResultActivity
                    Intent ii = new Intent(".MapsResultActivity");
                    ii.putExtra("defaultLocation", i);

                    // Buka MapsResultActivity dan tunggu kasil keluaran dari activity tersebut
                    startActivityForResult(ii, 1);
                    return false;
                }
            });
        }
    }

    // Untuk menangkap hasil dari MapsResultActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("defaultLocation", data.getStringExtra("defaultLocation"));
            Toast.makeText(this, getString(R.string.loc_succes_change) + data.getStringExtra("address"), Toast.LENGTH_LONG).show();
            editor.commit();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}