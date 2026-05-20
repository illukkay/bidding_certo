/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// Define o pacote onde está localizada esta classe
package com.bidding.system.bidding.service;


// Importa as classes necessárias para manipulação de tokens JWT e usuários
import com.bidding.system.bidding.model.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;



// Indica que esta classe é um serviço do Spring
// Indica que esta classe é um serviço do Spring (camada de lógica de negócio)
@Service
public class TokenService {
    // Injeta o valor do segredo do token a partir do arquivo application.properties
    // Este valor deve ser uma chave secreta em base64 para assinar tokens JWT
    @Value("${api.security.token.secret}")
    private String secret;

    // Método para obter a chave secreta usada para assinar e validar tokens JWT
    // A chave é gerada a partir do segredo em base64 armazenado nas configurações
    // Retorna: SecretKey que será usada para assinar e validar tokens
    public SecretKey getKeySign() {
        // Decodifica o segredo de base64 para bytes
        // Isto é necessário porque a chave secreta deve estar em formato base64 para armazenamento seguro
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        // Cria e retorna a chave HMAC SHA usando os bytes decodificados
        // HMAC SHA é um algoritmo de assinatura digital seguro
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Método para gerar um token JWT (JSON Web Token) para um usuário autenticado
    // user: objeto UserDTO contendo os dados do usuário (id, nome, role)
    // Retorna: string contendo o token JWT assinado e codificado
    public String gerarToken(UserDTO user) {
        // Verifica se algum campo obrigatório está faltando
        // Todos os campos são necessários para gerar um token válido
        if(
            user.getId() == 0 || user.getId() == null ||
            user.getNome().equals("") ||
            user.getEmail().equals("") ||
            user.getRole().equals("")
        ) {
            // Lança uma exceção com status 400 (Bad Request) se algum campo está faltando
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), 
            "Um ou mais campos faltantes");
        }

        // Cria e retorna um novo token JWT com os dados do usuário
        return Jwts.builder()
                // Define o subject (sujeito) do token como o nome do usuário
                // O subject é quem o token representa
                .subject(user.getNome())
                // Adiciona um claim personalizado: id do usuário
                // Claims são dados customizados armazenados no token
                .claim("id", user.getId())
                // Adiciona um claim personalizado: nome do usuário
                .claim("nome", user.getNome())
                // Adiciona um claim personalizado: papel/função do usuário
                .claim("role", user.getRole())
                // Define a data e hora de emissão do token (quando foi criado)
                .issuedAt(new Date())
                // Define a data e hora de expiração do token
                // O token expira em 3.000.000 milissegundos (~50 minutos a partir da emissão)
                .expiration(new Date(System.currentTimeMillis() + 3000000))
                // Assina o token com a chave secreta para garantir sua integridade
                // Ninguém sem a chave secreta pode modificar o token sem detectarmos
                .signWith(this.getKeySign())
                // Compacta o token em formato JWT (header.payload.signature)
                .compact();
    }

    // Método para extrair os dados do usuário a partir de um token JWT válido
    // token: string contendo o token JWT assinado
    // Retorna: objeto UserDTO contendo os dados do usuário extraídos do token
    public UserDTO extrairClaim(String token) {
        // Faz o parsing (análise) do token JWT e obtém os claims (dados)
        // O parser valida a assinatura do token usando a chave secreta
        Claims claims = Jwts.parser()
                // Define a chave secreta para verificar a assinatura
                .verifyWith(this.getKeySign())
                .build()
                // Faz o parsing dos claims assinados do token
                .parseSignedClaims(token)
                // Obtém a carga útil (payload) do token contendo os claims
                .getPayload();

        // Cria um novo objeto UserDTO para armazenar os dados extraídos
        UserDTO user = new UserDTO();
        // Extrai o id do claim "id" e define no objeto UserDTO
        // O tipo Long é especificado para converter corretamente
        user.setId(claims.get("id", Long.class));
        // Extrai o nome do claim "nome"
        user.setNome(claims.get("nome", String.class));
        // Extrai o papel/função do claim "role"
        user.setRole(claims.get("role", String.class));
        // Retorna o objeto UserDTO com os dados do token
        return user;
    }
    
    // Método para validar se um token JWT é válido e não expirou
    // token: string contendo o token JWT a ser validado
    // Retorna: true se o token é válido e não expirou, false caso contrário
    public boolean validarToken(String token) {
        try {
            // Cria um parser JWT com a chave secreta para validação
            Jwts.parser()
                    // Define a chave secreta para verificar a assinatura
                    .setSigningKey(getKeySign())
                    .build()
                    // Analisa e valida o token
                    // Este método lança uma exceção se o token for inválido ou expirado
                    .parseClaimsJws(token);
            // Se chegou aqui sem exceção, o token é válido
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Se qualquer exceção ocorrer, o token é inválido, expirou ou está malformado
            // Exceções JwtException podem ser: ExpiredJwtException, UnsupportedJwtException, etc.
            return false;
        }
    }
}
    

