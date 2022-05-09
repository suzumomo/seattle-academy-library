package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;



/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class BulkController {
	final static Logger logger = LoggerFactory.getLogger(BulkController.class);

    @Autowired
    private BooksService booksService;


    @RequestMapping(value = "/bulk", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String bulkBook(Model model) {
        return "bulk";
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
    
    @Transactional
    @RequestMapping(value = "/bulkBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
        public String bulkbook(Locale locale,@RequestParam("file") MultipartFile csvFile, Model model){
    	
    	 List<BookDetailsInfo> registList = new ArrayList<BookDetailsInfo>();
    	 List<String> errorList = new ArrayList<String>();
    	
        try (BufferedReader br = new BufferedReader(
        		new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))){
            String line;
            int count = 0;
            
            while ((line = br.readLine()) != null) {
              final String[] split = line.split(",",-1);
              count++;
              
              boolean nullCheck = (split[0].isEmpty()|| split[1].isEmpty()|| split[2].isEmpty()|| split[3].isEmpty());
              boolean pdCheck = split[3].matches("^[0-9]{8}$");
              boolean isbnCheck1 = split[4].matches("^[0-9]{10}$");
              boolean isbnCheck2 = split[4].matches("^[0-9]{13}$");
              
              if(nullCheck || (!isbnCheck1 && !isbnCheck2) || !pdCheck) {
            	  errorList.add (count+"行目の書籍登録でエラーが起きました。");
              }
              
              BookDetailsInfo bookInfo = new BookDetailsInfo();
              bookInfo.setTitle(split[0]);
              bookInfo.setAuthor(split[1]);
              bookInfo.setPublisher(split[2]);
              bookInfo.setPublishDate(split[3]);
              bookInfo.setIsbn(split[4]);
              
              registList.add (bookInfo);
              
            }
            if(registList.isEmpty()) {
            	model.addAttribute("emptyError", "CSVに書籍情報がありません。");
                return "bulk";
            }
            
          } catch (IOException e) {
            throw new RuntimeException("ファイルが読み込めません。", e);
          }
        
        
        if(errorList.size()>0) {
            model.addAttribute("bulkError", errorList);
        return "bulk";
        }
        
        for(BookDetailsInfo bookInfo : registList) {
        	booksService.registBook(bookInfo);
        }
		
        model.addAttribute("bookList", booksService.getBookList());
        return "home";
        
        
    }
    
}
