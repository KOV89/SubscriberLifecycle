create database subscriberlifecycle collate utf8_general_ci;

use subscriberlifecycle;

create table rates
(
    id           bigint auto_increment
        primary key,
    call_limit   int          not null,
    cost_call    float        not null,
    cost_message float        not null,
    title        varchar(255) not null
);

create table subscribers
(
    msisdn   bigint       not null,
    active   bit          not null,
    balance  float        not null,
    enabled  bit          not null,
    name     varchar(255) not null,
    password varchar(255) not null,
    surname  varchar(255) not null,
    rate_id  bigint       not null,
    primary key (msisdn),
    constraint FKt950wdjyfji0b2b887ognbi25
        foreign key (rate_id) references rates (id)
);

create table subscribers_role
(
    subscribers_msisdn bigint       not null,
    roles              varchar(255) null,
    constraint FKpjnd4o073q2na3ynbt7f7t1fv
        foreign key (subscribers_msisdn) references subscribers (msisdn)
);

create table calls
(
    id               bigint auto_increment
        primary key,
    date             date   null,
    time             time   null,
    recipient_msisdn bigint not null,
    sender_msisdn    bigint not null,
    constraint FK83sseny7o6eni2igoaps4lt1p
        foreign key (recipient_msisdn) references subscribers (msisdn),
    constraint FKsi9q7ul0pyfocl6ajyvyxx1eu
        foreign key (sender_msisdn) references subscribers (msisdn)
);

create table messages
(
    id               bigint auto_increment
        primary key,
    text             varchar(255) null,
    time             datetime     null,
    recipient_msisdn bigint       not null,
    sender_msisdn    bigint       not null,
    constraint FKhmu7hcbr4immg80tdq3adbd7f
        foreign key (recipient_msisdn) references subscribers (msisdn),
    constraint FKt9hognl73tg6f1wrvfv7w06wf
        foreign key (sender_msisdn) references subscribers (msisdn)
);

INSERT INTO `rates` (`id`, `call_limit`, `cost_call`, `cost_message`, `title`) 
VALUES (NULL, '5', '0.8', '0.3', 'Base'),
	(NULL, '5', '0.5', '0.5', 'All for 0.5'),
	(NULL, '5', '1', '1', 'All for 1');

INSERT INTO `subscribers` (`msisdn`, `active`, `balance`, `enabled`, `name`, `password`, `surname`, `rate_id`) 
VALUES ('89111111111', b'1', '0.32', b'1', 'Ivan', '1111', 'Ivanov', '1'),
	('89222222222', b'1', '55', b'1', 'Oleg', '2222', 'Olegov', '2'),
	('89333333333', b'1', '100', b'1', 'Petr', '3333', 'Petrov', '3'),
	('89444444444', b'0', '-32', b'1', 'Egor', '4444', 'Egorov', '1');

INSERT INTO `subscribers_role` (`subscribers_msisdn`, `roles`) 
VALUES ('89111111111', 'ROLE_USER'),
	('89222222222', 'ROLE_USER'),
	('89333333333', 'ROLE_USER'),
	('89444444444', 'ROLE_USER');

INSERT INTO `messages` (`id`, `text`, `time`, `recipient_msisdn`, `sender_msisdn`) 
VALUES (NULL, 'Message from Ivan to Oleg', '2019-10-02 11:11:11', '89111111111', '89222222222'),
	(NULL, 'Message from Ivan to Petr', '2019-10-02 12:12:12', '89111111111', '89333333333'),
	(NULL, 'Message from Ivan to Egor', '2019-10-02 13:13:13', '89111111111', '89444444444'),
	(NULL, 'Message from Oleg to Ivan', '2019-10-02 14:14:14', '89222222222', '89111111111'),
	(NULL, 'Message from Oleg to Egor', '2019-10-02 15:15:15', '89222222222', '89333333333');

INSERT INTO `calls` (`id`, `date`, `time`, `recipient_msisdn`, `sender_msisdn`) 
VALUES (NULL, '2019-11-02', '11:11:11', '89111111111', '89222222222'),
	(NULL, '2019-11-02', '12:12:12', '89111111111', '89333333333'),
	(NULL, '2019-11-02', '13:13:13', '89111111111', '89444444444'),
	(NULL, '2019-11-02', '14:14:14', '89222222222', '89111111111'),
	(NULL, '2019-11-02', '15:15:15', '89222222222', '89333333333');
