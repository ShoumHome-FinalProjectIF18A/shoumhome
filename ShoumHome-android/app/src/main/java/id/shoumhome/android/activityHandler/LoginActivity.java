package id.shoumhome.android.activityHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
        etusername = (EditText) findViewById(R.id. etusername);
        etpassword = (EditText) findViewById(R.id.etpassword);
        showPass = (CheckBox) findViewById(R.id.showPass);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set username dan password dengan "admin"
                if (etusername.equals("admin") && etpassword.equals("1234")) {
                    //jika login berhasil
                    Toast.makeText(getApplicationContext(), "BERHASIL MASUK", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
               } else {
                    //jika login gagal
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Username atau Password Anda salah!")
                            .setNegativeButton("Retry", null).create().show();
                }
            }
            });

        btnkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
}