package com.example.spacebar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class InscreverSenha extends AppCompatActivity {

    EditText txtSenha;
    String nome, login, email, celular, pais;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever_senha);

        txtSenha = findViewById(R.id.txtSenhaInscrever);

        Intent intent = getIntent();
        nome = intent.getStringExtra("nome");
        login = intent.getStringExtra("login");
        email = intent.getStringExtra("email");
        celular = intent.getStringExtra("celular");
        pais = intent.getStringExtra("pais");
    }

    public void seInscrever (View view) {
        String senha = txtSenha.getText().toString();

        // Recuperar os valores passados pela outra página
        String nome = getIntent().getStringExtra("nome");
        String login = getIntent().getStringExtra("login");
        String email = getIntent().getStringExtra("email");
        String celular = getIntent().getStringExtra("celular");
        String pais = getIntent().getStringExtra("pais");

        Acessa objA = new Acessa();
        objA.inserirDados(this, nome, login, email, celular, pais, senha);
    }
}