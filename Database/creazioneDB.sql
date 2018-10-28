use 2Stream;

create table Utente(
    Username varchar(30) BINARY primary key,
    Password varchar(30) BINARY not null,
    Email varchar(50) not null,
    DataCreazione date not null,
    UltimoAccesso timestamp not null default CURRENT_TIMESTAMP,
    ImmagineProfilo mediumblob not null,
    Stato varchar(100) not null
);

create table Amministratore(
    Username varchar(30) BINARY primary key,
    Password varchar(30) BINARY not null,
    DataCreazione date not null
);

create table Preferenza(
    Utente varchar(30) references Utente(Username),
    ColoreTema varchar(7) not null,
    PrivacyAccesso boolean not null,
    PrivacyImmagine boolean not null,
    ImmagineSfondo mediumblob not null,
    Lingua varchar(30) not null,
    Font varchar(30) not null,
    primary key(Utente)
);

create table Chat(
    IDChat int(10) UNSIGNED primary key,
    DataCreazione date not null,
    UltimaModifica timestamp not null default CURRENT_TIMESTAMP
);

create table MessaggioTestuale(
    IDMessaggioTestuale int(100) UNSIGNED primary key,
    Mittente varchar(30) references Utente(Username),
    DataInvio timestamp not null default CURRENT_TIMESTAMP,
    Testo text not null
);

create table MessaggioMultimediale(
    IDMessaggioMultimediale int(100) UNSIGNED primary key,
    Mittente varchar(30) references Utente(Username),
    DataInvio timestamp not null default CURRENT_TIMESTAMP,
    Contenuto mediumblob not null
);

create table UtilizzareChat(
    Utente varchar(30) references Utente(Username),
    Chat int(10) references Chat(IDChat),
    primary key (Utente,Chat)
);

create table ContenereChatTestuale(
    Chat int(10) references Chat(IDChat),
    MessaggioTestuale int(100) references MessaggioTestuale(IDMessaggioTestuale),
    primary key (Chat,MessaggioTestuale)
);

create table ContenereChatMultimediale(
    Chat int(10) references Chat(IDChat),
    MessaggioMultimediale int(100) references MessaggioMultimediale(IDMessaggioMultimediale),
    primary key (Chat,MessaggioMultimediale)
);
