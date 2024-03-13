package sec03.brd01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import oracle.jdbc.internal.ObjectDataFactory;

public class BoardDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private DataSource dataFactory;
	
	public BoardDAO() {
		System.out.println("BoardDAO 객체 생성");
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 전체 글 가져오기
	List<ArticleVO> selectAllArticles() {
		List<ArticleVO> articlesList = new ArrayList();

		try {
			conn = dataFactory.getConnection();
			String sql = "select level, articleno, parentno, title,content, id, writedate from t_board"
					+ " start with parentno=0 connect by prior articleno=parentno"
					+ " order siblings by articleno desc";

			System.out.println(sql);
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int level = rs.getInt("level");
				int articleNO = rs.getInt("articleNO");
				int parentNO = rs.getInt("parentNO");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				ArticleVO article=new ArticleVO();
				article.setLevel(level);
				article.setArticleNO(articleNO);
				article.setParentNO(parentNO);
				article.setTitle(title);
				article.setContent(content);
				article.setId(id);
				article.setWriteDate(writeDate);

				articlesList.add(article);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return articlesList;

	}

	// 새글 추가
	public int insertNewArticle(ArticleVO article) {
		
		System.out.println("DAO에서의 새 글 추가");
		int articleNO = getNewArticleNO();
		try {
			conn = dataFactory.getConnection();
			
			
			int parentNO = article.getParentNO();
			System.out.println("DAO에서의 parentNO");
			String title = article.getTitle();
			String content = article.getContent();
			String id = article.getId();
			String imageFileName = article.getImageFileName();

			String sql = "insert into t_board" 
					+ " (articleno, parentno, title, content, imagefilename, id)"
					+ " VALUES(?,?,?,?,?,?)";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, articleNO);
			pstmt.setInt(2, parentNO);
			pstmt.setString(3, title);
			pstmt.setString(4, content);
			pstmt.setString(5, imageFileName);
			pstmt.setString(6, id);
			
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("새 글 추가시 예외 발생");
			e.printStackTrace();
		}

		return articleNO;
	}

	// 새 글 번호
	public int getNewArticleNO() {

		try {
			conn = dataFactory.getConnection();
			String sql = "select max(articleNO) from t_board";
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public ArticleVO selectArticle(int articleNO) {
		ArticleVO article=new ArticleVO();
		
		try {
			conn=dataFactory.getConnection();
			String sql="select articleNO,parentNO,title,content,imageFileName,id,writeDate"
					+ " from t_board where articleNO=?";
			System.out.println(sql);
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, articleNO);
			ResultSet rs=pstmt.executeQuery();
			rs.next();
			
			int _articleNO=rs.getInt("articleNO");
			int parentNO=rs.getInt("parentNO");
			String title=rs.getString("title");
			String content=rs.getString("content");
			String imageFileName=rs.getString("imageFileName");
			String id=rs.getString("id");
			Date writeDate=rs.getDate("writeDate");
			
			article.setArticleNO(_articleNO);
			article.setParentNO(parentNO);
			article.setContent(content);
			article.setImageFileName(imageFileName);
			article.setId(id);
			article.setWriteDate(writeDate);
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return article;
	}
	
	

}
