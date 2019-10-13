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
    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> page = createPageVariablesMap(req);
        resp.getWriter().println(new PageGenerator().getPage("registrationPage.html", page));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> page = createPageVariablesMap(req);
        BankClient client = new BankClient(
                req.getParameter("name"),
                req.getParameter("password"),
                Long.parseLong(req.getParameter("money")));
        try {
            if (bankClientService.getClientByName(client.getName()) == null) {
                bankClientService.addClient(client);
                page.put("message", "Add client successful");
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
