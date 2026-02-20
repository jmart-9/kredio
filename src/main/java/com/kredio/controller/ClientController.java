// src/main/java/com/bdcopre/controller/ClientController.java
package com.kredio.controller;

import com.kredio.model.Client;
import com.kredio.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kredio.dto.ClientDTO;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:53248")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // src/main/java/com/bdcopre/controller/ClientController.java
    @GetMapping
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients(); // ← Debe devolver List<ClientDTO>
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client saved = clientService.createClient(client);
        return ResponseEntity.ok(saved);
    }
}