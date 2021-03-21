package com.example.bookshare.model;

import java.io.Serializable;

public class Cart implements Serializable {
    public int cartId;
    public int bookId;
    public String bookTitle;
    public double bookPrice;
    public String bookPicture;
    public int userId;
    public int quantity;
    public double totalPrice;
    public String dateAdded;

    public Cart() {
    }

    public Cart(int cartId, int bookId, String bookTitle, double bookPrice, String bookPicture, int userId, int quantity, double totalPrice) {
        this.cartId = cartId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookPrice = bookPrice;
        this.bookPicture = bookPicture;
        this.userId = userId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public int getBookId() {
        return bookId;
    }

    public int getUserId() {
        return userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public int getCartId() {
        return cartId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public String getBookPicture() {
        return bookPicture;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
