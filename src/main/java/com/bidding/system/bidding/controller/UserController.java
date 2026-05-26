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

@RestController
@RequestMapping("/api/autenticar")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registrar")
    public String registrar(@RequestBody UserDTO user) {
        userService.register(user);
        return "Cadastro realizado com sucesso!";
    }

    @PostMapping("/logar")
    public String logar(@RequestBody UserRequestDTO user) {
        return userService.logar(user);
    }
}