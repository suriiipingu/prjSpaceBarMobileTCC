package com.example.spacebar;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        public ImageView imageView, iconImagem, curtida;
        public TextView titulo, data, nomeUsuario, login;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgPost);
            titulo = itemView.findViewById(R.id.lblTitulo);
            data = itemView.findViewById(R.id.lblData);
            nomeUsuario = itemView.findViewById(R.id.lblNomeUsuario);
            login = itemView.findViewById(R.id.lblLogin);
            iconImagem = itemView.findViewById(R.id.imgUsuario);
            curtida = itemView.findViewById(R.id.imgBtnLike);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemLista item = itemList.get(position);
        //holder.imageView.setImageResource(item.getImagemResId());
        holder.titulo.setText(item.getTitulo());
        holder.data.setText(item.getData());
        holder.nomeUsuario.setText(item.getNome());
        holder.login.setText(item.getLogin());
        Glide.with(context) // Use o contexto fornecido no construtor
                .load(item.getIconImagem())
                .circleCrop()
                .into(holder.iconImagem); // Acesse o iconImagem através do objeto holder

        // Obter o código do usuário atual do SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

        boolean jaCurtiu = verificarCurtida(item.getId(), codigoUsuario);

        item.setCurtido(jaCurtiu);

        // Definir a imagem da curtida com base no estado de curtida atualizado
        if (item.isCurtido()) {
            holder.curtida.setImageResource(R.drawable.heart_fill);
        } else {
            holder.curtida.setImageResource(R.drawable.heart);
        }
        // Defina o ouvinte de clique para a imagem
        holder.curtida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Verifica se o usuário já curtiu a postagem
                if (!jaCurtiu) {
                    // Inverta o estado de curtida da imagem
                    item.setCurtido(!item.isCurtido());

                    // Atualize a imagem com base no estado de curtida atualizado
                    if (item.isCurtido()) {
                        holder.curtida.setImageResource(R.drawable.heart_fill);
                        aumentarCurtidaNoBancoDeDados(item.getId()); // Chame o método para aumentar a curtida no banco de dados
                    } else {
                        holder.curtida.setImageResource(R.drawable.heart);
                        diminuirCurtidaNoBancoDeDados(item.getId()); // Chame o método para diminuir a curtida no banco de dados
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    //verifciar se usuario ja curtiu uma postagem
    private boolean verificarCurtida(int itemId, int codigoUsuario) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IsLoggedIn", false);

        // Verificar se o usuário está logado antes de verificar a curtida
        if (isLoggedIn) {
            Acessa objA = new Acessa();
            Connection con = objA.entBanco(context);

            boolean jaCurtiu = false;

            try {
                String query = "SELECT COUNT(*) FROM tblPostagemCurtidas WHERE tblPostagemCurtidas_cod_post = ? AND tblPostagemCurtidas_cod_usuario = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, itemId);
                stmt.setInt(2, codigoUsuario);
                ResultSet resultSet = stmt.executeQuery();

                // Se houver algum resultado, significa que o usuário já curtiu a postagem
                if (resultSet.next()) {
                    jaCurtiu = true;
                }

                resultSet.close();
                stmt.close();
                con.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return jaCurtiu;
        } else {
            return false; // Caso o usuário não esteja logado, não é necessário verificar a curtida
        }
    }

    private void aumentarCurtidaNoBancoDeDados(int itemId) {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(context);

        int numeroCurtidasAtual = 0;

        try {
            String query = "SELECT curtidas_post FROM tblPost WHERE cod_post = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, itemId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                numeroCurtidasAtual = resultSet.getInt("curtidas_post");
            }

            // Incremente o número de curtidas em 1
            int novoNumeroCurtidas = numeroCurtidasAtual + 1;

            // Atualize o valor no banco de dados usando uma consulta SQL
            String updateQuery = "UPDATE tblPost SET curtidas_post = ? WHERE cod_post = ?";
            PreparedStatement updateStmt = con.prepareStatement(updateQuery);
            updateStmt.setInt(1, novoNumeroCurtidas);
            updateStmt.setInt(2, itemId);
            updateStmt.executeUpdate();

            // Feche a conexão e os recursos
            updateStmt.close();
            resultSet.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void diminuirCurtidaNoBancoDeDados(int itemId) {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(context);

        int numeroCurtidasAtual = 0;

        try {
            String query = "SELECT curtidas_post FROM tblPost WHERE cod_post = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, itemId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                numeroCurtidasAtual = resultSet.getInt("curtidas_post");
            }

            // Verifique se o número atual de curtidas é maior que zero
            if (numeroCurtidasAtual > 0) {
                // Decremente o número de curtidas em 1
                int novoNumeroCurtidas = numeroCurtidasAtual - 1;

                // Atualize o valor no banco de dados usando uma consulta SQL
                String updateQuery = "UPDATE tblPost SET curtidas_post = ? WHERE cod_post = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setInt(1, novoNumeroCurtidas);
                updateStmt.setInt(2, itemId);
                updateStmt.executeUpdate();

                // Feche a conexão e os recursos
                updateStmt.close();
            }

            resultSet.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
