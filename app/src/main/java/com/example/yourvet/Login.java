package com.example.yourvet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText mail= findViewById(R.id.mail);
        final EditText password= findViewById(R.id.password);
        final Button loginButton= findViewById(R.id.loginBtn);
        final TextView registerNowButton= findViewById(R.id.registerNowBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mailText= mail.getText().toString();
                final String passwordText=password.getText().toString();
                if(mailText.isEmpty()||passwordText.isEmpty()){
                    Toast.makeText(Login.this,"Te rugam sa introduci mail-ul sau parola",Toast.LENGTH_SHORT).show();
                }
            }
        });
        registerNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
                finish();
            }
        });
    }
}