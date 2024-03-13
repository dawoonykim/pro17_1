package sec01.ex01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/mem/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	MemberDAO dao;

	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("시작");
		dao = new MemberDAO();
	}

	@Override
	public void destroy() {
		System.out.println("종료");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("get method");
		doHandle(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("post method");
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action=request.getPathInfo();
		System.out.println("액션 : "+action);
		
		String nextPage=null;
		List<MemberVO> memberLists=null;
		
		if(action==null||action.equals("/listMembers.do")||action.equals("/")) {
			memberLists=dao.listMembers();
			
			request.setAttribute("memberLists", memberLists);
			nextPage="/test01/listMembers.jsp";
		}else if(action.equals("/addMember.do")){
			System.out.println("Member plus");
			String id=request.getParameter("id");
			String pwd=request.getParameter("pwd");
			String name=request.getParameter("name");
			String email=request.getParameter("email");
			
			MemberVO vo=new MemberVO(id, pwd, name, email);
			dao.addMember(vo);
			memberLists=dao.listMembers();
			request.setAttribute("memberLists", memberLists);
			nextPage="/test01/listMembers.jsp";
		}else if(action.equals("/modMemberForm.do")) {
			System.out.println("Mod Member Form");
			String id=request.getParameter("id");
			MemberVO memInfo=dao.findMember(id);
			System.out.println(id);
			request.setAttribute("memInfo", memInfo);
			
			nextPage="/test01/modMembers.jsp";
		}else if(action.equals("/modMember.do")) {
			System.out.println("Mod Member");
			String id=request.getParameter("id");
			String pwd=request.getParameter("pwd");
			String name=request.getParameter("name");
			String email=request.getParameter("email");
			System.out.println("/modMember.do after param : "+id+","+pwd+","+name+","+email);
			
			MemberVO vo=new MemberVO(id, pwd, name, email);
			System.out.println("수정할 회원 : "+vo.toString());
			dao.modMember(vo);
			
			memberLists=dao.listMembers();
			request.setAttribute("memberLists", memberLists);
			
			nextPage="/test01/listMembers.jsp";
		}else if(action.equals("/delMember.do")) {
			String id=request.getParameter("id");
			dao.delMember(id);
			memberLists=dao.listMembers();
			request.setAttribute("memberLists", memberLists);
			
			nextPage="/test01/listMembers.jsp";
		}
		
		RequestDispatcher dispatcher=request.getRequestDispatcher(nextPage);
		dispatcher.forward(request, response);
	}

}
