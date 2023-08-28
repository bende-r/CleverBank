create schema cleverbank;
create table cleverbank.banks(bank_id int not null primary key,bank_name text);
create table cleverbank.users(user_id int not null primary key,user_name varchar(20),user_surname varchar(20),user_middlename varchar(20));
create table cleverbank.scores(score_id int not null primary key, score_number varchar(10),bank_id int not null references cleverbank.banks (bank_id),currency varchar(3),balance money,user_id int not null references cleverbank.users (user_id));
create table cleverbank.transactions(transaction_id int not null primary key,sender_score int not null references cleverbank.scores (score_id),recipient_score int not null references cleverbank.scores (score_id),transfer_amount money);