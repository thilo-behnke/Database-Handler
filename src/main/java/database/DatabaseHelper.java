package database;

import model.User;

import java.sql.*;
import java.util.*;

// TODO: Add JDoc
public class DatabaseHelper {

    private static DatabaseHelper dbHelper;
    private Connection connection;

    // TODO: Look for better solution, e.g. enum
    public static final String USER_TABLE = "users";
    private static final String USER_COLUMN_ID = "id";
    private static final String USER_COLUMN_NAME = "name";
    private static final String USER_COLUMN_TYPE = "type";

    private static final String CUSTOMER_TABLE = "customers";
    private static final String CUSTOMER_COLUMN_RANK = "rank";

    private static final String EMPLOYEE_TABLE = "employees";
    private static final String EMPLOYEE_COLUMN_SALARY = "salary";
    private static final String EMPLOYEE_COLUMN_LOCATION = "location";

    private DatabaseHelper() {
    }

    public static DatabaseHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper();
        }
        return dbHelper;
    }

    public void connectToDatabase(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(
                    url, user, password
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createTable(DatabaseEntity entity) {
        try {
            DatabaseMetaData dmb = connection.getMetaData();
            if (!dmb.getTables(null, null, entity.getTable(), null).next()) {
                Statement statement = connection.createStatement();
                StringBuilder sb = new StringBuilder();
                sb.append("CREATE TABLE ")
                        .append(entity.getTable())
                        .append(" (");
                boolean isFirst = true;
                for (DatabaseEntity.Column c : entity.getColumns()) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append(c.getName())
                            .append(" ")
                            .append(c.getType());
                }
                sb.append(");");
                statement.execute(sb.toString());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insertCustomer(User.Customer customer) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    getInsertQuery(USER_TABLE), Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customer.getName());
            statement.setInt(2, User.UserType.Customer.id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating User data failed!");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            statement = connection.prepareStatement(
                    getInsertQuery(CUSTOMER_TABLE));
            statement.setInt(1, id);
            statement.setInt(2, customer.getRank());
            affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating Customer data failed!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insertEmployee(User.Employee employee) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    getInsertQuery(USER_TABLE), Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, employee.getName());
            statement.setInt(2, User.UserType.Employee.id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating User data failed!");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            statement = connection.prepareStatement(
                    getInsertQuery(EMPLOYEE_TABLE));
            statement.setInt(1, id);
            statement.setInt(2, employee.getSalary());
            statement.setString(3, employee.getLocation());
            affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating Customer data failed!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private String getInsertQuery(String tableName) {
        StringBuilder sb = new StringBuilder();
        switch (tableName) {
            case USER_TABLE:
                sb
                        .append("INSERT INTO ")
                        .append(USER_TABLE)
                        .append(" (")
                        .append(USER_COLUMN_NAME)
                        .append(", ")
                        .append(USER_COLUMN_TYPE)
                        .append(") ")
                        .append("VALUES ")
                        .append("(?, ?)")
                        .append(";");
                return sb.toString();
            case CUSTOMER_TABLE:
                sb
                        .append("INSERT INTO ")
                        .append(CUSTOMER_TABLE)
                        .append(" (")
                        .append(USER_COLUMN_ID)
                        .append(", ")
                        .append(CUSTOMER_COLUMN_RANK)
                        .append(") ")
                        .append("VALUES ")
                        .append("(?, ?)")
                        .append(";");
                return sb.toString();
            case EMPLOYEE_TABLE:
                sb
                        .append("INSERT INTO ")
                        .append(EMPLOYEE_TABLE)
                        .append(" (")
                        .append(USER_COLUMN_ID)
                        .append(", ")
                        .append(EMPLOYEE_COLUMN_SALARY)
                        .append(", ")
                        .append(EMPLOYEE_COLUMN_LOCATION)
                        .append(") ")
                        .append("VALUES ")
                        .append("(?, ?, ?)")
                        .append(";");
                return sb.toString();
            default:
                throw new IllegalArgumentException(tableName + " is not an allowed TableName!");
        }
    }

    // TODO: Check if method is still relevant
    public void insertUser(ArrayList<User.Customer> userList) {
        StringBuilder sb = new StringBuilder();
        sb
                .append("INSERT INTO ")
                .append(USER_TABLE)
                .append(" (")
                .append(USER_COLUMN_NAME)
                .append(")")
                .append(" VALUES ")
                .append("(\'");

        for (int i = 0, isFirst = 1; i < userList.size(); i++) {
            if (isFirst == 1) {
                isFirst = 0;
            } else {
                sb.append(", (\'");
            }
            sb.append(userList.get(i).getName());
            sb.append("\')");
        }
        sb.append(";");

        try {
            connection
                    .createStatement()
                    .execute(sb.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // TODO: Check if method is still relevant
    public Map<Integer, ArrayList<String>> getUserMap() {
        Map<Integer, ArrayList<String>> userMap = new HashMap<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                userMap.put(id, new ArrayList<>(Arrays.asList(name)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userMap;
    }


}
