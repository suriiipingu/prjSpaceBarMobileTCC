package com.example.spacebar.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacebar.Acessa;
import com.example.spacebar.ItemLista;
import com.example.spacebar.R;
import com.example.spacebar.User;
import com.example.spacebar.UserAdapter;
import com.example.spacebar.databinding.FragmentDashboardBinding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerViewUsuarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<User> userList = getUserListFromDatabase(); // Obtenha os 4 últimos usuários do banco de dados

        userAdapter = new UserAdapter(getContext(), userList);
        recyclerView.setAdapter(userAdapter);

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Método de exemplo para obter a lista de usuários do banco de dados
    private List<User> getUserListFromDatabase() {
        List<User> userList = new ArrayList<>();

        Acessa objA = new Acessa();
        Connection con = objA.entBanco(getContext());
        if(con != null){
            try{
                String query = "SELECT TOP 4 * FROM tblUsuario ORDER BY data_criacao DESC";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int userID = rs.getInt("cod_usuario");
                    String nomeUsuario = rs.getString("nome_usuario");
                    String login = rs.getString("login_usuario");
                    byte[] iconimg = rs.getBytes("icon_usuario");

                    User user = new User(userID,nomeUsuario, "@" + login, iconimg);
                    userList.add(user);
                }
                rs.close();
                stmt.close();
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return userList;
    }
}