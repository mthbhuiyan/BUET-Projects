/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boibikreta;

import DB.*;
import static boibikreta.View.selectedFile;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class BooksView {
    static File selectedFile;
    static VBox booksTable;
    static VBox cardList;
    static GridPane topLabel;
    static ArrayList<BooksView> books;
    String isbn;
    String title;
    String edition;
    ArrayList<String> authors;
    ArrayList<String> genres;
    String publisher;
    String publish_date;
    String page_no;
    String price;
    String availability;
    ByteArrayInputStream photo;
    File photofile;

    public BooksView(String isbn, String title, String edition, ArrayList<String> authors, ArrayList<String> genres, String publisher, String publish_date, String page_no, String price, String availability, ByteArrayInputStream photo, File photofile) {
        this.isbn = isbn;
        this.title = title;
        this.edition = edition;
        this.authors = authors;
        this.genres = genres;
        this.publisher = publisher;
        this.publish_date = publish_date;
        this.page_no = page_no;
        this.price = price;
        this.availability = availability;
        this.photo = photo;
        this.photofile = photofile;
    }
    
    static void AddCard(BooksView book)
    {
        GridPane card = new GridPane();
        card.add(new Label(book.isbn), 0, 1);
        card.add(new Label(book.title), 0, 2);
        card.add(new Label(book.edition), 0, 3);
        VBox authors = new VBox();
        book.authors.forEach(auth -> {
            authors.getChildren().add(new Label((String)auth));
        });
        card.add(authors, 0, 4);
        VBox genres = new VBox();
        book.genres.forEach(gen -> {
            genres.getChildren().add(new Label((String)gen));
        });
        card.add(genres, 0, 5);
        card.add(new Label(book.publisher), 0, 6);
        card.add(new Label(book.publish_date), 0, 7);
        card.add(new Label(book.page_no), 0, 8);
        card.add(new Label(book.price), 0, 9);
        card.add(new Label(book.availability), 0, 10);
        ImageView photo = new ImageView();
        if(book.photo==null)
            photo.setImage(new Image(book.photofile.toURI().toString()));
        else
            photo.setImage(new Image(book.photo));
        card.add(photo, 0, 0);
        cardList.getChildren().add(card);
    }
    static VBox Get()
    {
        books = new ArrayList<BooksView>();
        
        topLabel = new GridPane();
        Label photolabel = new Label("Photo");
        topLabel.add(photolabel, 0, 0);
        
        Label isbnlabel = new Label("ISBN");
        topLabel.add(isbnlabel, 0, 1);
        
        Label titlelabel = new Label("Tile");
        topLabel.add(titlelabel, 0, 2);
        
        Label editionlabel = new Label("Edition");
        topLabel.add(editionlabel, 0, 3);
        
        Label authorslabel = new Label("Author");
        topLabel.add(authorslabel, 0, 4);
        
        Label genreslabel = new Label("Genre");
        topLabel.add(genreslabel, 0, 5);
        
        Label publisherlabel = new Label("Publisher");
        topLabel.add(publisherlabel, 0, 6);
        
        Label publish_datelabel = new Label("Publishing Date");
        topLabel.add(publish_datelabel, 0, 7);
        
        Label pagelabel = new Label("No. of Pages");
        topLabel.add(pagelabel, 0, 8);
        
        Label pricelabel = new Label("Price");
        topLabel.add(pricelabel, 0, 9);
        
        Label availLabel = new Label("Availability");
        topLabel.add(availLabel, 0, 10);
        
        topLabel.setGridLinesVisible(true);
        
        cardList = new VBox();
        
        booksTable = new VBox(topLabel, new ScrollPane(cardList));
        booksTable.setAlignment(Pos.CENTER);
        return booksTable;
    }
    static void AddInsertField()
    {        
        TextField isbn = new TextField();
        
        TextField title = new TextField();
        
        TextField edition = new TextField();
        
        VBox authorfields = new VBox();
        
        Button newauthor = new Button("new");
        newauthor.setOnAction(e->{
            authorfields.getChildren().add(new TextField());
        });
        
        VBox authors = new VBox(authorfields, newauthor);
        
        VBox genrefields = new VBox();
        
        Button newgenre = new Button("new");
        newgenre.setOnAction(e->{
            genrefields.getChildren().add(new TextField());
        });
        
        VBox genres = new VBox(genrefields, newgenre);
        
        TextField publisher = new TextField();
        
        DatePicker publish_date = new DatePicker();
        
        TextField page_no = new TextField();
        page_no.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                page_no.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        TextField price = new TextField();
        price.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                price.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        ChoiceBox availability = new ChoiceBox();
        availability.setItems(FXCollections.observableArrayList("not available", "preorder available", "in stock", "out of stock"));
        
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Image File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        
        ImageView imgView = new ImageView();        
        imgView.setFitWidth(150);
        imgView.setFitHeight(150);
        imgView.setPreserveRatio(true);
        
        Button setPhoto = new Button("Browse");
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
        
        VBox photo = new VBox(imgView, setPhoto);
        photo.setSpacing(5);
        
        Button done = new Button("Done");
        done.setOnAction(e->{
            boolean success = true;
            for(Node auth: authorfields.getChildren()){
                TextField txt = (TextField)auth;
                if(txt.getText()!="" && !new Authors().isPresent(txt.getText()))
                {
                    txt.requestFocus();
                    success = false;
                    break;
                }
            }
            if (success) {
                for (Node gen : genrefields.getChildren()) {
                    TextField txt = (TextField) gen;
                    if (txt.getText() != "" && !new Genres().isPresent(txt.getText())) {
                        txt.requestFocus();
                        success = false;
                        break;
                    }
                }
            }
            if (success) {
                if (new Publishers().isPresent(publisher.getText())) {
                    publisher.requestFocus();
                    success = false;
                }
            }
            if (success) {
                ArrayList<String> authorsName = new ArrayList<String>();
                authors.getChildren().forEach(auth->{
                    TextField txt = (TextField)auth;
                    authorsName.add(txt.getText());
                });
                ArrayList<String> genresName = new ArrayList<String>();
                genres.getChildren().forEach(gen->{
                    TextField txt = (TextField)gen;
                    genresName.add(txt.getText());
                });
                
                BooksView bookcard = new BooksView(isbn.getText(), title.getText(), edition.getText(), authorsName, genresName, publisher.getText(), 
                        publish_date.getValue().toString(), page_no.getText(), price.getText(), availability.getValue().toString(), null, selectedFile);
                
                AddCard(bookcard);
                
                books.add(bookcard);
                
                isbn.setText("");
                title.setText("");
                edition.setText("");
                authorfields.getChildren().forEach(auth -> {
                    TextField txt = (TextField) auth;
                    txt.setText("");
                });
                genrefields.getChildren().forEach(gen -> {
                    TextField txt = (TextField) gen;
                    txt.setText("");
                });
                publisher.setText("");
                publish_date.setValue(LocalDate.now());
                page_no.setText("");
                price.setText("");
                availability.setValue("not available");
                imgView.setImage(null);
                selectedFile = null;
            }
        });
        
        Button clear = new Button("Clear");
        clear.setOnAction(e->{
            isbn.setText("");
            title.setText("");
            edition.setText("");
            authorfields.getChildren().forEach(auth->{
                TextField txt = (TextField)auth;
                txt.setText("");
            });
            genrefields.getChildren().forEach(gen->{
                TextField txt = (TextField)gen;
                txt.setText("");
            });
            publisher.setText("");
            publish_date.setValue(LocalDate.now());
            page_no.setText("");
            price.setText("");
            availability.setValue("not available");
            imgView.setImage(null);
            selectedFile = null;
        });
        
        topLabel.add(photo, 1, 0);
        topLabel.add(isbn, 1, 1);
        topLabel.add(title, 1, 2);
        topLabel.add(edition, 1, 3);
        topLabel.add(authors, 1, 4);
        topLabel.add(genres, 1, 5);
        topLabel.add(publisher, 1, 6);
        topLabel.add(publish_date, 1, 7);
        topLabel.add(page_no, 1, 8);
        topLabel.add(price, 1, 9);
        topLabel.add(availability, 1, 10);
        topLabel.add(done, 1, 11);
        topLabel.add(clear, 1, 12);
        
        Button save = new Button("Save");
        save.setOnAction(e->{
            save();
            cancel();
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e->{
            cancel();
        });
        
        booksTable.getChildren().add(new HBox(save, cancel));
    }
    static void save()
    {
        books.forEach(book->{
            String authorabbr = new String();
            book.authors.forEach(auth->{
                authorabbr.concat(auth+" ");
            });
            new Books().insert(book.isbn, book.title, book.edition, authorabbr, book.publisher, book.publish_date, 
                    book.page_no, book.price, book.availability, book.photofile);
            book.authors.forEach(auth->{
                BookAuthors.getInstance().insert(book.isbn, auth, "Writer");
            });
        });
    }
    static void cancel()
    {
        cardList.getChildren().removeAll(cardList.getChildren());
        books.clear();
    }
}
