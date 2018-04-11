/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boibikreta;

import DB.*;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author User
 */
public class OrderCard extends GridPane {
    OrderCard(Orders order)
    {
        super();
        
        this.setHgap(20);
        this.setVgap(20);
        this.setPadding(new Insets(10));
        
        Label line_no = new Label("Line");
        this.add(line_no, 0, 0);
        
        Label book = new Label("Book");
        this.add(book, 1, 0);
        
        Label quantity = new Label("Quantity");
        this.add(quantity, 2, 0);
        
        Label price = new Label("Price");
        this.add(price, 3, 0);
        
        ArrayList<OrderBooks> list = OrderBooks.getAllBooks(order.order_id);
        
        int i;
        for(i = 0; i < list.size(); i++)
        {
            Label order_line_no = new Label(String.valueOf(list.get(i).line_no));
            this.add(order_line_no, 0, i+1);

            Label order_book = new Label(Books.getBook(list.get(i).book_id).title);
            this.add(order_book, 1, i+1);

            Label order_quantity = new Label(String.valueOf(list.get(i).quantity));
            this.add(order_quantity, 2, i+1);

            Label order_price = new Label(String.valueOf(list.get(i).price));
            this.add(order_price, 3, i+1);
        }
        
        Label sum = new Label("Sum = ");
        this.add(sum, 2, i+1);
        
        Label total_price = new Label(String.valueOf(order.total_price));
        this.add(total_price, 3, i+1);
    }
}
