package com.example.spacebar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

        //tirar a barra de titulo da pagina
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        ImageButton btnVoltarConfig = findViewById(R.id.btnVoltar2);
        btnVoltarConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        txtNome = findViewById(R.id.txtNomeUsuario);
        txtCelular = findViewById(R.id.txtCelularConta);
        txtEmail = findViewById(R.id.txtEmailConta);
        txtPais = findViewById(R.id.txtPaisConta);

        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

        try{
            PreparedStatement statement = con.prepareStatement("Select * from tblUsuario where cod_usuario= ?");
                statement.setInt(1, codigoUsuario);
                ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String nomeUsu = rs.getString("login_usuario");
                String celular = rs.getString("cel_usuario");
                String email = rs.getString("email_usuario");
                String pais = rs.getString("pais_usuario");

                txtNome.setText("@" + nomeUsu);
                txtCelular.setText(celular);
                txtEmail.setText(email);
                txtPais.setText(pais);
            }
            rs.close();

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        ctnNomeUsuario = findViewById(R.id.ctnNomeUsuario);
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