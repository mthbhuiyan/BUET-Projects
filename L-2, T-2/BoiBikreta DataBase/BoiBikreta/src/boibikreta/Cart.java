/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boibikreta;

import DB.*;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author User
 */
public class Cart {
    private static ScrollPane view;
    private static VBox vbox;
    private static Cart instance;
    private static GridPane cart;
    public static ArrayList<Books> orders;
    public static ArrayList<Spinner<Integer>> quantities;
    public static int n_line;
    private static int total_price;
    public static ChoiceBox stores;
    
    Label sum;
    Label t_price;
    
    public static Cart getInstance()
    {
        if(instance == null)
        {
            instance = new Cart();
            instance.set();
        }
        return instance;
    }
    
    private void set()
    {
        cart = new GridPane();
        
        Label line = new Label("Line");
        cart.add(line, 0, 0);
        
        Label book = new Label("Book");
        cart.add(book, 1, 0);
        
        Label quantity = new Label("Quantity");
        cart.add(quantity, 2, 0);
        
        Label price = new Label("Price");
        cart.add(price, 3, 0);
        
        sum = new Label("Sum");
        
        t_price = new Label(String.valueOf(0));
        
        orders = new ArrayList<>();
        quantities = new ArrayList<>();
        
        n_line = 0;
        total_price = 0;
        
        vbox = new VBox();
        view = new ScrollPane(vbox);
        
        stores = new ChoiceBox();
        Stores.getAllStores().forEach(store->{
            stores.getItems().add(store.name+", "+store.address);
        });
        
        Button buy = new Button("Buy");
        buy.setOnAction(e->{
            if (User.getInstance().isLoggedIn())
            {
                Orders.setOrder();
                set();
            }
        });
        
        vbox.getChildren().addAll(cart, stores, buy);
    }
    
    public ScrollPane getView()
    {
        return view;
    }
    
    public void addToCart(Books bk)
    {
        orders.add(bk);
        n_line++;
        Label line = new Label(String.valueOf(n_line));
        cart.add(line, 0, n_line);
        
        Label book = new Label(bk.title+" "+bk.edition+" "+bk.author_abbr);
        cart.add(book, 1, n_line);
        
        Spinner<Integer> quantity = new Spinner<Integer>(1, 100, 1);
        cart.add(quantity, 2, n_line);
        quantities.add(quantity);
        
        Label price = new Label(String.valueOf(bk.price));
        cart.add(price, 3, n_line);
        
        total_price += bk.price;
        t_price.setText(String.valueOf(total_price));
        
        cart.getChildren().removeAll(sum, t_price);
        cart.add(sum, 2, n_line+1);
        cart.add(t_price, 3, n_line+1);
    }
    
    
}
