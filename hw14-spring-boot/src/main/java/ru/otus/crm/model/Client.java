package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("client")
public class Client {
    @Id
    private Long id;

    private String name;

    private Long addressId;

    public Client(String name, Long addressId) {
        this.name = name;
        this.addressId = addressId;
    }

    public Client(String name) {
        this.name = name;
    }

    @PersistenceConstructor
    public Client(Long id, String name, Long addressId) {
        this.id = id;
        this.name = name;
        this.addressId = addressId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addressId=" + addressId +
                '}';
    }
}
