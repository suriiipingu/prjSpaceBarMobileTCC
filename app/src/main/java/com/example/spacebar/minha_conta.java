package com.example.spacebar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class minha_conta extends AppCompatActivity {

    private TextView txtNome, txtCelular, txtEmail, txtPais;

    private LinearLayout ctnNomeUsuario, ctnCelular, ctnEmail, ctnPais, ctnSenha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);

        int statusBarColor = getResources().getColor(R.color.white);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor);
        }

        //tirar a barra de titulo da pagina
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        ImageButton btnVoltarConfig = findViewById(R.id.btnVoltar4);
        btnVoltarConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        txtNome = findViewById(R.id.txtCelularAtual);
        txtCelular = findViewById(R.id.txtCelularConta);
        txtEmail = findViewById(R.id.txtEmailConta);
        txtPais = findViewById(R.id.txtPaisConta);

        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        String celUsuario = sharedPreferences.getString("celularUsuario", "+00 (00) 00000-0000");
        String paisUsuario = sharedPreferences.getString("paisUsuario", "País");
        String loginUsuario = sharedPreferences.getString("loginUsuario", "@usuario");
        String emailUsuario = sharedPreferences.getString("emailUsuario", "usuario@email.com");
        int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);
        txtNome.setText(loginUsuario);
        txtCelular.setText(celUsuario);
        txtEmail.setText(emailUsuario);
        txtPais.setText(paisUsuario);

        ctnNomeUsuario = findViewById(R.id.NomeUsuarioAtual);
        ctnNomeUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(minha_conta.this, alterarNomeUsuario.class);
                startActivity(intent);
            }
        });

        ctnCelular = findViewById(R.id.ctnCelular);
        ctnCelular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(minha_conta.this, alterarCelular.class);
                startActivity(intent);
            }
        });

        ctnEmail = findViewById(R.id.ctnEmail);
        ctnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(minha_conta.this, alterarEmail.class);
                startActivity(intent);
            }
        });

        ctnPais = findViewById(R.id.ctnPais);
        ctnPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(minha_conta.this, alterarPais.class);
                startActivity(intent);
            }
        });

        ctnSenha = findViewById(R.id.ctnSenha);
        ctnSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(minha_conta.this, alterarSenha.class);
                startActivity(intent);
            }
        });

    }
}