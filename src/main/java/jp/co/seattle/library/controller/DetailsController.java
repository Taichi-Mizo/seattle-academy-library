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

import jp.co.seattle.library.service.BooksService;

/**
 * 詳細表示コントローラー
 */
@Controller
public class DetailsController {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);

    @Autowired
    private BooksService bookdService;

    /**
     * 詳細画面に遷移する
     * @param locale
     * @param bookId
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/details", method = RequestMethod.POST)
    public String detailsBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome detailsControler.java! The client locale is {}.", locale);
        //lendMngテーブルないのlend_idがあるかないかをbook_idを元に確認する。
        int lendId = bookdService.cfmLend(bookId);

            //lend_idの有無に応じて、詳細画面に貸出不可か貸出可能かを表示する。
            if (lendId == 1) {
                model.addAttribute("unavailable", "貸出不可");
            } else {model.addAttribute("available", "貸出可能");
        }
        model.addAttribute("bookDetailsInfo", bookdService.getBookInfo(bookId));

        return "details";
    }
}
