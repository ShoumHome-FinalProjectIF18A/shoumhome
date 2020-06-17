package id.shoumhome.android.activityHandler;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import id.shoumhome.android.DataSessionHandler;
import id.shoumhome.android.R;


public class LoginActivity extends AppCompatActivity {
    Button btnlogin,btnkembali;
    EditText etusername,etpassword;
    CheckBox showPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        btnlogin = (Button) findViewById(R.id.btn_login);
        btnkembali = (Button) findViewById(R.id.btn_kembali);
        etusername = (EditText) findViewById(R.id.etusername);
        etpassword = (EditText) findViewById(R.id.etpassword);
        showPass = (CheckBox) findViewById(R.id.showPass);

        etusername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) etpassword.requestFocus();
                return true;
            }
        });

        etpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                prosesLogin(v);
                return true;
            }
        });

        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showPass.isChecked()){
                    //Saat Checkbox dalam keadaan Checked, maka password akan di tampilkan
                    etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //Jika tidak, maka password akan di sembuyikan
                    etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }

    public void prosesLogin(View view) {
        //set username dan password dengan "admin"
        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();
        if (username.equals("admin") && password.equals("1234")) {
            //jika login berhasil
            Toast.makeText(getApplicationContext(), "Selamat datang di ShoumHome!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);

            // parameter
            DataSessionHandler dsh = new DataSessionHandler();
            dsh.setUsername(username);
            dsh.setPassword(password);
            dsh.setNama_lengkap("Dwi Candra Permana");
            dsh.setEmail("dwichan@outlook.com");

            intent.putExtra("session", dsh);

            startActivity(intent);
            LayarUtamaActivity.lua.finish();
            finish(); // jangan lupa, buat ngancurin activity biar gak balik lagi kesini
        } else {
            //jika login gagal
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("Username atau Password Anda salah!")
                    .setNegativeButton("Retry", null).create().show();
        }
    }

    public void getBack(View view) {
        super.onBackPressed();
    }
}