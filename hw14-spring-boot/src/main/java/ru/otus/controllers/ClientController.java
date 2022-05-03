package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.crm.service.ClientDao;

import java.util.List;

@Controller
public class ClientController {
    private final ClientDao clientDao;

    public ClientController(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @GetMapping({"/", "/api/client"})
    public String getAllClients(Model model) {
        List<ClientDto> clientDtos = clientDao.findAll();
        model.addAttribute("clients", clientDtos);
        return "clients";
    }

    @PostMapping("/api/client")
    public String saveClient(@RequestBody ClientDto clientDto) {
        clientDao.saveClient(clientDto);
        return "clients";
    }
}
