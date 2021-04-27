package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     *
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {

        // TODO 取得したい情報を取得するようにSQLを修正
        //remake
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "select id, title, author, publisher, publish_date, thumbnail_url from books order by title asc",
                new BookInfoRowMapper());

        return getedBookList;
    }

    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */

    //登録されているbookIdを取得したあと、そのidの書籍情報を取得する。
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する。上記で取得したbookIdを元にSQL内の書籍の詳細を呼び出す。
        String sql = "SELECT * FROM books where id ="
                + bookId;
        //SQLから取得した
        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }

    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */
    public void registBook(BookDetailsInfo bookInfo) {

        String sql = "INSERT INTO books (title,author,publisher,publish_date,thumbnail_url,thumbnail_name,reg_date,upd_date,isbn,comments) VALUES ('"
                + bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
                + bookInfo.getPublishDate() + "','" + bookInfo.getThumbnailUrl() + "','" + bookInfo.getThumbnail()
                + "',sysdate(),sysdate(),'"
                + bookInfo.getIsbn() + "','" + bookInfo.getComments() + "');";

        jdbcTemplate.update(sql);
    }

    //方法１：登録したidをSQLから取得する。Q.第二引数のInteger.classとは何か。
    public int bookId() {
        String sql = "select max(id) from books;";
        int bookId = jdbcTemplate.queryForObject(sql, Integer.class);

        return bookId;
    }

    //SQLコマンドをUPDATEで更新する。登録日時はそのままにして、更新日時のみを更新するにはどうすればいいのか。
    public void editBook(BookDetailsInfo bookInfo) {
        String sql = "UPDATE books SET title='" + bookInfo.getTitle() + "',author='" + bookInfo.getAuthor()
                + "',publisher='" + bookInfo.getPublisher()
                + "',publish_date='" + bookInfo.getPublishDate() + "',thumbnail_url='" + bookInfo.getThumbnailUrl()
                + "',thumbnail_name='" + bookInfo.getThumbnail()
                + "',upd_date=" + "sysdate()," + "isbn='" + bookInfo.getIsbn() + "',comments='" + bookInfo.getComments()
                + "' where id =" + bookInfo.getBookId() + ";";

        jdbcTemplate.update(sql);
        //編集内容を登録するだけだから、戻り値はなくても良い。
    }

    //編集した書籍のidをSQLから取得する。
    public int updatedBookId(BookDetailsInfo bookId) {
        String sql = "select" + bookId + "from books;";
        int updatedBookId = jdbcTemplate.queryForObject(sql, Integer.class);

        return updatedBookId;
    }

    //書籍を削除する
    public void deleteBookInfo(int bookId) {
        String sql = "delete from books where Id =" + bookId + ";";
        jdbcTemplate.update(sql);
    }

}