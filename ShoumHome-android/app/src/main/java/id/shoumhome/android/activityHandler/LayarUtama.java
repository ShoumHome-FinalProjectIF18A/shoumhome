package id.shoumhome.android.activityHandler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import id.shoumhome.android.R;

public class LayarUtama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layar_utama);
    }

    public void masuk_ke_login(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void masuk_ke_daftar(View view) {
        Intent i = new Intent(this, Menu_Daftar1.class);
        startActivity(i);
    }
}
