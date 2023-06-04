package com.example.spacebar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InscreverLogin extends AppCompatActivity {

    EditText txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever_login);

        txtLogin = findViewById(R.id.txtLogin);

    }

    public void irParaProximaPagina(View view) {
        String login = txtLogin.getText().toString();

        // Verificar se o login já existe no banco de dados
        Acessa objA = new Acessa();
        boolean loginExiste = objA.verificarLoginExistente(this, login);

        if (loginExiste) {
            // O login já existe, exibir mensagem de erro
            Toast.makeText(this, "Esse nome de usuário já existe, tente outro.", Toast.LENGTH_SHORT).show();
        } else {
            // O login não existe, prosseguir para a próxima página
            Intent intent = new Intent(this, InscreverEmail.class);
            TempData tempData = TempData.getInstance();
            tempData.setLogin(login);
            startActivity(intent);
        }
    }
}