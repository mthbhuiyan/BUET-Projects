/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import boibikreta.PublishersView;
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
public class Publishers {
    public long id;
    public String name;
    public String info;
    public static ArrayList<Publishers> getAllPublishers()
    {
        ArrayList<Publishers> list = new ArrayList<>();
        Connection con = new OracleDBMS().getConnection();
        String getPublisher = "Select * from publisher";
        try {
            PreparedStatement ps = con.prepareStatement(getPublisher);
            ResultSet rst = ps.executeQuery();
            while (rst.next())
            {
                Publishers publisher = new Publishers();
                
                publisher.id = rst.getLong("publisher_id");
                publisher.name = rst.getString("name");
                publisher.info = rst.getString("information");
                
                list.add(publisher);
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    public static Publishers getPublisher(long PUBLISHER_ID)
    {
        Publishers publisher = new Publishers();
        Connection con = new OracleDBMS().getConnection();
        String getPublisher = "Select * from publisher where publisher_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(getPublisher);
            ps.setLong(1, PUBLISHER_ID);
            ResultSet rst = ps.executeQuery();
            if (rst.next())
            {
                publisher.id = rst.getLong("publisher_id");
                publisher.name = rst.getString("name");
                publisher.info = rst.getString("information");
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return publisher;
    }
    public boolean isPresent(String PUBLISHER_NAME)
    {
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from publisher where name = ?";
        boolean success = false;
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ps.setString(1, PUBLISHER_NAME);
            ResultSet rst = ps.executeQuery();
            if (rst.next())
            {
                success = true;
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;        
    }
    public Long getPK(String PUBLISHER_NAME)
    {
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from publisher where name = ?";
        Long publisher_id = Long.valueOf(0);
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ps.setString(1, PUBLISHER_NAME);
            ResultSet rst = ps.executeQuery();
            if (rst.next())
            {
                publisher_id = rst.getLong("PUBLISHER_ID");
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return publisher_id;        
    }
    public ArrayList<PublishersView> getAll()
    {
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from publisher";
        ArrayList<PublishersView> publishers = new ArrayList<PublishersView>();
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ResultSet rst = ps.executeQuery();
            while(rst.next())
            {
                publishers.add(new PublishersView(rst.getString("NAME"), rst.getString("INFORMATION")));
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return publishers;
    }
    public void insert(String NAME, String INFO)
    {
        Connection con = new OracleDBMS().getConnection();
        String setPerson = "Insert into publisher(NAME, INFORMATION) values(?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(setPerson);
            ps.setString(1, NAME);
            ps.setString(2, INFO);
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
