package id.shoumhome.android.activityHandler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import id.shoumhome.android.R;

public class Menu_Daftar1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
    }

    public void kembali_ke_layar_utama(View view) {
        Intent i = new Intent(this, LayarUtama.class);
        startActivity(i);
    }

    public void lanjut_ke_login(View view) {
        Intent i = new Intent (this, LoginActivity.class);
        startActivity(i);
    }
}
