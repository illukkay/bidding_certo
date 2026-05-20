/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// Define o pacote onde está localizada esta classe
package com.bidding.system.bidding.repository;


// Importa as classes necessárias para manipulação de editais e banco de dados
import com.bidding.system.bidding.model.EditalDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Usuario
 */

// Indica que esta classe é um repositório do Spring
@Repository
public class EditalRepository {

    // Método para criar um novo edital no banco de dados
    // novo: objeto EditalDTO contendo os dados do edital a ser criado
    // Retorna: número de linhas afetadas pela inserção (0 = erro, > 0 = sucesso)
    public int criar(EditalDTO novo) {
        try {
            // Obtém uma conexão com o banco de dados MySQL
            Connection conn = Conexao.conectar();
            // Declara a variável para preparar o comando SQL
            PreparedStatement stmt = null;
            // Prepara o comando SQL INSERT para inserir um novo edital na tabela 'editais'
            // Os símbolos '?' são placeholders para os parâmetros
            stmt = conn.prepareStatement("INSERT INTO editais (titulo, descricao, data_fechamento, status)"
                    + "VALUES (?, ?, ?, ?)");
            // Define o primeiro parâmetro: título do edital
            stmt.setString(1, novo.getTitulo());
            // Define o segundo parâmetro: descrição do edital
            stmt.setString(2, novo.getDescricao());
            // Define o terceiro parâmetro: data de fechamento (tipo Date SQL)
            stmt.setDate(3, novo.getDataFechamento());
            // Define o quarto parâmetro: status inicial ("ABERTO")
            stmt.setString(4, novo.getStatus());

            // Executa o comando de inserção e retorna o número de linhas afetadas
            return stmt.executeUpdate();
        } catch(SQLException e ) {
            // Captura qualquer erro de SQL e imprime o rastreamento da pilha
            e.printStackTrace();
        }
        // Retorna 0 em caso de erro, indicando que nenhuma linha foi inserida
        return 0;
    }
    
    // Método para listar todos os editais cadastrados no banco de dados
    // Retorna: lista de objetos EditalDTO contendo todos os editais
    public List<EditalDTO> listar() {
        // Cria uma lista vazia para armazenar os editais
        List<EditalDTO> lista = new ArrayList<EditalDTO>();
        try {
            // Obtém uma conexão com o banco de dados
            Connection conn = Conexao.conectar();
            // Declara a variável para preparar o comando SQL
            PreparedStatement stmt = null;
            // Declara a variável para armazenar o resultado da consulta
            ResultSet rs = null;
            
            // Prepara o comando SQL para selecionar todos os editais
            stmt = conn.prepareStatement("SELECT * FROM editais");
            // Executa a consulta e armazena o resultado
            rs = stmt.executeQuery();
            // Itera sobre cada linha do resultado
            while(rs.next()) {
                // Cria um novo objeto EditalDTO para armazenar os dados da linha
                EditalDTO edital = new EditalDTO();
                // Extrai e define o id do edital
                edital.setId(rs.getLong("id"));
                // Extrai e define o título do edital
                edital.setTitulo(rs.getString("titulo"));
                // Extrai e define a descrição do edital
                edital.setDescricao(rs.getString("descricao"));
                // Extrai e define a data de fechamento do edital
                edital.setDataFechamento(rs.getDate("data_fechamento"));
                // Extrai e define o status do edital
                edital.setStatus(rs.getString("status"));
                
                // Adiciona o edital à lista
                lista.add(edital);
            }
        } catch(SQLException e ) {
            // Captura qualquer erro de SQL e imprime o rastreamento
            e.printStackTrace();
        }
        // Retorna a lista de editais (pode estar vazia se houver erro)
        return lista;
    }
    
    // Método para buscar um edital específico pelo seu id
    // id: identificador único do edital a ser recuperado
    // Retorna: objeto EditalDTO contendo os dados do edital encontrado
    public EditalDTO getById(Long id) {
        // Cria um novo objeto EditalDTO para armazenar os dados
        EditalDTO edital = new EditalDTO();
        try {
            // Obtém uma conexão com o banco de dados
            Connection conn = Conexao.conectar();
            // Declara a variável para preparar o comando SQL
            PreparedStatement stmt = null;
            // Declara a variável para armazenar o resultado da consulta
            ResultSet rs = null;
            
            // Prepara o comando SQL para selecionar um edital pelo id
            // Só recupera data_fechamento e status (conforme necessário para validações)
            stmt = conn.prepareStatement("SELECT data_fechamento, status FROM editais WHERE id = ?");
            // Define o parâmetro: id do edital a buscar
            stmt.setLong(1, id);
            // Executa a consulta
            rs = stmt.executeQuery();
            // Se encontrou um resultado
            if(rs.next()) {
                // Extrai e define a data de fechamento
                edital.setDataFechamento(rs.getDate("data_fechamento"));
                // Extrai e define o status
                edital.setStatus(rs.getString("status"));
            }
        } catch(SQLException e ) {
            // Captura qualquer erro de SQL e imprime o rastreamento
            e.printStackTrace();
        }
        // Retorna o edital (pode estar vazio se não encontrado ou houver erro)
        return edital;
    }
}
