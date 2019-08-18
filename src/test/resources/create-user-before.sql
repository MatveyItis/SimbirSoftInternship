delete
from user_role;
delete
from users;

insert into users (id, login, password)
values (1, 'admin', '$2a$10$df6y94MH6hXHccPus3lR3uKrJBTKpDrHSBakJTWKv3./WeefRzFH6'),
       (2, 'danis', '$2a$10$pry.yZ4YZppIwUd9J/Q/CuI34XhCTLP38CpdwxEDBu6rUMLg41Fuy');

insert into user_role (user_id, roles)
values (1, 'ADMIN'),
       (2, 'USER');