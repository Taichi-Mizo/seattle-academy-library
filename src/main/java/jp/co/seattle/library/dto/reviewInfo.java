package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍レビュー格納DTO
 */
@Configuration
@Data
// input info from users is included as follows
public class reviewInfo {

    private int userId;

    private int bookId;

    private String review;

    public reviewInfo() {

    }

    // コンストラクタ
    public reviewInfo(int bookId, int userId, String review) {
        this.userId = userId;
        this.bookId = bookId;
        this.review = review;

    }

}