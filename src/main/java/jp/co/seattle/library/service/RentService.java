package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.HistoryInfo;
import jp.co.seattle.library.rowMapper.HistoryRowMapper;

@Service
public class RentService {
	final static Logger logger = LoggerFactory.getLogger(RentService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍IDに紐づく書籍情報を貸出情報に登録する
	 *
	 * @param bookId 書籍ID
	 */
	public void rentBook(int bookId) {

		String sql = "insert into rent (book_id, rent_date) values (" + bookId + ", now())";
		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍の貸出しの際に履歴を更新する
	 * 
	 * @param bookId 書籍ID
	 */
	public int updateRentBook(int bookId) {

		try {
			String sql = "update rent set rent_date = now(),return_date = null where book_id =" + bookId;
			int newRentdate = jdbcTemplate.queryForObject(sql, int.class);
			return newRentdate;

		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 貸出履歴に書籍IDがあるか確認する
	 * 
	 * @return 貸出書籍情報
	 */

	public int rentBookCheck(int bookId) {

		try {
			String sql = "select book_id from rent where book_id =" + bookId;
			int rentBookId = jdbcTemplate.queryForObject(sql, int.class);
			return rentBookId;

		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 貸出し中の書籍の数を取得する
	 * 
	 * @return 書籍情報
	 */

	public int rentBookCount() {

		String sql = "select count (rent_date) from rent";
		int bookId = jdbcTemplate.queryForObject(sql, int.class);
		return bookId;
	}

	/**
	 * 書籍の返却の際に履歴を更新する
	 * 
	 * @param bookId 書籍ID
	 */
	public int returnBook(int bookId) {

		try {
			String sql = "update rent set return_date = now(),rent_date = null where book_id =" + bookId;
			int newReturndate = jdbcTemplate.queryForObject(sql, int.class);
			return newReturndate;

		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 該当の書籍の貸出し状況を調べる
	 * 
	 * @param bookId 書籍ID
	 */
	public int rentDateCheck(int bookId) {

		try {
			String sql = "SELECT book_id FROM rent WHERE book_id =" + bookId + "and return_date is null";
			int deleteId = jdbcTemplate.queryForObject(sql, int.class);
			return deleteId;

		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 書籍の貸出しリストを取得する
	 *
	 * @return 貸出しリスト
	 */
	public List<HistoryInfo> historyBookList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<HistoryInfo> historyBookList = jdbcTemplate.query(
				"SELECT rent.book_id, books.title, rent.rent_date, rent.return_date FROM books left outer JOIN rent ON books.id = rent.book_id where rent_date is not null or return_date is not null",
				new HistoryRowMapper());

		return historyBookList;
	}

}
