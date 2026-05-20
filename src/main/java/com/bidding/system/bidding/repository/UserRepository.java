/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Define o pacote onde está localizada esta classe
package com.bidding.system.bidding.repository;

// Importa as classes necessárias para manipulação de usuários e banco de dados
import com.bidding.system.bidding.model.UserDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Usuario
 */
@Repository
public class UserRepository {

    // Método para registrar um novo usuário no banco de dados
    // user: objeto UserDTO contendo os dados do novo usuário
    public void register(UserDTO user) {
        try {
            // Obtém uma conexão com o banco de dados MySQL
            Connection conn = Conexao.conectar();
            // Declara a variável para preparar o comando SQL
            PreparedStatement stmt = null;
            // Prepara o comando SQL INSERT para inserir um novo usuário na tabela 'usuarios'
            // Os símbolos '?' são placeholders para os parâmetros
            stmt = conn.prepareStatement("Insert into usuarios (nome, email, senha, role) values (?,?,?,?)");

            // Define os parâmetros do SQL com os dados do usuário
            // Parâmetro 1: nome do usuário
            stmt.setString(1, user.getNome());
            // Parâmetro 2: email do usuário
            stmt.setString(2, user.getEmail());
            // Parâmetro 3: senha do usuário
            stmt.setString(3, user.getSenha());
            // Parâmetro 4: papel/função do usuário (COMPRADOR ou FORNECEDOR)
            stmt.setString(4, user.getRole());

            // Executa o comando de inserção
            int linhasAfetadas = stmt.executeUpdate();
            // Verifica se alguma linha foi afetada (inserida com sucesso)
            if (linhasAfetadas == 0) {
                // Se nenhuma linha foi afetada, lança uma exceção
                throw new SQLException("Falha na atualização - Nenhuma linha foi afetada");
            }
        } catch (SQLException e) {
            // Captura qualquer erro de SQL e imprime o rastreamento da pilha
            e.printStackTrace();
        }
    }

    // Método para autenticar um usuário pelo email e senha
    // email: email do usuário tentando fazer login
    // senha: senha do usuário para verificação
    // Retorna: objeto UserDTO contendo os dados do usuário autenticado
    public UserDTO logar(String email, String senha) {
        // Cria um novo objeto UserDTO para armazenar os dados do usuário
        UserDTO user = new UserDTO();

        try {
            // Obtém uma conexão com o banco de dados
            Connection conn = Conexao.conectar();
            // Declara a variável para preparar o comando SQL
            PreparedStatement stmt = null;
            // Declara a variável para armazenar o resultado da consulta
            ResultSet rs = null;

            // Prepara o comando SQL para buscar o usuário com email e senha correspondentes
            // Os símbolos '?' são placeholders para os parâmetros
            stmt = conn.prepareStatement("select * from usuarios where email = ? and senha = ?");

            // Define os parâmetros do SQL
            // Parâmetro 1: email do usuário
            stmt.setString(1, email);
            // Parâmetro 2: senha do usuário
            stmt.setString(2, senha);

            // Executa a consulta e armazena o resultado
            rs = stmt.executeQuery();

            // Se encontrou um usuário com as credenciais fornecidas
            if (rs.next()) {
                // Extrai o email do resultado e define no objeto UserDTO
                user.setEmail(rs.getString("email"));
                // Extrai o id do resultado
                user.setId(rs.getLong("id"));
                // Extrai o nome do resultado
                user.setNome(rs.getString("nome"));
                // Extrai o papel/função do resultado
                user.setRole(rs.getString("role"));
            }

        } catch (SQLException e) {
            // Captura qualquer erro de SQL e imprime o rastreamento da pilha
            e.printStackTrace();

        }
        // Retorna o usuário encontrado (ou objeto vazio se não encontrou)
        return user;
    }
}
