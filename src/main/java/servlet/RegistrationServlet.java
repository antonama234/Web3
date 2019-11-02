package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> page = createPageVariablesMap(req);
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", page));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BankClientService bankClientService = BankClientService.getInstance();
        Map<String, Object> page = new HashMap<>();

   /*             String name = req.getParameter("name");
        String password = req.getParameter("password");
        Long money = Long.parseLong(req.getParameter("money"));*/

        BankClient client = new BankClient(
                req.getParameter("name"),
                req.getParameter("password"),
                Long.parseLong(req.getParameter("money")));
        try {
            if (!bankClientService.existClient(client.getName())) {
                bankClientService.addClient(client);
                page.put("message", "Add client successful");
            } else {
                page.put("message", "Client not add");
            }
        } catch (DBException e) {
            page.put("message", "Client not add");
        }
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", page));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("message", "");
        return pageVariables;
    }
}
