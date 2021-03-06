package work.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import work.Util.Util;
import work.model.dto.Member;
import work.model.dto.ReviewList;
import work.model.service.ReviewService;

/**
 * Servlet implementation class ReviewController
 */
public class ReviewController extends HttpServlet implements IController {
	private ReviewService service = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReviewController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();

		service = new ReviewService();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();

		service = null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("EUC-KR");
		process(request, response);
	}

	@Override
	public void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String action = req.getParameter("action");
		System.out.println("action: " + action);
		if (Util.isEqualsNull(action)) {
			res.sendRedirect("error.jsp");
			return;
		}

		switch (action) {
		case "addReview":
			addReview(req, res);
			break;
		case "getReviews":
			getReviews(req, res);

			break;
		case "updateReview":
			updateReview(req, res);
			break;

		case "getReviewPage":
			getReviewPage(req, res);
			break;

		default:
			res.sendRedirect("error.jsp");
		}

	}

	private void getReviews(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		ReviewList review = Util.isNull(req.getParameter("memberId"))
				? service.getReviews(req.getParameter("toiletNum"))
				: service.getReviewsWithMembers(req.getParameter("toiletNum"), req.getParameter("memberId"));

		if (Util.isValidId(review.getToilet().getId())) {
			req.setAttribute("review", review);
			req.getRequestDispatcher("detail.jsp").forward(req, res);

		} else {
			res.sendRedirect(req.getRequestURI());
		}

	}

	private void getReviewPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		System.out.println(req.getParameter("page"));
		System.out.println(req.getParameter("toiletNum"));

		ReviewList list = service.getReviewPage(req.getParameter("page"), req.getParameter("toiletNum"));
		System.out.println(list.toString());
		res.setContentType("application/json");
		res.getWriter().println(list.toString());
	}

	private void addReview(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		/*
		 * toilet_id / writer_id / review (10~100) / score(1~10)
		 */
		HttpSession session = req.getSession(false);

		if (!Util.isNull(session) && !Util.isNull(session.getAttribute("member"))) {

			int result = service.addReview(req.getParameter("toilet_id"), req.getParameter("writer_id"),
					req.getParameter("review"), req.getParameter("score"));

			if (result > 0) {

				req.getRequestDispatcher(getUriForRedirectToDetail(req.getParameter("toilet_id"), req.getParameter("writer_id"))).forward(req,res);
				return;
			}
		}

		res.sendRedirect("error.jsp");

	}

	private void updateReview(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false);

		if (!Util.isNull(session) && !Util.isNull(session.getAttribute("member"))) {

			// try {
			//
			// if (((Member) session.getAttribute("member")).getId() == Integer
			// .parseInt(req.getParameter("writer_id"))) {
			int result = service.updateReview(req.getParameter("toilet_id"), req.getParameter("writer_id"),
					req.getParameter("review"), req.getParameter("score"));

			if (result > 0) {
				req.getRequestDispatcher(getUriForRedirectToDetail(req.getParameter("toilet_id"), req.getParameter("writer_id"))).forward(req,res);
				return;

			}
			// }
			// } catch (NumberFormatException e) {
			// System.out.println("review > service > updateReview >
			// parseInt(writer_id)");
			// e.printStackTrace();
			//
			// }
		}

		res.sendRedirect("error.jsp");

	}

	private String getUriForRedirectToDetail(String toiletId, String memberId) {
		StringBuilder builder = new StringBuilder();

		builder.append("review?action=getReviews&toiletNum=").append(toiletId).append("&memberId=").append(memberId);
		System.out.println("---- add or update: redirect ---");
		System.out.println(builder.toString());
		return builder.toString();
	}

}
