package database;

import model.Database;
import model.user.User;

import java.util.List;
import java.util.Map;

public interface IDatabaseHelper {

    void connectToDatabase(String url, String db, String user, String password);

    void createTables(DatabaseEntity... entities);

    void insertUsers(User...user);

    List<User> searchUserInDB(Database.Table table, Map<Database.Table.Column, String> filterMap);

    boolean checkTableStatus(Database.Table...tables);
}
