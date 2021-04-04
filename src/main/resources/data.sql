INSERT INTO employee (id, email_id, first_name, last_name) values (1, 'shubham@gmail.com', 'shubham', 'sharma');
INSERT INTO employee (id, email_id, first_name, last_name) values (2, 'sharma@gmail.com', 'xyz', 'sharma');
INSERT INTO employee (id, email_id, first_name, last_name) values (3, 'abc@gmail.com', 'abc', 'sharma');


INSERT INTO employee_calender (id, calender_date, employee_id) values (1, '2021-05-05', 1);
INSERT INTO employee_calender (id, calender_date, employee_id) values (2, '2021-05-05', 2);



INSERT INTO event (id, title, description, start_time, end_time, employee_calender_id)
values (1, 'Scrum', 'daily standup', '2021-05-05 10:30:00', '2021-05-05 11:00:00', 1);

INSERT INTO event (id, title, description, start_time, end_time, employee_calender_id)
values (2, 'Prod issue', 'Discuss issues', '2021-05-05 13:30:00', '2021-05-05 14:00:00', 1);



INSERT INTO event (id, title, description, start_time, end_time, employee_calender_id)
values (3, 'Scrum', 'daily standup', '2021-05-05 11:30:00', '2021-05-05 12:00:00', 2);

INSERT INTO event (id, title, description, start_time, end_time, employee_calender_id)
values (4, 'Prod issue', 'Discuss issues', '2021-05-05 15:30:00', '2021-05-05 16:00:00', 2);