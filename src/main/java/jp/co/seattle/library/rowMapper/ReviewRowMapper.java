package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.ReviewInfo;

@Configuration
public class ReviewRowMapper implements RowMapper<ReviewInfo> {

    @Override
    public ReviewInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Query結果（ResultSet rs）を、オブジェクトに格納する実装
        ReviewInfo reviewInfo = new ReviewInfo();
        reviewInfo.setUserId(rs.getInt("user_id"));
        reviewInfo.setReview(rs.getString("review"));

        return reviewInfo;
    }

}