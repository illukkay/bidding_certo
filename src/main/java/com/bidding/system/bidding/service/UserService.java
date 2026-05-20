/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// Define o pacote onde está localizada esta classe
package com.bidding.system.bidding.service;


// Importa as classes necessárias para manipulação de usuários, requisições e exceções
import com.bidding.system.bidding.model.UserDTO;
import com.bidding.system.bidding.model.UserRequestDTO;
import com.bidding.system.bidding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Usuario
 */

// Indica que esta classe é um serviço do Spring
@Service
public class UserService {
    // Injeta automaticamente o repositório de usuários
    @Autowired
    private UserRepository repository;

    // Injeta automaticamente o serviço de token
    @Autowired
    private TokenService tokenService;

    // Método para registrar um novo usuário no sistema
    // user: objeto UserDTO contendo nome, email, senha e role opcionalmente
    public void register(UserDTO user) {
        // String que acumula mensagens de erro de validação
        String mensagem = "";
        // Valida se o nome foi preenchido
        if(user.getNome().equals("")) {
            mensagem = "Nome não preenchido";
        } else if(user.getEmail().equals("")) {
            // Valida se o email foi preenchido
            mensagem = "Email não preenchido";
        } else if(user.getSenha().equals("")) {
            // Valida se a senha foi preenchida
            mensagem = "Senha não preenchida";
        } else if(user.getRole().equals("")) {
            // Se o papel não foi preenchido, define como FORNECEDOR (padrão)
            user.setRole("FORNECEDOR");
        }

        // Se houver mensagem de erro, lança exceção com status 400 (Bad Request)
        if(!mensagem.equals("")) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), mensagem);
        }

        // Chama o repositório para registrar o usuário no banco de dados
        repository.register(user);
    }

    // Método para autenticar o usuário e gerar um token JWT
    // user: objeto UserRequestDTO contendo email e senha
    // Retorna: string contendo o token JWT se autenticado, ou exceção se inválido
    public String logar(UserRequestDTO user) {
        // String que acumula mensagens de erro de validação
        String mensagem = "";
        // Valida se o email foi preenchido
        if(user.getEmail().equals("")) {
            mensagem = "Email não preenchido";
        } else if (user.getSenha().equals("")) {
            // Valida se a senha foi preenchida
            mensagem = "Senha não preenchida";
        }

        // Se houver mensagem de erro, lança exceção com status 400 (Bad Request)
        if(!mensagem.equals("")) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), mensagem);
        }

        // Busca os dados do usuário autenticado no banco de dados
        // O repositório retorna um UserDTO vazio se o email/senha forem inválidos
        UserDTO dadosLogado = repository.logar(user.getEmail(), user.getSenha());
        // Gera e retorna o token JWT para o usuário
        return tokenService.gerarToken(dadosLogado); 
    }
}
