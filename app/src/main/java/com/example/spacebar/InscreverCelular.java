package com.example.spacebar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InscreverCelular extends AppCompatActivity {

    EditText txtCelular;
    String nome, login, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever_celular);

        txtCelular = findViewById(R.id.txtCelular);

        Intent intent = getIntent();
        nome = intent.getStringExtra("nome");
        login = intent.getStringExtra("login");
        email = intent.getStringExtra("email");
    }

    public void irParaProximaPagina(View view) {
        String celular = txtCelular.getText().toString();

        Intent intent = new Intent(this, InscreverPais.class);
        intent.putExtra("nome", nome);
        intent.putExtra("login", login);
        intent.putExtra("email", email);
        intent.putExtra("celular", celular);
        startActivity(intent);
    }
}