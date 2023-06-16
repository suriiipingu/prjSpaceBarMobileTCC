package com.example.spacebar;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserManager {
    public static boolean seguirUsuario(Context context, int usuarioAlvo) {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int usuarioSeguidor = sharedPreferences.getInt("codigoUsuario", -1);

        try {
            // Atualize o valor no banco de dados usando uma consulta SQL
            String insertQuery = "INSERT INTO tblSeguidores (id_usuario_seguidor, id_usuario_alvo) VALUES (?, ?)";
            PreparedStatement insertStmt = con.prepareStatement(insertQuery);
            insertStmt.setInt(1, usuarioSeguidor);
            insertStmt.setInt(2, usuarioAlvo);
            int rowsAffected = insertStmt.executeUpdate();

            // Feche a conexão e os recursos
            insertStmt.close();
            con.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deseguirUsuario(Context context, int usuarioAlvo) {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int usuarioSeguidor = sharedPreferences.getInt("codigoUsuario", -1);

        try {
            // Atualize o valor no banco de dados usando uma consulta SQL
            String deleteQuery = "DELETE FROM tblSeguidores WHERE id_usuario_seguidor = ? AND id_usuario_alvo = ?";
            PreparedStatement deleteStmt = con.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, usuarioSeguidor);
            deleteStmt.setInt(2, usuarioAlvo);
            int rowsAffected = deleteStmt.executeUpdate();

            // Feche a conexão e os recursos
            deleteStmt.close();
            con.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
