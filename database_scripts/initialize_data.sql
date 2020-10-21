insert into tags(name) values ("extreme");
insert into tags(name) values ("sport");
insert into tags(name) values ("spa");
insert into tags(name) values ("relax");

insert into certificates(name, price, create_date, last_update_date, duration)
values ("parachute jump", 50.00, current_timestamp(), current_timestamp(), 7);

insert into certificates(name, price, create_date, last_update_date, duration)
values ("yoga class", 20.00, current_timestamp(), current_timestamp(), 14);

insert into certificates(name, price, create_date, last_update_date, duration)
values ("helicopter flight", 50.00, current_timestamp(), current_timestamp(), 30);

insert into certificates(name, price, create_date, last_update_date, duration)
values ("sauna", 50.00, current_timestamp(), current_timestamp(), 7);

insert into certificate_tags(tag_id, certificate_id)
values (1, 1);

insert into certificate_tags(tag_id, certificate_id)
values (2, 1);

insert into certificate_tags(tag_id, certificate_id)
values (2, 2);

insert into certificate_tags(tag_id, certificate_id)
values (4, 2);

insert into certificate_tags(tag_id, certificate_id)
values (1, 3);

insert into certificate_tags(tag_id, certificate_id)
values (3, 4);

insert into certificate_tags(tag_id, certificate_id)
values (4, 4);
