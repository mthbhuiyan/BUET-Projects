/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boibikreta;

import DB.Books;
import DB.User;
import java.io.ByteArrayInputStream;
import java.io.File;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class View {
    static BorderPane root;
    static VBox topBar;
    static HBox menuBar;
    static HBox searchBar;
    static HBox navigation;
    static HBox logInfo;
    static File selectedFile;
    
    static ToggleButton home = new ToggleButton("Home");
    static ToggleButton books = new ToggleButton("Books");
    static ToggleButton authors = new ToggleButton("Authors");
    static ToggleButton publishers = new ToggleButton("Publishers");
    static ToggleButton offers = new ToggleButton("Offers");
    static ToggleButton employees = new ToggleButton("Employees");
    static ToggleButton myorders = new ToggleButton("My Orders");
    static ToggleButton cart = new ToggleButton("Cart");
    static void setInitialView(BorderPane root)
    {
        View.root = root;
        
        TextField username = new TextField();
        username.setPromptText("Email id");
        
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        
        Button login = new Button("Log In");
        login.setOnAction(e->{
            if(User.getInstance().findPerson(username.getText(), password.getText()))
            {
                setLoggedInView();
            }
            else
            {
                username.setText("");
                username.setPromptText("Email id, Try again");
                password.setText("");
                password.setPromptText("Password, Try again");
            }
        });
        Button signup = new Button("Sign Up");
        signup.setOnAction(e->{
            View.setSignUpView();
        });
        
        TextField searchfield = new TextField();
        searchfield.setPrefWidth(500);
        searchfield.setPromptText("Search Books by title, author or genre");
        
        Button search = new Button("Search");
        search.setOnAction(e->{
            if(!searchfield.getText().equals(""))
                root.setCenter(new BookPane("You searched for \""+searchfield.getText()+"\"",Books.SearchBook(searchfield.getText())));
        });
                        
        searchBar = new HBox(searchfield, search);
        
        ToggleGroup tgp = new ToggleGroup();
        
        home.setToggleGroup(tgp);
        home.setSelected(true);
        home.setOnAction(e->{
            if(!home.isSelected()){
                home.setSelected(true);
            }
        });
        
        books.setToggleGroup(tgp);
        books.setOnAction(e->{
            if(!books.isSelected()){
                books.setSelected(true);
            }
            root.setCenter(BookList.getInstance().getView());
        });
        
        authors.setToggleGroup(tgp);
        authors.setOnAction(e->{            
            if(!authors.isSelected()){
                authors.setSelected(true);
            }
            root.setCenter(AuthorList.getInstance().getView());
        });
        
        publishers.setToggleGroup(tgp);
        publishers.setOnAction(e->{
            if(!publishers.isSelected()){
                publishers.setSelected(true);
            }
            //root.setCenter(PublishersView.GetView(true));
            root.setCenter(PublisherList.getInstance().getView());
        });
        
        offers.setToggleGroup(tgp);
        offers.setOnAction(e->{            
            if(!offers.isSelected()){
                offers.setSelected(true);
            }
        });
        
        employees.setToggleGroup(tgp);
        employees.setOnAction(e->{            
            if(!employees.isSelected()){
                employees.setSelected(true);
            }
        });
        
        myorders.setToggleGroup(tgp);
        myorders.setOnAction(e->{            
            if(!myorders.isSelected()){
                myorders.setSelected(true);
            }
            root.setCenter(OrderList.getInstance().getView());
        });
        
        cart.setToggleGroup(tgp);
        cart.setOnAction(e->{
            if(!cart.isSelected()){
                cart.setSelected(true);
            }
            root.setCenter(Cart.getInstance().getView());
        });
        
        Region rgn = new Region();
        
        navigation = new HBox(home, books, authors, publishers, offers, cart);
        
        logInfo = new HBox(username, password, login, signup);
        logInfo.setAlignment(Pos.CENTER_LEFT);
        logInfo.setSpacing(10);
        
        menuBar = new HBox(navigation, rgn, logInfo);
        HBox.setHgrow(rgn, Priority.ALWAYS);
        menuBar.setPadding(new Insets(10,10,10,10));
        
        topBar = new VBox(menuBar, searchBar);
        topBar.setSpacing(10);
        
        root.setTop(topBar);
        root.setCenter(BookList.getInstance().getView());
    }
    static void setLoggedInView()
    {
        root.setCenter(BookList.getInstance().getView());
        
        Label username = new Label(User.getInstance().getFirst_name()+" "+User.getInstance().getLast_name());
        ImageView imgView = new ImageView();
        if(User.getInstance().getPhoto()!=null)
        {
            imgView.setImage(new Image(new ByteArrayInputStream(User.getInstance().getPhoto())));
            imgView.setFitWidth(25);
            imgView.setFitHeight(25);
            imgView.setPreserveRatio(true);
        }
        Button logout = new Button("Log Out");
        logout.setOnAction(e->{
            setInitialView(root);
            User.logOut();
        });
        
        navigation.getChildren().removeAll(navigation.getChildren());
        logInfo.getChildren().removeAll(logInfo.getChildren());
        if(User.getInstance().is_customer())
            navigation.getChildren().addAll(home, books, authors, publishers, offers, myorders, cart);
        //if(User.getInstance().is_employee())
        //    navigation.getChildren().addAll(employees, orders, supplies, transactions);
        logInfo.getChildren().addAll(username, imgView, logout);
        
        root.setCenter(BookList.getInstance().getView());
    }
    static void setSignUpView()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Image File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        
        ImageView imgView = new ImageView();
        imgView.setPreserveRatio(true);
        imgView.setFitWidth(150);
        imgView.setFitHeight(150);
        imgView.setPreserveRatio(true);
        Label photoLabel = new Label("Photo");
        Button setPhoto = new Button("Browse");
        VBox photo = new VBox(imgView, setPhoto);
        photo.setSpacing(5);
        setPhoto.setOnAction(e->{
            Stage chooser = new Stage();
            chooser.setAlwaysOnTop(true);
            selectedFile = fileChooser.showOpenDialog(chooser);
            if(selectedFile==null) {
            } else {
                    Image image = new Image(selectedFile.toURI().toString());
                    imgView.setImage(image);
            }            
        });
        
        Label fnameLabel = new Label("First name");
        TextField fname = new TextField();
        fname.setPromptText("First name");
        
        Label lnameLabel = new Label("Last name");
        TextField lname = new TextField();
        lname.setPromptText("Last name");
        
        Label addressLabel = new Label("Address");
        TextField address = new TextField();
        address.setPromptText("Address");
        
        Label bdateLabel = new Label("Birth Date");
        DatePicker bdate = new DatePicker();
        bdate.setPromptText("Birth Date");
        
        Label emailLabel = new Label("Email Id");
        TextField email = new TextField();
        email.setPromptText("Email Id");
        
        Label phoneLabel = new Label("Phone");
        TextField phone = new TextField();
        phone.setPromptText("Phone");
        
        Label passLabel = new Label("Password");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        
        Label conpassLabel = new Label("Confirm Password");
        PasswordField conpassword = new PasswordField();
        conpassword.setPromptText("Confirm Password");
        
        Button signup = new Button("Sign Up");
        
        GridPane grid = new GridPane();
        
        signup.setOnAction(e->{
            if(fname.getText().equals(""))
            {
                fname.requestFocus();
            }
            else if(lname.getText().equals(""))
            {
                lname.requestFocus();
            }
            else if(address.getText().equals(""))
            {
                address.requestFocus();
            }
            else if(email.getText().equals(""))
            {
                email.requestFocus();
            }
            else if(password.getText().equals(""))
            {
                password.requestFocus();
            }
            else if(conpassword.getText().equals(""))
            {
                conpassword.requestFocus();
            }
            else if(password.getText().equals(conpassword.getText()))
            {
                String birth = null;
                String ph = phone.getText();
                if(bdate.getValue()!=null)
                    birth = bdate.getValue().toString();
                if(ph.equals(""))
                    ph = "0";
                User.getInstance().insertPerson(password.getText(), 
                        email.getText(), fname.getText(), lname.getText(), address.getText(), 
                        birth, ph, selectedFile);
                setLoggedInView();
            }
            else
            {
                grid.add(new Label("Wrong"), 2, 7);
            }
        });
        grid.addRow(0, fnameLabel, fname);
        grid.addRow(1, lnameLabel, lname);
        grid.addRow(2, addressLabel, address);
        grid.addRow(3, bdateLabel, bdate);
        grid.addRow(4, emailLabel, email);
        grid.addRow(5, phoneLabel, phone);
        grid.addRow(6, passLabel, password);
        grid.addRow(7, conpassLabel, conpassword);
        grid.addRow(8, photoLabel, photo);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        
        VBox vbox = new VBox(grid, signup);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        root.setCenter(vbox);
    }
}
