package id.shoumhome.android.activity;

import android.app.NotificationChannel;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.shoumhome.android.R;
import id.shoumhome.android.adapters.ListArtikelAdapter;
import id.shoumhome.android.models.Article;
import id.shoumhome.android.viewmodels.ArticleViewModel;
import id.shoumhome.android.viewmodels.ReadArticleViewModel;

import static id.shoumhome.android.activity.ReadArticleActivity.EXTRA_ARTICLE_ID;

public class ArtikelActivity extends AppCompatActivity {
    ArrayList<Article> mArticle = new ArrayList<>();
    String id = "";
    private NotificationChannel v;
    ArticleViewModel articleViewModel;
    ListArtikelAdapter listArtikelAdapter = new ListArtikelAdapter(this);
    RecyclerView recDataArtikel;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.I_Judul:
                break;
        }
        return true;
    }

    @Override
    protected void  onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_artikel__islami);

        recDataArtikel = findViewById(R.id.recDataArtikel);
        // fungsi Toolbar dan ActionBar(back pd artikel)
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.read_article));

        // Ambil Bundle Value
        Bundle bundle = getIntent().getExtras();
        id = bundle.get(EXTRA_ARTICLE_ID).toString();

        recDataArtikel.setLayoutManager(new LinearLayoutManager(this));
        recDataArtikel.setAdapter(listArtikelAdapter);

        articleViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(ArticleViewModel.class);
        articleViewModel.getArticle().observe(this, new Observer<ArrayList<Article>>() {
            @Override
            public void onChanged(ArrayList<Article> it) {
               if (it != null){
                   listArtikelAdapter.setArticle(it);
               }
            }
        });

        articleViewModel.setArticleAsync(this, listArtikelAdapter, "");
    }

}

