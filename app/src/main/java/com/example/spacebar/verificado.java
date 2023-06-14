package com.example.spacebar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class verificado extends AppCompatActivity {

    TextView lblErro;
    EditText txtProfissao;
    Button btnEnviar;

    private ActivityResultLauncher<String> fileChooserLauncher;

    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificado);

        int statusBarColor = getResources().getColor(R.color.white);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor);
        }

        //tirar a barra de titulo da pagina
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ImageButton btnVoltar = findViewById(R.id.btnVoltar4);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences Session = this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int tipoUsuario = Session.getInt("tipoUsuario", -1);
        int codigoUsuario = Session.getInt("codigoUsuario", -1);
        String statusVerificado = Session.getString("statusVerificado", "nenhum");

        LinearLayout inserirLinearLayout = findViewById(R.id.inserirLayout2);

        if (tipoUsuario == 3 || tipoUsuario == 4 ||tipoUsuario == 5) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.verificado_true, inserirLinearLayout, false);

            inserirLinearLayout.addView(view);

        } else {
            if(statusVerificado.equals("pendente")){
                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.verificado_pendente, inserirLinearLayout, false);

                inserirLinearLayout.addView(view);
            }else if(statusVerificado.equals("negado")){
                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.verificado_negado, inserirLinearLayout, false);

                inserirLinearLayout.addView(view);
            }else{
                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.verificado_false, inserirLinearLayout, false);

                inserirLinearLayout.addView(view);

                ImageButton btnAdicionarImg2 = findViewById(R.id.btnAdicionarImg2);
                lblErro = findViewById(R.id.lblErroVerificado);
                txtProfissao = findViewById(R.id.txtProfissao);
                btnEnviar = findViewById(R.id.btnEnviar);

                btnAdicionarImg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fileChooserLauncher.launch("*/*");
                    }

                });

                btnEnviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Acessa objA = new Acessa();
                        Connection con = objA.entBanco(verificado.this);

                        String profissao = txtProfissao.getText().toString();
                        String status = "pendente";

                        if(!profissao.isEmpty()){
                            if (filePath != null){
                                byte[] imageBytes = convertImageToByteArray(filePath);
                                try(PreparedStatement statement = con.prepareStatement("Update tblUsuario set profissao=?, img_comprovante=?, status_verificado= ? where cod_usuario = ?")){
                                    statement.setString(1, profissao);
                                    statement.setBytes(2, imageBytes);
                                    statement.setString(3, status);
                                    statement.setInt(4, codigoUsuario);
                                    statement.executeUpdate();

                                    SharedPreferences.Editor editor = Session.edit();
                                    editor.putString("statusVerificado", status);
                                    editor.apply();

                                    Toast.makeText(verificado.this, "Solicitação enviada com sucesso!", Toast.LENGTH_SHORT).show();
                                }catch (SQLException a){
                                    a.printStackTrace();
                                }
                            }else{
                                lblErro.setText("Insira uma imagem para comprovar a sua solicitação.");
                            }
                        }else{
                            lblErro.setText("Insira uma profissão para enviar a solicitação.");
                        }

                    }
                });
            }

        }

        fileChooserLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    String fileExtension = getFileExtension(result); // Obtém a extensão do arquivo
                    if (fileExtension != null && isSupportedImageFormat(fileExtension)) {
                        filePath = getPathFromUri(result);
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