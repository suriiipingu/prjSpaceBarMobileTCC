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

public class alterarCelular extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_celular);

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

        TextView txtCelular = findViewById(R.id.txtCelularAtual);
        TextView lblErro = findViewById(R.id.lblErroAtualizar);
        EditText txtCelularAtualizar = findViewById(R.id.txtEditCelular);
        Button btnAtualizar = findViewById(R.id.btnAtualizar);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        String celUsuario = sharedPreferences.getString("celularUsuario", "+00 (00) 00000-0000");
        int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

        txtCelular.setText("+" + celUsuario);
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String celular = txtCelularAtualizar.getText().toString();
                if(!celular.isEmpty()){
                    if (!isValidPhoneNumber(celular)){
                        lblErro.setText("Insira um número de celular válido para atualizar.");
                        return;
                    }
                    try(PreparedStatement statement = con.prepareStatement("Update tblUsuario set cel_usuario = ? where cod_usuario = ?")){
                        statement.setString(1, celular);
                        statement.setInt(2, codigoUsuario);
                        statement.executeUpdate();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("celularUsuario", celular);
                        editor.apply();

                        lblErro.setText("Seu número foi atualizado com sucesso!");
                    }catch (SQLException a){
                        a.printStackTrace();
                    }
                }else{
                    lblErro.setText("Escreva o seu número de celular para atualizar.");
                }
            }
        });


    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Remova todos os caracteres não numéricos do número de celular
        String numericPhoneNumber = phoneNumber.replaceAll("[^0-9]", "");
        return numericPhoneNumber.length() == 13;
    }

}