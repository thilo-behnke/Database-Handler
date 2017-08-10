package database;

import user.User;

import java.sql.ResultSet;

public interface IDatabaseHelper {

    void connectToDatabase(String url, String db, String user, String password);

    void createTables(DatabaseEntity... entities);

    void insertUsers(User... user);

    ResultSet searchInDB(SearchPackage searchPackage);

    boolean checkTableStatus(Database.Table... tables);
}
