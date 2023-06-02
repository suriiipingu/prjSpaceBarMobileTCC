package com.example.spacebar;

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

        // Obter o código do usuário atual do SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

        //Checar se o usuário ja curtiu o psot
        int itemId = item.getId();
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(context);
        try {
            String query = "SELECT COUNT(*) FROM tblPostagemCurtidas WHERE tblPostagemCurtidas_cod_post = ? AND tblPostagemCurtidas_cod_usuario = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, itemId);
            stmt.setInt(2, codigoUsuario);
            ResultSet resultSet = stmt.executeQuery();
            // Se houver algum resultado, significa que o usuário já curtiu a postagem
            if (resultSet.next()) {
                int curtidaCount = resultSet.getInt(1);
                if (curtidaCount > 0) {
                    holder.curtida.setImageResource(R.drawable.heart_fill);
                } else {
                    holder.curtida.setImageResource(R.drawable.heart);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        holder.comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtém o item do post clicado
                ItemLista post = itemList.get(holder.getAdapterPosition());

                // Cria um Intent para abrir a atividade PostActivity
                Intent intent = new Intent(context, PostComentario.class);

                // Passa os detalhes do post para a atividade PostActivity
                intent.putExtra("postId", item.getId());
                intent.putExtra("titulo", post.getTitulo());
                intent.putExtra("data", post.getData());
                intent.putExtra("texto", post.getTexto());
                intent.putExtra("nomeUsuario", post.getNome());
                intent.putExtra("login", post.getLogin());
                intent.putExtra("iconImagem", post.getIconImagem());
                intent.putExtra("postImagem", post.getPostImagem());

                // Inicia a atividade PostActivity
                context.startActivity(intent);
            }
        });


                // Defina o ouvinte de clique para a imagem
        holder.curtida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_animation);
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    ItemLista item = itemList.get(adapterPosition);


                    // Verificar se o usuário está logado antes de verificar a curtida

                        // Obter o código do usuário atual do SharedPreferences
                        int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);
                        int itemId = item.getId();
                        Acessa objA = new Acessa();
                        Connection con = objA.entBanco(context);

                        try {
                            String query = "SELECT COUNT(*) FROM tblPostagemCurtidas WHERE tblPostagemCurtidas_cod_post = ? AND tblPostagemCurtidas_cod_usuario = ?";
                            PreparedStatement stmt = con.prepareStatement(query);
                            stmt.setInt(1, itemId);
                            stmt.setInt(2, codigoUsuario);
                            ResultSet resultSet = stmt.executeQuery();

                            // Se houver algum resultado, significa que o usuário já curtiu a postagem
                            if (resultSet.next()) {
                                int curtidaCount = resultSet.getInt(1);
                                if (curtidaCount > 0) {
                                    int dislikeResult = darDislike(item.getId());
                                    if (dislikeResult > 0) {
                                        holder.curtida.setImageResource(R.drawable.heart);
                                        holder.curtida.startAnimation(animation);
                                    }
                                } else {
                                    darLike(item.getId());
                                        holder.curtida.setImageResource(R.drawable.heart_fill);
                                    holder.curtida.startAnimation(animation);

                                }
                            }

                            resultSet.close();
                            stmt.close();
                            con.close();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }


            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    private int darLike(int itemId) {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(context);

        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
            int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

            // Atualize o valor no banco de dados usando uma consulta SQL
            String updateQuery = "INSERT INTO tblPostagemCurtidas (tblPostagemCurtidas_cod_post, tblPostagemCurtidas_cod_usuario) VALUES (?, ?)";
            PreparedStatement updateStmt = con.prepareStatement(updateQuery);
            updateStmt.setInt(1, itemId);
            updateStmt.setInt(2, codigoUsuario);
            int rowsAffected = updateStmt.executeUpdate();

            // Feche a conexão e os recursos
            updateStmt.close();
            con.close();

            return rowsAffected;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private int darDislike(int itemId) {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(context);

        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
            int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

            // Atualize o valor no banco de dados usando uma consulta SQL
            String updateQuery = " DELETE FROM tblPostagemCurtidas WHERE tblPostagemCurtidas_cod_post = ? AND tblPostagemCurtidas_cod_usuario = ?";
            PreparedStatement updateStmt = con.prepareStatement(updateQuery);
            updateStmt.setInt(1, itemId);
            updateStmt.setInt(2, codigoUsuario);
            int rowsAffected = updateStmt.executeUpdate();

            // Feche a conexão e os recursos
            updateStmt.close();
            con.close();

            return rowsAffected;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
