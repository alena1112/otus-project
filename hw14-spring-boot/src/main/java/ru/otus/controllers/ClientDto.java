package ru.otus.controllers;

import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientDto {
    private String id;
    private String name;
    private String address;
    private String phone;

    public ClientDto() {
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
        return phone != null ? List.of(phone.split(",")) : new ArrayList<>();
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
