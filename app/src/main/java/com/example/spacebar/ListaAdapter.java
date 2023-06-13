package com.example.spacebar;

import static com.example.spacebar.CurtidaManager.darDislike;
import static com.example.spacebar.Verificacoes.verificarCurtidaPost;
import static com.example.spacebar.Verificacoes.verificarSeTemImagem;
import static com.example.spacebar.Verificacoes.verificarSeTemTexto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {

    private List<ItemLista> itemList;
    private Context context;

    public ListaAdapter(Context context, List<ItemLista> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iconImagem, postImagem;
        public ImageButton curtida, comentario;
        public TextView titulo, data,texto, nomeUsuario, login;

        public boolean hasCurtida;


        public ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.lblTitulo);
            data = itemView.findViewById(R.id.lblData);
            texto = itemView.findViewById(R.id.lblTexto);
            nomeUsuario = itemView.findViewById(R.id.lblNomeUsuario);
            login = itemView.findViewById(R.id.lblLogin);
            iconImagem = itemView.findViewById(R.id.imgUsuario);
            postImagem = itemView.findViewById(R.id.imgPost);
            curtida = itemView.findViewById(R.id.imgBtnLike);
            comentario = itemView.findViewById(R.id.imgBtnComent);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    public int getItemViewType(int position) {
        ItemLista item = itemList.get(position);
        int postId = item.getId();
        boolean hasTexto = verificarSeTemTexto(context, postId);
        boolean hasImage = verificarSeTemImagem(context, postId);
        if (hasTexto && hasImage) {
            return R.layout.item_img_texto;
        } else if (hasTexto) {
            return R.layout.item_semimg_texto;
        } else if (hasImage) {
            return R.layout.item_img_semtexto;
        } else {
            return R.layout.item_semimg_semtexto;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemLista item = itemList.get(position);
        holder.titulo.setText(item.getTitulo());
        holder.data.setText(item.getData());
        holder.nomeUsuario.setText(item.getNome());
        holder.login.setText(item.getLogin());
        Glide.with(context) // Use o contexto fornecido no construtor
                .load(item.getIconImagem())
                .circleCrop()
                .into(holder.iconImagem); // Acesse o iconImagem através do objeto holder

        // Verificar se o campo texto contém um texto haha
        boolean hasTexto= verificarSeTemTexto(context, item.getId());
        if (hasTexto) {
            holder.texto.setText(item.getTexto());
        }

        // Verificar se o campo postImagem contém uma imagem
        boolean hasImage = verificarSeTemImagem(context, item.getId());
        if (hasImage) {
            Glide.with(context)
                    .load(item.getPostImagem())
                    .into(holder.postImagem);
        }

        // Atualizar valor de hasCurtida
        holder.hasCurtida = verificarCurtidaPost(context, item.getId());

        // Atualizar visualização da curtida
        if (holder.hasCurtida) {
            holder.curtida.setImageResource(R.drawable.heart_fill);
        } else {
            holder.curtida.setImageResource(R.drawable.heart);
        }


        holder.comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ItemLista item = itemList.get(position);

                    // Crie um Intent para iniciar a atividade PostComentario.class
                    Intent intent = new Intent(context, PostComentario.class);

                    // Passar as informações do post como extras para o Intent
                    intent.putExtra("postId", item.getId());
                    intent.putExtra("titulo", item.getTitulo());
                    intent.putExtra("data", item.getData());
                    intent.putExtra("texto", item.getTexto());
                    intent.putExtra("nomeUsuario", item.getNome());
                    intent.putExtra("login", item.getLogin());

                    // Verifique se a imagem do ícone existe antes de salvá-la em um diretório
                    if (item.getIconImagem() != null) {
                        String iconImagePath = saveImageToStorage(item.getIconImagem(), "icon_image.jpg");
                        intent.putExtra("iconImagePath", iconImagePath);
                    }

                    // Verifique se a imagem do post existe antes de salvá-la em um diretório
                    if (item.getPostImagem() != null) {
                        String postImagePath = saveImageToStorage(item.getPostImagem(), "post_image.jpg");
                        intent.putExtra("postImagePath", postImagePath);
                    }

                    // Inicie a atividade PostComentario.class
                    context.startActivity(intent);
                }
            }
        });


        holder.curtida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_animation);
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    ItemLista item = itemList.get(adapterPosition);
                    boolean hasCurtida = !holder.hasCurtida;

                    if (hasCurtida) {
                        holder.curtida.startAnimation(animation);
                        holder.curtida.setImageResource(R.drawable.heart_fill);
                        CurtidaManager.darLike(context, item.getId());
                    } else {
                        holder.curtida.startAnimation(animation);
                        holder.curtida.setImageResource(R.drawable.heart);
                        CurtidaManager.darDislike(context, item.getId());

                    }

                    holder.hasCurtida = hasCurtida;
                    }
            }
        });


    }
    private String saveImageToStorage(byte[] imageData, String imageName) {
        try {
            File directory = new File(context.getFilesDir(), "images");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File imageFile = new File(directory, imageName);
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(imageData);
            fos.close();
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
