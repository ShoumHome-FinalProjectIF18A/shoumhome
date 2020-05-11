package id.dwichan.shoumhome.activityHandler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import id.dwichan.shoumhome.R;

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
        super.onBackPressed();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference theme = (Preference) findPreference("theme");
            theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) { Toast.makeText(getActivity().getApplicationContext(), "Kamu perlu menjalankan ulang aplikasi ini untuk menerapkan perubahannya!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            Preference defaultLocation = (Preference) findPreference("defaultLocation");
            defaultLocation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SharedPreferences pf = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    String i = pf.getString("defaultLocation", "0.0,0.0");

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