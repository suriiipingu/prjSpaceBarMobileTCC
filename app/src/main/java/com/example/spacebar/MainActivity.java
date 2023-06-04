package com.example.spacebar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.sql.SQLException;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //tirar a barra de titulo da pagina
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    public void entrarlogin(View v){
        Intent intent = new Intent(MainActivity.this,login.class);
        startActivity(intent);
        finish();
    }

    public void entrarInscrever(View v){
        Intent intent = new Intent(MainActivity.this,Inscrever.class);
        startActivity(intent);
        finish();
    }
}