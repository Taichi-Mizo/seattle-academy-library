package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.reviewInfo;
import jp.co.seattle.library.rowMapper.ReviewRowMapper2;

/**
 * 書籍レビューに関するサービス
 * 
 *  chatHisテーブルに関する処理を実装する。
 */

@Service
public class ChatService {
    final static Logger logger = LoggerFactory.getLogger(ChatService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //chatHisテーブルにuserId,bookId,reviewを入力する。
    public void registReviews(reviewInfo reviewInfo) {
        String sql = "INSERT INTO chatHis(user_id,book_id,review,post_date) VALUES('" + reviewInfo.getUserId() + "','"
                + reviewInfo.getBookId() + "','" + reviewInfo.getReview() + "', sysdate()" + ");";
        jdbcTemplate.update(sql);
    }

    //    chatHisテーブルからuserId,reviewをchat_idの降順(idの大きいものから)取得,リストに格納
    public List<reviewInfo> getReviewList(reviewInfo reviewInfo) {
        String sql = "select user_id, review from chatHis where book_id = " + reviewInfo.getBookId()
                + " order by post_date desc;";

        List<reviewInfo> bookReview = jdbcTemplate.query(sql, new ReviewRowMapper2());
        return bookReview;
    }
}