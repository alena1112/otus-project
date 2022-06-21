package ru.otus.controllers.dto;

import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientDto {
    private String id;
    private String name;
    private String address;
    private String phone;

    public ClientDto() {
    }

    public ClientDto(Long id) {
        this.id = String.valueOf(id);
    }

    public ClientDto(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public ClientDto(Client client, Address address, String phone) {
        this.id = String.valueOf(client.getId());
        this.name = client.getName();
        this.address = address != null ? address.getStreet() : null;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getPhonesAsList() {
        return phone != null ? Arrays.asList(phone.split(",")) : new ArrayList<>();
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
