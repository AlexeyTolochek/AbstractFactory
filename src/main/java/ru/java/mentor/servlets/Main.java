package ru.java.mentor.servlets;

import ru.java.mentor.Factory.AbstractFactotyDao;
import ru.java.mentor.Factory.DAO;
import ru.java.mentor.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class Main extends HttpServlet {
    private final String page = "/WEB-INF/view/index.jsp";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO dao = AbstractFactotyDao.getDAO();
        response.setContentType("text/html");
        String idStr = request.getParameter("id");
        String submit = request.getParameter("submit");
        String edit = request.getParameter("edit");
        String delete = request.getParameter("delete");
        Long id;

        if (delete != null) {
            id = Long.parseLong(idStr);
            User user = dao.getUserById(id);
            if (user != null) {
                dao.deleteUser(user);
            }
        }
        if (edit != null) {
            id = Long.parseLong(idStr);
            User user = dao.getUserById(id);
            request.setAttribute("userEdit", user);
        }

        List<User> list = dao.getAllUsers();
        request.setAttribute("list", list);
        request.getRequestDispatcher(page).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");

        final String idStr = req.getParameter("id");
        final String name = req.getParameter("name");
        final String login = req.getParameter("login");
        final String password = req.getParameter("password");
        final String address = req.getParameter("address");
        final String birthdateStr = req.getParameter("birthdate");
        final String action = req.getParameter("action");

        if (requestIsValid(name, login, password, address, birthdateStr)) {
            DAO dao = AbstractFactotyDao.getDAO();
            final User user = new User(name, login, password, address, Integer.parseInt(birthdateStr));

            if (action != null) {
                if (action.equalsIgnoreCase("add")) {
                    dao.addUser(user);
                }
                if (action.equalsIgnoreCase("edit")) {
                    user.setId(Long.parseLong(idStr));
                    dao.editUser(user);
                }
            }
        }
        doGet(req, resp);
    }

    private boolean requestIsValid(String name, String login, String password, String address, String postIndexStr) {
        return name != null && !name.isEmpty() &&
                login != null && !login.isEmpty() &&
                password != null && !password.isEmpty() &&
                address != null && !address.isEmpty() &&
                postIndexStr != null && !postIndexStr.isEmpty();
    }
}

