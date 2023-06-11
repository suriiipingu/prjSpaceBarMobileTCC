package com.example.spacebar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class InscreverLogin extends AppCompatActivity {

    EditText txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever_login);

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


        txtLogin = findViewById(R.id.txtSenhaInscrever);

        ImageButton btnVoltar = findViewById(R.id.imgbtnVoltar2);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void irParaProximaPagina(View view) {
        String login = txtLogin.getText().toString();

        // Verificar se o login já existe no banco de dados
        Acessa objA = new Acessa();
        boolean loginExiste = objA.verificarLoginExistente(this, login);

        if (loginExiste) {
            // O login já existe, exibir mensagem de erro
            Toast.makeText(this, "Esse nome de usuário já existe, tente outro.", Toast.LENGTH_SHORT).show();
        } else {
            // O login não existe, prosseguir para a próxima página
            Intent intent = new Intent(this, InscreverEmail.class);
            TempData tempData = TempData.getInstance();
            tempData.setLogin(login);
            startActivity(intent);
        }
    }
}