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

import id.dwichan.shoumhome.adapters.ListArtikelAdapter;
import id.dwichan.shoumhome.adapters.ListKajianAdapter;
import id.dwichan.shoumhome.R;

public class ArtikelActivity extends AppCompatActivity {

    ArrayList<String> mImageJudul =new ArrayList<>();
    ArrayList<String> mJudul =new ArrayList<>();
    ArrayList<String> mRingkasan =new ArrayList<>();
    ArrayList<String> mImageLike =new ArrayList<>();
    ArrayList<String> mlike =new ArrayList<>();
    ArrayList<String> mtanggal =new ArrayList<>();
    ArrayList<String> mustad =new ArrayList<>();

    @Override
    protected void  onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_artikel__islami);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Artikel Islami");

        mImageJudul.add("https://ypiaflash.com/muslim.or.id/wp-content/uploads/2010/08/sedekah-ajib-810x500.jpg");
        mJudul.add("Dahsyatnya Sedekah di Bulan Ramadhan");
        mRingkasan.add("Salah satu pintu yang dibuka oleh Allah untuk meraih keuntungan besar dari bulan Ramadhan adalah melalui sedekah. Islam sering menganjurkan umatnya untuk banyak bersedekah. Dan bulan Ramadhan, amalan ini menjadi lebih dianjurkan lagi. Dan demikianlah sepatutnya akhlak seorang mukmin, yaitu dermawan. Allah dan Rasul-Nya memerintahkan bahkan memberi contoh kepada umat Islam untuk menjadi orang yang dermawan serta pemurah ");
        mImageLike.add("https://www.google.com/url?sa=i&url=http%3A%2F%2Fwww.iconarchive.com%2Fshow%2Foutline-icons-by-iconsmind%2FLike-2-icon.html&psig=AOvVaw2QR12bKnOHcUNfOLpHMB4w&ust=1590729464881000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCOC20p_n1ekCFQAAAAAdAAAAABAg");
        mlike.add("5 like");
        mtanggal.add("20 Mei 2020");
        mustad.add("Ustadz Yulian Purnama, S.Kom.");

        mImageJudul.add("https://ypiaflash.com/muslim.or.id/wp-content/uploads/2010/08/sedekah-ajib-810x500.jpg");
        mJudul.add("Dahsyatnya Sedekah di Bulan Ramadhan");
        mRingkasan.add("Salah satu pintu yang dibuka oleh Allah untuk meraih keuntungan besar dari bulan Ramadhan adalah melalui sedekah. Islam sering menganjurkan umatnya untuk banyak bersedekah. Dan bulan Ramadhan, amalan ini menjadi lebih dianjurkan lagi. Dan demikianlah sepatutnya akhlak seorang mukmin, yaitu dermawan. Allah dan Rasul-Nya memerintahkan bahkan memberi contoh kepada umat Islam untuk menjadi orang yang dermawan serta pemurah ");
        mImageLike.add("https://www.google.com/url?sa=i&url=http%3A%2F%2Fwww.iconarchive.com%2Fshow%2Foutline-icons-by-iconsmind%2FLike-2-icon.html&psig=AOvVaw2QR12bKnOHcUNfOLpHMB4w&ust=1590729464881000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCOC20p_n1ekCFQAAAAAdAAAAABAg");
        mlike.add("10like");
        mtanggal.add("20 Mei 2020");
        mustad.add("Ustadz Yulian Purnama, S.Kom.");

        mImageJudul.add("https://ypiaflash.com/muslim.or.id/wp-content/uploads/2010/08/sedekah-ajib-810x500.jpg");
        mJudul.add("Dahsyatnya Sedekah di Bulan Ramadhan");
        mRingkasan.add("Salah satu pintu yang dibuka oleh Allah untuk meraih keuntungan besar dari bulan Ramadhan adalah melalui sedekah. Islam sering menganjurkan umatnya untuk banyak bersedekah. Dan bulan Ramadhan, amalan ini menjadi lebih dianjurkan lagi. Dan demikianlah sepatutnya akhlak seorang mukmin, yaitu dermawan. Allah dan Rasul-Nya memerintahkan bahkan memberi contoh kepada umat Islam untuk menjadi orang yang dermawan serta pemurah ");
        mImageLike.add("https://www.google.com/url?sa=i&url=http%3A%2F%2Fwww.iconarchive.com%2Fshow%2Foutline-icons-by-iconsmind%2FLike-2-icon.html&psig=AOvVaw2QR12bKnOHcUNfOLpHMB4w&ust=1590729464881000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCOC20p_n1ekCFQAAAAAdAAAAABAg");
        mlike.add("200like");
        mtanggal.add("20 Mei 2020");
        mustad.add("Ustadz Yulian Purnama, S.Kom.");

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recDataArtikel);
        ListArtikelAdapter adapter=new ListArtikelAdapter(this, mImageJudul, mJudul, mRingkasan, mlike, mtanggal, mustad);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
