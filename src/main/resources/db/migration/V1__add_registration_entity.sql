CREATE TABLE "User"
(
    id         long,
    FirstName  varchar(25),
    LastName   varchar(50),
    SecondName varchar(25),
    Email      varchar(10),
    UserName   varchar(20),
    Password   varhcar(15),
    Rating     int,
    CreatedAt  timestap,
    role       int
)

CREATE TABLE "UserAddress"
(
    id            long,
    IdUser        long references "User" (id),
    AddrIndex     varchar(6),
    AddrCity      varchar(15),
    AddrStreet    varchar(25),
    AddrHouse     varchar(5),
    AddrStructure varchar(10),
    AddrApart     varchar(3),
    IsDefault     boolean
)

CREATE TABLE "ConfirmToken"
(
    id       long,
    idUser   long references "User" (id),
    expireAt timestap,
    token    UUID
)

CREATE TABLE "File"
(
    id   long,
    path varchar(255),
    url  varchar(255)
)

CREATE TABLE "Role"(
    id long,
    name varchar(255)
)