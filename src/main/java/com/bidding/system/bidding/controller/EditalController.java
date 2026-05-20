/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// Define o pacote onde está localizada esta classe
package com.bidding.system.bidding.controller;


// Importa as classes necessárias para o controller
import com.bidding.system.bidding.model.EditalDTO;
import com.bidding.system.bidding.model.LanceDTO;
import com.bidding.system.bidding.model.UserDTO;
import com.bidding.system.bidding.service.EditalService;
import com.bidding.system.bidding.service.LanceService;
import com.bidding.system.bidding.service.TokenService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
// Todas as URLs deste controller começarão com /api/editais
@RequestMapping("/api/editais")
public class EditalController {
    // Injeta automaticamente uma instância de EditalService
    // O @Autowired permite que o Spring crie e injete a dependência automaticamente
    // EditalService contém a lógica de negócio para operações com editais
    @Autowired
    private EditalService service;
    
    // Injeta automaticamente uma instância de LanceService
    // LanceService contém a lógica de negócio para operações com lances
    @Autowired
    private LanceService lanceService;

    // Mapeia requisições POST para /api/editais
    // POST é usado para criar novos recursos (neste caso, um novo edital)
    @PostMapping
    public String cadastrarEdital(
            // @RequestHeader: extrai o valor do header HTTP especificado
            // Authorization: header padrão para enviar credenciais/tokens
            // O valor deve ser: Bearer {token_jwt}
            @RequestHeader("Authorization") String auth,
            // @RequestBody: transforma o corpo JSON da requisição em um objeto EditalDTO
            // Exemplo JSON: {"titulo": "...", "descricao": "...", "dataFechamento": "..."}
            @RequestBody EditalDTO edital
    ) {
        // Remove o prefixo "Bearer " do token para obter apenas o token JWT
        // O token vem no formato "Bearer token_aqui", mas precisamos só de "token_aqui"
        String token = auth.replace("Bearer ", "");
        // Chama o serviço para criar o edital
        // O serviço valida se o usuário é COMPRADOR e valida os dados do edital
        service.criarEdital(edital, token);
        // Retorna mensagem de sucesso ao cliente
        return "Edital Cadastrado com sucesso!";
    }
    
    
    // Mapeia requisições GET para /api/editais
    // GET é usado para recuperar recursos (neste caso, listar editais)
    @GetMapping
    public List<EditalDTO> listarEditais(
            // @RequestHeader: extrai o header Authorization contendo o token JWT
            @RequestHeader("Authorization") String auth
    ) {
        // Remove o prefixo "Bearer " do token para obter apenas o token JWT
        String token = auth.replace("Bearer ", "");
        
        // Chama o serviço para listar todos os editais
        // O serviço valida se o token é válido antes de retornar a lista
        List<EditalDTO> lista = service.listarEditais(token);
        // Retorna a lista de editais como JSON
        return lista;
    }
    
    
    // Mapeia requisições POST para /api/editais/{id}/lances
    // POST é usado para criar novos recursos (neste caso, um novo lance em um edital)
    // {id} é um parâmetro dinâmico que será substituído pelo id real na URL
    @PostMapping("{id}/lances")
    public String registrarLance(
            // @RequestHeader: extrai o header Authorization contendo o token JWT
            @RequestHeader("Authorization") String auth,
            // @RequestBody: transforma o corpo JSON em um objeto LanceDTO
            // Exemplo JSON: {"valor": 1500.00, "data_lance": "2025-05-20", ...}
            @RequestBody LanceDTO lance,
            // @PathVariable: extrai o valor do parâmetro {id} da URL
            // Exemplo: se a URL for /api/editais/5/lances, id = 5
            @PathVariable Long id
    ) {
        // Remove o prefixo "Bearer " do token para obter apenas o token JWT
        String token = auth.replace("Bearer ", "");
        // Chama o serviço para criar o lance
        // O serviço valida se o usuário é FORNECEDOR, se o edital existe, se está aberto, e a data
        lanceService.criarLance(id, lance, token);
        // Retorna mensagem de sucesso ao cliente
        return "Lance Registrado com sucesso!";
    }
}
