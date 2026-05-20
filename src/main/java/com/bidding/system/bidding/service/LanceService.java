/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bidding.system.bidding.service;

import com.bidding.system.bidding.model.EditalDTO;
import com.bidding.system.bidding.model.LanceDTO;
import com.bidding.system.bidding.model.UserDTO;
import com.bidding.system.bidding.repository.EditalRepository;
import com.bidding.system.bidding.repository.LanceRepository;
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
public class LanceService {
    
    // Injeta automaticamente o serviço de token para validar autenticação
    @Autowired
    private TokenService tokenService;
    
    // Injeta automaticamente o repositório de editais
    // É usado para recuperar informações sobre o edital alvo do lance
    @Autowired
    private EditalRepository editalRepository;
    
    // Injeta automaticamente o repositório de lances
    // É responsável por persistir os lances no banco de dados
    @Autowired
    private LanceRepository lanceRepository;
    
    public void criarLance(Long id, LanceDTO lance, String token) {
        if(tokenService.validarToken(token)) {
            UserDTO userLogado = tokenService.extrairClaim(token);
            
            if(!userLogado.getRole().equals("FORNECEDOR")) {
                throw new ResponseStatusException(HttpStatusCode.valueOf(403), "Você precisa ser Fornecedor para cadastrar um lance!");
            }
            
            EditalDTO edital = editalRepository.getById(id);
            
            if(!edital.getStatus().equals("ABERTO")) {
                throw new ResponseStatusException(HttpStatusCode.valueOf(400), 
                "Você não pode criar lances para um edital fechado!");
            }
            
            if(edital.getDataFechamento().before(lance.getData_lance())) {
                throw new ResponseStatusException(HttpStatusCode.valueOf(400),
                "Data do lance posterior ao fechamento!");
            }
            
            int linhas = lanceRepository.criarLance(lance);
            if(linhas == 0) {
                throw new ResponseStatusException(HttpStatusCode.valueOf(500),
                "Erro ao inserir no banco de dados");
            }
        } else {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Token Invalido!");
            
        }
        
        
        
    }
    
}
