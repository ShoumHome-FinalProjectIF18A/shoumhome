package id.shoumhome.android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import java.util.ArrayList;

import id.shoumhome.android.adapters.ListKajianAdapter;
import id.shoumhome.android.R;
import id.shoumhome.android.models.Article;
import id.shoumhome.android.models.Kajian;
import id.shoumhome.android.viewmodels.ArticleViewModel;
import id.shoumhome.android.viewmodels.KajianViewModel;

public class KajianActivity extends AppCompatActivity {
    ArrayList<Kajian> mKajian = new ArrayList<>();
    String id = "";
    private NotificationChannel v;
    KajianViewModel kajianViewModel;
    ListKajianAdapter listKajianAdapter = new ListKajianAdapter(this);
    RecyclerView recDataKajian;

    Spinner spJmlKolom;

    //buat di ActionBar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.Image_Judul:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kajian);

        recDataKajian = findViewById(R.id.recDataKajian);
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.show_kajian));

        recDataKajian.setLayoutManager(new LinearLayoutManager(this));
        recDataKajian.setAdapter(listKajianAdapter);

        kajianViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(KajianViewModel.class);
        kajianViewModel.getKajian().observe(this, new Observer<ArrayList<Kajian>>() {
            @Override
            public void onChanged(ArrayList<Kajian> it) {
                if (it != null){
                    listKajianAdapter.setKajian(it);
                }
            }
        });

        kajianViewModel.setKajianAsync(this, listKajianAdapter, "");

    }
}
