create table address
(
    id   bigserial not null primary key,
    street varchar(50)
);

create table client
(
    id   bigserial not null primary key,
    name varchar(50),
    login varchar(50),
    password varchar(50),
    address_id bigint
);

alter table client add foreign key (address_id) references address(id);

create table phone
(
    id   bigserial not null primary key,
    number varchar(50),
    client_id bigint not null
);

alter table phone add foreign key (client_id) references client(id);
