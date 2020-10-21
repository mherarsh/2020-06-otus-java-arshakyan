package mherarsh.servlets;

import com.google.gson.Gson;
import mherarsh.core.model.AddressDataSet;
import mherarsh.core.model.PhoneDataSet;
import mherarsh.core.model.User;
import mherarsh.core.service.DBServiceUser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UsersApiServlet extends HttpServlet {
    private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    private final DBServiceUser serviceUser;
    private final Gson gson;

    public UsersApiServlet(DBServiceUser serviceUser, Gson gson) {
        this.serviceUser = serviceUser;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var users = serviceUser.findAll().stream()
                .map(UserViewModel::new).collect(Collectors.toList());

        response.setContentType(CONTENT_TYPE_JSON);
        response.getOutputStream().print(gson.toJson(users));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            saveUserFromRequest(request);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("{}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(String.format("{ errMsg:%s }", e.getMessage()));
        } finally {
            response.setContentType(CONTENT_TYPE_JSON);
            response.getWriter().close();
        }
    }

    private void saveUserFromRequest(HttpServletRequest request) {
        var name = request.getParameter("name");
        var address = request.getParameter("address");
        var phone = request.getParameter("phone");

        saveUser(name, address, phone);
    }

    private void saveUser(String name, String address, String phone) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is empty");
        }

        var user = User.builder().name(name).build();
        user.setAddress(AddressDataSet.builder().street(address).build());
        user.setPhones(List.of(
                PhoneDataSet.builder().number(phone).user(user).build()
        ));

        serviceUser.saveUser(user);
    }
}

class UserViewModel {
    long id = 0;
    String name = "";
    String address = "";
    String phone = "";

    public UserViewModel(User user) {
        id = user.getId();
        name = user.getName();
        address = user.getAddress().getStreet();

        user.getPhones().stream().findFirst().ifPresent(phoneDataSet -> phone = phoneDataSet.getNumber());
    }
}
