package com.example.spacebar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

public class criar_post extends AppCompatActivity {

    private ActivityResultLauncher<String> fileChooserLauncher;

    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_post);

        ImageButton btnSelectFile = findViewById(R.id.btnAdicionarImg);
        ImageButton btnVoltar = findViewById(R.id.btnVoltar);
        Button btnPostar = findViewById(R.id.btnPostar);
        EditText txtTitulo = findViewById(R.id.txtTitulo);
        EditText txttexto = findViewById(R.id.txtTexto);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Obtém a data e hora atual
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //botão para selecionar foto do post
        btnSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooserLauncher.launch("*/*");
            }
        });
        fileChooserLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    String fileExtension = getFileExtension(result); // Obtém a extensão do arquivo
                    if (fileExtension != null && isSupportedImageFormat(fileExtension)) {
                        filePath = getPathFromUri(result); // Obtém o caminho do arquivo a partir do URI
                        // Faça algo com o arquivo selecionado
                    } else {
                        // Arquivo com extensão inválida, exiba uma mensagem de erro ou faça outra ação apropriada
                        Toast.makeText(criar_post.this, "Formato de imagem não suportado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        btnPostar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Acessa objA = new Acessa();
                Connection con = objA.entBanco(criar_post.this);
                SharedPreferences sharedPreferences = criar_post.this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
                int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

                String titulo = txtTitulo.getText().toString();
                String texto = txttexto.getText().toString();

                if (!titulo.isEmpty()) {
                    if(!texto.isEmpty()){
                        if (filePath != null) {
                            byte[] imageBytes = convertImageToByteArray(filePath);
                            try (
                                    PreparedStatement statement = con.prepareStatement("Insert into tblPost (cod_usuario, titulo_post, texto_post, data_post, img_post) values (?, ?, ?, ?, ?)")) {
                                statement.setInt(1, codigoUsuario);
                                statement.setString(2, titulo);
                                statement.setString(3, texto);
                                statement.setTimestamp(4, timestamp);
                                statement.setBytes(5, imageBytes);
                                statement.executeUpdate();
                                // A imagem foi enviada para o banco de dados com sucesso
                            } catch (SQLException e) {
                                e.printStackTrace();
                                // Ocorreu um erro ao enviar a imagem para o banco de dados
                            }

                        } else {

                            try (
                                PreparedStatement statement = con.prepareStatement("Insert into tblPost (cod_usuario, titulo_post, texto_post, data_post) values (?, ?, ?, ?)")) {
                                statement.setInt(1, codigoUsuario);
                                statement.setString(2, titulo);
                                statement.setString(3, texto);
                                statement.setTimestamp(4, timestamp);
                                statement.executeUpdate();
                                // O post foi enviado sem imagem
                            } catch (SQLException e) {
                                e.printStackTrace();
                                // Ocorreu um erro ao enviar o post para o banco de dados
                            }
                        }
                    }else{
                        if (filePath != null) {
                            byte[] imageBytes = convertImageToByteArray(filePath);
                            try (
                                    PreparedStatement statement = con.prepareStatement("Insert into tblPost (cod_usuario, titulo_post,data_post, img_post) values (?, ?, ?, ?)")) {
                                statement.setInt(1, codigoUsuario);
                                statement.setString(2, titulo);
                                statement.setTimestamp(3, timestamp);
                                statement.setBytes(4, imageBytes);
                                statement.executeUpdate();
                                // A imagem foi enviada para o banco de dados com sucesso
                            } catch (SQLException e) {
                                e.printStackTrace();
                                // Ocorreu um erro ao enviar a imagem para o banco de dados
                            }

                        } else {

                            try (
                                    PreparedStatement statement = con.prepareStatement("Insert into tblPost (cod_usuario, titulo_post,data_post) values (?, ?, ?)")) {
                                statement.setInt(1, codigoUsuario);
                                statement.setString(2, titulo);
                                statement.setTimestamp(3, timestamp);
                                statement.executeUpdate();
                                // O post foi enviado sem imagem
                            } catch (SQLException e) {
                                e.printStackTrace();
                                // Ocorreu um erro ao enviar o post para o banco de dados
                            }
                        }
                    }

                } else {
                    Toast.makeText(criar_post.this.getApplicationContext(), "Precisa de um titulo para enviar", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getFileExtension(Uri uri) {
        String extension = null;
        String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String fileName = cursor.getString(0);
            if (fileName != null) {
                extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            }
            cursor.close();
        }
        return extension;
    }

    private boolean isSupportedImageFormat(String extension) {
        return extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg");
    }

    private String getPathFromUri(Uri uri) {
        String path = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                File file = new File(getCacheDir(), "temp_image");
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[4 * 1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                path = file.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }




    private byte[] convertImageToByteArray(String filePath) {
        try {
            File file = new File(filePath);
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                throw new IOException("File is too large");
            }
            byte[] byteArray = new byte[(int) length];
            FileInputStream fileInputStream = new FileInputStream(file);
            int offset = 0;
            int numRead;
            while (offset < byteArray.length && (numRead = fileInputStream.read(byteArray, offset, byteArray.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < byteArray.length) {
                throw new IOException("Could not completely read file");
            }
            fileInputStream.close();
            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}