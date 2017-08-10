import database.DatabaseHelper;
import model.user.User;
import model.user.UserBuilder;
import model.user.UserMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static model.Database.Table.*;

public class DBHandlerTest {

    private DatabaseHelper dbHelper;

    @Before
    public void prepare() {
        dbHelper = DatabaseHelper.getInstance();
        dbHelper.connectToDatabase("jdbc:postgresql:", "mydb", "postgres", "admin");
    }

    @Test
    public void createTablesTest() {
        dbHelper.createTables(
                UserMapper.getEntityMapping(USERS),
                UserMapper.getEntityMapping(CUSTOMERS),
                UserMapper.getEntityMapping(EMPLOYEES));
        Assert.assertTrue(dbHelper.checkTableStatus(USERS, CUSTOMERS, EMPLOYEES));
    }

    @Test
    public void insertCustomer() {
        User.Customer customer =
                new UserBuilder()
                        .setId(1)
                        .setName("Max Mustermann")
                        .createCustomer()
                        .setRank("A").getCustomer();
        dbHelper.insertUsers(customer);

        List<User> userList = dbHelper.searchUserInDB(CUSTOMERS, null);
        Assert.assertTrue(userList.size() == 1);
        Assert.assertTrue(userList.get(0).equals(customer));
    }

    @Test
    public void insertEmployee() {
        User.Employee employee =
                new UserBuilder()
                .setId(2)
                .setName("Maike Musterfrau")
                .createEmployee()
                .setSalary(30000)
                .setLocation("Germany").getEmployee();
        dbHelper.insertUsers(employee);

        List<User> userList = dbHelper.searchUserInDB(EMPLOYEES, null);
        Assert.assertTrue(userList.size() == 1);
        Assert.assertTrue(userList.get(0).equals(employee));
    }
}
