insert into "user" (id, name, email, monthly_salary, monthly_expenses)
values ('ebed4c17-844e-4331-b507-044618d9d146', 'John', 'john@doe.com', 200000, 150000);
insert into "user" (id, name, email, monthly_salary, monthly_expenses)
values ('0e370603-54f0-4f7e-8636-4d121e1932df', 'Jane', 'jane@doe.com', 300000, 200000);

insert into account (id, user_id)
values ('9735acb2-821a-48eb-aada-3ad4a5b952f2', 'ebed4c17-844e-4331-b507-044618d9d146');
insert into account (id, user_id)
values ('ed18efeb-9c76-4663-88aa-3d92258f051d', 'ebed4c17-844e-4331-b507-044618d9d146');
insert into account (id, user_id)
values ('dfc25dfa-f3cf-4d0e-9600-0e15d84b1ac9', '0e370603-54f0-4f7e-8636-4d121e1932df');