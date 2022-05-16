package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     *
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {

        // TODO 取得したい情報を取得するようにSQLを修正
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "select id,title,author,publisher,publish_date,thumbnail_url,thumbnail_name from books order by title asc",
                new BookInfoRowMapper());

        return getedBookList;
    }

    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT * FROM books LEFT OUTER JOIN rent ON books.id = rent.book_id WHERE books.id =" + bookId;

        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }

    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */
    public void registBook(BookDetailsInfo bookInfo) {

        String sql = "INSERT INTO books (title, author,publisher,publish_date,isbn,explanation,thumbnail_name,thumbnail_url,reg_date,upd_date) VALUES ('"
                + bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
                + bookInfo.getPublishDate() + "','"
                + bookInfo.getIsbn() + "','"
                + bookInfo.getExplanation() + "','"
                + bookInfo.getThumbnailName() + "','"
                + bookInfo.getThumbnailUrl() + "',"
                + "now(),"
                + "now())";

        jdbcTemplate.update(sql);
    }
    
    /**
     * 最新で追加した書籍情報を所得する
     */
    public int maxId () {
    	String sql = "SELECT max(id) FROM books";
    	int maxId = jdbcTemplate.queryForObject(sql,int.class);
    	return maxId;
    }
    
    /**
     * 書籍を削除する
     *
     * @param bookId 書籍ID
     */
    public void deleteBook(int bookId) {
    	
    	String sql = "DELETE FROM books WHERE id =" + bookId;
    	
    	jdbcTemplate.update(sql);
    }
    
    /**
     * 登録済みの書籍情報を編集する
     *
     * @param bookInfo 書籍情報
     */
    public void editBook(BookDetailsInfo bookInfo) {
    	String sql = "UPDATE books SET title ='" + bookInfo.getTitle() 
    	+ "', author = '" + bookInfo.getAuthor() 
    	+ "', publisher = '" + bookInfo.getPublisher() 
    	+ "', publish_date = '" + bookInfo.getPublishDate() 
    	+ "', thumbnail_name = '" + bookInfo.getThumbnailName()
    	+ "', thumbnail_url = '" + bookInfo.getThumbnailUrl() 
    	+ "', isbn ='"  + bookInfo.getIsbn() 
        + "', upd_date = now(), explanation ='" + bookInfo.getExplanation() 
        + "' Where id = " + bookInfo.getBookId();
    	
    	 jdbcTemplate.update(sql);
    }
    
    /**
     * 検索した書籍情報を取得する
     * 
     * @return 書籍リスト
     */
    public List<BookInfo> searchBookList(String title) {

        // TODO 取得したい情報を取得するようにSQLを修正
        List<BookInfo> searchedBookList = jdbcTemplate.query(
                "select id,title,author,publisher,publish_date,thumbnail_url,thumbnail_name from books where title like '%" + title + "%' order by title asc",
                new BookInfoRowMapper());

        return searchedBookList;
    }
    
   


}
