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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Books {    
    public String id;
    public String title;
    public String edition;
    public String author_abbr;
    public Float rating;
    public long publisher_id;
    public String publishing_date;
    public int no_page;
    public float price;
    public String availability;
    public byte[] photo;
    
    public static Books getBook(String BOOK_ID)
    {
        Books book = new Books();
        Connection con = new OracleDBMS().getConnection();
        String getBook = "Select * from book where book_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(getBook);
            ps.setString(1, BOOK_ID);
            ResultSet rst = ps.executeQuery();
            if (rst.next())
            {
                book.id = rst.getString("book_id");
                book.title = rst.getString("title");
                book.edition = rst.getString("edition");
                book.author_abbr = rst.getString("author_abbr");
                if(rst.getInt("no_rating")!=0)
                    book.rating = Float.valueOf(rst.getInt("combined_rating"))/rst.getInt("no_rating");
                book.publisher_id = rst.getLong("publisher_id");
                book.publishing_date = rst.getString("publishing_date");
                book.no_page = rst.getInt("page_no");
                book.price = rst.getFloat("price");
                book.availability = rst.getString("availability");
                book.photo = rst.getBytes("photo");
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return book;
    }
    
    public static ArrayList<Books> getAllBooks()
    {
        ArrayList<Books> list = null;
        Connection con = new OracleDBMS().getConnection();
        String getBook = "Select * from book";
        try {
            PreparedStatement ps = con.prepareStatement(getBook);
            ResultSet rst = ps.executeQuery();
            list = new ArrayList<>();
            while (rst.next())
            {
                Books book = new Books();
                
                book.id = rst.getString("book_id");
                book.title = rst.getString("title");
                book.edition = rst.getString("edition");
                book.author_abbr = rst.getString("author_abbr");
                if(rst.getInt("no_rating")!=0)
                    book.rating = Float.valueOf(rst.getInt("combined_rating"))/rst.getInt("no_rating");
                book.publisher_id = rst.getLong("publisher_id");
                book.publishing_date = rst.getString("publishing_date");
                book.no_page = rst.getInt("page_no");
                book.price = rst.getFloat("price");
                book.availability = rst.getString("availability");
                book.photo = rst.getBytes("photo");
                
                list.add(book);
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public static ArrayList<Books> getPublishersAllBooks(Long publisher_id)
    {
        ArrayList<Books> list = new ArrayList<>();
        Connection con = new OracleDBMS().getConnection();
        String getPublisher = "Select * from Book where publisher_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(getPublisher);
            ps.setLong(1, publisher_id);
            ResultSet rst = ps.executeQuery();
            while (rst.next())
            {
                Books book = new Books();
                
                book.id = rst.getString("book_id");
                book.title = rst.getString("title");
                book.edition = rst.getString("edition");
                book.author_abbr = rst.getString("author_abbr");
                if(rst.getInt("no_rating")!=0)
                    book.rating = Float.valueOf(rst.getInt("combined_rating"))/rst.getInt("no_rating");
                book.publisher_id = rst.getLong("publisher_id");
                book.publishing_date = rst.getString("publishing_date");
                book.no_page = rst.getInt("page_no");
                book.price = rst.getFloat("price");
                book.availability = rst.getString("availability");
                book.photo = rst.getBytes("photo");
                
                list.add(book);
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public static ArrayList<Books> SearchBook(String key)
    {
        ArrayList<Books> list = getAllBooks();
        ArrayList<Books> nsol = new ArrayList<>();
        key = key.toLowerCase();
        String[] k = key.split(" ");
        for( Books book : list )
        {
            String ss = book.title+" "+book.author_abbr+BookAuthors.getAllAuthors(book.id)+BookGenres.getAllGenres(book.id);
            ss = ss.toLowerCase();
            ss.replaceAll( " " , "" );
            int cnt = 0;
            for( String s: k )
            {
                if( ss.contains(s) ) 
                    cnt++;
            }
            if( cnt > 0 )
            {
                nsol.add(book);
            }
        }
        return nsol;
    }
    
    public static ArrayList<Books> search(String str)
    {
        ArrayList<Books> list = null;
        Connection con = new OracleDBMS().getConnection();
        String getBook = "Select * from book where title like ? and ";
        try {
            PreparedStatement ps = con.prepareStatement(getBook);
            ResultSet rst = ps.executeQuery();
            list = new ArrayList<>();
            while (rst.next())
            {
                Books book = new Books();
                
                book.id = rst.getString("book_id");
                book.title = rst.getString("title");
                book.edition = rst.getString("edition");
                book.author_abbr = rst.getString("author_abbr");
                if(rst.getInt("no_rating")!=0)
                    book.rating = Float.valueOf(rst.getInt("combined_rating"))/rst.getInt("no_rating");
                book.publisher_id = rst.getLong("publisher_id");
                book.publishing_date = rst.getString("publishing_date");
                book.no_page = rst.getInt("page_no");
                book.price = rst.getFloat("price");
                book.availability = rst.getString("availability");
                book.photo = rst.getBytes("photo");
                
                list.add(book);
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public boolean isPresent(String BOOK_ID)
    {
        Connection con = new OracleDBMS().getConnection();
        String getPerson = "Select * from book where book_id = ?";
        boolean success = false;
        try {
            PreparedStatement ps = con.prepareStatement(getPerson);
            ps.setString(1, BOOK_ID);
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
    public void insert(String ISBN,String TITLE, String EDITION, String AUTHOR, String PUBLISHER_NAME, String PUBLISHING_DATE, String PAGE_NO, String PRICE, String AVAILABILITY, File PHOTO)
    {
        Connection con = new OracleDBMS().getConnection();
        String setPerson = "Insert into book(BOOK_ID, TITLE, EDITION, AUTHOR_ABBR, PUBLISHER_ID, PUBLISHING_DATE, PAGE_NO, PRICE, AVAILABILITY, PHOTO) values(?,?,?,?,?,?,?,?,?,?)";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = null;
        if (PUBLISHING_DATE != null) {
            try {
                parsed = format.parse(PUBLISHING_DATE);
            } catch (ParseException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        java.sql.Date sqlDate = null;
        if(parsed!=null)sqlDate = new java.sql.Date(parsed.getTime());
        try {
            PreparedStatement ps = con.prepareStatement(setPerson);
            ps.setString(1, ISBN);
            ps.setString(2, TITLE);
            ps.setString(3, EDITION);
            ps.setString(4, AUTHOR);
            ps.setLong(5, new Publishers().getPK(PUBLISHER_NAME));
            ps.setDate(6, null);
            if(sqlDate!=null)ps.setDate(6, sqlDate);
            ps.setLong(7, Long.parseLong(PAGE_NO));
            ps.setDouble(8, Double.parseDouble(PRICE));
            ps.setString(9, AVAILABILITY);
            ps.setBinaryStream(10, null);
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
