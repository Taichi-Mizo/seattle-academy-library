package jp.co.seattle.library.controller;

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

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class returnBookController {
    final static Logger logger = LoggerFactory.getLogger(returnBookController.class);

    @Autowired
    private BooksService booksService;

    /**
     * 書籍情報を登録する
     * @param locale ロケール情報
     * @param title 書籍名
     * @param author 著者名
     * @param publisher 出版社
     * @param file サムネイルファイル
     * @param model モデル
     * @return 遷移先画面
     */
    //bold になってるのはjspのたぶ、actionの中身。requestparam、jspの情報を取得。
    @Transactional
    @RequestMapping(value = "/returnBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")

    public String insertBook(Locale locale,
            @RequestParam("bookId") int bookId,
            Model model) {

        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        //MySQLのlendMngテーブルからlend_idとidを削除するメソッドを実行。
        booksService.returnBook(bookId);

        // TODO 登録した書籍の詳細情報を表示するように実装
        BookDetailsInfo newBookDetails = booksService.getBookInfo(bookId);
        model.addAttribute("bookDetailsInfo", newBookDetails);
        //lendMngにlned_idがある時にこのファイルが動く。details.jspに返すのは。
        model.addAttribute("available", "貸出可能");
        //          詳細画面に遷移する
        return "details";
    }

}
