package com.example.spacebar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListaAdapterComentario extends BaseAdapter {
    private List<ItemListaComentario> itemList;
    private Context context;

    public ListaAdapterComentario(Context context, List<ItemListaComentario> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comentario, parent, false);

            holder = new ViewHolder();
            holder.nome = convertView.findViewById(R.id.lblNomeCom);
            holder.login = convertView.findViewById(R.id.lblLoginCom);
            holder.data = convertView.findViewById(R.id.lblDataCom);
            holder.textoComentario = convertView.findViewById(R.id.lblSeguidor);
            holder.iconImagem = convertView.findViewById(R.id.imgUsuarioCom);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        ItemListaComentario item = itemList.get(position);
        holder.nome.setText(item.getNome());
        holder.login.setText(item.getLogin());
        holder.data.setText(item.getData());
        holder.textoComentario.setText(item.getTexto());

        Glide.with(context)
                .load(item.getIconImagem())
                .circleCrop()
                .into(holder.iconImagem);

        return convertView;
    }

    private static class ViewHolder {
        TextView nome, login, data, textoComentario;
        ImageView iconImagem;
    }
}
