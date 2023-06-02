package com.example.spacebar;

import static com.example.spacebar.Verificacoes.verificarSeTemImagem;
import static com.example.spacebar.Verificacoes.verificarSeTemTexto;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostComentario extends AppCompatActivity {

    private TextView tituloTextView, dataTextView, textoTextView, nomeUsuarioTextView, loginTextView;
    private ImageView iconImagemImageView, imgPost;
    private LinearLayout linearLayoutItens;

    private ListView listViewCom;

    private List<ItemListaComentario> ItemLista;

    private Acessa objA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.post_comentario);

        //tirar a barra de titulo da pagina
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        objA = new Acessa();

        linearLayoutItens = findViewById(R.id.linear_layout_itens);

        // Recupere as informações do intent
        Intent intent = getIntent();
        int postId = intent.getIntExtra("postId", 0);
        String titulo = intent.getStringExtra("titulo");
        String data = intent.getStringExtra("data");
        String texto = intent.getStringExtra("texto");
        String nomeUsuario = intent.getStringExtra("nomeUsuario");
        String login = intent.getStringExtra("login");
        byte[] iconImagem = intent.getByteArrayExtra("iconImagem");
        byte[] postImagem = intent.getByteArrayExtra("postImagem");

        boolean hasTexto = verificarSeTemTexto(this, postId);
        boolean hasImage = verificarSeTemImagem(this, postId);

        if (hasTexto && hasImage) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View itemView = inflater.inflate(R.layout.item_img_texto, linearLayoutItens, false);
            linearLayoutItens.addView(itemView);

            // Inicialize as Views
            tituloTextView = findViewById(R.id.lblTitulo);
            dataTextView = findViewById(R.id.lblData);
            textoTextView = findViewById(R.id.lblTexto);
            nomeUsuarioTextView = findViewById(R.id.lblNomeUsuario);
            loginTextView = findViewById(R.id.lblLogin);
            iconImagemImageView = findViewById(R.id.imgUsuario);
            imgPost = findViewById(R.id.imgPost);

            // Atualize a interface com as informações do post selecionado
            tituloTextView.setText(titulo);
            dataTextView.setText(data);
            textoTextView.setText(texto);
            nomeUsuarioTextView.setText(nomeUsuario);
            loginTextView.setText(login);
            Glide.with(this)
                    .load(iconImagem)
                    .circleCrop()
                    .into(iconImagemImageView);
            Glide.with(this)
                    .load(postImagem)
                    .into(imgPost);

        } else if (hasTexto) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View itemView = inflater.inflate(R.layout.item_semimg_texto, linearLayoutItens, false);
            linearLayoutItens.addView(itemView);

            // Inicialize as Views
            tituloTextView = findViewById(R.id.lblTitulo);
            dataTextView = findViewById(R.id.lblData);
            textoTextView = findViewById(R.id.lblTexto);
            nomeUsuarioTextView = findViewById(R.id.lblNomeUsuario);
            loginTextView = findViewById(R.id.lblLogin);
            iconImagemImageView = findViewById(R.id.imgUsuario);

            // Atualize a interface com as informações do post selecionado
            tituloTextView.setText(titulo);
            dataTextView.setText(data);
            textoTextView.setText(texto);
            nomeUsuarioTextView.setText(nomeUsuario);
            loginTextView.setText(login);
            Glide.with(this)
                    .load(iconImagem)
                    .circleCrop()
                    .into(iconImagemImageView);

        } else if (hasImage) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View itemView = inflater.inflate(R.layout.item_img_semtexto, linearLayoutItens, false);
            linearLayoutItens.addView(itemView);

            // Inicialize as Views
            tituloTextView = findViewById(R.id.lblTitulo);
            dataTextView = findViewById(R.id.lblData);
            nomeUsuarioTextView = findViewById(R.id.lblNomeUsuario);
            loginTextView = findViewById(R.id.lblLogin);
            iconImagemImageView = findViewById(R.id.imgUsuario);
            imgPost = findViewById(R.id.imgPost);

            // Atualize a interface com as informações do post selecionado
            tituloTextView.setText(titulo);
            dataTextView.setText(data);
            nomeUsuarioTextView.setText(nomeUsuario);
            loginTextView.setText(login);
            Glide.with(this)
                    .load(iconImagem)
                    .circleCrop()
                    .into(iconImagemImageView);
            Glide.with(this)
                    .load(postImagem)
                    .into(imgPost);

        }else {
            LayoutInflater inflater = LayoutInflater.from(this);
            View itemView = inflater.inflate(R.layout.item_semimg_semtexto, linearLayoutItens, false);
            linearLayoutItens.addView(itemView);

            // Inicialize as Views
            tituloTextView = findViewById(R.id.lblTitulo);
            dataTextView = findViewById(R.id.lblData);
            nomeUsuarioTextView = findViewById(R.id.lblNomeUsuario);
            loginTextView = findViewById(R.id.lblLogin);
            iconImagemImageView = findViewById(R.id.imgUsuario);

            // Atualize a interface com as informações do post selecionado
            tituloTextView.setText(titulo);
            dataTextView.setText(data);
            nomeUsuarioTextView.setText(nomeUsuario);
            loginTextView.setText(login);
            Glide.with(this)
                    .load(iconImagem)
                    .circleCrop()
                    .into(iconImagemImageView);
        }

        listViewCom = findViewById(R.id.listViewCom);
        ItemLista = new ArrayList<>();
        ListaAdapterComentario adapter = new ListaAdapterComentario(this, ItemLista);
        listViewCom.setAdapter(adapter);
        Connection con = objA.entBanco(this);
        if(con != null){
            try{
                String query = "SELECT * from tblPost INNER JOIN tblUsuario tU on tU.cod_usuario = tblPost.cod_usuario";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int codPost = rs.getInt("cod_post");
                    String nomeUsu = rs.getString("titulo_post");
                    String loginUsu = rs.getString("texto_post");
                    String dataCom = rs.getString("data_post");
                    String textoCom = rs.getString("nome_usuario");
                    byte[] iconimgCom = rs.getBytes("icon_usuario");
                    ItemLista.add(new ItemListaComentario(nomeUsu, "@" + loginUsu, dataCom, textoCom, iconimgCom));
                    //ItemLista.get(ItemLista.size() - 1).setId(codPost);
                }
                adapter.notifyDataSetChanged(); // Notificar o adaptador sobre as mudanças na lista
                rs.close();
                stmt.close();
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        listViewCom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });



    }

}