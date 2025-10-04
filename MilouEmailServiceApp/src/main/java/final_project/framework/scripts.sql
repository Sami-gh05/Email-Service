create table Emails (
    email_code char(6) primary key,
    sender_email nvarchar(255) not null ,
    subject nvarchar(63) not null ,
    body nvarchar(255) not null ,
    is_read boolean default false,
    sent_date date not null ,
    foreign key (sender_email) references Users(email)
);


create table Users (
    name nvarchar(31) not null ,
    email nvarchar(255) primary key ,
    password nvarchar(255) not null
);


create table Email_Recipients(
    email_code char(6) not null ,
    recipient_email nvarchar(255) not null ,
    primary key (email_code, recipient_email) ,
    foreign key (email_code) references Emails(email_code) ,
    foreign key (recipient_email) references Users(email)
);
