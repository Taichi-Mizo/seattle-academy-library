package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍基本情報格納DTO
 */
@Configuration
@Data
// input info from users is included as follows
public class BookInfo {

    private int bookId;

    private String title;

    private String author;

    private String publisher;

    private String publishDate;

    private String isbn;

    private String comments;

    private String thumbnail;

    public BookInfo() {

    }

    // コンストラクタ
    public BookInfo(int bookId, String title, String author, String publisher, String publishDate, String isbn,
            String comments, String thumbnail) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.isbn = isbn;
        this.comments = comments;
        this.thumbnail = thumbnail;
    }

}