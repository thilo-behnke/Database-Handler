import database.DatabaseHelper;
import model.user.User;
import model.user.UserBuilder;
import model.user.UserMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static model.Database.Table.*;

public class DBHandlerTest {

    DatabaseHelper dbHelper;

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

        Assert.assertTrue(dbHelper.areTablesAvailable(USERS, CUSTOMERS, EMPLOYEES));
    }

    @Test
    public void insertCustomer() {
        User.Customer customer =
                new UserBuilder()
                        .setId(1)
                        .setName("Max Mustermann")
                        .createCustomer()
                        .setRank(2).getCustomer();
        dbHelper.insertUser(customer);
    }
}
