package com.libraryapp.servlet;

import com.libraryapp.dao.BookDAO;
import com.libraryapp.model.Book;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class BookStatusServlet extends HttpServlet {

    private BookDAO bookDAO = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if student is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("student") == null) {
            // Redirect to login page if no session or not logged in
            response.sendRedirect("studentLogin.jsp");
            return;
        }

        // Get list of all books from DAO
        List<Book> books = bookDAO.getAllBooks();
        // Set books list as request attribute for JSP
        request.setAttribute("books", books);

        // Forward request to bookStatus.jsp
        request.getRequestDispatcher("bookStatus.jsp").forward(request, response);
    }
}
