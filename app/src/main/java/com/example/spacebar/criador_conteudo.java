package com.example.spacebar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        ImageButton btnVoltar = findViewById(R.id.btnVoltar4);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // obtendo a Conexao
        Acessa objA = new Acessa();
        Connection conexao = objA.entBanco(this);

        SharedPreferences Session = this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int tipoUsuario = Session.getInt("tipoUsuario", -1);
        int codigoUsuario = Session.getInt("codigoUsuario", -1);

        if (tipoUsuario == 2 || tipoUsuario == 4 ||tipoUsuario == 5) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.criador_conteudo_true, inserirLinearLayout, false);

            inserirLinearLayout.addView(view);

        } else {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.criador_conteudo_false, inserirLinearLayout, false);

            inserirLinearLayout.addView(view);



            Button btnCriar = findViewById(R.id.btnCriar);
            btnCriar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if(conexao != null){
                   try(PreparedStatement statement = conexao.prepareStatement("Update tblUsuario set cod_tipo = ? where cod_usuario = ?")){
                       int tipo = 0;
                       if(tipoUsuario == 1){
                           tipo = 2;
                           statement.setInt(1, tipo);
                       }else if(tipoUsuario == 3){
                           tipo = 4;
                           statement.setInt(1, 4);
                       }
                       statement.setInt(2, codigoUsuario);
                       statement.executeUpdate();

                       // Atualizar a sessão com o novo tipo de usuário
                       atualizarTipoUsuario(tipo);
                       Toast.makeText(criador_conteudo.this, "Agora você é um criador de conteúdo!", Toast.LENGTH_SHORT).show();
                   }catch(SQLException ex){
                       ex.printStackTrace();
                   }
                }
                }
            });
        }
    }

    private void atualizarTipoUsuario(int novoTipoUsuario) {
        SharedPreferences session = getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = session.edit();
        editor.putInt("tipoUsuario", novoTipoUsuario);
        editor.apply();
    }
}