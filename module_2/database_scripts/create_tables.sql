create database gift_certificates;

use gift_certificates;

create table tags(
	id int primary key auto_increment,
    name varchar(100) not null unique
);

create table certificates(
	id int primary key auto_increment,
    name varchar(100) not null unique,
    description varchar(255),
    price decimal(10, 2),
    create_date timestamp,
    last_update_date timestamp,
    duration int
);

create table certificate_tags(
	id int primary key auto_increment,
    certificate_id int not null,
    tag_id int not null,
    foreign key(certificate_id) references certificates(id) 
    on delete cascade
    on update cascade,
    foreign key(tag_id) references tags(id)
    on delete cascade
    on update cascade
);
