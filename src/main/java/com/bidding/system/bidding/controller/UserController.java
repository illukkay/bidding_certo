/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// Define o pacote onde está localizada esta classe
package com.bidding.system.bidding.controller;


// Importa as classes necessárias para o funcionamento do controller
import com.bidding.system.bidding.model.UserDTO;
import com.bidding.system.bidding.model.UserRequestDTO;
import com.bidding.system.bidding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Usuario
 */

// Indica que esta classe é um controller REST do Spring
// REST = Representational State Transfer (padrão de API web)
@RestController
// Define o caminho base para as rotas deste controller
// Todas as URLs deste controller começarão com /api/auth
@RequestMapping("/api/auth")
public class UserController {
    // Injeta automaticamente uma instância de UserService
    // O @Autowired permite que o Spring crie e injete a dependência automaticamente
    // UserService contém a lógica de negócio para operações com usuários (registro e login)
    @Autowired
    private UserService service;

    // Mapeia requisições POST para /api/auth/registrar
    // POST é usado para criar novos recursos (neste caso, um novo usuário)
    @PostMapping("/registrar")
    public String registrar(@RequestBody UserDTO user) {
        // Chama o método de registro do serviço
        // O serviço valida se todos os campos obrigatórios foram preenchidos
        service.register(user);
        // Retorna mensagem de sucesso ao cliente
        return "Cadastro Feito com sucesso!";
    }

    // Mapeia requisições POST para /api/auth/logar
    // POST é usado para autenticar um usuário e obter um token JWT
    @PostMapping("/logar")
    public String login(@RequestBody UserRequestDTO user) {
        // Chama o método de login do serviço
        // O serviço valida as credenciais (email e senha) contra o banco de dados
        // Se válido, gera e retorna um token JWT
        // Se inválido, lança uma exceção com erro 401 (Não Autorizado)
        return service.logar(user);
    }
}
