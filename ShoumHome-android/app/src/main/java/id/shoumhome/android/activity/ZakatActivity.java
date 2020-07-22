package id.shoumhome.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import id.shoumhome.android.R;

public class ZakatActivity extends AppCompatActivity {
    EditText input_pertama, input_kedua;
    TextView hasil;
    Spinner spjeniszakat;
    Double v1,v2,hasil1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zakat);

        input_pertama = (EditText) findViewById(R.id.editText);
        input_kedua = (EditText) findViewById(R.id.editText1);
        hasil = (TextView) findViewById(R.id.textView7);
    }

    public void konver(){
        //konversi inputan ke double
        v1 = Double.parseDouble(input_pertama.getText().toString());
        v2 = Double.parseDouble(input_kedua.getText().toString());
    }

    public void proseskali(View view) {
        konver();
        hasil1 = v1*v2;  //perhitungan
        hasil.setText(Double.toString(hasil1));  //output
    }

    //TODO
    //Menambahkan Aksi Spinner Jenis Zakat
}
