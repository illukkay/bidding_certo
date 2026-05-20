package com.bidding.system.bidding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Anotação que marca esta como a classe principal da aplicação Spring Boot
@SpringBootApplication
public class BiddingApplication {

	// Método principal que inicia a aplicação Spring Boot
	// args: argumentos de linha de comando passados para a aplicação
	public static void main(String[] args) {
		// Cria uma instância da aplicação e inicia o servidor
		// Esta linha inicializa todos os componentes Spring, configurações e abre a porta padrão (8080)
		SpringApplication.run(BiddingApplication.class, args);
	}

}
