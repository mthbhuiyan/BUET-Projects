/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class BookReviews {
    public String reviewer_title;
    public byte[] reviewer_photo;
    public String review_message;
    public String review_date;
    
    public static ArrayList<BookReviews> getAllReviews(String book_id)
    {
        ArrayList<BookReviews> list = null;
        Connection con = new OracleDBMS().getConnection();
        String getReview = "Select * from book_review join person using(person_id) where book_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(getReview);
            ps.setString(1, book_id);
            ResultSet rst = ps.executeQuery();
            list = new ArrayList<>();
            while (rst.next())
            {
                BookReviews review = new BookReviews();
                
                review.reviewer_title = rst.getString("first_name")+" "+rst.getString("last_name");
                review.reviewer_photo = rst.getBytes("photo");
                review.review_message = rst.getString("review");
                review.review_date = rst.getString("review_date");
                
                list.add(review);
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public static BookReviews getReview(String book_id, Long person_id)
    {
        BookReviews review = null;
        Connection con = new OracleDBMS().getConnection();
        String getReview = "Select * from book_review join person using(person_id) where book_id = ? and person_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(getReview);
            ps.setString(1, book_id);
            ps.setLong(2, person_id);
            ResultSet rst = ps.executeQuery();
            if (rst.next())
            {
                review = new BookReviews();
                
                review.reviewer_title = rst.getString("first_name")+" "+rst.getString("last_name");
                review.reviewer_photo = rst.getBytes("photo");
                review.review_message = rst.getString("review");
                review.review_date = rst.getString("review_date");
            }
            rst.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return review;
    }
    
    public static void insert(String book_id, Long person_id, String review)
    {
        Connection con = new OracleDBMS().getConnection();
        String setReview = "Insert into book_review(book_id, person_id, review) values(?,?,?)";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = null;
        java.sql.Date sqlDate = null;
        if(parsed!=null)sqlDate = new java.sql.Date(parsed.getTime());
        try {
            PreparedStatement ps = con.prepareStatement(setReview);
            ps.setString(1, book_id);
            ps.setLong(2, person_id);
            ps.setString(3, review);
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
