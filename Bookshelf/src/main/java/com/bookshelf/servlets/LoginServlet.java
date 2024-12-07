package com.bookshelf.servlets;

import com.bookshelf.beans.User;
import com.bookshelf.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String email = request.getParameter("email").toLowerCase();
        String password = request.getParameter("password");

        try {
            // Authenticate user
            System.out.println("LoginServlet: Authenticating user with email = " + email);
            User user = UserDao.authenticateUser(email, password);

            if (user == null) {
                System.out.println("Login failed - Invalid email or password.");
                request.setAttribute("message", "Invalid email or password. Please try again.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return;
            }

            // Retrieve user_id using findIdByEmail
            System.out.println("LoginServlet: Retrieving user_id for email = " + email);
            String userId = UserDao.findIdByEmail(email);

            if (userId == null || userId.isEmpty()) {
                System.out.println("Login failed - User ID not found for email: " + email);
                request.setAttribute("message", "An error occurred during login. Please try again.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return;
            }

            // Start session and set user attributes
            HttpSession session = request.getSession();
            session.setAttribute("user", user); 
            session.setAttribute("loggedInUserId", userId); 
            session.setAttribute("loggedInUser", user.getUsername()); 
            session.setAttribute("email", email);

            System.out.println("Login successful - User ID: " + userId);
            System.out.println("Login successful - Username: " + user.getUsername());
            System.out.println("Login successful - Email: " + email);

            // Verify session attributes
            System.out.println("Session Attributes:");
            System.out.println("loggedInUserId = " + session.getAttribute("loggedInUserId"));
            System.out.println("loggedInUser = " + session.getAttribute("loggedInUser"));
            System.out.println("userEmail = " + session.getAttribute("userEmail"));

            // Redirect to dashboard
            response.sendRedirect("dashboard.jsp");

        } catch (Exception e) {
            System.out.println("LoginServlet: Exception occurred during login process.");
            e.printStackTrace();
            request.setAttribute("message", "An error occurred during login. Please try again.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
