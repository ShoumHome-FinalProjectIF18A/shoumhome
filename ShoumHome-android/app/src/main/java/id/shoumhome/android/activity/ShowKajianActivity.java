package id.shoumhome.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import id.shoumhome.android.R;

public class ShowKajianActivity extends AppCompatActivity {
    private TextView tvKajianTitle, tvMosqueAddress, tvUstadzName, tvCategory, tvTimestampAnnounce, textView7, tvDescription;
    private ImageView imgThumbnail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_kajian);

    }
}