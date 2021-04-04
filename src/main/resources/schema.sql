CREATE TABLE IF NOT EXISTS EMPLOYEE (
    id bigint PRIMARY KEY auto_increment,
    created_at timestamp,
    email_id varchar(255) not null unique,
    first_name varchar(255),
    last_name varchar(255),
    updated_at timestamp
);

CREATE TABLE IF NOT EXISTS EMPLOYEE_CALENDER (
    id bigint PRIMARY KEY auto_increment,
    created_at timestamp,
    calender_date date not null,
    updated_at timestamp,
    employee_id bigint,
    foreign key (employee_id) references EMPLOYEE(id)
);

CREATE TABLE IF NOT EXISTS EVENT (
    id bigint PRIMARY KEY auto_increment,
    created_at timestamp,
    description clob,
    end_time timestamp,
    start_time timestamp,
    title varchar(255),
    updated_at timestamp,
    employee_calender_id bigint,
    foreign key (employee_calender_id) references EMPLOYEE_CALENDER(id)
);