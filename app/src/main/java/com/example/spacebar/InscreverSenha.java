package com.example.spacebar;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;

public class InscreverSenha extends AppCompatActivity {

    EditText txtSenha, txtSenhaconf;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever_senha);
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



        txtSenha = findViewById(R.id.txtSenhaInscrever);
        txtSenhaconf = findViewById(R.id.txtSenhaInscrever3);
        ImageButton btnVoltar = findViewById(R.id.imgbtnVoltar2);
        Button btnlgl = findViewById(R.id.button3);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnlgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senha = txtSenha.getText().toString();
                String senhaconf = txtSenhaconf.getText().toString();

                if(senhaconf.equals(senha)){
                    String senhaCriptografada = criptografarSenha(senhaconf);

                    TempData tempData = TempData.getInstance();
                    String nome = tempData.getNome();
                    String login = tempData.getLogin();
                    String email = tempData.getEmail();
                    String celular = tempData.getCell();
                    String pais = tempData.getPais();

                    Acessa objA = new Acessa();
                    objA.inserirDados(InscreverSenha.this, nome, login, email, celular, pais, senhaCriptografada);
                }else{
                    Toast.makeText(InscreverSenha.this, "As senhas não conhecidem", Toast.LENGTH_SHORT).show();
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