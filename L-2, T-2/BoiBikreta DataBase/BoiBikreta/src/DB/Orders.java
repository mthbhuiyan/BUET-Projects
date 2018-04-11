/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import boibikreta.Cart;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Orders {
    public Long order_id;
    public Long customer_id;
    public Long store_id;
    public Long employee_id;
    public String order_date;
    public String packing_date;
    public String delivery_date;
    public Long delivery_price;
    public Long total_price;
    public String order_method;
    public String delivery_place;
    public String order_update;
    public Long paid_amount;
    
    public static ArrayList<Orders> getCustomersAllOrders(Long customer_id)
    {
        ArrayList<Orders> orders = new ArrayList<>();
        Connection con = new OracleDBMS().getConnection();
        String getOrder = "Select * from order_invoice where customer_id = ?";
        ArrayList<Books> books = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(getOrder);
            ps.setLong(1, customer_id);
            ResultSet rst = ps.executeQuery();
            while (rst.next())
            {
                Orders order = new Orders();
                
                order.order_id = rst.getLong("order_id");
                order.customer_id = rst.getLong("customer_id");
                order.store_id = rst.getLong("store_id");
                order.employee_id = rst.getLong("employee_id");
                order.order_date = rst.getString("order_date");
                order.packing_date = rst.getString("packing_date");
                order.delivery_date = rst.getString("delivery_date");
                order.delivery_price = rst.getLong("delivery_price");
                order.total_price = rst.getLong("total_price");
                order.order_method = rst.getString("order_method");
                order.delivery_place = rst.getString("delivery_place");
                order.order_update = rst.getString("order_update");
                order.paid_amount = rst.getLong("paid_amount");
                
                orders.add(order);
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orders;
    }
    
    public static void setOrder()
    {
        Connection con = new OracleDBMS().getConnection();
        String getOrder_id = "Select Order_seq.nextval from dual";
        Long order_id = Long.valueOf(0);
        try {
            PreparedStatement ps = con.prepareStatement(getOrder_id);
            ResultSet rst = ps.executeQuery();
            if (rst.next())
            {
                order_id = rst.getLong(1);
            }
            rst.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String insertOrder = "INSERT INTO ORDER_INVOICE (ORDER_ID, CUSTOMER_ID, STORE_ID, DELIVERY_PRICE, ORDER_METHOD, DELIVERY_PLACE, ORDER_UPDATE)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(insertOrder);
            ps.setLong(1, order_id);
            ps.setLong(2, User.getInstance().getPerson_id());
            ps.setLong(3, Stores.getPK((String)Cart.stores.getValue()));
            ps.setLong(4, 0);
            ps.setString(5, "online");
            ps.setString(6, "home");
            ps.setString(7, "pending");
            
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (int i = 0; i < Cart.n_line; i++) {
            String insertOrderLine = "INSERT INTO ORDER_INVOICE_LINE (ORDER_ID, LINE_NO, BOOK_ID, QUANTITY)"
                    + "VALUES (?, ?, ?, ?)";
            try {
                PreparedStatement ps = con.prepareStatement(insertOrderLine);
                ps.setLong(1, order_id);
                ps.setLong(2, i+1);
                ps.setString(3, Cart.orders.get(i).id);
                ps.setLong(4, (Integer)Cart.quantities.get(i).getValue());

                ps.execute();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
