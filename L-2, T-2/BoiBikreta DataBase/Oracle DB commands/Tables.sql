CREATE TABLE Publisher (
    Publisher_id        Number,
    Name                Varchar2(128) NOT NULL,
    Information         Varchar2(2048),
    CONSTRAINT Publisher_pk PRIMARY KEY (Publisher_id)
);

CREATE TABLE Book (
    Book_id             Char(13),
    Title               Varchar2(128) NOT NULL,
    Edition             Varchar2(16),
    Author_Abbr         Varchar2(16),
    No_Rating           Number DEFAULT 0,
    Combined_Rating     Number DEFAULT 0,
    Publisher_id        Number CONSTRAINT Book_fk_Publisher REFERENCES Publisher(Publisher_id),
    Publishing_date     Date,
    Page_no             Number NOT NULL CHECK (Page_no>0),
    Price               Number NOT NULL CHECK (Price>0),
    Availability        Varchar2(32) DEFAULT 'not available' CHECK (Availability IN('preorder available','in stock','out of stock','not available')),
    Photo               BLOB,
    CONSTRAINT Book_pk PRIMARY KEY (Book_id)
);

CREATE TABLE Author (
    Author_id           Number,
    Name                Varchar2(128) NOT NULL,
    Photo               BLOB,
    Information         Varchar2(2048),
    CONSTRAINT Author_pk PRIMARY KEY (Author_id)
);

CREATE TABLE Book_Author (
    Book_id             Char(13) NOT NULL CONSTRAINT Book_Author_fk_Book REFERENCES Book(Book_id),
    Author_id           Number NOT NULL CONSTRAINT Book_Author_fk_Author REFERENCES Author(Author_id),
    Contribution        Varchar2(32) NOT NULL,
    CONSTRAINT Book_Author_pk PRIMARY KEY (Book_id, Author_id)
);

CREATE TABLE Genre (
    Genre_id            Number,
    Name                Varchar2(32) NOT NULL,
    Definition          Varchar2(256),
    CONSTRAINT Genre_pk PRIMARY KEY (Genre_id)
);

CREATE TABLE Book_Genre (
    Book_id             Char(13) NOT NULL CONSTRAINT Book_Genre_fk_Book REFERENCES Book(Book_id),
    Genre_id            Number NOT NULL CONSTRAINT Book_Genre_fk_Genre REFERENCES Genre(Genre_id),
    CONSTRAINT Book_Genre_pk PRIMARY KEY (Book_id, Genre_id)
);

CREATE TABLE Store (
    Store_id            Number,
    Name                Varchar2(128) NOT NULL,
    Address             Varchar2(256) NOT NULL,
    CONSTRAINT Store_pk PRIMARY KEY (Store_id)
);

CREATE TABLE Book_Availability (
    Store_id            Number NOT NULL CONSTRAINT Book_Availability_fk_Store REFERENCES Store(Store_id),
    Book_id             Char(13) NOT NULL CONSTRAINT Book_Availability_fk_Book REFERENCES Book(Book_id),
    Quantity            Number NOT NULL CHECK (Quantity>=0),
    CONSTRAINT Book_Availability_pk PRIMARY KEY (Store_id, Book_id)
);

CREATE TABLE Offer (
    Offer_id            Number,
    Name                Varchar2(64) NOT NULL,
    Start_date          Date NOT NULL,
    End_date            Date NOT NULL,
    CONSTRAINT Offer_pk PRIMARY KEY (Offer_id)
);

CREATE TABLE Offer_Book (
    Offer_id            Number NOT NULL CONSTRAINT Offer_Book_fk_Offer REFERENCES Offer(Offer_id),
    Book_id             Char(13) NOT NULL CONSTRAINT Offer_Book_fk_Book REFERENCES Book(Book_id),
    Commission_pct      Number NOT NULL CHECK (Commission_pct>0 AND Commission_pct<=1),
    CONSTRAINT Offer_Book_pk PRIMARY KEY (Offer_id, Book_id)
);

CREATE TABLE Person (
    Person_id           Number,
    Password            Varchar2(128) NOT NULL,
    Email               Varchar2(64) NOT NULL UNIQUE,
    First_name          Varchar2(32) NOT NULL,
    Last_name           Varchar2(32) NOT NULL,
    Address             Varchar2(256) NOT NULL,
    Birth_date          Date,
    Phone               Number(16),
    Photo               BLOB,
    is_Employee         Char(1) CHECK (is_Employee IN('y','n')),
    is_Customer         Char(1) CHECK (is_Customer IN('y','n')),
    CONSTRAINT Person_pk PRIMARY KEY (Person_id)
);

