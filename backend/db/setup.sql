drop database blockit;
drop user blockit;

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

/*create table blocklist_data
(
    id                    bigint       not null primary key auto_increment,
    blocklist_registry_id bigint       not null,
    entry                 varchar(255) null,
    constraint FK9yuvisphoiu4ihg9tj0j7ld4m
        foreign key (blocklist_registry_id) references blocklist_registry (id) on DELETE CASCADE
);*/

create table blocklist_data
(
    id                    bigint       not null primary key auto_increment,
    blocklist_registry_id bigint       not null,
    entry                 longblob null,
    constraint FK9yuvisphoiu4ihg9tj0j7ld4m
        foreign key (blocklist_registry_id) references blocklist_registry (id) on DELETE CASCADE,
        constraint UK8vokk572tsadfsbi5duq2vw2k unique (blocklist_registry_id)
);

delete
from blocklist_registry;
delete
from blocklist_data;

select count(1)
from blockit.blocklist_data;