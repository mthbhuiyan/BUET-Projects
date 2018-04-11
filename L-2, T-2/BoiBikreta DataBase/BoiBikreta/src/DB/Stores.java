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
public class Stores {
    public long id;
    public String name;
    public String address;
    
    public static long getPK(String str)
    {
        long store_id = 0;
        Connection con = new OracleDBMS().getConnection();
        String getStore = "Select * from store where name||', '||address = ?";
        try {
            PreparedStatement ps = con.prepareStatement(getStore);
            ps.setString(1, str);
            ResultSet rst = ps.executeQuery();
            while (rst.next())
            {
                Stores store = new Stores();
                
                store_id = rst.getLong("store_id");
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return store_id;
    }
    
    public static ArrayList<Stores> getAllStores()
    {
        ArrayList<Stores> list = null;
        Connection con = new OracleDBMS().getConnection();
        String getStore = "Select * from store";
        try {
            PreparedStatement ps = con.prepareStatement(getStore);
            ResultSet rst = ps.executeQuery();
            list = new ArrayList<>();
            while (rst.next())
            {
                Stores store = new Stores();
                
                store.id = rst.getLong("store_id");
                store.name = rst.getString("name");
                store.address = rst.getString("address");
                
                list.add(store);
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
