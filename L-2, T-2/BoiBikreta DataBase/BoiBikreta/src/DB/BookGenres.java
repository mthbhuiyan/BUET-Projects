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
public class BookGenres {
    
    public static ArrayList<Books> getAllBooks(Long genre_id)
    {
        ArrayList<Books> list = null;
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from book_genre where genre_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ps.setLong(1, genre_id);
            ResultSet rst = ps.executeQuery();
            list = new ArrayList<>();
            while (rst.next())
            {                
                list.add(Books.getBook(rst.getString("book_id")));
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public static void insert(String ISBN, String GENRE)
    {
        Connection con = new OracleDBMS().getConnection();
        String setPerson = "Insert into book_genre(BOOK_ID, GENRE_ID) values(?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(setPerson);
            ps.setString(1, ISBN);
            ps.setLong(2, new Genres().getPK(GENRE));
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static ArrayList<String> getAllGenres(String BOOK_ID)
    {
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from book_genre join genre using(genre_id) where book_id = ?";
        ArrayList<String> genres = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ps.setString(1, BOOK_ID);
            ResultSet rst = ps.executeQuery();
            while (rst.next())
            {
                genres.add(rst.getString("NAME"));
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return genres;
    }
}
