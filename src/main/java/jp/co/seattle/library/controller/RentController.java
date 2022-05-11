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
public class RentController {
	final static Logger logger = LoggerFactory.getLogger(RentController.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private RentService rentService;

	/**
	 * 書籍の貸出し
	 * 
	 * @param locale　ロケール情報
	 * @param model　モデル情報
	 * @param bookId　書籍ID
	 * @param　count1　貸出し処理前の貸出中の書籍数
	 * @param　count2　貸出処理後の貸出中の書籍数
	 * @return　遷移先情報
	 */
	@Transactional
	@RequestMapping(value = "/rentBook", method = RequestMethod.POST)
	public String RentBook(Locale locale, @RequestParam("bookId") Integer bookId, Model model) {
		// デバッグ用ログ
		logger.info("Welcome detailsControler.java! The client locale is {}.", locale);

		int count1 = rentService.idCount();

		rentService.rentBook(bookId);
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

		int count2 = rentService.idCount();

		if (count1 == count2) {
			model.addAttribute("rentError", "貸出し中です。");
		}

		return "rent";
	}
}
