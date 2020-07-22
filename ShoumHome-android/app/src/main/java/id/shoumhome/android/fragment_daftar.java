package id.shoumhome.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class fragment_daftar extends AppCompatActivity {

    Button btLanjut;
    EditText etUsername_daftar, etNoTelp_daftar, etTTL, etEmail, etKataSandi, etUlangiKataSandi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_daftar);
    }

    etUsername_daftar=(EditText)findViewById(R.id.etUser)
}

