package com.example.spacebar;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class InscreverSenha extends AppCompatActivity {

    EditText txtSenha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever_senha);



        txtSenha = findViewById(R.id.txtSenhaInscrever);


    }

    public void seInscrever (View view) {

        String senha = txtSenha.getText().toString();

        TempData tempData = TempData.getInstance();
        String nome = tempData.getNome();
        String login = tempData.getLogin();
        String email = tempData.getEmail();
        String celular = tempData.getCell();
        String pais = tempData.getPais();

        Acessa objA = new Acessa();
        objA.inserirDados(this, nome, login, email, celular, pais, senha);
    }
}