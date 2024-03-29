package com.example.spacebar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacebar.databinding.ActivityHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class home extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private RecyclerView recyclerView;

    private List<ItemLista> ItemLista;
    private Acessa objA;
    private SharedPreferences session;
    private int tipoUsuario;

    private FloatingActionButton fab;

    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("tipoUsuario")) {
                tipoUsuario = sharedPreferences.getInt("tipoUsuario", -1);
                updateFabVisibility();
            }
        }
    };

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
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_perfil)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


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


        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, criar_post.class);
                startActivity(intent);
            }
        });

        session = getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        tipoUsuario = session.getInt("tipoUsuario", -1);
        session.registerOnSharedPreferenceChangeListener(listener);

        updateFabVisibility();


        // Adicione este código para ocultar a BottomNavigationView quando a segunda activity for aberta
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_configuracoes) {
                    navView.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);
                } else {
                    navView.setVisibility(View.VISIBLE);
                    updateFabVisibility();
                }
            }
        });
        NavigationUI.setupWithNavController(navView, navController);



        recyclerView = findViewById(R.id.recyclerView);
        ItemLista = new ArrayList<>();
        ListaAdapter adapter = new ListaAdapter(this, ItemLista);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Connection con = objA.entBanco(this);
        if(con != null){
            try{
                String query = "SELECT * from tblPost INNER JOIN tblUsuario tU on tU.cod_usuario = tblPost.cod_usuario order by cod_post desc";

                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int codPost = rs.getInt("cod_post");
                    String titulo = rs.getString("titulo_post");
                    String texto = rs.getString("texto_post");
                    String data = rs.getString("data_post");
                    String nomeUsuario = rs.getString("nome_usuario");
                    String login= rs.getString("login_usuario");
                    byte[] postImg = rs.getBytes("img_post");
                    byte[] iconimg = rs.getBytes("icon_usuario");
                    ItemLista.add(new ItemLista(titulo, data, texto, nomeUsuario, "@" + login, iconimg, postImg));
                    ItemLista.get(ItemLista.size() - 1).setId(codPost);
                }
                adapter.notifyDataSetChanged(); // Notificar o adaptador sobre as mudanças na lista
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        session.unregisterOnSharedPreferenceChangeListener(listener);
    }

    private void updateFabVisibility() {
        if (tipoUsuario == 2 || tipoUsuario == 4 || tipoUsuario == 5) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
    }
}
