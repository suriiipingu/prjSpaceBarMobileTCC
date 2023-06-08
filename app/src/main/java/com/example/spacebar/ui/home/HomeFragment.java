package com.example.spacebar.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacebar.Acessa;
import com.example.spacebar.ItemLista;
import com.example.spacebar.ListaAdapter;
import com.example.spacebar.R;
import com.example.spacebar.databinding.FragmentHomeBinding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ItemLista> itemLista;
    private Acessa objA;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        objA = new Acessa();

        recyclerView = root.findViewById(R.id.recyclerView);
        itemLista = new ArrayList<>();
        ListaAdapter adapter = new ListaAdapter(getActivity(), itemLista);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Connection con = objA.entBanco(getActivity());
        if (con != null) {
            try {
                String query = "SELECT * from tblPost INNER JOIN tblUsuario tU on tU.cod_usuario = tblPost.cod_usuario";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int codPost = rs.getInt("cod_post");
                    String titulo = rs.getString("titulo_post");
                    String texto = rs.getString("texto_post");
                    String data = rs.getString("data_post");
                    String nomeUsuario = rs.getString("nome_usuario");
                    String login = rs.getString("login_usuario");
                    byte[] postImg = rs.getBytes("img_post");
                    byte[] iconimg = rs.getBytes("icon_usuario");
                    itemLista.add(new ItemLista(titulo, data, texto, nomeUsuario, "@" + login, iconimg, postImg));
                    itemLista.get(itemLista.size() - 1).setId(codPost);
                }
                adapter.notifyDataSetChanged(); // Notificar o adaptador sobre as mudanças na lista
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return root;
    }
}