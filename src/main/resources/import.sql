alter table chat drop constraint fk71ctmia4b6l13pw5cmf5kl04f;

alter table chat add constraint fk71ctmia4b6l13pw5cmf5kl04f foreign key (admin_id) references users on update cascade on delete set null;

alter table chat drop constraint fkl429jfa5hye9qdps1xmjt4a0s;

alter table chat add constraint fkl429jfa5hye9qdps1xmjt4a0s foreign key (owner_id) references users on update cascade on delete cascade;

alter table chat_members drop constraint fkcjnigrgwbin0pdph6mdphhp6i;

alter table chat_members add constraint fkcjnigrgwbin0pdph6mdphhp6i foreign key (chat_id) references chat on update cascade on delete cascade;

alter table chat_members drop constraint fkcjnigrgwbin0pdph6mdphhp6i;

alter table chat_members add constraint fkcjnigrgwbin0pdph6mdphhp6i foreign key (chat_id) references chat on update cascade on delete cascade;

alter table chat_moderators drop constraint fk8u059kkiwqiub61fxi4kq0xu5;

alter table chat_moderators add constraint fk8u059kkiwqiub61fxi4kq0xu5 foreign key (chat_id) references chat on update cascade on delete cascade;

alter table chat_moderators drop constraint fk8u059kkiwqiub61fxi4kq0xu5;

alter table chat_moderators add constraint fk8u059kkiwqiub61fxi4kq0xu5 foreign key (chat_id) references chat on update cascade on delete cascade;

alter table message drop constraint fkmejd0ykokrbuekwwgd5a5xt8a;

alter table message add constraint fkmejd0ykokrbuekwwgd5a5xt8a foreign key (chat_id) references chat on update cascade on delete cascade;


