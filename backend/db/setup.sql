CREATE DATABASE blockit CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
create user 'blockit' identified by 'blockit';
GRANT all on blockit.* to blockit;
use blockit;

create table blocklist_registry
(
    id      bigint       not null primary key auto_increment,
    active  bit          null,
    url     varchar(255) null,
    version bigint       null,
    constraint UK8vokk572tlthssbi5duq2vw2k
        unique (url)
);

create table blocklist_data
(
    id                    bigint   not null primary key auto_increment,
    blocklist_registry_id bigint   not null,
    entry                 longblob null,
    constraint FK9yuvisphoiu4ihg9tj0j7ld4m
        foreign key (blocklist_registry_id) references blocklist_registry (id) on DELETE CASCADE,
    constraint UK8vokk572tsadfsbi5duq2vw2k unique (blocklist_registry_id)
);

create table configuration
(
    id                bigint        not null primary key auto_increment,
    conf_key          varchar(255)  not null,
    conf_key_provider varchar(255)  not null,
    conf_value        varchar(2048) null,
    constraint UKconfkeyconstraint unique (conf_key, conf_key_provider, conf_value)
);

create table unbound_stats_data
(
    id            bigint   not null primary key auto_increment,
    creation_date datetime null,
    version       bigint   null
);

create table unbound_stats_point
(
    id                    bigint        not null primary key auto_increment,
    unbound_stats_data_id bigint        not null,
    stats_key             varchar(255)  not null,
    stats_value           varchar(2048) null,
    constraint FKstats_point foreign key (unbound_stats_data_id) references unbound_stats_data (id) on delete cascade
);