CREATE TABLE Book_Review (
    Book_id             Char(13) NOT NULL CONSTRAINT Book_Review_fk_Book REFERENCES Book(Book_id),
    Person_id           Number NOT NULL CONSTRAINT Book_Review_fk_Person REFERENCES Person(Person_id),
    Review              Varchar2(128),
    Review_date         Date,
    Rating              Number(2),
    CONSTRAINT Book_Review_pk PRIMARY KEY (Book_id, Person_id)
);

CREATE TABLE Job (
    Job_id              Number,
    Job_Title           Varchar2(32) NOT NULL,
    CONSTRAINT Job_pk PRIMARY KEY (Job_id)
);

CREATE TABLE Employee (
    Employee_id         Number NOT NULL CONSTRAINT Employee_fk_Person REFERENCES Person(Person_id),
    Job_id              Number NOT NULL CONSTRAINT Employee_fk_Job REFERENCES Job(Job_id),
    Store_id            Number NOT NULL CONSTRAINT Employee_fk_Store REFERENCES Store(Store_id),
    Salary              Number NOT NULL CHECK (Salary>0),
    CONSTRAINT Employee_pk PRIMARY KEY (Employee_id)
);

CREATE TABLE Employee_Job (
    Employee_id         Number NOT NULL CONSTRAINT Employee_Job_fk_Employee REFERENCES Person(Person_id),
    Store_id            Number NOT NULL CONSTRAINT Employee_Job_fk_Store REFERENCES Store(Store_id),
    Job_id              Number NOT NULL CONSTRAINT Employee_Job_fk_Job REFERENCES Job(Job_id),
    Join_date           Date NOT NULL,
    Resign_date         Date,
    CONSTRAINT Employee_Job_pk PRIMARY KEY (Employee_id, Join_date)
);

CREATE TABLE Customer (
    Customer_id         Number NOT NULL CONSTRAINT Customer_fk_Person REFERENCES Person(Person_id),
    CONSTRAINT Customer_pk PRIMARY KEY (Customer_id)
);

CREATE TABLE Order_Invoice (
    Order_id            Number,
    Customer_id         Number NOT NULL CONSTRAINT Order_fk_Customer REFERENCES Customer(Customer_id),
    Store_id            Number NOT NULL CONSTRAINT Order_fk_Store REFERENCES Store(Store_id),
    Employee_id         Number CONSTRAINT Order_fk_Employee REFERENCES Person(Person_id),
    Order_date          Date NOT NULL,
    Packing_date        Date,
    Delivery_date       Date,
    Delivery_price      Number DEFAULT 0 CHECK (Delivery_price>=0),
    Total_price         Number DEFAULT 0 CHECK (Total_price>=0),
    Order_method        Varchar2(32) NOT NULL CHECK (Order_method IN('offline','online')),
    Delivery_place      Varchar2(32) NOT NULL CHECK (Delivery_place IN('home','store')),
    Order_update        Varchar2(32) NOT NULL CHECK (Order_update IN('canceled','pending','packaged','delivered')),
    Paid_amount         Number DEFAULT 0,
    CONSTRAINT Order_Invoice_pk PRIMARY KEY (Order_id)
);

CREATE TABLE Order_Invoice_Line (
    Order_id            Number NOT NULL CONSTRAINT Order_Line_fk_Order REFERENCES Order_Invoice(Order_id),
    Line_no             Number NOT NULL,
    Book_id             Char(13) NOT NULL CONSTRAINT Order_Line_fk_Book REFERENCES Book(Book_id),
    Offer_id            Number CONSTRAINT Order_Line_fk_Offer REFERENCES Offer(Offer_id),
    Quantity            Number NOT NULL CHECK (Quantity>0),
    Price               Number NOT NULL CHECK (Price>0),
    CONSTRAINT Order_Invoice_Line_pk PRIMARY KEY (Order_id, Line_no)
);

CREATE TABLE Supplier (
    Supplier_id         Number,
    Name                Varchar2(128) NOT NULL,
    Address             Varchar2(256) NOT NULL,
    CONSTRAINT Supplier_pk PRIMARY KEY (Supplier_id)
);

CREATE TABLE Supplier_Contact (
    Supplier_id         Number NOT NULL CONSTRAINT Supplier_Contact_fk_Supplier REFERENCES Supplier(Supplier_id),
    Post                Varchar2(32) NOT NULL,
    Email               Varchar2(64),
    Phone               Number(16),
    CONSTRAINT Supplier_Contact_pk PRIMARY KEY (Supplier_id, Email)
);

