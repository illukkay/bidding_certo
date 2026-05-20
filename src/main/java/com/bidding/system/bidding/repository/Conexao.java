/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// Define o pacote onde está localizada esta classe
package com.bidding.system.bidding.repository;


// Importa as classes necessárias para conexão com o banco de dados
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Usuario
 */

// Classe responsável por gerenciar a conexão com o banco de dados
// Implementa o padrão Singleton para garantir uma única conexão reutilizável
public class Conexao {
    // URL de conexão com o banco de dados MySQL
    // Formato: jdbc:mysql://host:porta/nome_banco_dados
    // localhost = máquina local, 3306 = porta padrão MySQL, db_bidding_system = nome da base de dados
    private static final String url = "jdbc:mysql://localhost:3306/db_bidding_system";
    // Usuário do banco de dados MySQL para autenticação
    private static final String user = "root";
    // Senha do banco de dados MySQL para autenticação
    private static final String senha = "1234";
    // Objeto de conexão reutilizável (implementa padrão Singleton)
    // Mantém uma única conexão aberta durante a vida da aplicação
    private static Connection conn = null;

    // Construtor padrão (vazio)
    public Conexao () {}

    // Método para obter uma conexão com o banco de dados de forma sincronizada
    // O synchronized garante que apenas uma thread acesse este método por vez
    // Previne condições de corrida (race conditions) ao criar múltiplas conexões
    // Retorna: objeto Connection para executar comandos SQL
    public static synchronized Connection conectar() {
        try {
            // Verifica se a conexão ainda não foi criada ou se foi fechada
            // conn == null: primeira vez que o método é chamado
            // conn.isClosed(): conexão anterior foi fechada
            if(conn == null || conn.isClosed()) {
                // DriverManager.getConnection estabelece uma nova conexão com o banco de dados
                // Parâmetros: (URL da conexão, usuário, senha)
                // Retorna um objeto Connection que representa a conexão ativa
                conn = DriverManager.getConnection(url, user, senha);
            }
        } catch(SQLException e ) {
            // Em caso de erro (banco não disponível, credenciais inválidas, porta bloqueada, etc.)
            // Imprime o rastreamento completo da pilha para diagnóstico e debugging
            e.printStackTrace();
        }
        // Retorna o objeto de conexão (pode ser null se houver erro na conexão)
        return conn;
    }
}
