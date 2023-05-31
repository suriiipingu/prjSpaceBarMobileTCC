package com.example.spacebar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacebar.databinding.ActivityHomeBinding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class home extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private RecyclerView recyclerView;

    private List<ItemLista> ItemLista;
    private Acessa objA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objA = new Acessa();


        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.nav_criarPost) {
                    // Ação a ser executada quando o botão for clicado
                    Intent intent = new Intent(home.this, criar_post.class);
                    startActivity(intent);
                    return true;
                }
                // Outros casos de seleção de item
                return false;
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        //lblTitulo = findViewById(R.id.lblTitulo);
        ItemLista = new ArrayList<>();
        ListaAdapter adapter = new ListaAdapter(this, ItemLista);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //int indexRecylerAtual = setupRecyclerView(); //0

        Connection con = objA.entBanco(this);
        if(con != null){
            try{
                String query = "SELECT * from tblPost INNER JOIN tblUsuario tU on tU.cod_usuario = tblPost.cod_usuario";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int codPost = rs.getInt("cod_post");
                    String titulo = rs.getString("titulo_post");
                    String data = rs.getString("data_post");
                    String nomeUsuario = rs.getString("nome_usuario");
                    String login= rs.getString("login_usuario");
                    byte[] iconimg = rs.getBytes("icon_usuario");
                    ItemLista.add(new ItemLista(titulo, data, nomeUsuario, "@"+login, iconimg));
                    ItemLista.get(ItemLista.size() - 1).setId(codPost);
                }
                adapter.notifyDataSetChanged(); // Notificar o adaptador sobre as mudanças na lista
                rs.close();
                stmt.close();
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

        //List<ItemLista> itemList = new ArrayList<>();
        //itemList.add(new ItemLista(R.drawable.icon, "Texto 1"));        //itemList.add(new ItemLista(R.drawable.icon, "Texto 2"));




}