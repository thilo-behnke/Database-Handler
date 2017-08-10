import database.DatabaseHelper;
import model.Database;
import model.user.User;
import model.user.UserMapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static model.Database.Table.*;

public class MainClass {

    public static void main(String args[]) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();
        dbHelper.connectToDatabase("jdbc:postgresql:", "mydb", "postgres", "admin");
        dbHelper.createTables(
                UserMapper.getEntityMapping(USERS),
                UserMapper.getEntityMapping(CUSTOMERS),
                UserMapper.getEntityMapping(EMPLOYEES));

        List<User> list = new ArrayList<>(readUsersFromFile());
        // TODO: Refactor
        list.stream().filter(x -> x instanceof User.Customer).map(x -> (User.Customer) x).forEach(dbHelper::insertUser);
        list.stream().filter(x -> x instanceof User.Employee).map(x -> (User.Employee) x).forEach(dbHelper::insertUser);
    }

    // TODO: Move to another class that handles file reading
    private static List<User> readUsersFromFile() {
        List<User> userList = new ArrayList<>();
        // TODO: Make file paths relative
        userList.addAll(getUsersFromFile(Paths.get("C:\\Users\\Thilo\\Desktop\\employee_import.txt"), UserMapper::createEmployeeByAttributes, EMPLOYEES));
        userList.addAll(getUsersFromFile(Paths.get("C:\\Users\\Thilo\\Desktop\\customer_import.txt"), UserMapper::createCustomerByAttributes, CUSTOMERS));
        return userList;
    }

    // TODO: Move to another class that handles file reading
    private static List<User> getUsersFromFile(Path path, Function<? super Map<Column, String>, ? extends User> mappMethod, Database.Table table) {
        List<User> userList = new ArrayList<>();
        try (Stream<String> lines = Files.lines(path, Charset.defaultCharset())) {
            userList = lines.skip(1)
                    .map(x -> new ArrayList<>(Arrays.asList(x.split(";"))))
                    .map(x -> x.stream().collect(Collectors.toMap(y -> UserMapper.getColumnByIndex(table, x.indexOf(y)), Function.identity())))
                    .map(mappMethod)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return userList;
    }
}
