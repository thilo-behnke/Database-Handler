import database.DatabaseHelper;
import model.User;
import model.UserMapper;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainClass {

    public static void main(String args[]) {

        DatabaseHelper dbHelper = DatabaseHelper.getInstance();
        dbHelper.connectToDatabase();
        dbHelper.createTable(UserMapper.getUserTableStructure());
        dbHelper.createTable(UserMapper.getEmployeeTableStructure());
        dbHelper.createTable(UserMapper.getCustomerTableStructure());

        List<User> list = new ArrayList<>(readUsersFromFile());
        list.stream().filter(x -> x instanceof User.Customer).map(x -> (User.Customer) x).forEach(dbHelper::insertCustomer);
        list.stream().filter(x -> x instanceof User.Employee).map(x -> (User.Employee) x).forEach(dbHelper::insertEmployee);
    }

    private static List<User> readUsersFromFile() {
        List<User> userList = new ArrayList<>();
        userList.addAll(getUsersFromFile(Paths.get("C:\\Users\\Thilo\\Desktop\\employee_import.txt"), UserMapper::createEmployeeByAttributes));
        userList.addAll(getUsersFromFile(Paths.get("C:\\Users\\Thilo\\Desktop\\customer_import.txt"), UserMapper::createCustomerByAttributes));
        return userList;
    }

    private static List<User> getUsersFromFile(Path path, Function<? super ArrayList<String>, ? extends User> mappMethod) {
        List<User> userList = new ArrayList<>();
        try (Stream<String> lines = Files.lines(path, Charset.defaultCharset())) {
            userList = lines.map(x -> new ArrayList<>(Arrays.asList(x.split(";"))))
                    .map(mappMethod)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return userList;
    }
}
