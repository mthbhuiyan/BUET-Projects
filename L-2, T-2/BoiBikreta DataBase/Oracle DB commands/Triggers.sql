CREATE OR REPLACE TRIGGER Publisher_on_insert
BEFORE INSERT ON Publisher
FOR EACH ROW
BEGIN
    IF :new.Publisher_id IS NULL THEN
        SELECT Publisher_seq.nextval
        INTO :new.Publisher_id
        FROM dual;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Author_on_insert
BEFORE INSERT ON Author
FOR EACH ROW
BEGIN
    IF :new.Author_id IS NULL THEN
        SELECT Author_seq.nextval
        INTO :new.Author_id
        FROM dual;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Genre_on_insert
BEFORE INSERT ON Genre
FOR EACH ROW
BEGIN
    IF :new.Genre_id IS NULL THEN
        SELECT Genre_seq.nextval
        INTO :new.Genre_id
        FROM dual;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Store_on_insert
BEFORE INSERT ON Store
FOR EACH ROW
BEGIN
    IF :new.Store_id IS NULL THEN
        SELECT Store_seq.nextval
        INTO :new.Store_id
        FROM dual;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Offer_on_insert
BEFORE INSERT ON Offer
FOR EACH ROW
BEGIN
    IF :new.Offer_id IS NULL THEN
        SELECT Offer_seq.nextval
        INTO :new.Offer_id
        FROM dual;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Person_on_insert
BEFORE INSERT ON Person
FOR EACH ROW
BEGIN
    IF :new.Person_id IS NULL THEN
        SELECT Person_seq.nextval
        INTO :new.Person_id
        FROM dual;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Book_review_on_change
AFTER INSERT OR UPDATE OF Rating ON Book_Review
FOR EACH ROW
BEGIN
    IF :new.Rating IS NULL THEN
        UPDATE Book
        SET No_Rating = No_Rating + 1, Combined_Rating = Combined_Rating + :new.Rating
        WHERE Book_id = :new.Book_id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Job_on_insert
BEFORE INSERT ON Job
FOR EACH ROW
BEGIN
    IF :new.Job_id IS NULL THEN
        SELECT Job_seq.nextval
        INTO :new.Job_id
        FROM dual;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Employee_on_insert
BEFORE INSERT ON Employee
FOR EACH ROW
BEGIN
    UPDATE Person
    SET Is_Employee = 'y'
    WHERE Person_id = :new.Employee_id;
END;
/

CREATE OR REPLACE TRIGGER Employee_on_delete
BEFORE DELETE ON Employee
FOR EACH ROW
BEGIN
    UPDATE Person
    SET Is_Employee = 'n'
    WHERE Person_id = :old.Employee_id;
END;
/

CREATE OR REPLACE TRIGGER Customer_on_insert
BEFORE INSERT ON Customer
FOR EACH ROW
BEGIN
    UPDATE Person
    SET Is_Customer = 'y'
    WHERE Person_id = :new.Customer_id;
END;
/

CREATE OR REPLACE TRIGGER Order_on_insert
BEFORE INSERT ON Order_Invoice
FOR EACH ROW
DECLARE
    C NUMBER;
BEGIN
    IF :new.Order_id IS NULL THEN
        SELECT Order_seq.nextval
        INTO :new.Order_id
        FROM dual;
    END IF;
    
    SELECT Count(*) INTO C
    FROM Customer
    WHERE Customer_id = :new.Customer_id;
    
    IF C = 0 THEN
        INSERT INTO Customer
        VALUES (:new.Customer_id);
    END IF;
    
    IF :new.Total_price IS NULL THEN
        :new.Total_price := :new.Delivery_price;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Order_on_update
BEFORE UPDATE OF Order_update ON Order_Invoice
FOR EACH ROW
DECLARE
    Payment_not_full EXCEPTION;
    Update_not_possible EXCEPTION;
BEGIN
    IF :new.Order_update = 'packaged' AND :old.Order_update = 'pending' THEN
        FOR O IN
            (
            SELECT Book_id, Quantity
            FROM Order_Invoice_Line
            WHERE Order_id = :new.Order_id
            )
        LOOP
            UPDATE Book_Availability
            SET Quantity = Quantity - O.Quantity
            WHERE Book_id = O.Book_id;
        END LOOP;
    ELSIF :new.Order_update = 'delivered' AND :old.Order_update = 'packaged' THEN
        IF :new.Total_price <> :new.Paid_amount THEN
            RAISE Payment_not_full;
        END IF;
    ELSIF NOT (:new.Order_update = 'canceled' AND :old.Order_update = 'pending') THEN
        RAISE Update_not_possible;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Order_Line_on_insert
BEFORE INSERT ON Order_Invoice_Line
FOR EACH ROW
DECLARE
    Book_price NUMBER;
    Commission NUMBER;
BEGIN
    SELECT Price INTO book_price
    FROM Book
    WHERE Book_id = :new.Book_id;
  
    IF :new.Offer_id IS NOT NULL THEN
        SELECT Commission_pct INTO Commission
        FROM Offer_Book
        WHERE Offer_id = :new.Offer_id AND Book_id = :new.Book_id;
    END IF;
  
    :new.Price := :new.Quantity*Book_price*(1-NVL(Commission, 0));
  
    UPDATE Order_Invoice
    SET Total_price = Total_price + :new.Price
    WHERE Order_id = :new.Order_id;
END;
/

CREATE OR REPLACE TRIGGER Supplier_on_insert
BEFORE INSERT ON Supplier
FOR EACH ROW
BEGIN
    IF :new.Supplier_id IS NULL THEN
        SELECT Supplier_seq.nextval
        INTO :new.Supplier_id
        FROM dual;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Supply_on_insert
BEFORE INSERT ON Supply_Invoice
FOR EACH ROW
BEGIN
    IF :new.Supply_id IS NULL THEN
        SELECT Supply_seq.nextval
        INTO :new.Supply_id
        FROM dual;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Supply_on_update
BEFORE UPDATE OF Supply_update ON Supply_Invoice
FOR EACH ROW
DECLARE
    Update_not_possible EXCEPTION;
BEGIN
    IF :new.Supply_update = 'received' AND :old.Supply_update = 'ordered' THEN
        FOR S IN
            (
            SELECT Book_id, Quantity
            FROM Supply_Invoice_Line
            WHERE Supply_id = :new.Supply_id
            )
        LOOP
            UPDATE Book_Availability
            SET Quantity = Quantity - S.Quantity
            WHERE Book_id = S.Book_id;
        END LOOP;
    ELSIF NOT(:new.Supply_update = 'ordered' AND :old.Supply_update = 'pending') THEN
        RAISE Update_not_possible;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Supply_Line_on_insert
BEFORE INSERT ON Supply_Invoice_Line
FOR EACH ROW
BEGIN
    UPDATE Supply_Invoice
    SET Total_price = Total_price + :new.Price
    WHERE Supply_id = :new.Supply_id;
END;
/

CREATE OR REPLACE TRIGGER Bank_on_insert
BEFORE INSERT ON Bank
FOR EACH ROW
BEGIN
    IF :new.Bank_id IS NULL THEN
        SELECT Bank_seq.nextval
        INTO :new.Bank_id
        FROM dual;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER Transaction_on_insert
BEFORE INSERT ON Transaction
FOR EACH ROW
BEGIN
    IF :new.Transaction_id IS NULL THEN
        SELECT Transaction_seq.nextval
        INTO :new.Transaction_id
        FROM dual;
    END IF;
END;
/