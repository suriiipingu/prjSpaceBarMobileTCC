package com.example.spacebar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InscreverEmail extends AppCompatActivity {

    EditText txtEmail;
    String nome, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever_email);

        txtEmail = findViewById(R.id.txtEmailinscrever);

        Intent intent = getIntent();
        nome = intent.getStringExtra("nome");
        login = intent.getStringExtra("login");
    }

    public void irParaProximaPagina(View view) {
        String email = txtEmail.getText().toString();

        Intent intent = new Intent(this, InscreverCelular.class);
        intent.putExtra("nome", nome);
        intent.putExtra("login", login);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}