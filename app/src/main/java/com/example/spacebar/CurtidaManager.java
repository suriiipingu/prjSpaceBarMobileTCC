package com.example.spacebar;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CurtidaManager {
    public static int darLike(Context context, int itemId) {
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

    public static int darDislike(Context context, int itemId) {
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
