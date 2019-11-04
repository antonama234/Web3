
import exception.DBException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import service.BankClientService;
import model.BankClient;
import servlet.*;

public class Main {
    public static void main(String[] args) throws Exception{
        ApiServlet apiServlet = new ApiServlet();
        MoneyTransactionServlet money = new MoneyTransactionServlet();
        RegistrationServlet registration = new RegistrationServlet();
        ResultServlet result = new ResultServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(apiServlet), "/api/*");
        context.addServlet(new ServletHolder(money), "/transaction");
        context.addServlet(new ServletHolder(registration), "/registration");
        context.addServlet(new ServletHolder(result), "/result");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
    /*    BankClientService bankClientService = BankClientService.getInstance();
        bankClientService.createTable();
        BankClient client = new BankClient("test", "cfececdc", 233L);
        BankClient client2 = new BankClient("test3", "cfdc", 23L);
        bankClientService.addClient(client);
        bankClientService.addClient(client2);
        bankClientService.sendMoneyToClient(client, "test3", 100L);*/
        server.join();
    }
}
