package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import dao.MessageDAO;

/**
 * Servlet implementation class MessagesServlet
 */
public class MessagesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Send Message Clicked
		if(request.getParameter("sendMessage") != null)
		{
			String msg = "";
			HttpSession session = request.getSession();
			int receiverId = Integer.parseInt((String)session.getAttribute("receiverId"));
			User user = (User)session.getAttribute("user");
			int  senderId = user.getUserId();
			String message = (String)request.getParameter("messageText");
			if(message == null)
			{
				message = "";
			}
			if(message.length() ==0)
			{
				msg = "Please enter Message Text.";
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/sendMessage.jsp");
				dispatcher.forward(request,  response);
			}
			MessageDAO dataMessage = new MessageDAO();
			try {
				dataMessage.sendMessage(receiverId, senderId, message);
				dataMessage.connection.DB_Close();
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			} catch (Throwable e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			
				msg = "Message sent successfully!";
			
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/sendMessage.jsp");
				dispatcher.forward(request,  response);
		}
	}

}
