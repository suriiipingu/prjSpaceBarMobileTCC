package com.example.spacebar;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserManager {
    public static int seguirUsuario(Context context, int usuarioAlvo) {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int usuarioSeguidor = sharedPreferences.getInt("codigoUsuario", -1);

        try {


            // Atualize o valor no banco de dados usando uma consulta SQL
            String updateQuery = "INSERT INTO tblSeguidores (id_usuario_seguidor,id_usuario_alvo) VALUES (?, ?)";
            PreparedStatement updateStmt = con.prepareStatement(updateQuery);
            updateStmt.setInt(1, usuarioSeguidor);
            updateStmt.setInt(2, usuarioAlvo);
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

    public static int deseguirUsuario(Context context, int usuarioAlvo) {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int usuarioSeguidor = sharedPreferences.getInt("codigoUsuario", -1);

        try {

            // Atualize o valor no banco de dados usando uma consulta SQL
            String updateQuery = "DELETE FROM tblSeguidores WHERE id_usuario_seguidor = ? AND id_usuario_alvo = ?";
            PreparedStatement updateStmt = con.prepareStatement(updateQuery);
            updateStmt.setInt(1, usuarioSeguidor);
            updateStmt.setInt(2, usuarioAlvo);
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
