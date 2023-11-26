package sky.pro.Animals.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.service.ClientServiceImpl;
import sky.pro.Animals.service.InfoServiceImpl;

import java.util.Collection;

/**
 * Controller for CRUD operations with clients data
 * <p>
 * <hr>
 * <p>
 * Контроллер для CRUD операций с данными клиентов
 */
@RestController
@RequestMapping(path = "/client")
public class ClientController {
    private final ClientServiceImpl clientService;
    private final InfoServiceImpl infoService;

    public ClientController(ClientServiceImpl clientService, InfoServiceImpl infoService) {
        this.clientService = clientService;
        this.infoService = infoService;
    }

    @GetMapping(path = "/get")
    public ResponseEntity<Collection<Client>> getAllClients() {
        infoService.checkInfo();
        Collection<Client> clients = clientService.getAll();
        if (clients == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(clients);
        }
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        infoService.checkInfo();
        Client client = clientService.getById(id);
        if (client == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(client);
        }
    }

    @PostMapping(path = "/write")
    public ResponseEntity<Client> writeClient(@RequestParam Client client) {
        infoService.checkInfo();
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
