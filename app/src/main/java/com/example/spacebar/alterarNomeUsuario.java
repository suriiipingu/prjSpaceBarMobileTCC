package com.example.spacebar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class alterarNomeUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_nome_usuario);

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

        ImageButton btnVoltar = findViewById(R.id.btnVoltar4);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView txtNomeUsuario = findViewById(R.id.txtNomeAtual);
        TextView lblErro = findViewById(R.id.lblErroAtualizar);
        EditText txtNomeAtualizar = findViewById(R.id.txtEditNome);
        Button btnAtualizar = findViewById(R.id.btnAtualizar);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        String loginUsuario = sharedPreferences.getString("loginUsuario", "@usuario");
        int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

        txtNomeUsuario.setText("@" + loginUsuario);
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = txtNomeAtualizar.getText().toString();
                if(!login.isEmpty()){
                    boolean loginExiste = objA.verificarLoginExistente(alterarNomeUsuario.this, login);
                    if (loginExiste){
                        lblErro.setText("Esse nome de usuário já existe. Tente outro!");
                    }else{
                        try(PreparedStatement statement = con.prepareStatement("Update tblUsuario set login_usuario = ? where cod_usuario = ?")){
                            statement.setString(1, login);
                            statement.setInt(2, codigoUsuario);
                            statement.executeUpdate();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("loginUsuario", login);
                            editor.apply();

                            lblErro.setText("Nome de usuário atualizado com sucesso!");
                        }catch (SQLException a){
                            a.printStackTrace();
                        }
                    }
                }else{
                    lblErro.setText("Escreva um nome de usuário para atualizar.");
                }
            }
        });



    }
}