CREATE TABLE Contract (
    Supplier_id         Number NOT NULL CONSTRAINT Contract_fk_Supplier REFERENCES Supplier(Supplier_id),
    Publisher_id        Number NOT NULL CONSTRAINT Contract_fk_Publisher REFERENCES Publisher(Publisher_id),
    CONSTRAINT Contract_pk PRIMARY KEY (Supplier_id, Publisher_id)
);

CREATE TABLE Supply_Invoice (
    Supply_id           Number,
    Supplier_id         Number NOT NULL CONSTRAINT Supply_fk_Supplier REFERENCES Supplier(Supplier_id),
    Store_id            Number NOT NULL CONSTRAINT Supply_fk_Store REFERENCES Store(Store_id),
    Total_price         Number DEFAULT 0 CHECK (Total_price>0),
    Order_date          Date,
    Receive_date        Date,
    Supply_update       Varchar2(16) NOT NULL CHECK (Supply_update IN('pending','ordered','received')),
    Paid_amount         Number DEFAULT 0,
    CONSTRAINT Supply_Invoice_pk PRIMARY KEY (Supply_id)
);

CREATE TABLE Supply_Invoice_Line (
    Supply_id           Number NOT NULL CONSTRAINT Supply_Line_fk_Supply REFERENCES Supply_Invoice(Supply_id),
    Line_no             Number NOT NULL,
    Book_id             Char(13) NOT NULL CONSTRAINT Supply_Line_fk_Book REFERENCES Book(Book_id),
    Quantity            Number NOT NULL CHECK (Quantity>0),
    Price               Number NOT NULL CHECK (Price>0),
    CONSTRAINT Supply_Line_pk PRIMARY KEY (Supply_id, Line_no)
);

CREATE TABLE Bank (
    Bank_id             Number,
    Bank_name           Varchar2(32) NOT NULL UNIQUE,
    Account_no          Varchar2(64),
    CONSTRAINT Bank_pk PRIMARY KEY (Bank_id)
);

CREATE TABLE Pay_Method (
    Pay_method          Varchar2(32)
    CONSTRAINT Pay_Method_pk PRIMARY KEY
);

CREATE TABLE Pay_System (
    Pay_method          Varchar2(32) NOT NULL CONSTRAINT Pay_System_fk_Pay_method REFERENCES Pay_Method(Pay_method),
    Bank_id             Number CONSTRAINT Pay_System_fk_Bank REFERENCES Bank(Bank_id)
);

CREATE TABLE Transaction (
    Transaction_id      Number,
    Amount              Number NOT NULL,
    Transaction_date     Date NOT NULL,
    Pay_method          Varchar2(32) NOT NULL CONSTRAINT Transaction_fk_Pay_method REFERENCES Pay_Method(Pay_method),
    Bank_id             Number CONSTRAINT Transaction_fk_Bank REFERENCES Bank(Bank_id),
    CONSTRAINT Transaction_pk PRIMARY KEY (Transaction_id)
);

CREATE TABLE Order_Payment (
    Transaction_id      Number NOT NULL CONSTRAINT Order_Payment_fk_Transaction REFERENCES Transaction(Transaction_id),
    Order_id            Number NOT NULL CONSTRAINT Order_Payment_fk_Order REFERENCES Order_Invoice(Order_id),
    CONSTRAINT Order_Payment_pk PRIMARY KEY (Transaction_id, Order_id)
);

CREATE TABLE Supply_Payment (
    Transaction_id      Number NOT NULL CONSTRAINT Supply_Payment_fk_Transaction REFERENCES Transaction(Transaction_id),
    Supply_id           Number NOT NULL CONSTRAINT Supply_Payment_fk_Supply REFERENCES Supply_Invoice(Supply_id),
    CONSTRAINT Supply_Payment_pk PRIMARY KEY (Transaction_id, Supply_id)
);


/*
DROP TABLE Supply_Payment;
DROP TABLE Order_Payment;
DROP TABLE Transaction;
DROP TABLE Pay_System;
DROP TABLE Pay_Method;
DROP TABLE Bank;
DROP TABLE Supply_Invoice_Line;
DROP TABLE Supply_Invoice;
DROP TABLE Contract;
DROP TABLE Supplier_Contact;
DROP TABLE Supplier;
DROP TABLE Order_Invoice_Line;
DROP TABLE Order_Invoice;
DROP TABLE Customer;
DROP TABLE Employee_Job;
DROP TABLE Employee;
DROP TABLE Job;
DROP TABLE Book_Review;
DROP TABLE Person;
DROP TABLE Offer_Book;
DROP TABLE Offer;
DROP TABLE Book_Availability;
DROP TABLE Store;
DROP TABLE Book_Genre;
DROP TABLE Genre;
DROP TABLE Book_Author;
DROP TABLE Author;
DROP TABLE Book;
DROP TABLE Publisher;
*/