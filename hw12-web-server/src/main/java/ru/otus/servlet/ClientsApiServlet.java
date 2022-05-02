package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.ClientDao;

import java.io.IOException;
import java.util.List;


public class ClientsApiServlet extends HttpServlet {
    private final ClientDao clientDao;
    private final Gson gson;

    public ClientsApiServlet(ClientDao clientDao, Gson gson) {
        this.clientDao = clientDao;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Client client = extractClientFromRequest(request);
            client = clientDao.saveClient(client);
            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            out.print(String.format("Клиент %s успешно создан", client.getName()));
        } catch (Exception e) {
            ServletOutputStream out = response.getOutputStream();
            out.print(String.format("Ошибка! %s", e.getMessage()));
        }
    }

    private Client extractClientFromRequest(HttpServletRequest request) throws IOException {
        var clientDto = gson.fromJson(request.getReader(), ClientDto.class);
        return new Client(clientDto.getName(),
                clientDto.getAddress() != null && !clientDto.getAddress().isEmpty() ? new Address(clientDto.getAddress()) : null,
                clientDto.getPhone() != null && !clientDto.getPhone().isEmpty() ? List.of(new Phone(clientDto.getPhone())) : null);
    }
}
