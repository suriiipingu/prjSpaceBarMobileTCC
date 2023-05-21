package com.example.spacebar;
import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Acessa {
    ResultSet RS;
    java.sql.Statement stmt;
    Connection con;
    public Connection entBanco(Context ctx){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Toast.makeText(ctx.getApplicationContext(), "Drive correto", Toast.LENGTH_SHORT).show();
        }
        catch(Exception ex){
            Toast.makeText(ctx.getApplicationContext(), "Drive não correto", Toast.LENGTH_SHORT).show();
        }

        try{
            String url = "jdbc:jtds:sqlserver://192.168.15.30:1433;databaseName=SpaceBar";
            con = DriverManager.getConnection(url, "sa", "123456");
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            Toast.makeText(ctx.getApplicationContext(), "conectado", Toast.LENGTH_SHORT).show();
        }
        catch(SQLException ex){
            Toast.makeText(ctx.getApplicationContext(), "não conectado", Toast.LENGTH_SHORT).show();
        }
        return con;
    }

    public void inserirDados(Context context, String nome, String login, String email, String celular, String pais, String senha) {
        try {
            // Estabelecer conexão com o banco de dados
            Connection connection = entBanco(context);

            // Preparar a instrução SQL para inserir os dados
            String sql = "INSERT INTO tblUsuario (nome, login, email, celular, pais, senha) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nome);
            statement.setString(2, login);
            statement.setString(3, email);
            statement.setString(4, celular);
            statement.setString(5, pais);
            statement.setString(6, senha);

            // Executar a instrução SQL
            statement.executeUpdate();

            // Fechar a conexão
            connection.close();

            // Informar que os dados foram inseridos com sucesso
            Toast.makeText(context, "Dados inseridos com sucesso", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, "Erro ao inserir dados no banco de dados", Toast.LENGTH_SHORT).show();
        }
    }
    //Fim do método que vai conectar o banco
}
