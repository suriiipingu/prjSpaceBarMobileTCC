package com.example.spacebar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class perfil_config extends AppCompatActivity {

    private ImageView imageView, imgFundo, imgSelo1, imgSelo2, imgSelo3;

    private Button btnSalvar;
    private EditText txtNome, txtBio;
    private boolean isPerfilButtonPressed = false, isFundoButtonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_config);


        // Ocultar a barra de título da página
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        ImageButton btnVoltar = findViewById(R.id.imgBtnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imageView = findViewById(R.id.imgPerfilAtualizar);
        imgFundo= findViewById(R.id.imgFundoAtualizar);
        imgSelo1 = findViewById(R.id.imgSelo1);
        imgSelo2 = findViewById(R.id.imgSelo2);
        imgSelo3 = findViewById(R.id.imgSelo3);
        txtNome = findViewById(R.id.txtNomeAtualizar);
        txtBio = findViewById(R.id.txtBio);
        btnSalvar = findViewById(R.id.btnSalvar);

        txtNome.setFocusableInTouchMode(false);
        txtBio.setFocusableInTouchMode(false);
        txtNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNome.setFocusableInTouchMode(true);
                txtNome.requestFocus();
            }
        });

        txtBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtBio.setFocusableInTouchMode(true);
                txtBio.requestFocus();
            }
        });

        ImageButton btnEnviarImgPerfil = findViewById(R.id.btnEnviarImgPerfil);
        ImageButton btnEnviarImgFundo = findViewById(R.id.btnEnviarImgFundo);

        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);

        try(PreparedStatement statement = con.prepareStatement("Select * from tblUsuario where cod_usuario= ?")) {
            statement.setInt(1, codigoUsuario);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                //pegar a imagem de perfil e de background do usuario e exibir
                byte[] iconUsuario = rs.getBytes("icon_usuario");
                byte[] imgfundoUsuario = rs.getBytes("imgfundo_usuario");
                if (iconUsuario != null) {
                    Glide.with(this)
                            .load(iconUsuario)
                            .circleCrop()
                            .into(imageView);
                }else{
                    Glide.with(this)
                            .load(R.drawable.bloco_criarpost)
                            .circleCrop()
                            .into(imageView);
                }
                if(imgfundoUsuario != null)
                {
                    Glide.with(this)
                            .load(imgfundoUsuario)
                            .into(imgFundo);
                }

                //comparar o tipo do usuario e exibir os selos

                int tipo_usuario = rs.getInt("cod_tipo");
                if(tipo_usuario == 2){
                    Glide.with(this)
                            .load(R.drawable.backspace)
                            .into(imgSelo1);
                }else if(tipo_usuario == 3) {
                    Glide.with(this)
                            .load(R.drawable.verificado_selo)
                            .into(imgSelo1);
                }else if(tipo_usuario == 4){
                    Glide.with(this)
                            .load(R.drawable.backspace)
                            .into(imgSelo1);
                    Glide.with(this)
                            .load(R.drawable.verificado_selo)
                            .into(imgSelo2);
                }else{
                    Glide.with(this)
                            .load(R.drawable.backspace)
                            .into(imgSelo1);
                    Glide.with(this)
                            .load(R.drawable.moderador)
                            .into(imgSelo2);
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }

        ActivityResultLauncher<Intent> launchImagePicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                // Exibir a imagem selecionada no ImageView com corte circular
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                                    Glide.with(perfil_config.this)
                                            .load(bitmap)
                                            .circleCrop()
                                            .into(imageView);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
        ActivityResultLauncher<Intent> launchImagePickerFundo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                                    Glide.with(perfil_config.this)
                                            .load(bitmap)
                                            .into(imgFundo);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

        btnEnviarImgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPerfilButtonPressed = true;
                // Criar um Intent para selecionar a imagem
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); // Apenas imagens
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // Iniciar a atividade de seleção de imagem e obter o resultado
                launchImagePicker.launch(intent);
            }
        });



        btnEnviarImgFundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFundoButtonPressed = true;
                // Criar um Intent para selecionar a imagem
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); // Apenas imagens
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // Iniciar a atividade de seleção de imagem e obter o resultado
                launchImagePickerFundo.launch(intent);
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean atualizacaoRealizada = false; // Variável para controlar se ocorreu alguma atualização

                // Verificar se o usuário inseriu algum nome
                String nome = txtNome.getText().toString();
                if (!nome.isEmpty()) {
                    try (PreparedStatement statement = con.prepareStatement("UPDATE tblUsuario SET nome_usuario = ? WHERE cod_usuario = ?")) {
                        statement.setString(1, nome);
                        statement.setInt(2, codigoUsuario);
                        statement.executeUpdate();
                        atualizacaoRealizada = true; // Atualização realizada
                    } catch (SQLException a) {
                        a.printStackTrace();
                    }
                }

                // Verificar se o usuário inseriu alguma bio
                String bio = txtBio.getText().toString();
                if (!bio.isEmpty()) {
                    try (PreparedStatement statement = con.prepareStatement("UPDATE tblUsuario SET bio_usuario = ? WHERE cod_usuario = ?")) {
                        statement.setString(1, bio);
                        statement.setInt(2, codigoUsuario);
                        statement.executeUpdate();
                        atualizacaoRealizada = true; // Atualização realizada
                    } catch (SQLException a) {
                        a.printStackTrace();
                    }
                }
                // Enviar a imagem selecionada para o banco de dados
                if (isPerfilButtonPressed) { // Verificar se o botão de perfil foi pressionado
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    byte[] imagemPerfil = getBytesFromBitmap(bitmap, Bitmap.CompressFormat.JPEG);

                    try (PreparedStatement statement = con.prepareStatement("UPDATE tblUsuario SET icon_usuario = ? WHERE cod_usuario = ?")) {
                        statement.setBytes(1, imagemPerfil);
                        statement.setInt(2, codigoUsuario);
                        statement.executeUpdate();
                        atualizacaoRealizada = true; // Atualização realizada
                    } catch (SQLException a) {
                        a.printStackTrace();
                    }
                }

                if(isFundoButtonPressed){
                    Bitmap bitmap = ((BitmapDrawable) imgFundo.getDrawable()).getBitmap();
                    byte[] imagemFundo = getBytesFromBitmap(bitmap, Bitmap.CompressFormat.JPEG);
                    try (PreparedStatement statement = con.prepareStatement("UPDATE tblUsuario SET imgFundo_usuario = ? WHERE cod_usuario = ?")) {
                        statement.setBytes(1, imagemFundo);
                        statement.setInt(2, codigoUsuario);
                        statement.executeUpdate();
                        atualizacaoRealizada = true; // Atualização realizada
                    } catch (SQLException a) {
                        a.printStackTrace();
                    }
                }


                // Exibir o Toast apenas se ocorreu alguma atualização
                if (atualizacaoRealizada) {
                    Toast.makeText(perfil_config.this, "Dados atualizados com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Método para converter um Bitmap em um array de bytes usando o formato especificado
    private byte[] getBytesFromBitmap(Bitmap bitmap, Bitmap.CompressFormat format) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(format, 100, stream);
        return stream.toByteArray();
    }

}
