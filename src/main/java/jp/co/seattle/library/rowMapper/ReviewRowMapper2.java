package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.reviewInfo;

@Configuration
public class ReviewRowMapper2 implements RowMapper<reviewInfo> {

    @Override
    public reviewInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Query結果（ResultSet rs）を、オブジェクトに格納する実装
        reviewInfo reviewInfo = new reviewInfo();
        reviewInfo.setUserId(rs.getInt("user_id"));
        reviewInfo.setReview(rs.getString("review"));

        return reviewInfo;
    }

}