package com.example.spacebar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InscreverLogin extends AppCompatActivity {

    EditText txtLogin;
    String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever_login);

        txtLogin = findViewById(R.id.txtLogin);

        Intent intent = getIntent();
        nome = intent.getStringExtra("nome");
    }

    public void irParaProximaPagina(View view) {
        String login = txtLogin.getText().toString();

        Intent intent = new Intent(this, InscreverEmail.class);
        intent.putExtra("nome", nome);
        intent.putExtra("login", login);
        startActivity(intent);
    }
}