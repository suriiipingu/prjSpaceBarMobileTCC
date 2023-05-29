package com.example.spacebar;
import android.content.Context;
import android.content.SharedPreferences;
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
            String url = "jdbc:jtds:sqlserver://192.168.0.33:1433;databaseName=SpaceBar";
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

    public boolean verificarLoginExistente(Context ctx, String login) {
        boolean loginExiste = false;
        try {
            Connection con = entBanco(ctx);
            String query = "SELECT COUNT(*) FROM tblUsuario WHERE login_usuario = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, login); // esta linha serve para colocar um parâmetro na string do sql, então para outros modelos, não terá essa linha, só se precisar
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                loginExiste = (count > 0);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loginExiste;
    }

    public boolean verificarEmailExistente(Context ctx, String email) {
        boolean emailExiste = false;
        try {
            Connection con = entBanco(ctx);
            String query = "SELECT COUNT(*) FROM tblUsuario WHERE email_usuario = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                emailExiste = (count > 0);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emailExiste;
    }


}
