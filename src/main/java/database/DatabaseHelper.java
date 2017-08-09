package database;

import model.Database;
import model.Database.Table.Columns;
import model.user.User;
import model.user.UserMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

// TODO: Add JDoc
public class DatabaseHelper {

    private static DatabaseHelper dbHelper;
    private Connection connection;

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

    public void createTables(DatabaseEntity... entities) {
        for (DatabaseEntity e : entities) {
            try {
                DatabaseMetaData dmb = connection.getMetaData();
                ResultSet resultSet = dmb.getTables("mydb", "public", e.getTable().name().toLowerCase(), null);
                if (resultSet.next()) {
                    dropTable(resultSet.getString("table_name"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            createTable(e);
        }
    }

    private void dropTable(String tableName) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("Drop table " + tableName + ";");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void createTable(DatabaseEntity entity) {
        try {
            Statement statement = connection.createStatement();
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ")
                    .append(entity.getTable())
                    .append(" (");
            boolean isFirst = true;
            for (Columns c : entity.getColumns()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append(", ");
                }
                sb.append(c.name())
                        .append(" ")
                        .append(c.getType());
            }
            sb.append(");");
            statement.execute(sb.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insertUser(User user) {
//        try {
//            PreparedStatement statement = connection.prepareStatement(
//                    getInsertQuery(user.getType()), Statement.RETURN_GENERATED_KEYS);
//            // set user data
//            DatabaseEntity databaseEntity = UserMapper.getEntityMapping(user.getType());
//            statement.setString(1, employee.getName());
//            statement.setInt(2, User.UserType.Employee.id);
//            int affectedRows = statement.executeUpdate();
//            if (affectedRows == 0) {
//                throw new SQLException("Creating User data failed!");
//            }
//            ResultSet generatedKeys = statement.getGeneratedKeys();
//            generatedKeys.next();
//            int id = generatedKeys.getInt(1);
//            statement = connection.prepareStatement(
//                    getInsertQuery(EMPLOYEE_TABLE));
//            statement.setInt(1, id);
//            statement.setInt(2, employee.getSalary());
//            statement.setString(3, employee.getLocation());
//            affectedRows = statement.executeUpdate();
//            if (affectedRows == 0) {
//                throw new SQLException("Creating Customer data failed!");
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
    }

    private String getInsertQuery(Database.Table table) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(table.name()).append(" (");
        for (Iterator<Columns> it = table.getColumns(table).iterator(); it.hasNext(); ) {
            Columns c = it.next();
            sb.append(c.name());
            while (it.hasNext()) {
                sb.append(", ").append(c.name());
            }
        }
        sb.append(")").append("VALUES (");
        for (int i = 0; i < table.getColumns(table).size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("?");
        }
        sb.append(");");
        return sb.toString();
    }

    public boolean areTablesAvailable(Database.Table... tables) {
        for (Database.Table t : tables) {
            if (!isTableAvailable(t)) {
                System.out.println(t + " is not available");
                return false;
            }
        }
        return true;
    }

    private boolean isTableAvailable(Database.Table table) {
        try {
            ResultSet resultSetTable = getTableSchema(table);
            if (resultSetTable != null) {
                while (resultSetTable.next()) {
                    if (resultSetTable.getString("table_name").equals(table.name().toLowerCase())) {
                        ResultSet resultSetColumns = getColumnSchema(table);
                        List<String> columnsReference =
                                table.getColumns(table)
                                        .stream()
                                        .map(Enum::name)
                                        .map(x -> x = x.toLowerCase())
                                        .sorted()
                                        .collect(Collectors.toList());
                        List<String> columnsDB = new ArrayList<>();
                        while (resultSetColumns.next()) {
                            columnsDB.add(resultSetColumns.getString("column_name"));
                        }
                        if (columnsDB.stream().sorted().collect(Collectors.toList())
                                .equals(columnsReference)) return true;
                    }
                }
                return false;
            } else return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private ResultSet getTableSchema(Database.Table table) {
        try {
            return connection.getMetaData().getTables("mydb", "public", table.name().toLowerCase(), null);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private ResultSet getColumnSchema(Database.Table table) {
        try {
            return connection.getMetaData().getColumns("mydb", "public", table.name().toLowerCase(), null);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // TODO: Think of better way than returning null - NullPointerException!
        return null;
    }


}
