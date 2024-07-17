package book.catalog;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class Book implements Comparable<Book>{
    private String title;
    private String author;
    private List<String> genres;
    private int numberOfPages;
    private LocalDate publishedDate;

    public Book(String title, String author, int numberOfPages, LocalDate publishedDate, String... genres) {
        this.title = title;
        this.author = author;
        this.genres = Arrays.asList(genres);
        this.numberOfPages = numberOfPages;
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getPublishedDateFormatted() {
        return DateTimeFormatter.ofPattern("d-MMM-yyyy").format(publishedDate);
    }

    public LocalDate getPublishedDate() {
        return this.publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public String toString() {
        return "Title: " + getTitle() + "\nAuthor: " + getAuthor() + "\nGenres: " + getGenres() + "\nNumber of pages: " + getNumberOfPages() + "\nPublished date: " + getPublishedDateFormatted();
    }

    @Override
    public int compareTo(Book other) {
        return this.title.toLowerCase().compareTo(other.title.toLowerCase());
    }

    public static Type getGenreListType() {
        return new TypeToken<List<String>>(){}.getType();
    }
}
