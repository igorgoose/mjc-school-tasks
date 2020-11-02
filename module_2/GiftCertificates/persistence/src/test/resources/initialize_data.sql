insert into tags
values (1, 'extreme');
insert into tags
values (2, 'sport');
insert into tags
values (3, 'spa');
insert into tags
values (4, 'relax');

insert into certificates
values (1, 'parachute jump', null, 50.00, current_timestamp(), current_timestamp(), 7);

insert into certificates
values (2, 'yoga class', null, 20.00, current_timestamp(), current_timestamp(), 14);

insert into certificates
values (3, 'helicopter flight', null, 50.00, current_timestamp(), current_timestamp(), 30);

insert into certificates
values (4, 'sauna', null, 50.00, current_timestamp(), current_timestamp(), 7);

insert into certificate_tags
values (1, 1, 1);

insert into certificate_tags
values (2, 2, 1);

insert into certificate_tags
values (3, 2, 2);

insert into certificate_tags
values (4, 4, 2);

insert into certificate_tags
values (5, 1, 3);

insert into certificate_tags
values (6, 3, 4);

insert into certificate_tags
values (7, 4, 4);
