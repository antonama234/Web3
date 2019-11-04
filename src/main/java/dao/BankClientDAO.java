package dao;

import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {
    private static BankClientDAO instance;
    private Connection connection;

    private BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public static BankClientDAO getInstance(Connection connection) {
        if (instance == null) {
            instance = new BankClientDAO(connection);
        }
        return instance;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("select * from bank_client");
        ResultSet result = statement.getResultSet();
        List<BankClient> list = new ArrayList<>();
        while (result.next()) {
            BankClient client = new BankClient();
            client.setId(result.getLong("id"));
            client.setName(result.getString("name"));
            client.setPassword(result.getString("password"));
            client.setMoney(result.getLong("money"));
            list.add(client);
        }
        statement.close();
        result.close();
        return list;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        BankClient client = getClientByName(name);
        return client.getPassword().equals(password);
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select * from bank_client where name=?");
        statement.setString(1, name);
        ResultSet result = statement.executeQuery();
        result.next();
        if (validateClient(name, password)) {
            Long rst = result.getLong("money") + transactValue;
            statement = connection.prepareStatement("update bank_client set money = ? where name LIKE ? and password = ?");
            statement.setLong(1, rst);
            statement.setString(2, name);
            statement.setString(3, password);
            statement.executeUpdate();
        }
        result.close();
        statement.close();
    }

    public BankClient getClientById(long id) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("select * from bank_client where id = '" + id + "'");
        ResultSet result = statement.getResultSet();
        BankClient client = new BankClient(
                result.getLong("id"),
                result.getString("name"),
                result.getString("password"),
                result.getLong("money"));
        result.close();
        statement.close();
        return client;
    }

        public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("select * from bank_client where name = '" + name + "'");
        ResultSet result = statement.getResultSet();
        result.next();
        Long money = result.getLong("money");
        result.close();
        statement.close();
        return money >= expectedSum;
    }

    public BankClient getClientByName(String name) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("select * from bank_client where name = '" + name + "'");
        ResultSet result = statement.getResultSet();
        result.next();
        BankClient client = new BankClient(
                result.getLong("id"),
                result.getString("name"),
                result.getString("password"),
                result.getLong("money"));
        result.close();
        statement.close();
        return client;
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_clien where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public void addClient(BankClient client) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("insert into bank_client (name, password, money) " +
                "values ('" + client.getName() + "', '" + client.getPassword() + "', '" + client.getMoney() + "')");
        statement.close();
    }

    public void removeClient(String name) throws SQLException {
        Statement statement = connection.createStatement();
        Long id = getClientIdByName(name);
        statement.execute("delete from bank_client where id = '" + id + "'");
        statement.close();
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }
}
