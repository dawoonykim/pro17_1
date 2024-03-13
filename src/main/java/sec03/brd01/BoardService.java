package sec03.brd01;

import java.util.List;

import oracle.jdbc.internal.ObjectDataFactory;

public class BoardService {

	BoardDAO boardDAO;

	public BoardService() {
		System.out.println("BoardService 객체 생성");
		boardDAO = new BoardDAO();
	}

	List<ArticleVO> listArticles() {
		return boardDAO.selectAllArticles();
	}

	// 새 글 추가
	public int addArticle(ArticleVO article) {
		System.out.println("서비스에서의 글 추가");
		boardDAO.insertNewArticle(article);
		return boardDAO.getNewArticleNO();
	}
	
	public ArticleVO  viewArticle(int articleNO) {
		ArticleVO article=null;
		article=boardDAO.selectArticle(articleNO);
		return article;
		
		
	}

	
}
