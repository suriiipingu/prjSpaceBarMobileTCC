package com.example.spacebar;

import static com.example.spacebar.Verificacoes.verificarCurtidaPost;
import static com.example.spacebar.Verificacoes.verificarSeUsuarioJaESeguido;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;
    public UserAdapter(Context context, List<User> userList) {
        this.userList = userList;
        this.context = context;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UserViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(user.getName());
        holder.userLogin.setText(user.getLogin());
        Glide.with(context) // Use o contexto fornecido no construtor
                .load(user.getIconResId())
                .circleCrop()
                .into(holder.userIcon); // Acesse o iconImagem através do objeto holder


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, perfilusuarioqualquer.class);
                intent.putExtra("userId", user.getUserID());
                context.startActivity(intent);
            }
        });



    }


    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        public boolean segueUser;

        LinearLayout linearLayout;
        ImageView userIcon;
        TextView userName;
        TextView userLogin;
        EditText pesquisaUsuario;
        Button btnSeguir;

        public boolean hasSeguido;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            pesquisaUsuario = itemView.findViewById(R.id.txtPesquisa);
            userIcon = itemView.findViewById(R.id.imgUsuarioP);
            userName = itemView.findViewById(R.id.lblNomeUsu);
            userLogin = itemView.findViewById(R.id.lblLoginUsu);
            linearLayout = itemView.findViewById(R.id.linearUsuPerfil);
        }
    }
}
