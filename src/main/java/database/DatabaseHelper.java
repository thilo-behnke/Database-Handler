package database;

import database.Database.Table.Column;
import user.User;
import user.UserMapper;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static database.Database.Table.USERS;
import static database.Database.Table.getColumns;

/**
 * Class for handling the connection to a specified database.
 */
public class DatabaseHelper implements IDatabaseHelper {

    private static DatabaseHelper dbHelper;
    private Connection connection;
    private String db;

    private DatabaseHelper() {}

    /**
     * Singleton method for receiving the DBHelper instance.
     * @return singleton object of DBHelper.
     */
    public static DatabaseHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper();
        }
        return dbHelper;
    }

    /**
     * Connect to database at specified location.
     * @param url path to database.
     * @param db name of database.
     * @param user access credential.
     * @param password access credential.
     */
    @Override
    public void connectToDatabase(String url, String db, String user, String password) {
        try {
            connection = DriverManager.getConnection(
                    url + db, user, password
            );
            this.db = db;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Create tables for specified database entities.
     * @param entities list of entities.
     */
    @Override
    public void createTables(DatabaseEntity... entities) {
        for (DatabaseEntity e : entities) {
            try {
                DatabaseMetaData dmb = connection.getMetaData();
                ResultSet resultSet = dmb.getTables(db, "public", e.getTable().name().toLowerCase(), null);
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
            for (Column c : entity.getColumns()) {
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

    @Override
    public void insertUsers(User...users){
        for(User u : users){
            insertUser(u);
        }
    }

    public void insertUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    getInsertQuery(user.getType()), Statement.RETURN_GENERATED_KEYS);
            setQueryParameters(statement, user, user.getType());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating data failed!");
            }
            statement = connection.prepareStatement(
                    getInsertQuery(USERS), Statement.RETURN_GENERATED_KEYS);
            setQueryParameters(statement, user, USERS);
            affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating data failed!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void setQueryParameters(PreparedStatement statement, User user, Database.Table table) {
        int counter = 1;
        try {
            for (Column c : getColumns(table)) {
                if (c.getType().equals(Database.Types.SERIAL) || c.getType().equals(Database.Types.INTEGER)) {
                    statement.setInt(counter, (int) c.getAttribute(user));
                } else if (c.getType().equals(Database.Types.TEXT)) {
                    statement.setString(counter, (String) c.getAttribute(user));
                }
                counter++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private String getInsertQuery(Database.Table table) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(table.name()).append(" (");
        for (Iterator<Database.Table.Column> it = getColumns(table).iterator(); it.hasNext(); ) {
            Database.Table.Column c = it.next();
            sb.append(c.name());
            while (it.hasNext()) {
                c = it.next();
                sb.append(", ").append(c.name());
            }
        }
        sb.append(")").append("VALUES (");
        for (int i = 0; i < getColumns(table).size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("?");
        }
        sb.append(");");
        return sb.toString();
    }

    @Override
    public List<User> searchUserInDB(Database.Table table, Map<Database.Table.Column, String> filterMap) {
        try {
            Statement statement = connection.createStatement();
            List<Column> selectList = new ArrayList<>();
            selectList.addAll(getColumns(table));
            // TODO: Refactor
            selectList.addAll(getColumns(USERS));
            List<Database.Table> joinList = new ArrayList<>();
            joinList.add(USERS);
            ResultSet resultSet = statement.executeQuery(createSelectQuery(
                    new SearchPackage(table, selectList, filterMap, joinList)));
            List<User> resultList = new ArrayList<>();
            while (resultSet.next()) {
                Map<Column, String> attributeMap = new HashMap<>();
                for (Database.Table.Column c : getColumns(table)) {
                    attributeMap.put(c, resultSet.getString(c.name()));
                }
                for (Database.Table.Column c : getColumns(USERS)) {
                    attributeMap.put(c, resultSet.getString(c.name()));
                }
                resultList.add(UserMapper.createUserByAttributes(table, attributeMap));
            }
            return resultList;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String createSelectQuery(SearchPackage searchPackage) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for(Iterator<Database.Table.Column> it = searchPackage.getSelectList().iterator(); it.hasNext();){
            Column c = it.next();
            sb.append(c.name());
            if(it.hasNext()) sb.append(", ");
        }
        sb.append(" FROM ").append(searchPackage.getMainTable());
        if (searchPackage.getJoinTable() == null) sb.append(";");
        else {
            for(Database.Table t : searchPackage.getJoinTable()){
                sb.append(" JOIN ").append(t).append(" ON ")
                        .append(searchPackage.getMainTable().getTableKey())
                        .append(" = ")
                        .append(t.getTableKey());
            }
            if(searchPackage.getFilterMap() == null) sb.append(";");
            else {
                boolean isFirst = true;
                for (Map.Entry<Database.Table.Column, String> entry : searchPackage.getFilterMap().entrySet()) {
                    sb.append(" WHERE ");
                    if (!isFirst) sb.append(" AND ");
                    sb.append(entry.getKey())
                            .append(" = ")
                            .append(entry.getValue());
                    if (isFirst) isFirst = false;
                }
                sb.append(";");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean checkTableStatus(Database.Table... tables) {
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
                                getColumns(table)
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
            return connection.getMetaData().getTables(db, "public", table.name().toLowerCase(), null);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private ResultSet getColumnSchema(Database.Table table) {
        try {
            return connection.getMetaData().getColumns(db, "public", table.name().toLowerCase(), null);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
