package com.example.spacebar.ui.dashboard;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacebar.Acessa;
import com.example.spacebar.R;
import com.example.spacebar.User;
import com.example.spacebar.UserAdapter;
import com.example.spacebar.databinding.FragmentPesquisaUsuarioBinding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PesquisaUsuarioFragment extends Fragment {

    private RecyclerView recyclerViewUsuarios, recyclerViewPesUsu;
    private UserAdapter userAdapter, userAdapterPesquisa;
    private FragmentPesquisaUsuarioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PesquisaUsuarioViewModel pesquisaUsuarioViewModel =
                new ViewModelProvider(this).get(PesquisaUsuarioViewModel.class);

        // Ocultar a ActionBar
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        binding = FragmentPesquisaUsuarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //mostrar 4 usuarios no recyler view
        recyclerViewUsuarios = root.findViewById(R.id.recyclerViewUsuarios);
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));

        //mostrar usuario pesquisado no recycler view
        recyclerViewPesUsu = root.findViewById(R.id.recyclerViewPesUsu);
        recyclerViewPesUsu.setLayoutManager(new LinearLayoutManager(getContext()));


        List<User> searchResults = new ArrayList<>();
        List<User> userList = getUserListFromDatabase();

        userAdapterPesquisa = new UserAdapter(getContext(), searchResults);
        userAdapter = new UserAdapter(getContext(), userList);
        recyclerViewPesUsu.setAdapter(userAdapterPesquisa);
        recyclerViewUsuarios.setAdapter(userAdapter);

        EditText txtPesquisa = root.findViewById(R.id.txtPesquisa);

        txtPesquisa.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String texto = txtPesquisa.getText().toString();
                    if (!texto.isEmpty()) {
                        Acessa objA = new Acessa();
                        Connection con = objA.entBanco(getContext());
                        if (con != null) {
                            try {
                                String query = "SELECT * FROM tblUsuario WHERE login_usuario LIKE ?";
                                try (PreparedStatement statement = con.prepareStatement(query)) {
                                    statement.setString(1, "%" + texto + "%");
                                    try (ResultSet rs = statement.executeQuery()) {
                                        searchResults.clear();  // Limpar os resultados anteriores
                                        while (rs.next()) {
                                            int userID = rs.getInt("cod_usuario");
                                            String nomeUsuario = rs.getString("nome_usuario");
                                            String login = rs.getString("login_usuario");
                                            byte[] iconimg = rs.getBytes("icon_usuario");

                                            User user = new User(userID, nomeUsuario, "@" + login, iconimg);
                                            searchResults.add(user);
                                        }
                                        userAdapterPesquisa.notifyDataSetChanged();  // Notificar o adaptador sobre as mudanças na lista
                                    }
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else {
                        searchResults.clear();  // Limpar os resultados se o campo de pesquisa estiver vazio
                        userAdapterPesquisa.notifyDataSetChanged();  // Notificar o adaptador sobre as mudanças na lista
                    }
                    // Limpar o texto do EditText após pressionar Enter
                    txtPesquisa.getText().clear();
                    return true;
                }
                return false;
            }
        });

        // Obter a lista de usuários do banco de dados pela primeira vez
        userList = getUserListFromDatabase();
        userAdapter.setUserList(userList);

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