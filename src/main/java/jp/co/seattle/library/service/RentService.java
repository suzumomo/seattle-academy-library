package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RentService {
    final static Logger logger = LoggerFactory.getLogger(RentService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
    * 書籍IDに紐づく書籍詳細情報を取得する
    *
    * @param bookId 書籍ID
    * @return 書籍情報
    */
    public void rentBook(int bookId) {
	
	    String sql = "insert into rent (book_id) select " + bookId 
	    		+ " where NOT EXISTS (select book_id from rent where book_id=" + bookId + ")";
	    
	    jdbcTemplate.update(sql);
    }
    
    public int idCount() {
    	
    	String sql = "select count (book_id) from rent";
    	int bookId = jdbcTemplate.queryForObject(sql,int.class);
    	return bookId;
    }
    
}  
