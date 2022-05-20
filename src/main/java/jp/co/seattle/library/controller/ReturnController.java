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
import jp.co.seattle.library.service.RentService;

@Controller
public class ReturnController {
	final static Logger logger = LoggerFactory.getLogger(ReturnController.class);
	
	@Autowired
    private BooksService booksService;
	
	@Autowired
    private RentService rentService;

	/**
	 * 書籍の返却
	 * 
	 * @param locale　ロケール情報
	 * @param model　モデル情報
	 * @param bookId　書籍ID
	 * @param　before　返却処理前の貸出中の書籍数
	 * @param　after　返却処理後の貸出中の書籍数
	 * @return　遷移先情報
	 */
    @Transactional
    @RequestMapping(value = "/returnBook", method = RequestMethod.POST)
    public String ReturnBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome detailsControler.java! The client locale is {}.", locale);
        
        int before = rentService.rentBookCount();
        
        rentService.returnBook(bookId);
        
        int after = rentService.rentBookCount();
        
        
        if (before == after) {
        	model.addAttribute("returnError","貸出しされていません。");
        }
        
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
        return"details";
    }
}
