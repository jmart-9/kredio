// src/main/java/com/bdcopre/service/ClientService.java
package com.kredio.service;

import com.kredio.dto.ClientDTO;
import com.kredio.model.Client;
import com.kredio.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(Client client) {
        // Asignar tenant por defecto si no viene (para MVP; en prod usar TenantContext)
        if (client.getTenantId() == null) {
            client.setTenantId(1L);  // Cambia por lógica real (JWT o default tenant)
        }
        return clientRepository.save(client);
    }

    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(client -> new ClientDTO(
                        client.getId(),
                        client.getNombre(),
                        client.getTelefono(),
                        client.getDireccion(),
                        client.getEmail()
                ))
                .collect(Collectors.toList());
    }

    // Método adicional útil (opcional por ahora)
    public List<ClientDTO> getClientsByTenant(Long tenantId) {
        // Implementar cuando tengas filtro multi-tenant
        return getAllClients(); // placeholder
    }
}