import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spacebar.Acessa;
import com.example.spacebar.ItemLista;
import com.example.spacebar.ListaAdapter;
import com.example.spacebar.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class perfilusuarioqualquer extends AppCompatActivity {

    ImageView imgusu, imgback, imgSelo1;
    TextView lblusu,lbllogin,lblbio,lblseguidores,lblseguindo;
    private RecyclerView recyclerView;
    private List<com.example.spacebar.ItemLista> ItemLista;
    SharedPreferences sharedPreferences;
    int codigoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilusuario_qualquer);

        imgusu = findViewById(R.id.imgusuario1);
        imgback = findViewById(R.id.imgbackground1);
        lblusu = findViewById(R.id.lblusuario1);
        lbllogin = findViewById(R.id.lbllogin1);
        lblbio = findViewById(R.id.lblbio1);
        lblseguidores = findViewById(R.id.txtseguidores1);
        lblseguindo = findViewById(R.id.txtseguindo1);
        recyclerView = findViewById(R.id.recyclerView);
        imgSelo1 = findViewById(R.id.igSelo11);
        ItemLista = new ArrayList<>();
        ListaAdapter adapter = new ListaAdapter(this, ItemLista);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM tblPost INNER JOIN tblUsuario ON tblPost.cod_usuario = tblUsuario.cod_usuario WHERE tblPost.cod_usuario = ?");
            stmt.setInt(1, codigoUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome_usuario");
                String login = rs.getString("login_usuario");
                byte[] iconUsuario = rs.getBytes("icon_usuario");
                String bio = rs.getString("bio_usuario");
                byte[] background = rs.getBytes("imgfundo_usuario");
                int seguidores = seGuidores();
                int seguindo = seGuindo();

                lblusu.setText(nome);
                lbllogin.setText(login);
                lblbio.setText(bio);
                lblseguidores.setText(String.valueOf(seguidores));
                lblseguindo.setText(String.valueOf(seguindo));

                if (iconUsuario != null) {
                    Glide.with(this)
                            .load(iconUsuario)
                            .circleCrop()
                            .into(imgusu);
                } else {
                    Glide.with(this)
                            .load(R.drawable.bloco_criarpost)
                            .circleCrop()
                            .into(imgusu);
                }
                if(background != null) {
                    Glide.with(this)
                            .load(background)
                            .into(imgback);
                }

                int tipo_usuario = rs.getInt("cod_tipo");
                if (tipo_usuario == 2 || tipo_usuario == 4) {
                    Glide.with(this)
                            .load(R.drawable.backspace)
                            .into(imgSelo1);
                }

                do {
                    int codPost = rs.getInt("cod_post");
                    String titulo = rs.getString("titulo_post");
                    String texto = rs.getString("texto_post");
                    String data = rs.getString("data_post");
                    byte[] postImg = rs.getBytes("img_post");
                    ItemLista.add(new ItemLista(titulo, data, texto, nome, "@" + login, iconUsuario, postImg));
                    ItemLista.get(ItemLista.size() - 1).setId(codPost);
                } while (rs.next());

                adapter.notifyDataSetChanged(); // Notificar o adaptador sobre as mudanças na lista
            }

            // Fechar a conexão e os recursos
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int seGuidores() {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return followersCount;
    }

    public int seGuindo() {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return followingCount;
    }
}
