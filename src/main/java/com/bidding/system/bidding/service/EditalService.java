/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bidding.system.bidding.service;

import com.bidding.system.bidding.model.EditalDTO;
import com.bidding.system.bidding.model.UserDTO;
import com.bidding.system.bidding.repository.EditalRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Usuario
 */
// Anotação que marca esta classe como um serviço do Spring (camada de negócio)
@Service
public class EditalService {
    
    // Injeta automaticamente uma instância do repositório de editais
    // O repositório é responsável pelas operações de banco de dados relacionadas a editais
    @Autowired
    private EditalRepository repository;
    
    // Injeta automaticamente o serviço de token
    // É usado para validar tokens JWT e extrair informações do usuário autenticado
    @Autowired
    private TokenService tokenService;
    
    // Método responsável por criar um novo edital no sistema
    // novo: objeto contendo os dados do edital (título, descrição, data de fechamento)
    // token: token JWT do usuário autenticado para validação de autorização
    public void criarEdital(EditalDTO novo, String token) {
        // Extrai as informações do usuário a partir do token JWT
        // Obtém id, nome e role (papel) do usuário
        UserDTO userLogado = tokenService.extrairClaim(token);
        
        // Verifica se o usuário tem o papel de COMPRADOR
        // Apenas COMPRADORES podem criar editais no sistema
        if (userLogado.getRole().equals("COMPRADOR")) {
            // String que acumula mensagens de erro de validação
            String mensagem = "";
            
            // Valida se o título foi preenchido
            if (novo.getTitulo().equals("")) {
                mensagem += "Titulo não preenchido!\n";
            }
            // Valida se a descrição foi preenchida
            if (novo.getDescricao().equals("")) {
                mensagem += "Descrição não preenchida!\n";
            }
            // Valida se a data de fechamento foi preenchida
            if (novo.getDataFechamento() == null) {
                mensagem += "Data não preenchida!\n";
            }
            
            // Se houver erros de validação, lança uma exceção com status 400 (Bad Request)
            if (!mensagem.equals("")) {
                throw new ResponseStatusException(HttpStatusCode.valueOf(400), mensagem);
            }
            
            // Define o status inicial do edital como "ABERTO" para receber lances
            novo.setStatus("ABERTO");
            // Chama o repositório para inserir o edital no banco de dados
            // Retorna o número de linhas afetadas pela inserção
            int linhas = repository.criar(novo);
            // Valida se a inserção foi bem-sucedida
            if(linhas == 0) {
                // Se nenhuma linha foi afetada, lança exceção com status 500 (Erro Interno do Servidor)
                throw new ResponseStatusException(HttpStatusCode.valueOf(500), 
                "Erro ao cadastrar no banco de dados");
            }
        } else {
            // Se o usuário não é COMPRADOR, lança exceção com status 403 (Acesso Proibido)
            throw new ResponseStatusException(HttpStatusCode.valueOf(403), 
                    "Acesso não autorizado!");
        }
        
    }

    // Método para listar todos os editais cadastrados no sistema
    // token: token JWT do usuário autenticado para validação
    // Retorna: lista de todos os editais disponíveis
    public List<EditalDTO> listarEditais(String token) {
        // Valida se o token é válido e não expirou
        if(tokenService.validarToken(token)) {
            // Se o token é válido, retorna a lista de editais do repositório
            return repository.listar();
        } else {
            // Se o token é inválido ou expirou, lança exceção com status 401 (Não Autorizado)
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Necessário Logar com conta valida");
        }
    }
}
