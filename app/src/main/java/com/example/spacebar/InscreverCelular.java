package com.example.spacebar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

        // Verificar se o número de celular é válido
        if (!isValidPhoneNumber(celular)) {
            // Exibir uma mensagem de erro se o número de celular não for válido
            Toast.makeText(this, "Por favor, insira um número de celular válido", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, InscreverPais.class);
        intent.putExtra("nome", nome);
        intent.putExtra("login", login);
        intent.putExtra("email", email);
        intent.putExtra("celular", celular);
        startActivity(intent);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Remova todos os caracteres não numéricos do número de celular
        String numericPhoneNumber = phoneNumber.replaceAll("[^0-9]", "");

        // Verificar se o número de celular tem o tamanho adequado
        // Aqui, assumimos que um número de celular válido possui 11 dígitos
        return numericPhoneNumber.length() == 13;
    }

}