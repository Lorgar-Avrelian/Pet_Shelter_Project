package sky.pro.Animals.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.service.ClientServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping(path = "/client")
public class ClientController {
    private final ClientServiceImpl clientService;

    public ClientController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @GetMapping(path = "/get")
    public ResponseEntity<Collection<Client>> getAllClients() {
        Collection<Client> clients = clientService.getAll();
        if (clients == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(clients);
        }
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        Client client = clientService.getById(id);
        if (client == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(client);
        }
    }

    @PostMapping(path = "/write")
    public ResponseEntity<Client> writeClient(@RequestParam Client client) {
        Client savedClient = clientService.save(client);
        if (savedClient == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(savedClient);
        }
    }

    @PutMapping(path = "/edit/{id}")
    public ResponseEntity<Client> editClient(@PathVariable Long id, @RequestParam Client client) {
        Client editedClient = clientService.save(client);
        if (editedClient == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(editedClient);
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Client> deleteClient(@PathVariable Long id) {
        Client deletedClient = clientService.delete(id);
        if (deletedClient == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(deletedClient);
        }
    }
}
