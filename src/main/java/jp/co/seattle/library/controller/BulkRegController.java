package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class BulkRegController {
    final static Logger logger = LoggerFactory.getLogger(BulkRegController.class);

    @Autowired
    private BooksService booksService;

    //    @Autowired
    //    private ThumbnailService thumbnailService;

    //RequestParamでname属性を取得, value＝actionで指定したパラメータ
    @RequestMapping(value = "/bulkRegist", method = RequestMethod.GET)

    public String login(Model model) {
        return "bulkRegist";
    }

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
    /**
     * @param locale
     * @param readFile
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")

    public String bulkBook(Locale locale,
            @RequestParam("fileReader") MultipartFile readFile,
            Model model) {

        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        String[] splitLine = new String[6];

        //リスト1(csvから取得した)を作成。データ型には配列型のStringを指定。
        ArrayList<String[]> books = new ArrayList<String[]>();

        //入力内容等エラーがある書籍の位置をストックしていくリスト。
        ArrayList<String> errorLows = new ArrayList<String>();

        try {
            //fileReaderは、文字ファイルを読み込むための簡易クラス。
            //ここではインスタンス化して、同時に引数にString型のファイル名を与えている。
            InputStream stream = readFile.getInputStream();
            Reader reader = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(reader);

            //読み込んだ１行の値を格納するための変数
            String line;

            //ファイルの１行目を読み込み、nullが帰ってくるまで、以下を繰り返す。
            while ((line = br.readLine()) != null) {
                //ファイルの中身はただの文字列。読み込んだlineの各値をカンマで区切り、配列にする。
                //要素の数だけ、配列の箱に入れる。splitではnullの場合(,,)も認知できる。
                splitLine = line.split(",", -1);

                //配列の要素に名前をつけて、バリデーションチェックで配列の変数を呼び出しやすくする。
                String prepT = splitLine[0];
                String prepA = splitLine[1];
                String prepP = splitLine[2];
                String prepPD = splitLine[3];
                String prepI = splitLine[4];

                //booksリストに格納していく。
                books.add(splitLine);

                //ISBNの正規表現
                boolean isValidIsbn = prepI.matches("^[0-9]+$");

                //バリデーションチェック(VC)、必須項目(title,author,publisher,publishDate)に対して。
                if (prepT.isEmpty() || prepA.isEmpty() || prepP.isEmpty() || prepPD.isEmpty()) {

                    errorLows.add(books.size() + "冊目の書籍名、著者名、出版社名、出版日のいづれかが入力されていません。");
                }

                //VC - publishDateの日付の正規表現
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyymmdd");
                    sdf.setLenient(false);
                    sdf.parse(prepPD);

                } catch (ParseException pe) {
                    errorLows.add(books.size() + "冊目の出版日の形式が正しくありません。");
                }
                //VC - ISBN
                StringBuilder sb = new StringBuilder(prepI);
                if (!isValidIsbn || sb.length() != 10 && sb.length() != 13) {
                    errorLows.add(books.size() + "冊目のISBNの記法が正しくありません。");
                }
            } //while-close.
              //ストリームを閉じて、BufferedReaderのリソースを開放。                
            br.close();

        } catch (IOException ie) {
            model.addAttribute("erMsg", "ファイル読み込みに失敗しました。");
        }

        //while文で全ての行を読み取った後、errorLowsリストを要素の数だけ展開する。 
        if (errorLows.size() > 0) {
            model.addAttribute("erMsg", errorLows);
            return "bulkRegist";

        }

        //エラーがなければ、ここ以降の処理が行われる。
        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();

        for (int i = 0; i < books.size(); i++) {
            //booksリストの１行を取得

            //各要素の取り出し。booksリストのi行目の配列の[0~5]番目を取得し、各カラムに格納する。                         
            bookInfo.setTitle(books.get(i)[0]);
            bookInfo.setAuthor(books.get(i)[1]);
            bookInfo.setPublisher(books.get(i)[2]);
            bookInfo.setPublishDate(books.get(i)[3]);
            bookInfo.setIsbn(books.get(i)[4]);
            bookInfo.setComments(books.get(i)[5]);

            //取得した１行分の書籍の情報をMySQLに登録
            booksService.registBook(bookInfo);
        }
        model.addAttribute("resultMessage", "登録完了");
        return "bulkRegist";

    }
}