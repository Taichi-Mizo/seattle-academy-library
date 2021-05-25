package jp.co.seattle.library.controller;

import java.util.List;
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

import jp.co.seattle.library.dto.reviewInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ChatService;

/**
 * 詳細表示コントローラー
 */
@Controller
public class DetailsController {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private ChatService chatService;

    //    @Autowired
    //    private ChatService chatService;

    /**
     * @param locale
     * @param userId ユーザーID
     * @param bookId 書籍ID
     * @param review 書籍レビュー
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/details", method = RequestMethod.POST)

    //ここで入力値を受け取る。
    public String detailsBook(Locale locale,
            @RequestParam("bookId") int bookId,
            @RequestParam("userId") int userId,
            Model model) {

        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        //書籍の詳細
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

        //home.jspから取得したuserIdをdetails.jspに返す。
        reviewInfo reviewInfo = new reviewInfo();
        reviewInfo.setUserId(userId);

        //ホーム画面から遷移したときにレビューリストを詳細画面に返す。
        List<reviewInfo> reviewList = chatService.getReviewList(reviewInfo);

        model.addAttribute("reviewList", reviewList);

        //lendMngテーブルないのlend_idがあるかないかをbook_idを元に確認する。
        int lendId = booksService.cfmLend(bookId);

        //lend_idの有無に応じて、詳細画面に貸出不可か貸出可能かを表示する。
        if (lendId == 1) {
            model.addAttribute("unavailable", "貸出不可");
        } else {
            model.addAttribute("available", "貸出可能");
        }

        return "details";

    }
}
