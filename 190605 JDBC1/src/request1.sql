select * from customers;
insert into customers (Id, Name) Values (2, 'Nick');
update customers set Name='Niko' where id=2;
update customers set Age=27 where Name='Niko';
update customers set Address='Perm';