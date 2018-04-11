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
public class BookAuthors {
    private static BookAuthors instance;
    public static BookAuthors getInstance()
    {
        if(instance==null)
            instance = new BookAuthors();
        return instance;
    }
    public void insert(String ISBN, String AUTHOR, String CONTRIBUTION)
    {
        Connection con = new OracleDBMS().getConnection();
        String setPerson = "Insert into book_author(BOOK_ID, AUTHOR_ID, CONTRIBUTION) values(?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(setPerson);
            ps.setString(1, ISBN);
            ps.setLong(2, new Authors().getPK(AUTHOR));
            ps.setString(3, CONTRIBUTION);
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static ArrayList<Authors> getAllAuthors(String BOOK_ID)
    {
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from book_author where book_id = ?";
        ArrayList<Authors> authors = new ArrayList<Authors>();
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ps.setString(1, BOOK_ID);
            ResultSet rst = ps.executeQuery();
            while (rst.next())
            {
                authors.add(Authors.getAuthor(rst.getLong("author_id")));
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return authors;
    }
    public static ArrayList<Books> getAllBooks(Long author_id)
    {
        Connection con = new OracleDBMS().getConnection();
        String getBook = "Select * from book_author where author_id = ?";
        ArrayList<Books> books = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(getBook);
            ps.setLong(1, author_id);
            ResultSet rst = ps.executeQuery();
            while (rst.next())
            {
                books.add(Books.getBook(rst.getString("book_id")));
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return books;
    }
}