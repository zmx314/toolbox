show databases ;
/*创建数据库*/
drop database if exists toolbox;
create database if not exists toolbox;
/*切换数据库*/
use toolbox;
/*用户表*/
-- char的存取数度还是要比varchar要快得多，因为其长度固定，方便程序的存储与查找
-- char的存储方式是，对英文字符（ASCII）占用1个字节，对一个汉字占用两个字节
-- varchar的存储方式是，对每个英文字符占用2个字节，汉字也占用2个字节
drop table if exists user;
create table if not exists `user` (
    `id` smallint unsigned auto_increment,
    `normal_id` char(20) not null default '',
    `phone_id` char(13) not null default '',
    `mail_id` varchar(254) not null default '',
    `password` char(32) not null default '888888',
    `gender` char(1) not null default '',
    `birth` date not null default '1900-01-01',
    `document_type` tinyint not null default 0,
    `document_num` char(18) not null default '',
    `real_name` varchar(40) not null default '',
    `question1` varchar(20) not null default '',
    `answer1` varchar(40) not null default '',
    `question2` varchar(20) not null default '',
    `answer2` varchar(40) not null default '',
    `question3` varchar(20) not null default '',
    `answer3` varchar(40) not null default '',
    `opt_time` datetime not null default now(),
    primary key (`id`)
) engine=InnoDB default charset=utf8mb4;

-- alter table user modify column `opt_time` datetime not null default now();

/*公司表*/
drop table if exists `company`;
create table if not exists `company`(
`id` smallint unsigned auto_increment,
`short_name` char(10) unique not null default '',
`full_name` char(30) unique not null default '',
primary key (`id`)
)engine=InnoDB default charset=utf8mb4;
/*部门表*/
drop table if exists `department`;
create table if not exists `department`(
`id` smallint unsigned auto_increment,
`name` char(20) unique not null,
primary key (`id`)
)engine=InnoDB default charset=utf8mb4;
/*职位表*/
drop table if exists `position`;
create table if not exists `position`(
`id` smallint unsigned auto_increment,
`name` char(20) unique not null,
primary key (`id`)
)engine=InnoDB default charset=utf8mb4;
/*考勤分组表*/
drop table if exists `attn_group`;
create table `attn_group` (
`id` tinyint unsigned primary key auto_increment,
`name` char(30) not null default '',
`date_cell` char(10) not null default 'A2',
`start_cell` char(10) not null default 'B6',
`duty_cell` char(10) not null default 'A1',
`time_cell` char(10) not null default 'A1',
`man_hour_cell` char(10) not null default 'A1',
`off_cell` char(10) not null default 'A1'
)engine=InnoDB default charset=utf8mb4;
/*员工表*/
drop table if exists `employee`;
create table `employee` (
`user_id` smallint unsigned,
`employee_name` char(30) not null default '', -- 作为雇员的名字
`company_id` smallint unsigned not null default 0,
`department_id` smallint unsigned not null default 0,
`position_id` smallint unsigned not null default 0,
constraint `fk_user_id` foreign key (user_id) references `user`(`id`),
constraint `fk_company_id` foreign key (company_id) references `company`(`id`),
constraint `fk_department_id` foreign key (department_id) references `department`(`id`),
constraint `fk_position_id` foreign key (position_id) references `position`(`id`),
constraint primary key (`user_id`,`company_id`,`department_id`,`position_id`)
)engine=InnoDB default charset=utf8mb4;
alter table `employee` add column `is_attn_officer` enum('Y','N') default 'N' after `position_id`;

select a.company_id,c.full_name,a.department_id,d.name,a.position_id,p.name,b.id,b.normal_id,b.real_name,a.is_attn_officer
from employee a
    left join user b on a.user_id=b.id
    left join company c on a.company_id = c.id
left join department d on a.department_id = d.id
left join toolbox.position p on p.id = a.position_id;

