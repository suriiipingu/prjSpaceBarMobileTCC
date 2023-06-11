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

public class InscreverCelular extends AppCompatActivity {

    EditText txtCelular;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever_celular);
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

        txtCelular = findViewById(R.id.txtSenhaInscrever);
        ImageButton btnVoltar = findViewById(R.id.imgbtnVoltar2);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void irParaProximaPagina(View view) {
        String celular = txtCelular.getText().toString();

        // Verificar se o número de celular é válido
        if (!isValidPhoneNumber(celular)) {
            // Exibir uma mensagem de erro se o número de celular não for válido
            Toast.makeText(this, "Por favor, insira um número de celular válido", Toast.LENGTH_SHORT).show();

            return;
        }

        Intent intent = new Intent(this, InscreverPais.class);
        TempData tempData = TempData.getInstance();
        tempData.setCell(celular);
        startActivity(intent);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Remova todos os caracteres não numéricos do número de celular
        String numericPhoneNumber = phoneNumber.replaceAll("[^0-9]", "");

        // Verificar se o número de celular tem o tamanho adequado
        // Aqui, assumimos que um número de celular válido possui 11 dígitos
        return numericPhoneNumber.length() == 13;
    }

}