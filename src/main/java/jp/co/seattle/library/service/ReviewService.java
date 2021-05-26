package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.ReviewInfo;
import jp.co.seattle.library.rowMapper.ReviewRowMapper;

/**
 * 書籍レビューに関するサービス
 * 
 *  chatHisテーブルに関する処理を実装する。
 */

@Service
public class ReviewService {
    final static Logger logger = LoggerFactory.getLogger(ReviewService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //chatHisテーブルにuserId,bookId,reviewを入力する。
    public void registReviews(ReviewInfo reviewInfo) {
        String sql = "INSERT INTO chatHis(user_id,book_id,review,post_date) VALUES('" + reviewInfo.getUserId() + "','"
                + reviewInfo.getBookId() + "','" + reviewInfo.getReview() + "', sysdate()" + ");";
        jdbcTemplate.update(sql);
    }

    //    chatHisテーブルからuserId,reviewをchat_idの降順(idの大きいものから)取得,リストに格納
    /**
     * @param reviewInfo ユーザーID、書籍ID、レビューを格納するDTO
     * @return
     */

    public List<ReviewInfo> getReviewList(ReviewInfo reviewInfo) {
        String sql = "select user_id, review from chatHis where book_id = " + reviewInfo.getBookId()
                + " order by post_date desc;";

        List<ReviewInfo> bookReview = jdbcTemplate.query(sql, new ReviewRowMapper());
        return bookReview;
    }
}