package sec03.brd01;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	BoardService boardService;
	ArticleVO articleVO;

	public void init(ServletConfig config) throws ServletException {
		System.out.println("BoardController 시작");
		boardService = new BoardService();
		System.out.println("BoardController 객체 만들어진 후 boardService 객체 " + boardService);
	}

	public void destroy() {
		System.out.println("BoardController 종료");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		System.out.println("action : " + action);

		List<ArticleVO> articleList = new ArrayList();

		String nextPage = "";

		if (action == null) {
			articleList = boardService.listArticles();
			request.setAttribute("articleList", articleList);
			nextPage = "/board01/listArticles.jsp";
		} else if (action.equals("listArticles.do")) {
			articleList = boardService.listArticles();
			request.setAttribute("articleList", articleList);
			nextPage = "/board01/listArticles.jsp";
		} else if (action.equals("/articleForm.do")) {
			nextPage = "/board01/articleForm.jsp";
		} else if (action.equals("/addArticle.do")) {
			int articleNO=0;
			
			System.out.println("글쓰기 버튼 누름");
			// 파일도 올리고 새글의 내용도 같이 올리는 저장 형태 : 맵(키, 밸류)
			// 내용이 많이 upload 라는 함수로 새로 작성 (매개변수는 해당 request, response)
			Map<String, String> articleMap = upload(request, response);
			
			System.out.println("articleMap : "+articleMap);
			String title = articleMap.get("title");
			String content = articleMap.get("content");
			String imageFileName = articleMap.get("imageFileName");

			articleVO=new ArticleVO();
			
			articleVO.setParentNO(0);
			articleVO.setId("hong");
			articleVO.setTitle(title);
			articleVO.setContent(content);
			articleVO.setImageFileName(imageFileName);
			
			boardService.addArticle(articleVO);
		
			articleNO=boardService.addArticle(articleVO);
			
			// 파일을 첨부한 경우에만 임시 폴더에 새글 번호로 폴더명 만들기
			
			if(imageFileName!=null&&imageFileName.length()!=0) {
				File srcFile=new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+imageFileName);
				File destDir=new File(ARTICLE_IMAGE_REPO+"\\"+ articleNO);
				destDir.mkdirs();
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}
			
			
			PrintWriter pw=response.getWriter();
			
			pw.print("<script>" 
			         +"  alert('새글을 추가했습니다.');" 
					 +" location.href='http://localhost:8080/pro17_1/board/listArticles.do';"
			         +"</script>");
			
			/*
			 * articleList = boardService.listArticles();
			 * request.setAttribute("articleList", articleList);
			 * 
			 * nextPage = "/board01/listArticles.jsp";
			 */
			
			return;
		} else {

			nextPage = "/board01/listArticles.jsp";
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
		dispatcher.forward(request, response);

	}

	// 글의 내용과 파일의 내용을 같이 올리는 함수
	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, String> articleMap = new HashMap<String, String>();
		String encoding = "utf-8";

		File currentDirPath = new File(ARTICLE_IMAGE_REPO);

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024 * 1024);

		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			List items = upload.parseRequest(request);
			for (int i = 0; i < items.size(); i++) {

				FileItem fileItem = (FileItem) items.get(i);
				if (fileItem.isFormField()) {
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
				} else {
					System.out.println("파라미터명 : " + fileItem.getFieldName());
					System.out.println("파일크기 : " + fileItem.getSize() + "bytes");

					if (fileItem.getSize() > 0) {
						int idx = fileItem.getName().lastIndexOf("\\");
						if (idx == -1) {
							idx = fileItem.getName().lastIndexOf("/");
						}

						String fileName = fileItem.getName().substring(idx + 1);
						System.out.println("파일명 : " + fileName);
						articleMap.put(fileItem.getFieldName(), fileName);
						File uploadFile = new File(currentDirPath + "\\temp\\" + fileName);
						fileItem.write(uploadFile);

					}
				}
			}
		} catch (Exception e) {
			System.out.println("파일 업로드 중 예외 발생");
			e.printStackTrace();
		}

		return articleMap;
	}
}
