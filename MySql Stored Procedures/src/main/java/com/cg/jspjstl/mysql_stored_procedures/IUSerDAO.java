package com.cg.jspjstl.mysql_stored_procedures;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public interface IUSerDAO {
    void insertUser (User user) throws SQLException;
    User selectUser(int id);
    List<User> sellectAllUsers();
    boolean deleteUser(int id) throws SQLException;
    boolean updateUser(User user) throws SQLException;
    User getUserById(int id);
    void insertUserStore(User user) throws SQLException;
}
@Override
public User getUserById(int id) {
    User user = null;
    String query = "{CALL get_user_by_id(?)}";

    try (Connection connection = getConnection();
         CallableStatement callableStatement = connection.prepareCall(query)) {
        callableStatement.setInt(1, id);
        ResultSet rs = callableStatement.executeQuery();

        while (rs.next()) {
            String name = rs.getString("name");
            String email = rs.getString("email");
            String country = rs.getString("country");
            user = new User(id, name, email, country);
        }
    } catch (SQLException e) {
        printSQLException(e);
    }
    return user;
}
@Override
public void insertUserStore(User user) throws SQLException {
    String query = "{CALL insert_user(?,?,?)}";

    try (Connection connection = getConnection();
         CallableStatement callableStatement = connection.prepareCall(query);) {
        callableStatement.setString(1, user.getName());
        callableStatement.setString(2, user.getEmail());
        callableStatement.setString(3, user.getCountry());
        System.out.println(callableStatement);
        callableStatement.executeUpdate();
    } catch (SQLException e) {
        printSQLException(e);
    }
}
private void showEditForm(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, ServletException, IOException {
    int id = Integer.parseInt(request.getParameter("id"));
    User existingUser = userDAO.getUserById(id);
    RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
    request.setAttribute("user", existingUser);
    dispatcher.forward(request, response);
}
