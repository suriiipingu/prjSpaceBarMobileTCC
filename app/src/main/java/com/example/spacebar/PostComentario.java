package com.example.spacebar;

import static com.example.spacebar.Verificacoes.verificarCurtidaPost;
import static com.example.spacebar.Verificacoes.verificarSeTemImagem;
import static com.example.spacebar.Verificacoes.verificarSeTemTexto;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PostComentario extends AppCompatActivity {

    private TextView tituloTextView, dataTextView, textoTextView, nomeUsuarioTextView, loginTextView;
    private ImageView iconImagemImageView, imgPost, imgUsu;

    private ImageButton curtida;
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

        // Obtém a data e hora atual
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        objA = new Acessa();
        listViewCom = findViewById(R.id.listViewCom);
        ItemLista = new ArrayList<>();
        ListaAdapterComentario adapter = new ListaAdapterComentario(this, ItemLista);
        listViewCom.setAdapter(adapter);

        linearLayoutItens = findViewById(R.id.linear_layout_itens);

        // Recupere as informações do intent
        Intent intent = getIntent();
        int postId = intent.getIntExtra("postId", 0);
        String titulo = intent.getStringExtra("titulo");
        String data = intent.getStringExtra("data");
        String texto = intent.getStringExtra("texto");
        String nomeUsuario = intent.getStringExtra("nomeUsuario");
        String login = intent.getStringExtra("login");
        String iconImagePath = getIntent().getStringExtra("iconImagePath");
        String postImagePath = getIntent().getStringExtra("postImagePath");


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
            curtida = findViewById(R.id.imgBtnLike);
            imgPost = findViewById(R.id.imgPost);

            // Atualize a interface com as informações do post selecionado
            tituloTextView.setText(titulo);
            dataTextView.setText(data);
            textoTextView.setText(texto);
            nomeUsuarioTextView.setText(nomeUsuario);
            loginTextView.setText(login);
            if (iconImagePath != null) {
                File iconImageFile = new File(iconImagePath);
                Glide.with(this)
                        .load(iconImageFile)
                        .circleCrop()
                        .into(iconImagemImageView);
            }
            if (postImagePath != null) {
                File postImageFile = new File(postImagePath);
                Glide.with(this).load(postImageFile).into(imgPost);
            }

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
            curtida = findViewById(R.id.imgBtnLike);
            iconImagemImageView = findViewById(R.id.imgUsuario);

            // Atualize a interface com as informações do post selecionado
            tituloTextView.setText(titulo);
            dataTextView.setText(data);
            textoTextView.setText(texto);
            nomeUsuarioTextView.setText(nomeUsuario);
            loginTextView.setText(login);
            if (iconImagePath != null) {
                File iconImageFile = new File(iconImagePath);
                Glide.with(this)
                        .load(iconImageFile)
                        .circleCrop()
                        .into(iconImagemImageView);
            }

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
            curtida = findViewById(R.id.imgBtnLike);
            imgPost = findViewById(R.id.imgPost);

            // Atualize a interface com as informações do post selecionado
            tituloTextView.setText(titulo);
            dataTextView.setText(data);
            nomeUsuarioTextView.setText(nomeUsuario);
            loginTextView.setText(login);
            if (iconImagePath != null) {
                File iconImageFile = new File(iconImagePath);
                Glide.with(this)
                        .load(iconImageFile)
                        .circleCrop()
                        .into(iconImagemImageView);
            }
            if (postImagePath != null) {
                File postImageFile = new File(postImagePath);
                Glide.with(this).load(postImageFile).into(imgPost);
            }

        }else {
            LayoutInflater inflater = LayoutInflater.from(this);
            View itemView = inflater.inflate(R.layout.item_semimg_semtexto, linearLayoutItens, false);
            linearLayoutItens.addView(itemView);

            // Inicialize as Views
            tituloTextView = findViewById(R.id.lblTitulo);
            dataTextView = findViewById(R.id.lblData);
            nomeUsuarioTextView = findViewById(R.id.lblNomeUsuario);
            loginTextView = findViewById(R.id.lblLogin);
            curtida = findViewById(R.id.imgBtnLike);
            iconImagemImageView = findViewById(R.id.imgUsuario);

            // Atualize a interface com as informações do post selecionado
            tituloTextView.setText(titulo);
            dataTextView.setText(data);
            nomeUsuarioTextView.setText(nomeUsuario);
            loginTextView.setText(login);
            if (iconImagePath != null) {
                File iconImageFile = new File(iconImagePath);
                Glide.with(this).load(iconImageFile).into(iconImagemImageView);
            }
        }

        final AtomicBoolean hasCurtida = new AtomicBoolean(verificarCurtidaPost(this, postId));
        if (hasCurtida.get()) {
            curtida.setImageResource(R.drawable.heart_fill);
        } else {
            curtida.setImageResource(R.drawable.heart);
        }

        curtida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(PostComentario.this, R.anim.scale_animation);
                if (hasCurtida.get()) {
                    CurtidaManager.darDislike(PostComentario.this, postId);
                    curtida.setImageResource(R.drawable.heart);
                    curtida.startAnimation(animation);
                    hasCurtida.set(false);
                } else {
                    CurtidaManager.darLike(PostComentario.this, postId);
                    curtida.setImageResource(R.drawable.heart_fill);
                    curtida.startAnimation(animation);
                    hasCurtida.set(true);
                }


            }
        });


        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);
        imgUsu = findViewById(R.id.imgUsuarioP);
        try(
                PreparedStatement statement = con.prepareStatement("Select * from tblUsuario where cod_usuario= ?")) {
            statement.setInt(1, codigoUsuario);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                byte[] iconUsuario = rs.getBytes("icon_usuario");
                if (iconUsuario != null) {
                    Glide.with(this)
                            .load(iconUsuario)
                            .circleCrop()
                            .into(imgUsu);
                }else{
                    Glide.with(this)
                            .load(R.drawable.round_background)
                            .circleCrop()
                            .into(imgUsu);
                }
            }
        }catch(SQLException a){
                a.printStackTrace();
            }

        ImageButton btnVoltar = findViewById(R.id.imgBtnVoltar2);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();;
            }
        });


        ImageButton btnEnviarCom = findViewById(R.id.btnEnviarCom);
        btnEnviarCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtComentario = findViewById(R.id.txtComentario);
                String texto = txtComentario.getText().toString();
                if(!texto.isEmpty()){
                    try(PreparedStatement statement = con.prepareStatement("Insert into tblComentarios (cod_post, cod_usuario, conteudo_comentario,data_comentario) values (?, ?, ?, ?)")){
                        statement.setInt(1, postId);
                        statement.setInt(2, codigoUsuario);
                        statement.setString(3, texto);
                        statement.setTimestamp(4, timestamp);
                        statement.executeUpdate();

                        // Atualizar a lista de comentários
                        ItemLista.clear(); // Limpar a lista existente
                        carregarComentarios(); // Carregar os comentários atualizados
                        adapter.notifyDataSetChanged(); // Notificar o adaptador sobre as mudanças na lista

                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(PostComentario.this.getApplicationContext(), "Precisa de um texto para enviar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        carregarComentarios();


    }

    private void carregarComentarios(){
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);

        Intent intent = getIntent();
        int postId = intent.getIntExtra("postId", 0);

        ItemLista = new ArrayList<>();
        ListaAdapterComentario adapter = new ListaAdapterComentario(this, ItemLista);
        listViewCom.setAdapter(adapter);
        if(con != null){
            try{
                String query = "SELECT * FROM tblComentarios INNER JOIN tblUsuario ON tblComentarios.cod_usuario = tblUsuario.cod_usuario where tblComentarios.cod_post = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, postId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String nomeUsu = rs.getString("nome_usuario");
                    String loginUsu = rs.getString("login_usuario");
                    String dataCom = rs.getString("data_comentario");
                    String textoCom = rs.getString("conteudo_comentario");
                    byte[] iconimgCom = rs.getBytes("icon_usuario");
                    ItemLista.add(new ItemListaComentario(nomeUsu, "@" + loginUsu, dataCom, textoCom, iconimgCom));
                    ItemLista.get(ItemLista.size() - 1).setId(postId);
                }
                adapter.notifyDataSetChanged(); // Notificar o adaptador sobre as mudanças na lista
                rs.close();
                stmt.close();
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}