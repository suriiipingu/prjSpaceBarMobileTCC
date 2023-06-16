package com.example.spacebar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

        // Verificar se o usuário já está logado
        if (isUserLoggedIn()) {
            startActivity(new Intent(this, home.class));
            finish(); // Finalizar a atividade de login para que o usuário não possa voltar a ela pressionando o botão "Voltar"
        }

        setContentView(R.layout.activity_login);

        // Defina a cor da barra de status para cada página
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
                        int tipoUsuario = rs.getInt("cod_tipo");
                        String loginUsuario = rs.getString("login_usuario");
                        String nomeUsuario = rs.getString("nome_usuario");
                        String bioUsuario = rs.getString("bio_usuario");
                        String celularUsuario = rs.getString("cel_usuario");
                        String emailUsuario = rs.getString("email_usuario");
                        String paisUsuario = rs.getString("pais_usuario");
                        String statusVerificado = rs.getString("status_verificado");
                        String msgVerificado = rs.getString("mensagem_verificado");

                        // Salvar código do usuário na sessão (SharedPreferences)
                        SharedPreferences sharedPreferences = getSharedPreferences("SessaoUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("codigoUsuario", codigoUsuario);
                        editor.putInt("tipoUsuario", tipoUsuario);
                        editor.putString("nomeUsuario", nomeUsuario);
                        editor.putString("loginUsuario", loginUsuario);
                        editor.putString("bioUsuario", bioUsuario);
                        editor.putString("celularUsuario", celularUsuario);
                        editor.putString("emailUsuario", emailUsuario);
                        editor.putString("paisUsuario", paisUsuario);
                        editor.putString("statusVerificado", statusVerificado);
                        editor.putString("mensagemVerificado", msgVerificado);
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();
                        // Redirecionar para a próxima tela
                            startActivity(new Intent(this, home.class));

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
    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("SessaoUsuario", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    public void entrarInscrever(View v){
        Intent intent = new Intent(this,Inscrever.class);
        startActivity(intent);
        finish();
    }

}