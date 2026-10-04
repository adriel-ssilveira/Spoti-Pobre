package br.com.adriel;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinMustache;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.fileRenderer(new JavalinMustache());
            config.staticFiles.add("/public");
        }).start(7070);

        // =========================================================
        // PÁGINA INICIAL
        // =========================================================

        app.get("/", ctx -> {
            ctx.render("templates/index.html");
        });


        // =========================================================
        // DASHBOARD
        // =========================================================

        app.get("/dashboard", ctx -> {

            List<Map<String, String>> musicas = new ArrayList<>();

            String sql = """
                    SELECT
                        musica.id_musica AS id_musica,
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

            try (
                    Connection conexao = Conexao.conectar();
                    Statement comando = conexao.createStatement();
                    ResultSet resultado = comando.executeQuery(sql)
            ) {

                while (resultado.next()) {

                    Map<String, String> musica = new HashMap<>();

                    String album = resultado.getString("album");

                    musica.put(
                            "id_musica",
                            resultado.getString("id_musica")
                    );

                    musica.put(
                            "album",
                            album
                    );

                    musica.put(
                            "artista",
                            resultado.getString("artista")
                    );

                    musica.put(
                            "musica",
                            resultado.getString("musica")
                    );


                    // =================================================
                    // CAPA DO ÁLBUM
                    // =================================================

                    if (album.equals("Appetite for Destruction")) {

                        musica.put(
                                "imagem",
                                "/images/appetite.png"
                        );

                    } else if (album.equals("Graduation")) {

                        musica.put(
                                "imagem",
                                "/images/graduation.png"
                        );

                    } else if (album.equals("Thriller")) {

                        musica.put(
                                "imagem",
                                "/images/thriller.png"
                        );
                    }


                    musicas.add(musica);
                }


                ctx.render(
                        "templates/dashboard.html",
                        Map.of("musicas", musicas)
                );

            } catch (Exception e) {

                e.printStackTrace();

                ctx.status(500);
                ctx.result(
                        "Erro ao consultar o banco de dados."
                );
            }
        });


        // =========================================================
        // FORMULÁRIO DE NOVA PLAYLIST
        // =========================================================

        app.get("/playlists/nova", ctx -> {

            ctx.render(
                    "templates/nova-playlist.html"
            );
        });


        // =========================================================
        // CRIAR PLAYLIST
        // =========================================================

        app.post("/playlists/nova", ctx -> {

            String nome = ctx.formParam("nome");

            boolean publica = Boolean.parseBoolean(
                    ctx.formParam("publica")
            );

            int idUsuario = Integer.parseInt(
                    ctx.formParam("id_usuario")
            );


            String sqlPlaylist = """
                    INSERT INTO playlist
                        (nome, publica)
                    VALUES
                        (?, ?)
                    RETURNING id_playlist
                    """;


            String sqlUsuarioPlaylist = """
                    INSERT INTO usuario_playlist
                        (id_usuario, id_playlist, dono)
                    VALUES
                        (?, ?, ?)
                    """;


            try (
                    Connection conexao = Conexao.conectar()
            ) {

                // ---------------------------------------------
                // INSERE A PLAYLIST
                // ---------------------------------------------

                var comandoPlaylist =
                        conexao.prepareStatement(sqlPlaylist);

                comandoPlaylist.setString(
                        1,
                        nome
                );

                comandoPlaylist.setBoolean(
                        2,
                        publica
                );


                ResultSet resultado =
                        comandoPlaylist.executeQuery();

                resultado.next();


                int idPlaylist =
                        resultado.getInt("id_playlist");


                resultado.close();
                comandoPlaylist.close();


                // ---------------------------------------------
                // VINCULA O USUÁRIO COMO DONO
                // ---------------------------------------------

                var comandoUsuarioPlaylist =
                        conexao.prepareStatement(
                                sqlUsuarioPlaylist
                        );

                comandoUsuarioPlaylist.setInt(
                        1,
                        idUsuario
                );

                comandoUsuarioPlaylist.setInt(
                        2,
                        idPlaylist
                );

                comandoUsuarioPlaylist.setBoolean(
                        3,
                        true
                );


                comandoUsuarioPlaylist.executeUpdate();

                comandoUsuarioPlaylist.close();


                // Volta para o Dashboard

                ctx.redirect("/dashboard");

            } catch (Exception e) {

                e.printStackTrace();

                ctx.status(500);
                ctx.result(
                        "Erro ao criar playlist."
                );
            }
        });


        // =========================================================
        // REGISTRAR REPRODUÇÃO
        // =========================================================

        app.post("/reproduzir", ctx -> {

            int idMusica = Integer.parseInt(
                    ctx.formParam("id_musica")
            );

            int idUsuario = Integer.parseInt(
                    ctx.formParam("id_usuario")
            );


            String sql = """
                    INSERT INTO reproducao
                        (id_musica, id_usuario, data_hora)
                    VALUES
                        (?, ?, CURRENT_TIMESTAMP)
                    """;


            try (
                    Connection conexao = Conexao.conectar();

                    var comando =
                            conexao.prepareStatement(sql)
            ) {

                comando.setInt(
                        1,
                        idMusica
                );

                comando.setInt(
                        2,
                        idUsuario
                );


                comando.executeUpdate();


                // Volta para o Dashboard

                ctx.redirect("/dashboard");

            } catch (Exception e) {

                e.printStackTrace();

                ctx.status(500);
                ctx.result(
                        "Erro ao registrar reprodução."
                );
            }
        });
    }
}