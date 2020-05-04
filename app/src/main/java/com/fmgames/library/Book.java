package com.fmgames.library;

import androidx.annotation.NonNull;

public class Book {

    private String title;
    private String author;
    private String description;
    private String state;
    private int currentPage;
    private int totalPages;

    public Book(String title, String author, String description, String state, int currentPage, int totalPages) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.state = state;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String getCurrentPageString() {
        return String.valueOf(currentPage);
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public String getTotalPagesString() {
        return String.valueOf(totalPages);
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                '}';
    }
}
