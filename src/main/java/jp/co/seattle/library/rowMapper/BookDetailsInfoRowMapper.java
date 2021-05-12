package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.BookDetailsInfo;

@Configuration
public class BookDetailsInfoRowMapper implements RowMapper<BookDetailsInfo> {

    @Override
    public BookDetailsInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Query結果（ResultSet rs）を、オブジェクトに格納する実装
        BookDetailsInfo bookDetailsInfo = new BookDetailsInfo();

        bookDetailsInfo.setBookId(rs.getInt("book_id"));
        bookDetailsInfo.setTitle(rs.getString("title"));
        bookDetailsInfo.setAuthor(rs.getString("author"));
        bookDetailsInfo.setPublisher(rs.getString("publisher"));
        bookDetailsInfo.setPublishDate(rs.getString("publish_date"));
        bookDetailsInfo.setThumbnailUrl(rs.getString("thumbnail_url"));
        bookDetailsInfo.setThumbnail(rs.getString("thumbnail_name"));
        bookDetailsInfo.setIsbn(rs.getString("isbn"));
        bookDetailsInfo.setComments(rs.getString("comments"));
        return bookDetailsInfo;
    }

}

//書籍詳細画面にISBNとcomments(説明文)を追加済み。