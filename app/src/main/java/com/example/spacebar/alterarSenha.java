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

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class alterarSenha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

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

        TextView lblErro = findViewById(R.id.lblErroAtualizar);
        EditText txtSenha = findViewById(R.id.txtEditSenha);
        EditText txtConfSenha = findViewById(R.id.txtEditConfSenha);
        Button btnAtualizar = findViewById(R.id.btnAtualizar);

        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String senha = txtSenha.getText().toString();
            String confSenha = txtConfSenha.getText().toString();
            String senhaCriptografada = criptografarSenha(senha);
                if (senha.isEmpty()) {
                    lblErro.setText("Você precisa digitar uma senha para atualizar.");
                }else{
                    if(confSenha.equals(senha)){
                        try(PreparedStatement statement = con.prepareStatement("Update tblUsuario set senha_usuario = ? where cod_usuario = ?")){
                            statement.setString(1, senhaCriptografada);
                            statement.setInt(2, codigoUsuario);
                            statement.executeUpdate();

                            lblErro.setText("Senha atualizada com sucesso!");
                        }catch (SQLException a){
                            a.printStackTrace();
                        }
                    }else{
                        lblErro.setText("As senhas não coincidem, tente novamente.!");
                    }
                }
            }
        });

    }
    public String criptografarSenha(String senha) {
        int fatorCusto = 12;

        String salt = BCrypt.gensalt(fatorCusto);

        String hashSenha = BCrypt.hashpw(senha, salt);

        return hashSenha;
    }
}