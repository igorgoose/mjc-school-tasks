create database gift_certificates;

use gift_certificates;

create table tags(
	id int primary key auto_increment,
    name varchar(100) not null unique
);

create table certificates(
	id int primary key auto_increment,
    name varchar(100) not null,
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
    foreign key(certificate_id) references certificates(id),
    foreign key(tag_id) references tags(id)
);
