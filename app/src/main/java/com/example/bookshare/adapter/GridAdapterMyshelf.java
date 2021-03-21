package com.example.bookshare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookshare.R;
import com.example.bookshare.model.Book;

import java.util.List;

public class GridAdapterMyshelf extends BaseAdapter {

    private int[] bookImages;
    private String[] bookNames;
    private Context context;
    private LayoutInflater inflater;
    private List<Book> bookList;
    private ImageView bookImage;
    private TextView bookTitle, bookPrice, bookLabel;

    public GridAdapterMyshelf(String[] bookNames, int[] bookImages, Context context) {
        this.bookNames = bookNames;
        this.bookImages = bookImages;
        this.context = context;
    }

    public GridAdapterMyshelf(List<Book> bookList, int[] bookImages, Context context) {
        this.bookList = bookList;
        this.bookImages = bookImages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Book b = bookList.get(position);

        //For Top 10 books per week gridview
        View gridview_myshelf_books = convertView;
        if(convertView==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridview_myshelf_books = inflater.inflate(R.layout.row_item_book_shelf, null);
        }

        bookImage = (ImageView) gridview_myshelf_books.findViewById(R.id.grid_image);
        bookTitle = (TextView) gridview_myshelf_books.findViewById(R.id.grid_bookName);
        bookPrice = (TextView) gridview_myshelf_books.findViewById(R.id.grid_bookPrice);
        bookLabel = (TextView) gridview_myshelf_books.findViewById(R.id.grid_label);

        //gridImageMyshelfBook.setImageResource(bookImages[position]);
        bookImage.setImageResource(R.drawable.single_book);
        bookTitle.setText(b.title);
        bookPrice.setText("৳ "+b.price);
        if(b.purpose==1){
            bookLabel.setText("For Sell");
            bookLabel.setBackgroundColor(Color.RED);
        }else if(b.purpose==2){
            bookLabel.setText("For Rent");
            bookLabel.setBackgroundColor(Color.BLUE);
        }

        return gridview_myshelf_books;
    }
}
