package jp.co.seattle.library.controller;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.dto.ReviewInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ReviewService;

/**
 * レビュー表示コントローラー
 */
@Controller
public class ReviewController {
    final static Logger logger = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BooksService booksService;

    /**書籍レビュー取得メソッド
     * @param locale
     * @param userId ユーザーID
     * @param bookId 書籍ID
     * @param review 書籍レビュー
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/reviewBook", method = RequestMethod.POST)

    //ここで入力値を受け取る。
    public String reviewBook(Locale locale,
            @RequestParam("userId") int userId,
            @RequestParam("bookId") int bookId,
            @RequestParam("reviewPosted") String review,
            Model model) {

        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        // パラメータで受け取ったレビュー情報をDtoに格納する。
        ReviewInfo reviewInfo = new ReviewInfo();
        reviewInfo.setUserId(userId);
        reviewInfo.setBookId(bookId);
        reviewInfo.setReview(review);

        //レビューの入力があった時に、以下の処理が事項される。
        if (!StringUtils.isEmpty(reviewInfo.getReview()) && reviewInfo.getReview().length() <= 280) {
            //ユーザーIDとレビューをchatHisテーブルに入力(戻り値なし)
            reviewService.registReviews(reviewInfo);
            model.addAttribute("postCfm", "投稿しました！");
        } else {
            String errorMsg = "入力に誤りがあります。";
            model.addAttribute("postError", errorMsg);
        }

        //DBからレコードの取得
        List<ReviewInfo> reviewList = reviewService.getReviewList(reviewInfo);

        model.addAttribute("reviewList", reviewList);

        //書籍の詳細
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

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
