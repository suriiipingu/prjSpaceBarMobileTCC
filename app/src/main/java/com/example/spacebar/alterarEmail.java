package com.example.spacebar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class alterarEmail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_email);

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

        TextView txtEmailUsuario = findViewById(R.id.txtEmailAtual);
        TextView lblErro = findViewById(R.id.lblErroAtualizar);
        EditText txtEmailAtualizar = findViewById(R.id.txtEditEmail);
        Button btnAtualizar = findViewById(R.id.btnAtualizar);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        String emailUsuario = sharedPreferences.getString("emailUsuario", "usuario@email.com");
        int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

        txtEmailUsuario.setText(emailUsuario);
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmailAtualizar.getText().toString();
                if(!email.isEmpty()){
                    if (!isValidEmail(email)) {
                        // Exibir uma mensagem de erro se o e-mail não for válido
                        lblErro.setText("Insira um email válido.");
                        return;
                    }else{
                        // Verificar se o email já existe no banco de dados
                        boolean emailExiste = objA.verificarEmailExistente(alterarEmail.this, email);
                        if (emailExiste) {
                            lblErro.setText("Esse email já existe. Tente outro.");
                        } else {
                            try(PreparedStatement statement = con.prepareStatement("Update tblUsuario set email_usuario = ? where cod_usuario = ?")){
                                statement.setString(1, email);
                                statement.setInt(2, codigoUsuario);
                                statement.executeUpdate();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("emailUsuario", email);
                                editor.apply();

                                lblErro.setText("Email atualizado com sucesso!");
                            }catch (SQLException a){
                                a.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}