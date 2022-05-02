package ru.otus.servlet;

public class ClientDto {
    private final String name;
    private final String address;
    private final String phone;

    public ClientDto(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
