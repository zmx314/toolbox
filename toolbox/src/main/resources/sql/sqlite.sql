drop table if exists company;
create table company (
    id integer primary key autoincrement,
    name text not null unique,
    full_name text);

drop table if exists department;
create table department (
    id integer primary key autoincrement,
    name text not null unique);

drop table if exists section;
create table section (
    id integer primary key autoincrement,
    name text not null unique);

drop table if exists position;
create table position (
    id integer primary key autoincrement,
    name text not null unique);

drop table if exists attn_group;
create table attn_group (
    id integer primary key autoincrement,
    name text not null unique,
    date_cell text not null default 'A2',
    start_cell text not null default 'B6',
    duty_cell text not null default 'A1',
    time_cell text not null default 'A1',
    man_hour_cell text not null default 'A1',
    off_cell text not null default 'A1'
);

drop table if exists employee;
create table employee (
    id text primary key,
    name text not null,
    company_id integer not null default 0,
    company_name text not null default '',
    department_id integer not null default 0,
    department_name text not null default '',
    position_id integer not null default 0,
    position_name text not null default '',
    attn_group_id integer not null default 0,
    attn_group_name text not null default '',
    attn_officer text not null default 'N'
) ;
-- alter table employee save column attn_officer text not null default 'N';
drop table if exists keyword;
create table keyword (
    id integer primary key autoincrement ,
    key text not null,
    full_name text not null,
    constraint unique_key unique (key,full_name)
);

drop table if exists shipping_group;
create table shipping_group (
    id integer primary key autoincrement ,
    name text unique not null,
    type text not null default '单去',
    company_separate text not null default '是'
);

drop table if exists shipping_company;
create table shipping_company (
    id integer primary key autoincrement ,
    name text unique not null,
    group_id int not null ,
    group_name text not null ,
    type text not null default '去程'
);
drop table if exists round_trip_price;
create table round_trip_price (
    id integer primary key autoincrement,
    shipping_company_name text not null,
    ticket_type text not null,
    start_date  text not null,
    end_date text not null,
    price text not null
);


