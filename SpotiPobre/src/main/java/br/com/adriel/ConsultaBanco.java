package br.com.adriel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConsultaBanco {

    public static void main(String[] args) {

        try {
            Connection conexao = Conexao.conectar();

            String sql = """
                    SELECT
                        album.nome AS album,
                        album.artista AS artista,
                        musica.nome AS musica
                    FROM album
                    JOIN album_musica
                        ON album.id_album = album_musica.id_album
                    JOIN musica
                        ON musica.id_musica = album_musica.id_musica
                    ORDER BY album.id_album, musica.id_musica
                    """;

            Statement comando = conexao.createStatement();

            ResultSet resultado = comando.executeQuery(sql);

            while (resultado.next()) {

                String album = resultado.getString("album");
                String artista = resultado.getString("artista");
                String musica = resultado.getString("musica");

                System.out.println(
                        album + " | " + artista + " | " + musica
                );
            }

            resultado.close();
            comando.close();
            conexao.close();

        } catch (Exception e) {

            System.out.println("Erro ao consultar o banco:");
            e.printStackTrace();
        }
    }
}