package com.example.spacebar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Inscrever extends AppCompatActivity {

    EditText txtNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever);

        txtNome = findViewById(R.id.txtNome);
    }

    public void irParaProximaPagina(View view) {
        String nome = txtNome.getText().toString();

        Intent intent = new Intent(this, InscreverLogin.class);
        intent.putExtra("nome", nome);
        startActivity(intent);
    }
}