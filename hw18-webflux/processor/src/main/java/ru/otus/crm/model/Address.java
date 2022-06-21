package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
public class Address {
    @Id
    private Long id;

    private String street;

    @MappedCollection(idColumn = "address_id")
    private Client client;

    public Address(String street) {
        this.street = street;
    }

    @PersistenceConstructor
    public Address(Long id, String street, Client client) {
        this.id = id;
        this.street = street;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", client=" + client +
                '}';
    }
}
