package com.example.spacebar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class InscreverPais extends AppCompatActivity {

    Spinner SpinnerPais;
    String nome, login, email, celular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever_pais);
        SpinnerPais = findViewById(R.id.spinnerPais);

        Intent intent = getIntent();
        nome = intent.getStringExtra("nome");
        login = intent.getStringExtra("login");
        email = intent.getStringExtra("email");
        celular = intent.getStringExtra("celular");
    }

    public void irParaProximaPagina(View view) {
        String pais = SpinnerPais.getSelectedItem().toString();

        Intent intent = new Intent(this, InscreverSenha.class);
        intent.putExtra("nome", nome);
        intent.putExtra("login", login);
        intent.putExtra("email", email);
        intent.putExtra("celular", celular);
        intent.putExtra("pais", pais);
        startActivity(intent);
    }
}