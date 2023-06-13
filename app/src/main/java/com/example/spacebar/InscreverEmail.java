package com.example.spacebar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class InscreverEmail extends AppCompatActivity {

    EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever_email);
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

        txtEmail = findViewById(R.id.txtSenhaInscrever);

        ImageButton btnVoltar = findViewById(R.id.imgbtnVoltar2);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void irParaProximaPagina(View view) {
        String email = txtEmail.getText().toString();


        Acessa objA = new Acessa();

        if (!isValidEmail(email)) {
            // Exibir uma mensagem de erro se o e-mail não for válido
            Toast.makeText(this, "Por favor, insira um e-mail válido", Toast.LENGTH_SHORT).show();
            return;
        }else{
            // Verificar se o email já existe no banco de dados
            boolean emailExiste = objA.verificarEmailExistente(this, email);

            if (emailExiste) {
                // O email já existe, exibir mensagem de erro
                Toast.makeText(this, "Esse email de usuário já existe, tente outro.", Toast.LENGTH_SHORT).show();
            } else {
                // O email não existe, prosseguir para a próxima página
                Intent intent = new Intent(this, InscreverCelular.class);
                TempData tempData = TempData.getInstance();
                tempData.setEmail(email);
                startActivity(intent);
            }
        }


    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}