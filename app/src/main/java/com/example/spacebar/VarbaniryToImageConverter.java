package com.example.spacebar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VarbaniryToImageConverter {
    public static void convertVarbinaryToImage(Connection connection, String tableName, String columnName, int imageId, String outputFileName) {
        try {
            String sql = "SELECT * from tblPost WHERE ID = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, imageId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        byte[] imageData = resultSet.getBytes(columnName);

                        // Converter o array de bytes para um objeto Bitmap
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                        // Salvar a imagem em um arquivo
                        try (OutputStream outputStream = new FileOutputStream(outputFileName)) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            System.out.println("Imagem salva com sucesso.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Nenhum resultado encontrado para o ID fornecido.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