/*港口表*/
drop table if exists `port`;
create table if not exists `port` (
`id` smallint unsigned auto_increment, -- 港口ID
`name` char(20) not null default '', -- 港口名
primary key (`id`) -- 主键约束
) engine=InnoDB default charset=utf8mb4;

/*业务单元表*/
drop table if exists `business_unit`;
create table if not exists `business_unit` (
`id` smallint unsigned auto_increment, -- 业务单元ID
`name` char(40) unique not null default '', -- 业务单元名
primary key (`id`) -- 主键约束
) engine=InnoDB default charset=utf8mb4;

/*航线组类型表*/
drop table if exists `route_group_type`;
create table if not exists `route_group_type` (
`id` smallint unsigned auto_increment, -- 航线组类型ID
`name` char(40) unique not null default '', -- 航线组类型名
primary key (`id`) -- 主键约束
) engine=InnoDB default charset=utf8mb4;
/*航线组表*/
drop table if exists `route_group`;
create table if not exists `route_group` (
`id` smallint unsigned auto_increment, -- 航线组ID
`name` char(40) unique not null default '', -- 航线组名
`port_id` smallint unsigned not null, -- 港口ID
`business_unit_id` smallint unsigned not null, -- 业务单元ID
`route_group_type_id` smallint unsigned not null, -- 航线组类型ID
primary key (`id`), -- 主键约束
constraint `fk_port_id` foreign key(`port_id`) references `port` (`id`), -- 引用港口表的id列
constraint `fk_business_unit_id` foreign key(`business_unit_id`) references `business_unit` (`id`), -- 引用业务单元表的 id列
constraint `fk_route_group_type_id` foreign key(`route_group_type_id`) references `route_group_type` (`id`) -- 引用航线组类型表的id列
) engine=InnoDB default charset=utf8mb4;
/*show create table tbl_name;*/
/*alter table table_name add constraint `constraint_name` foreign key(`foreign_key_column_name` references `reference_table_name` (`reference_table_column`))*/
/*alter table table_name drop foreign key `foreign_key_name`;*/
/*航线表*/
drop table if exists `route`;
create table if not exists `route` (
`id` smallint unsigned auto_increment, -- 航线ID
`name` char(40) unique not null default '', -- 航线名
`route_group_id` smallint unsigned default 0, -- 航线组类型id
primary key (`id`),
constraint `fk_route_group_id` foreign key(`route_group_id`) references `route_group` (`id`) -- 引用航线组类型表的id列
) engine=InnoDB default charset=utf8mb4;

drop table if exists `shipping_company`;
create table if not exists `shipping_company`(
`id` smallint unsigned auto_increment,
`name` char(40) unique not null default '',
primary key (`id`)
) engine=InnoDB default charset=utf8mb4;
/*船舱等级表*/
drop table if exists `ship_class`;
create table if not exists `ship_class` (
`id` smallint unsigned auto_increment,
`name` char(8) not null default '',
primary key (`id`)
) engine=InnoDB default charset=utf8mb4;
/*证件类型表*/
drop table if exists `doc_type`;
create table if not exists `doc_type` (
`id` smallint unsigned auto_increment,
`name` char(10) not null default '',
primary key (`id`)
) engine=InnoDB default charset=utf8mb4;
/*出港结算单表*/
drop table if exists `depart_statement`;
create table if not exists `depart_statement` (
`depart_date` date not null,
`depart_time` time not null,
`route_id` smallint unsigned not null,
`seating_capacity` smallint not null,
`pax_num`smallint not null default 0,
`tkt_num`smallint not null default 0,
`hand_made_tkt_num`smallint not null default 0,
`shipping_company_id` smallint unsigned not null,
primary key (`depart_date`,`depart_time`,`route_id`)
) engine=InnoDB default charset=utf8mb4;
/*alter table `depart_statement` change  column `depart_date` `depart_date` date not null;*/
/*每日出港数据表*/
drop table if exists `depart_daily_data`;
create table if not exists  `depart_daily_data`(
`depart_date` date not null, 
`route_id` smallint unsigned not null,
`shipping_company_id` smallint unsigned not null,
`ferry_num` smallint not null default 0,
`seating_capacity` smallint not null default 0,
`pax_num`smallint not null default 0,
`tkt_num`smallint not null default 0,
`hand_made_tkt_num`smallint not null default 0,
primary key (`depart_date`,`route_id`,`shipping_company_id`)
) engine=InnoDB default charset=utf8mb4;

