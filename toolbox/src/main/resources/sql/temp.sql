drop table if exists sex;
create table sex (
    id integer primary key autoincrement,
    name text not null unique);

insert into sex (name) values ('男');
insert into sex (name) values ('女');

select * from sex;

drop table if exists doc_type;
create table doc_type (
    id integer primary key autoincrement,
    name text not null unique);

insert into doc_type (name) values ('中国居民身份证');
insert into doc_type (name) values ('港澳居民来往内地通行证');
insert into doc_type (name) values ('台湾居民来往大陆通行证');
insert into doc_type (name) values ('护照');
insert into doc_type (name) values ('外国人永久居留身份证');
insert into doc_type (name) values ('港澳台居民居住证');
insert into doc_type (name) values ('其他');

select * from doc_type;


drop table if exists user;
create table user(
    id integer primary key autoincrement,
    normal_account text not null default '',
    phone_account text not null default '',
    email_account text not null default '',
    pwd text not null default '888888',
    name text not null default '',
    doc_type_id int not null default 0,
    doc_num text not null default '',
    sex_id int not null default 1,
    phone text not null default '',
    address text not null default '',
    constraint fk_doc_type foreign key (doc_type_id) references doc_type(id),
    constraint fk_sex foreign key (sex_id) references sex(id)
);

drop table if exists employee;
create table employee (
    id integer primary key autoincrement ,
    user_id int not null,
    name text not null,
    company_id int not null default 0,
    department_id int not null default 0,
    position_id int not null default 0,
    attn_group_id int not null default 0,
    constraint fk_company foreign key (company_id) references company(id),
    constraint fk_department foreign key (department_id) references department(id),
    constraint fk_position foreign key (position_id) references position(id),
    constraint fk_attn_group foreign key (attn_group_id) references attn_group(id)
) ;

alter table COMPANY add full_name text;