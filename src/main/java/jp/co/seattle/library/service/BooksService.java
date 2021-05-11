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
/**
 * @author user
 *
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
                "select book_id, title, author, publisher, publish_date, thumbnail_url from books order by title asc",
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
        String sql = "SELECT * FROM books where book_id ="
                + bookId + ";";
        //SQLから取得した
        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }

    //ホーム画面で指定した書籍のbookIdがlendMngにあるか確認する。DetailsControllerにて使用。
    //    public int cfmLend(int bookId) {
    //        String sql = "select lend_id from lendMng where book_id =" + bookId;
    //        int lendId = jdbcTemplate.queryForObject(sql, Integer.class);
    //        return lendId;
    //    }
    //book_idに対応するlend_idの個数を数える。0個なら貸出可能、1個なら貸出不可能の処理を行う。
    public int cfmLend(int bookId) {
        String sql = "select count(lend_id) from lendMng where book_id =" + bookId;
        int lendId = jdbcTemplate.queryForObject(sql, Integer.class);
        return lendId;
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

    //方法１：登録したidをSQLから取得する。
    public int bookId() {
        String sql = "select max(book_id) from books;";
        int bookId = jdbcTemplate.queryForObject(sql, Integer.class);

        return bookId;
    }

    /**
     * SQLコマンドをUPDATEで更新する。
     * 
     * @param bookInfo
     */
    public void editBook(BookDetailsInfo bookInfo) {
        String sql = "UPDATE books SET title='" + bookInfo.getTitle() + "',author='" + bookInfo.getAuthor()
                + "',publisher='" + bookInfo.getPublisher()
                + "',publish_date='" + bookInfo.getPublishDate() + "',thumbnail_url='" + bookInfo.getThumbnailUrl()
                + "',thumbnail_name='" + bookInfo.getThumbnail()
                + "',upd_date=" + "sysdate()," + "isbn='" + bookInfo.getIsbn() + "',comments='" + bookInfo.getComments()
                + "' where book_id =" + bookInfo.getBookId() + ";";

        jdbcTemplate.update(sql);
        //編集内容を登録するだけだから、戻り値はなくても良い。
    }

    /**
     * 編集した書籍のidをSQLから取得する。
     * @param bookId
     * @return
     */
    public int updatedBookId(BookDetailsInfo bookId) {
        String sql = "select" + bookId + "from books;";
        int updatedBookId = jdbcTemplate.queryForObject(sql, Integer.class);

        return updatedBookId;
    }

    //書籍を削除する
    /**
     * @param bookId
     */
    public void deleteBookInfo(int bookId) {
        String sql = "delete from books where book_id =" + bookId + ";";
        jdbcTemplate.update(sql);
    }

    //sql:詳細画面から取得したbookIdを,lendMngテーブルのidに登録をする。
    /**
     * @param bookId
     */
    public void lendBook(int bookId) {
        String sql = "insert into lendMng(book_id) values('" + bookId + "');";
        //sqlの実行
        jdbcTemplate.update(sql);
    }

    //sql:受け取ったbookIdを where文の条件式に入れて、対応するlend_idとidを削除する。
    /**
     * @param bookId
     */
    public void returnBook(int bookId) {
        String sql = "delete from lendMng where book_id =" + bookId + ";";
        jdbcTemplate.update(sql);
    }
}