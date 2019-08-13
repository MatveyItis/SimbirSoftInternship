create table if not exists users(id       bigserial    not null constraint users_pkey primary key,login    varchar(255) not null constraint uk_ow0gan20590jrb00upg3va2fn unique,password varchar(255) not null);

create table if not exists chat(id                bigserial not null constraint chat_pkey primary key,created_chat_date timestamp,chat_name         varchar(255) unique, chat_type         varchar(255),admin_id          bigint references users,owner_id          bigint references users);

create table if not exists chat_members(chat_id    bigint not null references chat,members_id bigint not null references users,constraint chat_members_pkey primary key (chat_id, members_id));

create table if not exists chat_moderators(chat_id       bigint not null references chat,moderators_id bigint not null references users,constraint chat_moderators_pkey primary key (chat_id, moderators_id));

create table if not exists message(id           bigserial     not null constraint message_pkey primary key references message,date_time    timestamp,text         varchar(1024) not null,message_type varchar(255),chat_id      bigint references chat, sender_id    bigint references users);

create table if not exists user_role(user_id bigint not null references users,roles   varchar(255));

create table if not exists spring_session(primary_id            char(36) not null constraint spring_session_pk primary key,session_id            char(36) not null,creation_time         bigint   not null,last_access_time      bigint   not null,max_inactive_interval integer  not null,expiry_time           bigint   not null,principal_name        varchar(100));

create unique index if not exists spring_session_ix1 on spring_session (session_id);

create index if not exists spring_session_ix2 on spring_session (expiry_time);

create index if not exists spring_session_ix3 on spring_session (principal_name);

create table if not exists spring_session_attributes(session_primary_id char(36)     not null constraint spring_session_attributes_fk references spring_session on delete cascade,attribute_name     varchar(200) not null,attribute_bytes    bytea        not null,constraint spring_session_attributes_pk primary key (session_primary_id, attribute_name));

create table if not exists ban_list( id             bigserial not null constraint ban_list_pkey primary key,begin_of_ban   timestamp,end_of_ban     timestamp, banned_user_id bigint constraint fkn5trglbbhk2ccu0bpisdidmub references users);

CREATE TABLE if not exists spring_session(primary_id            CHAR(36) NOT NULL CONSTRAINT spring_session_pk PRIMARY KEY,session_id            CHAR(36) NOT NULL,creation_time         BIGINT   NOT NULL, last_access_time      BIGINT   NOT NULL,max_inactive_interval INTEGER  NOT NULL, expiry_time           BIGINT   NOT NULL,principal_name        VARCHAR(300));

CREATE UNIQUE INDEX if not exists spring_session_ix1   ON spring_session (session_id);

CREATE INDEX if not exists spring_session_ix2    ON spring_session (expiry_time);

CREATE INDEX if not exists spring_session_ix3    ON spring_session (principal_name);

CREATE TABLE if not exists spring_session_attributes( session_primary_id CHAR(36) NOT NULL CONSTRAINT spring_session_attributes_fk REFERENCES spring_session ON DELETE CASCADE, attribute_name     VARCHAR(200) NOT NULL, attribute_bytes    BYTEA        NOT NULL, CONSTRAINT spring_session_attributes_pk PRIMARY KEY (session_primary_id, attribute_name));
alter table chat drop constraint fk71ctmia4b6l13pw5cmf5kl04f;
alter table chat add constraint fk71ctmia4b6l13pw5cmf5kl04f foreign key (admin_id) references users on update cascade on delete set null;

alter table chat drop constraint fkl429jfa5hye9qdps1xmjt4a0s;
alter table chat add constraint fkl429jfa5hye9qdps1xmjt4a0s foreign key (owner_id) references users on update cascade on delete cascade;

alter table chat_members drop constraint fkcjnigrgwbin0pdph6mdphhp6i;
alter table chat_members add constraint fkcjnigrgwbin0pdph6mdphhp6i foreign key (chat_id) references chat on update cascade on delete cascade;

alter table chat_moderators drop constraint fk8u059kkiwqiub61fxi4kq0xu5;
alter table chat_moderators add constraint fk8u059kkiwqiub61fxi4kq0xu5 foreign key (chat_id) references chat on update cascade on delete cascade;

alter table message drop constraint fkmejd0ykokrbuekwwgd5a5xt8a;
alter table message add constraint fkmejd0ykokrbuekwwgd5a5xt8a foreign key (chat_id) references chat on update cascade on delete cascade;

alter table chat_moderators drop constraint fkscbehcbqsrhh9xuh06ilty8x2;
alter table chat_moderators add constraint fkscbehcbqsrhh9xuh06ilty8x2 foreign key (moderators_id) references users on update cascade on delete cascade;

alter table chat_moderators drop constraint fk8u059kkiwqiub61fxi4kq0xu5;
alter table chat_moderators add constraint fk8u059kkiwqiub61fxi4kq0xu5 foreign key (chat_id) references chat on update cascade on delete cascade;

alter table chat_members drop constraint fkejh3xk8tktifvk858ypgtqft5;
alter table chat_members add constraint fkejh3xk8tktifvk858ypgtqft5 foreign key (members_id) references users on update cascade on delete cascade;

alter table chat_members drop constraint fkcjnigrgwbin0pdph6mdphhp6i;
alter table chat_members add constraint fkcjnigrgwbin0pdph6mdphhp6i foreign key (chat_id) references chat on update cascade on delete cascade;

alter table message drop constraint fkbi5avhe69aol2mb1lnm6r4o2p;
alter table message add constraint fkbi5avhe69aol2mb1lnm6r4o2p foreign key (sender_id) references users on update cascade on delete cascade;
