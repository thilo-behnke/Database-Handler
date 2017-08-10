import database.DatabaseHelper;
import files.FileReader;
import user.User;
import user.DatabaseController;

import java.util.*;

import static database.Database.Table.*;

public class MainClass {

    public static void main(String args[]) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();
        dbHelper.connectToDatabase("jdbc:postgresql:", "mydb", "postgres", "admin");
        dbHelper.createTables(
                DatabaseController.getEntityMapping(USERS),
                DatabaseController.getEntityMapping(CUSTOMERS),
                DatabaseController.getEntityMapping(EMPLOYEES));

        List<User> list = new ArrayList<>(FileReader.readUsersFromFile());
        list.stream().filter(x -> x instanceof User.Customer).map(x -> (User.Customer) x).forEach(dbHelper::insertUser);
        list.stream().filter(x -> x instanceof User.Employee).map(x -> (User.Employee) x).forEach(dbHelper::insertUser);
    }
}
