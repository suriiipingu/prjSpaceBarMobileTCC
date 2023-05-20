package com.example.spacebar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class login extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtSenha;
    private TextView lblErro;
    private Acessa objA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        lblErro = findViewById(R.id.lblErro);

        objA = new Acessa();

        // Configurar a política de thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


    }

    public void TesteBanco(View alci){
        String email = txtEmail.getText().toString().trim();
        String senha = txtSenha.getText().toString();

        Connection con = objA.entBanco(this);

        if (con != null) {
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM tblUsuario WHERE email_usuario='" + email + "'");

                if (rs.next()) {
                    String senhaCriptografadaBanco = rs.getString("senha_usuario");

                    boolean senhaCorreta = false;
                    if (senhaCriptografadaBanco != null && senhaCriptografadaBanco.length() >= 60) {
                        senhaCorreta = BCrypt.checkpw(senha, senhaCriptografadaBanco);
                    }

                    if (senhaCorreta) {
                        // Login bem-sucedido
                        int codigoUsuario = rs.getInt("cod_usuario");
                        String loginUsuario = rs.getString("login_usuario");
                        String nomeUsuario = rs.getString("nome_usuario");
                        String emailUsuario = rs.getString("email_usuario");
                        String celUsuario = rs.getString("cel_usuario");
                        String paisUsuario = rs.getString("pais_usuario");
                        String bioUsuario = rs.getString("bio_usuario");

                        // Salvar informações do usuário na sessão ou em SharedPreferences, dependendo da abordagem utilizada

                        // Redirecionar para a próxima tela
                            startActivity(new Intent(this, Home.class));
                    } else {
                        lblErro.setText("Credenciais incorretas, verifique-as e tente novamente!");
                    }
                } else {
                    lblErro.setText("A senha está incorreta, tente novamente!");
                }

                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                lblErro.setText("Erro ao executar a consulta no banco de dados");
            }
        } else {
            lblErro.setText("Não foi possível conectar ao banco de dados");
        }
    }
}