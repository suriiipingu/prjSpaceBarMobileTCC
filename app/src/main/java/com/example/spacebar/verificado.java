package com.example.spacebar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class verificado extends AppCompatActivity {

    ImageButton btnAdicionarImg2;
    EditText txtProfissao;
    Button btnEnviar;

    private ActivityResultLauncher<String> fileChooserLauncher;

    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificado);

        btnAdicionarImg2 = findViewById(R.id.btnAdicionarImg2);
        txtProfissao = findViewById(R.id.txtProfissao);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnAdicionarImg2.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(verificado.this, "Formato de imagem não suportado", Toast.LENGTH_SHORT).show();
                    }
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
}