package br.com.adriel;

import java.sql.Connection;

public class TesteConexao {

    public static void main(String[] args) {

        try {
            Connection conexao = Conexao.conectar();

            System.out.println("Conexão com o PostgreSQL realizada com sucesso!");

            conexao.close();

        } catch (Exception e) {
            System.out.println("Erro ao conectar com o PostgreSQL:");
            e.printStackTrace();
        }
    }
}