drop table if exists `depart_monthly_data`;
create table if not exists  `depart_monthly_data`(
`depart_year_month` date not null,
`route_id` smallint unsigned not null,
`shipping_company_id` smallint unsigned not null,
`ferry_num` mediumint not null default 0,
`seating_capacity` int not null default 0,
`pax_num` int not null default 0,
`tkt_num` int not null default 0,
`hand_made_tkt_num` mediumint not null default 0,
primary key (`depart_year_month`,`route_id`,`shipping_company_id`)
) engine=InnoDB default charset=utf8mb4;

drop table if exists `arrive_daily_data`;
create table if not exists  `arrive_daily_data`(
`arrive_date` date not null,
`route_id` smallint unsigned not null,
`shipping_company_id` smallint unsigned not null,
`ferry_num` mediumint not null default 0,
`pax_num` int not null default 0,
primary key (`arrive_date`,`route_id`,`shipping_company_id`)
) engine=InnoDB default charset=utf8mb4;

drop table if exists `arrive_monthly_data`;
create table if not exists  `arrive_monthly_data`(
`arrive_year_month` date not null,
`route_id` smallint unsigned not null,
`shipping_company_id` smallint unsigned not null,
`ferry_num` mediumint not null default 0,
`pax_num`int not null default 0,
primary key (`arrive_year_month`,`route_id`,`shipping_company_id`)
) engine=InnoDB default charset=utf8mb4;
select * from `arrive_monthly_data`;
select `route_id`,sum(`pax_num`) from `arrive_monthly_data`  group by `route_id`;

drop table if exists `ticket_detail`;
create table if not exists  `ticket_detail`(
`tkt_id` int,
`depart_date` date not null,
`depart_time` time not null,
`depart_port_id` smallint not null,
`arrive_port_id` smallint not null,
`order_id` bigint not null,
`class_id` smallint not null,
`seat_no` smallint not null,
`doc_type_id` smallint not null,
`doc_num` varchar(18) not null,
`pax_name` varchar(50) not null,
`pax_mob` varchar(11) not null,
`is_cert` tinyint not null default 0, 
primary key (`tkt_id`)
) engine=InnoDB default charset=utf8mb4;

drop table if exists `cert_pax`;
create table if not exists  `cert_pax`(
`doc_type_id` smallint not null,
`doc_num` varchar(18) not null,
`pax_mob` varchar(11) not null,
`pax_name` varchar(50) not null,
primary key (`doc_type_id`,`doc_num`,`pax_mob`)
) engine=InnoDB default charset=utf8mb4;

drop table if exists `reg_loc`;
create table if not exists  `reg_loc`(
`prefix` char(3) not null,
`infix` char(4) not null,
`province` varchar(3) not null, 
`city` varchar(10) not null,
primary key (`prefix`,`infix`)
) engine=InnoDB default charset=utf8mb4;

drop table if exists `park_record`;
create table if not exists  `park_record`(
`license_plate_num` varchar(8) not null,
`arrive_at` datetime not null,
`leave_at` datetime not null, 
`park_duration` bigint not null,
`car_province` varchar(3) not null,
`car_city` varchar(10) not null,
primary key (`license_plate_num`,`arrive_at`)
) engine=InnoDB default charset=utf8mb4;

drop table if exists `file_data`;
create table if not exists `file_data`(
`file_name` varchar(80),
`file_path` varchar(256),
`upload_at` datetime,
primary key (`file_name`,`file_path`)
) engine=InnoDB default charset=utf8mb4;