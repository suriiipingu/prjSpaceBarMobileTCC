package com.example.spacebar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class criador_conteudo extends AppCompatActivity {

    LinearLayout inserirLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criador_conteudo);

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
        LinearLayout inserirLinearLayout = findViewById(R.id.inserirLinearLayout);

        // obtendo a Conexao
        Acessa objA = new Acessa();
        Connection conexao = objA.entBanco(this);
        //obtendo o código do usuário da sua sessão
        SharedPreferences Session = this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int codigoUsuario = Session.getInt("codigoUsuario", -1);

        if(conexao != null){

            try(
                    PreparedStatement stringSQL = conexao.prepareStatement("Select cod_tipo from tblUsuario where cod_usuario= ?")) {
                stringSQL.setInt(1, codigoUsuario);
                ResultSet resultadoSQL = stringSQL.executeQuery();
                if (resultadoSQL.next()) {
                    int cod_tipo = resultadoSQL.getInt(1);
                    if(cod_tipo == 2 || cod_tipo == 4 || cod_tipo ==5){
                        //mostrar o criador_conteudo_true

                        // Inflate the XML view
                        LayoutInflater inflater = LayoutInflater.from(this);
                        View view = inflater.inflate(R.layout.criador_conteudo_true, inserirLinearLayout, false);

                        // Add the inflated view to the LinearLayout
                        inserirLinearLayout.addView(view);

                    }else{
                        // Inflate the XML view
                        LayoutInflater inflater = LayoutInflater.from(this);
                        View view = inflater.inflate(R.layout.criador_conteudo_false, inserirLinearLayout, false);

                        // Add the inflated view to the LinearLayout
                        inserirLinearLayout.addView(view);
                    }
                }
            }catch(SQLException a){
                a.printStackTrace();
            }
        }
    }
}