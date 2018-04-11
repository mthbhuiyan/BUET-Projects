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
public class Genres {
    public long id;
    public String name;
    public String definition;
    
    public static ArrayList<Genres> getAllGenres()
    {
        ArrayList<Genres> list = null;
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from genre";
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ResultSet rst = ps.executeQuery();
            list = new ArrayList<>();
            while (rst.next())
            {
                Genres genre = new Genres();
                
                genre.id = rst.getLong("Genre_id");
                genre.name = rst.getString("name");
                genre.definition = rst.getString("definition");
                
                list.add(genre);
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    public boolean isPresent(String GENRE_NAME)
    {
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from genre where name = ?";
        boolean success = false;
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ps.setString(1, GENRE_NAME);
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
    public Long getPK(String GENRE_NAME)
    {
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from genre where name = ?";
        Long publisher_id = Long.valueOf(0);
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ps.setString(1, GENRE_NAME);
            ResultSet rst = ps.executeQuery();
            if (rst.next())
            {
                publisher_id = rst.getLong("GENRE_ID");
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return publisher_id;        
    }
    public void insert(String NAME, String DEFINITION)
    {
        Connection con = new OracleDBMS().getConnection();
        String setPerson = "Insert into genre(NAME, DEFINITION) values(?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(setPerson);
            ps.setString(1, NAME);
            ps.setString(2, DEFINITION);
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
