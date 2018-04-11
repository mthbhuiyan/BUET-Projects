/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

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
public class OrderBooks {
    public Long order_id;
    public int line_no;
    public String book_id;
    public Long offer_id;
    public int quantity;
    public Long price;
    
    public static ArrayList<OrderBooks> getAllBooks(Long order_id)
    {
        ArrayList<OrderBooks> list = new ArrayList<>();
        Connection con = new OracleDBMS().getConnection();
        String getOrder = "Select * from order_invoice_line where order_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(getOrder);
            ps.setLong(1, order_id);
            ResultSet rst = ps.executeQuery();
            while (rst.next())
            {
                OrderBooks line = new OrderBooks();
                
                line.order_id = rst.getLong("order_id");
                line.line_no = rst.getInt("line_no");
                line.book_id = rst.getString("book_id");
                line.offer_id = rst.getLong("offer_id");
                line.quantity = rst.getInt("quantity");
                line.price = rst.getLong("price");
                
                list.add(line);
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
