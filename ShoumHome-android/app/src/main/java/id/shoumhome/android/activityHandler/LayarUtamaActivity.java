package id.shoumhome.android.activityHandler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import id.shoumhome.android.R;

public class LayarUtamaActivity extends AppCompatActivity {
    public static Activity lua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lua = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layar_utama);
        Button btnMasuk = findViewById(R.id.btnMasuk);
        Button btnDaftar = findViewById(R.id.btnDaftar);

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LayarUtamaActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LayarUtamaActivity.this, DaftarActivity.class);
                startActivity(i);
            }
        });
    }
}
