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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class alterarPais extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_pais);

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

        TextView txtPais = findViewById(R.id.txtPaisAtual);
        TextView lblErro = findViewById(R.id.lblErroAtualizar);
        Button btnAtualizar = findViewById(R.id.btnAtualizar);
        Spinner spinnerPais = findViewById(R.id.spinnerPaisAtualizar);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        String paisUsuario = sharedPreferences.getString("paisUsuario", "País");
        int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

        txtPais.setText(paisUsuario);
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);

        List<String> countryList = new ArrayList<>();
        countryList.add("Afeganistão");
        countryList.add("África do Sul");
        countryList.add("Albânia");
        countryList.add("Alemanha");
        countryList.add("Andorra");
        countryList.add("Angola");
        countryList.add("Antígua e Barbuda");
        countryList.add("Arábia Saudita");
        countryList.add("Argélia");
        countryList.add("Argentina");
        countryList.add("Armênia");
        countryList.add("Austrália");
        countryList.add("Áustria");
        countryList.add("Azerbaijão");
        countryList.add("Bahamas");
        countryList.add("Bahrein");
        countryList.add("Bangladesh");
        countryList.add("Barbados");
        countryList.add("Belarus");
        countryList.add("Bélgica");
        countryList.add("Belize");
        countryList.add("Benin");
        countryList.add("Bhutan");
        countryList.add("Bolívia");
        countryList.add("Bósnia e Herzegovina");
        countryList.add("Botswana");
        countryList.add("Brasil");
        countryList.add("Brunei");
        countryList.add("Bulgária");
        countryList.add("Burkina Faso");
        countryList.add("Burundi");
        countryList.add("Butão");
        countryList.add("Cabo Verde");
        countryList.add("Camarões");
        countryList.add("Camboja");
        countryList.add("Canadá");
        countryList.add("Catar");
        countryList.add("Cazaquistão");
        countryList.add("Chade");
        countryList.add("Chile");
        countryList.add("China");
        countryList.add("Chipre");
        countryList.add("Cingapura");
        countryList.add("Colômbia");
        countryList.add("Comores");
        countryList.add("Coreia do Norte");
        countryList.add("Coreia do Sul");
        countryList.add("Costa do Marfim");
        countryList.add("Costa Rica");
        countryList.add("Croácia");
        countryList.add("Cuba");
        countryList.add("Dinamarca");
        countryList.add("Djibouti");
        countryList.add("Dominica");
        countryList.add("Egito");
        countryList.add("El Salvador");
        countryList.add("Emirados Árabes Unidos");
        countryList.add("Equador");
        countryList.add("Eritreia");
        countryList.add("Eslováquia");
        countryList.add("Eslovênia");
        countryList.add("Espanha");
        countryList.add("Estados Unidos");
        countryList.add("Estônia");
        countryList.add("Etiópia");
        countryList.add("Fiji");
        countryList.add("Filipinas");
        countryList.add("Finlândia");
        countryList.add("França");
        countryList.add("Gabão");
        countryList.add("Gâmbia");
        countryList.add("Gana");
        countryList.add("Geórgia");
        countryList.add("Granada");
        countryList.add("Grécia");
        countryList.add("Guatemala");
        countryList.add("Guiana");
        countryList.add("Guiné");
        countryList.add("Guiné Equatorial");
        countryList.add("Guiné-Bissau");
        countryList.add("Haiti");
        countryList.add("Honduras");
        countryList.add("Hungria");
        countryList.add("Iêmen");
        countryList.add("Ilhas Marshall");
        countryList.add("Ilhas Maurício");
        countryList.add("Ilhas Salomão");
        countryList.add("Índia");
        countryList.add("Indonésia");
        countryList.add("Irã");
        countryList.add("Iraque");
        countryList.add("Irlanda");
        countryList.add("Islândia");
        countryList.add("Israel");
        countryList.add("Itália");
        countryList.add("Jamaica");
        countryList.add("Japão");
        countryList.add("Jordânia");
        countryList.add("Kiribati");
        countryList.add("Kuwait");
        countryList.add("Laos");
        countryList.add("Lesoto");
        countryList.add("Letônia");
        countryList.add("Líbano");
        countryList.add("Libéria");
        countryList.add("Líbia");
        countryList.add("Liechtenstein");
        countryList.add("Lituânia");
        countryList.add("Luxemburgo");
        countryList.add("Macedônia do Norte");
        countryList.add("Madagascar");
        countryList.add("Malásia");
        countryList.add("Malawi");
        countryList.add("Maldivas");
        countryList.add("Mali");
        countryList.add("Malta");
        countryList.add("Marrocos");
        countryList.add("Mauritânia");
        countryList.add("México");
        countryList.add("Micronésia");
        countryList.add("Moçambique");
        countryList.add("Moldávia");
        countryList.add("Mônaco");
        countryList.add("Mongólia");
        countryList.add("Montenegro");
        countryList.add("Myanmar");
        countryList.add("Namíbia");
        countryList.add("Nauru");
        countryList.add("Nepal");
        countryList.add("Nicarágua");
        countryList.add("Níger");
        countryList.add("Nigéria");
        countryList.add("Noruega");
        countryList.add("Nova Zelândia");
        countryList.add("Omã");
        countryList.add("Países Baixos");
        countryList.add("Palau");
        countryList.add("Panamá");
        countryList.add("Papua Nova Guiné");
        countryList.add("Paquistão");
        countryList.add("Paraguai");
        countryList.add("Peru");
        countryList.add("Polônia");
        countryList.add("Portugal");
        countryList.add("Quênia");
        countryList.add("Quirguistão");
        countryList.add("Reino Unido");
        countryList.add("República Centro-Africana");
        countryList.add("República Checa");
        countryList.add("República Democrática do Congo");
        countryList.add("República do Congo");
        countryList.add("República Dominicana");
        countryList.add("Romênia");
        countryList.add("Ruanda");
        countryList.add("Rússia");
        countryList.add("Samoa");
        countryList.add("San Marino");
        countryList.add("Santa Lúcia");
        countryList.add("São Cristóvão e Nevis");
        countryList.add("São Tomé e Príncipe");
        countryList.add("São Vicente e Granadinas");
        countryList.add("Seicheles");
        countryList.add("Senegal");
        countryList.add("Serra Leoa");
        countryList.add("Sérvia");
        countryList.add("Singapura");
        countryList.add("Síria");
        countryList.add("Somália");
        countryList.add("Sri Lanka");
        countryList.add("Suazilândia");
        countryList.add("Sudão");
        countryList.add("Sudão do Sul");
        countryList.add("Suécia");
        countryList.add("Suíça");
        countryList.add("Suriname");
        countryList.add("Tailândia");
        countryList.add("Taiwan");
        countryList.add("Tajiquistão");
        countryList.add("Tanzânia");
        countryList.add("Timor-Leste");
        countryList.add("Togo");
        countryList.add("Tonga");
        countryList.add("Trinidade e Tobago");
        countryList.add("Tunísia");
        countryList.add("Turcomenistão");
        countryList.add("Turquia");
        countryList.add("Tuvalu");
        countryList.add("Uganda");
        countryList.add("Ucrânia");
        countryList.add("Uruguai");
        countryList.add("Uzbequistão");
        countryList.add("Vanuatu");
        countryList.add("Vaticano");
        countryList.add("Venezuela");
        countryList.add("Vietnã");
        countryList.add("Zâmbia");
        countryList.add("Zimbábue");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPais.setAdapter(adapter);

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pais = spinnerPais.getSelectedItem().toString();
                try(PreparedStatement statement = con.prepareStatement("Update tblUsuario set pais_usuario = ? where cod_usuario = ?")){
                    statement.setString(1, pais);
                    statement.setInt(2, codigoUsuario);
                    statement.executeUpdate();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("paisUsuario", pais);
                    editor.apply();

                    lblErro.setText("Seu país foi atualizado com sucesso!");
                }catch (SQLException a){
                    a.printStackTrace();
                }
            }
        });

    }
}