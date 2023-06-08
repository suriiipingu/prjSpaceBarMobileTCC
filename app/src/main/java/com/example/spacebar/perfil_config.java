package com.example.spacebar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

    private ImageView imageView, imgFundo, imgSelo1, imgSelo2;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private Button btnSalvar;
    private EditText txtNome, txtBio;
    private boolean isPerfilButtonPressed = false;

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
                            .load(R.drawable.round_background)
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
                if(tipo_usuario == 2 || tipo_usuario == 4){
                    Glide.with(this)
                            .load(R.drawable.backspace)
                            .into(imgSelo1);
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();
                            if (imageUri != null) {
                                if (isPerfilButtonPressed) {
                                    exibirImagemSelecionada(imageUri);
                                } else {
                                    exibirImagemFundoSelecionada(imageUri);
                                }
                            }
                        }
                    }
                });

        btnEnviarImgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPerfilButtonPressed = true;
                abrirGaleria();
            }
        });


        btnEnviarImgFundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPerfilButtonPressed = false;
                abrirGaleriaFundo();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean atualizacaoRealizada = false; // Variável para controlar se ocorreu alguma atualização

                // Verificar se uma imagem de perfil foi selecionada
                Drawable drawable = imageView.getDrawable();
                Bitmap drawableBitmap = null;
                if (drawable != null && drawable instanceof BitmapDrawable) {
                    drawableBitmap = ((BitmapDrawable) drawable).getBitmap();
                    if (drawableBitmap != null && drawableBitmap.getWidth() > 0 && drawableBitmap.getHeight() > 0) {
                        // Converter o Bitmap em um array de bytes (JPEG com qualidade 100)
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        drawableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] imageBytes = stream.toByteArray();
                        try (PreparedStatement statement = con.prepareStatement("UPDATE tblUsuario SET icon_usuario = ? WHERE cod_usuario = ?")) {
                            statement.setBytes(1, imageBytes);
                            statement.setInt(2, codigoUsuario);
                            statement.executeUpdate();
                            atualizacaoRealizada = true; // Atualização realizada
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }



                // Verificar se uma imagem de fundo foi selecionada
                Drawable fundoDrawable = imgFundo.getDrawable();
                Bitmap fundoBitmap = null;
                if (fundoDrawable != null && fundoDrawable instanceof BitmapDrawable) {
                    fundoBitmap = ((BitmapDrawable) fundoDrawable).getBitmap();
                    if (fundoBitmap != null) {
                        // Converter o Bitmap em um array de bytes (JPEG com qualidade 100)
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        fundoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] imageBytes = stream.toByteArray();
                        try (PreparedStatement statement = con.prepareStatement("UPDATE tblUsuario SET imgfundo_usuario = ? WHERE cod_usuario = ?")) {
                            statement.setBytes(1, imageBytes);
                            statement.setInt(2, codigoUsuario);
                            statement.executeUpdate();
                            atualizacaoRealizada = true; // Atualização realizada
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }


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

                // Exibir o Toast apenas se ocorreu alguma atualização
                if (atualizacaoRealizada) {
                    Toast.makeText(perfil_config.this, "Dados atualizados com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abrirGaleriaFundo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void exibirImagemFundoSelecionada(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            imgFundo.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void exibirImagemSelecionada(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            Bitmap roundedBitmap = getRoundedBitmap(bitmap);
            imageView.setImageBitmap(roundedBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int radius = Math.min(width, height) / 2;

        Bitmap croppedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(croppedBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawCircle(width / 2, height / 2, radius, paint);

        return croppedBitmap;
    }

    private boolean verificarNomeExistente(String nome) {
        Acessa objA = new Acessa();
        Connection con = objA.entBanco(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        int codigoUsuario = sharedPreferences.getInt("codigoUsuario", -1);
        boolean nomeExistente = false;

        try (PreparedStatement statement = con.prepareStatement("SELECT COUNT(*) AS count FROM tblUsuario WHERE cod_usuario != ? AND nome = ?")) {
            statement.setInt(1, codigoUsuario);
            statement.setString(2, nome);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                nomeExistente = (count > 0);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return nomeExistente;
    }


}
