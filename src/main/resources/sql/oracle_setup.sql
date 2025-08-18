-- Oracle Database Setup Script for Spring Boot CRUD API
-- Execute this in your Oracle database as HR user

-- Create sequence for User ID generation
create sequence user_seq start with 1 increment by 1 nomaxvalue nocycle cache 20;

-- Create Users table
create table users (
   id    number(19) primary key,
   name  varchar2(100) not null,
   email varchar2(150) not null unique,
   age   number(3)
);

-- Create index on email for better performance
create index idx_users_email on
   users (
      email
   );

-- Insert sample data (optional)
insert into users (
   id,
   name,
   email,
   age
) values ( user_seq.nextval,
           'أحمد محمد',
           'ahmed@example.com',
           25 );
insert into users (
   id,
   name,
   email,
   age
) values ( user_seq.nextval,
           'سارة أحمد',
           'sara@example.com',
           28 );
insert into users (
   id,
   name,
   email,
   age
) values ( user_seq.nextval,
           'محمد علي',
           'mohamed@example.com',
           32 );

-- Commit the changes
commit;

-- Verify the data
select *
  from users;