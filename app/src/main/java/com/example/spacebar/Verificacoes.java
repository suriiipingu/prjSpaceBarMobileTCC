package com.example.spacebar;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Verificacoes {


    public static boolean verificarSeTemTexto(Context context, int postId) {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(context);
        ResultSet resultSet = null;
        PreparedStatement stmt = null;

        try {
            // Execute a consulta SQL para verificar se o campo img_post contém uma imagem
            String query = "SELECT COUNT(*) FROM tblPost WHERE cod_post = ? AND texto_post IS NOT NULL";
            stmt = con.prepareStatement(query);
            stmt.setInt(1, postId);
            resultSet = stmt.executeQuery();

            // Se houver algum resultado, significa que o campo img_post contém uma imagem
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Certifique-se de fechar a conexão e os recursos
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false; // Se ocorrer algum erro ou se não houver resultados, retorne false
    }

    public static boolean verificarSeTemImagem(Context context, int postId) {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(context);
        ResultSet resultSet = null;
        PreparedStatement stmt = null;

        try {
            // Execute a consulta SQL para verificar se o campo img_post contém uma imagem
            String query = "SELECT COUNT(*) FROM tblPost WHERE cod_post = ? AND img_post IS NOT NULL";
            stmt = con.prepareStatement(query);
            stmt.setInt(1, postId);
            resultSet = stmt.executeQuery();

            // Se houver algum resultado, significa que o campo img_post contém uma imagem

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Certifique-se de fechar a conexão e os recursos
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false; // Se ocorrer algum erro ou se não houver resultados, retorne false
    }

    public static boolean verificarCurtidaPost(Context context, int itemId) {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(context);
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
            int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

            String query = "SELECT COUNT(*) FROM tblPostagemCurtidas WHERE tblPostagemCurtidas_cod_post = ? AND tblPostagemCurtidas_cod_usuario = ?";
            stmt = con.prepareStatement(query);
            stmt.setInt(1, itemId);
            stmt.setInt(2, codigoUsuario);
            resultSet = stmt.executeQuery();

            // Se houver algum resultado, significa que o usuário já curtiu a postagem
            if (resultSet.next()) {
                int curtidaCount = resultSet.getInt(1);
                return curtidaCount > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Certifique-se de fechar a conexão e os recursos
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

            return false;
        }

}

