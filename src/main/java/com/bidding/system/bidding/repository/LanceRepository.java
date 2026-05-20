/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bidding.system.bidding.repository;

import com.bidding.system.bidding.model.LanceDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Usuario
 */
@Repository
public class LanceRepository {
    
    // Método responsável por criar um novo lance no banco de dados
    // lance: objeto contendo os dados do lance (valor, data, id do edital, id do usuário)
    // Retorna: número de linhas afetadas pela inserção (0 = erro, > 0 = sucesso)
    public int criarLance(LanceDTO lance) {
        try {
            // Obtém uma conexão com o banco de dados MySQL
            Connection conn = Conexao.conectar();
            // Declara a variável para preparar o comando SQL
            PreparedStatement stmt = null;
            
            // Prepara o comando SQL INSERT para inserir um novo lance na tabela 'lances'
            // Os símbolos '?' são placeholders para os parâmetros
            stmt = conn.prepareStatement("INSERT FROM lances (valor, data_lance, id_edital,id_usuario) "
                    + "VALUES (?,?,?,?)");
            // Define o primeiro parâmetro: valor do lance (double)
            stmt.setDouble(1, lance.getValor());
            // Define o segundo parâmetro: data do lance (Date SQL)
            stmt.setDate(2, lance.getData_lance());
            // Define o terceiro parâmetro: id do edital associado ao lance
            stmt.setLong(3, lance.getIdEdital());
            // Define o quarto parâmetro: id do usuário que fez o lance
            stmt.setLong(4, lance.getIdUsuario());
            
            // Executa o comando de inserção e retorna o número de linhas afetadas
            return stmt.executeUpdate();
        } catch(SQLException e) {
            // Captura qualquer erro de SQL e imprime o rastreamento da pilha
            e.printStackTrace();
        }
        // Retorna 0 em caso de erro, indicando que nenhuma linha foi inserida
        return 0;
    }
}
 
