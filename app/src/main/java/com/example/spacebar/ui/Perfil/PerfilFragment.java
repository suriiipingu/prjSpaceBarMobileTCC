package com.example.spacebar.ui.Perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spacebar.Acessa;
import com.example.spacebar.ItemLista;
import com.example.spacebar.ListaAdapter;
import com.example.spacebar.R;
import com.example.spacebar.databinding.FragmentPerfilBinding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    ImageView imgusu, imgback, imgSelo1, imgSelo2;
    TextView lblusu, lbllogin, lblbio, lblseguidores, lblseguindo;
    private RecyclerView recyclerView;
    private List<ItemLista> itemLista;
    SharedPreferences sharedPreferences;
    int codigoUsuario;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imgusu = root.findViewById(R.id.imgusuario1);
        imgback = root.findViewById(R.id.imgbackground1);
        lblusu = root.findViewById(R.id.lblusuario1);
        lbllogin = root.findViewById(R.id.lbllogin1);
        lblbio = root.findViewById(R.id.lblbio1);
        lblseguidores = root.findViewById(R.id.txtseguidores1);
        lblseguindo = root.findViewById(R.id.txtseguindo1);
        recyclerView = root.findViewById(R.id.recyclerView);
        imgSelo1 = root.findViewById(R.id.igSelo1);
        imgSelo2 = root.findViewById(R.id.igSelo2);
        itemLista = new ArrayList<>();
        ListaAdapter adapter = new ListaAdapter(requireContext(), itemLista);
        final NestedScrollView nestedScrollView= root.findViewById(R.id.scroltudo);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setNestedScrollingEnabled(false);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // Rolagem para baixo
                    if (scrollY >= (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        recyclerView.setNestedScrollingEnabled(true);
                    }
                } else {
                    // Rolagem para cima
                    if (scrollY <= 0) {
                        recyclerView.setNestedScrollingEnabled(false);
                    }
                }
            }
        });

        sharedPreferences = requireContext().getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        String loginUsuario = sharedPreferences.getString("loginUsuario", "@usuario");
        String nomeUsuario = sharedPreferences.getString("nomeUsuario", "usuario");
        String bioUsuario = sharedPreferences.getString("bioUsuario", "");
        int tipoUsuario = sharedPreferences.getInt("tipoUsuario", -1);
        codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

        Acessa objA = new Acessa();
        Connection con = objA.entBanco(requireContext());
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM tblUsuario WHERE cod_usuario = ?");
            stmt.setInt(1, codigoUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                byte[] iconUsuario = rs.getBytes("icon_usuario");
                byte[] background = rs.getBytes("imgfundo_usuario");
                int seguidores = seGuidores();
                int seguindo = seGuindo();

                lblusu.setText(nomeUsuario);
                lbllogin.setText(loginUsuario);
                lblbio.setText(bioUsuario);
                lblseguidores.setText(String.valueOf(seguidores));
                lblseguindo.setText(String.valueOf(seguindo));

                if (iconUsuario != null) {
                    Glide.with(requireContext())
                            .load(iconUsuario)
                            .circleCrop()
                            .into(imgusu);
                } else {
                    Glide.with(requireContext())
                            .load(R.drawable.bloco_criarpost)
                            .circleCrop()
                            .into(imgusu);
                }
                if(background != null) {
                    Glide.with(requireContext())
                            .load(background)
                            .into(imgback);
                }

                if(tipoUsuario == 2){
                    Glide.with(this)
                            .load(R.drawable.backspace)
                            .into(imgSelo1);
                }else if(tipoUsuario == 3) {
                    Glide.with(this)
                            .load(R.drawable.verificado_selo)
                            .into(imgSelo1);
                }else if(tipoUsuario == 4){
                    Glide.with(this)
                            .load(R.drawable.backspace)
                            .into(imgSelo1);
                    Glide.with(this)
                            .load(R.drawable.verificado_selo)
                            .into(imgSelo2);
                }else{
                    Glide.with(this)
                            .load(R.drawable.backspace)
                            .into(imgSelo1);
                    Glide.with(this)
                            .load(R.drawable.moderador)
                            .into(imgSelo2);
                }
            }

            // Fechar o primeiro conjunto de resultados
            rs.close();
            stmt.close();

            // Selecionar as postagens do usuário
            stmt = con.prepareStatement("SELECT * FROM tblPost Inner join tblUsuario ON tblPost.cod_usuario = tblUsuario.cod_usuario WHERE tblPost.cod_usuario = ? order by cod_post desc");
            stmt.setInt(1, codigoUsuario);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int codPost = rs.getInt("cod_post");
                String titulo = rs.getString("titulo_post");
                String texto = rs.getString("texto_post");
                String data = rs.getString("data_post");
                byte[] postImg = rs.getBytes("img_post");
                String nome = rs.getString("nome_usuario");
                String login = rs.getString("login_usuario");
                byte[] iconUsuario = rs.getBytes("icon_usuario");

                itemLista.add(new ItemLista(titulo, data, texto, nome, "@" + login, iconUsuario, postImg));
                itemLista.get(itemLista.size() - 1).setId(codPost);
            }

            adapter.notifyDataSetChanged(); // Notificar o adaptador sobre as mudanças na lista

            // Fechar a conexão e os recursos
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public int seGuidores() {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(requireContext());
        int followersCount = 0;

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) AS FollowersCount FROM tblSeguidores WHERE id_usuario_alvo = ?");
            stmt.setInt(1, codigoUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                followersCount = rs.getInt("FollowersCount");
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }

        return followersCount;
    }

    public int seGuindo() {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(requireContext());
        int followingCount = 0;

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) AS FollowingCount FROM tblSeguidores WHERE id_usuario_seguidor = ?");
            stmt.setInt(1, codigoUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                followingCount = rs.getInt("FollowingCount");
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }

        return followingCount;
    }
}
