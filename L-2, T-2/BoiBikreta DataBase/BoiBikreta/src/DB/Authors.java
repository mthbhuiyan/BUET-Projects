/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
public class Authors {
    public long id;
    public String name;
    public String information;
    public byte[] photo;
    public static Authors getAuthor(long AUTHOR_ID)
    {
        Authors author = new Authors();
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from author where author_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ps.setLong(1, AUTHOR_ID);
            ResultSet rst = ps.executeQuery();
            if (rst.next())
            {
                author.id = rst.getLong("author_id");
                author.name = rst.getString("name");
                author.information = rst.getString("information");
                author.photo = rst.getBytes("photo");
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return author;
    }
    public static ArrayList<Authors> getAllAuthors()
    {
        ArrayList<Authors> list = new ArrayList<>();
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from author";
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ResultSet rst = ps.executeQuery();
            while (rst.next())
            {
                Authors author = new Authors();
                
                author.id = rst.getLong("author_id");
                author.name = rst.getString("name");
                author.information = rst.getString("information");
                author.photo = rst.getBytes("photo");
                
                list.add(author);
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    public boolean isPresent(String AUTHOR_NAME)
    {
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from author where name = ?";
        boolean success = false;
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ps.setString(1, AUTHOR_NAME);
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
    public Long getPK(String AUTHOR_NAME)
    {
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from author where name = ?";
        Long author_id = Long.valueOf(0);
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ps.setString(1, AUTHOR_NAME);
            ResultSet rst = ps.executeQuery();
            if (rst.next())
            {
                author_id = rst.getLong("AUTHOR_ID");
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return author_id;
    }
    public void insert(String NAME, String INFO, File PHOTO)
    {
        Connection con = new OracleDBMS().getConnection();
        String setPerson = "Insert into author(NAME, INFORMATION, PHOTO) values(?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(setPerson);
            ps.setString(1, NAME);
            ps.setString(2, INFO);
            ps.setBinaryStream(3, null);
            FileInputStream fin = null;
            if (PHOTO != null) {
                try {
                    fin = new FileInputStream(PHOTO);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    ps.setBinaryStream(10, fin, fin.available());
                } catch (IOException ex) {
                    Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}