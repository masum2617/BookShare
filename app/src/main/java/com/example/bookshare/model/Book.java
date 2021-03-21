package com.example.bookshare.model;

import com.example.bookshare.R;

public class Book{
    public int id;
    public String title;
    public double price;
    public String picture;
    public int minimumQuantity;
    public String link;
    public String edition;
    public String isbn;
    public int totalPage;
    public String publisher;
    public String country;
    public String language;
    public String description;
    public String category;
    public String author;
    public String authorBio;
    public int purpose;
    public int idUser;
    public String userName;

    public int imageTemp = R.drawable.single_book;
    public int cartValue = 0;


    public Book(){

    }

    public Book(int id, String title, double price, String picture, int minimumQuantity) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.picture = picture;
        this.minimumQuantity = minimumQuantity;
    }

    public Book(int id, String title, double price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getPicture() {
        return picture;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public String getLink() {
        return link;
    }

    public String getEdition() {
        return edition;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorBio() {
        return authorBio;
    }

    public int getImageTemp() {
        return imageTemp;
    }

    public int getCartValue() {
        return cartValue;
    }

    public double getTotalPriceForCart(){
        return price*minimumQuantity;
    }

    public double getUpdateTotalPriceForCart(){
        return price*cartValue;
    }

    public int getPurpose() {
        return purpose;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getUserName() {
        return userName;
    }
}
