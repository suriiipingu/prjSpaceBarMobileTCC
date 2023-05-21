package com.example.spacebar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InscreverEmail extends AppCompatActivity {

    EditText txtEmail;
    String nome, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever_email);

        txtEmail = findViewById(R.id.txtEmailinscrever);

        Intent intent = getIntent();
        nome = intent.getStringExtra("nome");
        login = intent.getStringExtra("login");
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
                Toast.makeText(this, "Esse nome de usuário já existe, tente outro.", Toast.LENGTH_SHORT).show();
            } else {
                // O email não existe, prosseguir para a próxima página
                Intent intent = new Intent(this, InscreverCelular.class);
                intent.putExtra("nome", nome);
                intent.putExtra("login", login);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        }


    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}