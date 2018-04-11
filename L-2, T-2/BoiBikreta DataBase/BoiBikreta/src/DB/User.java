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
import java.sql.*;  
import java.io.*; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.sql.BLOB;

/**
 *
 * @author User
 */
public class User {
    private static User instance;
    private Long person_id;
    private String email;
    private String first_name;
    private String last_name;
    private String address;
    private String birthdate;
    private String phone;
    private byte[] photo;
    private boolean is_employee;
    private boolean is_customer;

    public long getPerson_id() {
        return person_id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public boolean is_employee() {
        return is_employee;
    }

    public boolean is_customer() {
        return is_customer;
    }
    
    public static User getInstance()
    {
        if (instance == null)
        {
            instance = new User();
        }
        return instance;
    }
    
    public boolean isLoggedIn()
    {
        return person_id != null;
    }
    
    public static void logOut()
    {
        instance = null;
    }
    
    public boolean findPerson(String EMAIL, String PASSWORD)
    {
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from person where email = ? and password = ?";
        boolean success = false;
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ps.setString(1, EMAIL);
            ps.setString(2, PASSWORD);
            ResultSet rst = ps.executeQuery();
            if (rst.next())
            {
                success = true;
                person_id = rst.getLong("PERSON_ID");
                email = rst.getString("EMAIL");
                first_name = rst.getString("FIRST_NAME");
                last_name = rst.getString("lAST_NAME");
                address = rst.getString("ADDRESS");
                birthdate = rst.getString("BIRTH_DATE");
                phone = rst.getString("PHONE");
                photo = rst.getBytes("PHOTO");
                is_employee = rst.getString("is_employee").equals("y");
                is_customer = rst.getString("is_customer").equals("y");
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;        
    }
    
    public void insertPerson(String PASSWORD, String EMAIL, String FIRST_NAME, String LAST_NAME, String ADDRESS, String BIRTH_DATE, String PHONE, File PHOTO)
    {
        Connection con = new OracleDBMS().getConnection();
        String setPerson = "Insert into person(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, ADDRESS, BIRTH_DATE, PHONE, PHOTO) values(?,?,?,?,?,?,?,?)";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = null;
        if (BIRTH_DATE != null) {
            try {
                parsed = format.parse(BIRTH_DATE);
            } catch (ParseException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        java.sql.Date sqlDate = null;
        if(parsed!=null)sqlDate = new java.sql.Date(parsed.getTime());
        try {
            PreparedStatement ps = con.prepareStatement(setPerson);
            ps.setString(1, EMAIL);
            ps.setString(2, PASSWORD);
            ps.setString(3, FIRST_NAME);
            ps.setString(4, LAST_NAME);
            ps.setString(5, ADDRESS);
            ps.setDate(6, null);
            if(sqlDate!=null)ps.setDate(6, sqlDate);
            ps.setLong(7, Long.parseLong(PHONE));
            ps.setBinaryStream(8, null);
            FileInputStream fin = null;
            if (PHOTO != null) {
                try {
                    fin = new FileInputStream(PHOTO);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    ps.setBinaryStream(8, fin, fin.available());
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
        findPerson(EMAIL, PASSWORD);
    }
}
