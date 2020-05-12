package id.dwichan.shoumhome.activityHandler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;

import id.dwichan.shoumhome.adapters.ListKajianAdapter;
import id.dwichan.shoumhome.R;

public class KajianActivity extends AppCompatActivity {
    ArrayList<String> mFoto = new ArrayList<>();
    ArrayList<String> mJudul = new ArrayList<>();
    ArrayList<String> mUstad = new ArrayList<>();
    ArrayList<String> mLink = new ArrayList<>();
    RecyclerView recyclerView;

    Spinner spJmlKolom;

    //buat di ActionBar
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
        setContentView(R.layout.kajian_main);

        // fungsi Toolbar dan ActionBar
        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("Kajian Online");
        setSupportActionBar(tb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mFoto.add("data:image/webp;base64,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");
        mJudul.add("Judul    : ");
        mUstad.add("Ustad  :");
        mLink.add("Link    :");

        mFoto.add("data:image/webp;base64,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");
        mJudul.add("Judul    :");
        mUstad.add("Ustad  :");
        mLink.add("Link    :");

        mFoto.add("data:image/webp;base64,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");
        mJudul.add("Judul    :");
        mUstad.add("Ustad  :");
        mLink.add("Link    :");

        mFoto.add("https://andi.link/wp-content/uploads/2018/10/Profil-Dosen-Andi-Dwi-Riyanto-M.Kom_..jpg");
        mJudul.add("Judul    :");
        mUstad.add("Ustad  :");
        mLink.add("Link    :");

        mFoto.add("data:image/jpeg;base64,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");
        mJudul.add("Judul    :");
        mUstad.add("Ustad  :");
        mLink.add("Link    :");

        mFoto.add("https://media-exp1.licdn.com/dms/image/C4D03AQGv6Nk-JzlpYw/profile-displayphoto-shrink_200_200/0?e=1591833600&v=beta&t=1_gEgJ-fTqsKmRRXjMYu_CeWsYDbtnh7Be3AyN9SdDc");
        mJudul.add("Judul    :");
        mUstad.add("Ustad  :");
        mLink.add("Link    :");

        mFoto.add("https://0.academia-photos.com/2725166/878772/1097226/s200_dhanar_intan_surya.saputra.jpg");
        mJudul.add("Judul    :");
        mUstad.add("Ustad  :");
        mLink.add("Link    :");

        mFoto.add("https://www.musabaqah.id/mtq-jatim-28-kab-tuban/foto/peserta/5323.jpg");
        mJudul.add("Judul    :");
        mUstad.add("Ustad  :");
        mLink.add("Link    :");

        mFoto.add("https://pbs.twimg.com/profile_images/3405272151/2e3146844d4d34a992a3b4da9b4a0390_400x400.jpeg");
        mJudul.add("Judul    :");
        mUstad.add("Ustad  :");
        mLink.add("Link    :");

        mFoto.add("https://i1.rgstatic.net/ii/profile.image/619877798785024-1524801792066_Q128/Ari_Tri_Astuti.jpg");
        mJudul.add("Judul    :");
        mUstad.add("Ustad  :");
        mLink.add("Link    :");

        recyclerView = (RecyclerView)findViewById(R.id.recDataKajian);
        ListKajianAdapter adapter = new ListKajianAdapter(this,mFoto,mJudul,mUstad,mLink);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spJmlKolom=(Spinner)findViewById(R.id.spJumlahKolom);
        spJmlKolom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().equalsIgnoreCase("1")) {
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                } else if (parent.getSelectedItem().toString().equalsIgnoreCase("2")) {
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1 ));
        } else if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2 ));
        }
    }
}
