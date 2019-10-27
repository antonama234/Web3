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

public class MoneyTransactionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> page = createPageVariablesMap(req);
        resp.getWriter().println(new PageGenerator().getPage("registrationPage.html", page));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BankClientService bankClientService = BankClientService.getInstance();
        Map<String, Object> page = createPageVariablesMap(req);
        String name = req.getParameter("senderName");
        String password = req.getParameter("senderPassword");
        Long money = Long.valueOf(req.getParameter("money"));
        String nameTo = req.getParameter("nameTo");
        try {
            BankClient clientFrom = bankClientService.getClientByName(name);
            if (bankClientService.sendMoneyToClient(clientFrom, nameTo, money)){
                page.put("message", "The transaction was successful");
            }
        } catch (DBException e) {
            page.put("message", "transaction rejected");
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
