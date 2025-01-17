package jp.co.seattle.library.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class EditBookController {
    final static Logger logger = LoggerFactory.getLogger(EditBookController.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private ThumbnailService thumbnailService;

    @RequestMapping(value = "/editBook", method = RequestMethod.POST) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    //details.jspから取得したbookIdを元に、書籍の詳細情報を呼び出し、editBook.jpsに渡す。
    public String detailsInfo(
            @RequestParam("bookId") int bookId,
            Model model) {
        BookDetailsInfo newBookDetails = booksService.getBookInfo(bookId);
        model.addAttribute("bookDetailsInfo", newBookDetails);
        //        String imgUrl = thumbnailService.getURL;
        //        model.addAttribute("thumnail", imgUrl);

        return "editBook";
    }

    //bookDetailsInfoをデータ型として使用するときは、インスタンス化しなくてもいいのか(現場として、正常に動いている。)

    /**
     * 書籍情報を編集する
     * @param bookId 登録されているID
     * @param locale ロケール情報
     * @param title 書籍名
     * @param author 著者名
     * @param publisher 出版社
     * @param file サムネイルファイル
     * @param model モデル
     * @return 遷移先画面
     */
    //bold になってるのはjspのタグ、actionの中身。requestparam、jspの情報を取得。
    @Transactional
    @RequestMapping(value = "/updateBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")

    //ここで入力値を受け取る。
    public String editBook(Locale locale,
            @RequestParam("bookId") int bookId,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("publishDate") String publishDate,
            @RequestParam("isbn") String isbn,
            @RequestParam("comments") String comments,
            @RequestParam("thumbnail") MultipartFile file,

            Model model) {

        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setBookId(bookId);
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setPublishDate(publishDate);
        bookInfo.setIsbn(isbn);
        bookInfo.setComments(comments);

        // クライアントのファイルシステムにある元のファイル名を設定する
        String thumbnail = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnail(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);
                return "editBook";
            }
        }

        //バリデーションチェック
        //ISBNは10桁もしくは13桁の半角数字。
        boolean isValidIsbn = isbn.matches("[0-9]{10}?$||[0-9]{13}?$");
        boolean isError = false;

        //isbn、dateでエラー、もしくは両方でエラーが起きた時にaddBookに戻れるように、
        //ISBNの入力がある時とないときで、処理内容を分ける。
        //if (isbn.length() != 0) {
        if (!isValidIsbn) {
            isError = true;
            model.addAttribute("errorMsg", "ISBNの桁数または半角数字が正しくありません");
        }
        //}
        //日付のvalidation check, //yyyymmdd
        //半角英数字もバリデーションチェックの条件に含む。
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyymmdd");
            sdf.setLenient(false);
            sdf.parse(publishDate);

        } catch (ParseException pe) {
            isError = true;
            model.addAttribute("wrongDate", "出版日は半角数字のYYYYMMDD形式で入力して下さい");
        }

        if (isError) {
            model.addAttribute("bookDetailsInfo", bookInfo);
            return "editBook";
        }

        //編集した書籍情報をMySQLのUPDATEする
        booksService.editBook(bookInfo);
        model.addAttribute("resultMessage", "更新完了");

        // TODO 編集した書籍の詳細情報を表示するように実装
        BookDetailsInfo newBookDetails = booksService.getBookInfo(bookId);
        model.addAttribute("bookDetailsInfo", newBookDetails);
        model.addAttribute("available", "貸出可能");

        //  詳細画面に遷移する
        return "details";
    }

}
