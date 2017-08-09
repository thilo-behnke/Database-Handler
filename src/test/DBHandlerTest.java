import database.DatabaseHelper;
import model.UserMapper;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;

import static model.Database.Table.CUSTOMERS;
import static model.Database.Table.EMPLOYEES;
import static model.Database.Table.USERS;

public class DBHandlerTest {

    @Test
    public void createTablesTest() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();
        dbHelper.connectToDatabase("jdbc:postgresql:mydb", "postgres", "admin");

        dbHelper.createTables(
                UserMapper.getEntityMapping(USERS),
                UserMapper.getEntityMapping(CUSTOMERS),
                UserMapper.getEntityMapping(EMPLOYEES));

        Assert.assertTrue(dbHelper.isTableAvailable(USERS));
        Assert.assertTrue(dbHelper.isTableAvailable(CUSTOMERS));
        Assert.assertTrue(dbHelper.isTableAvailable(EMPLOYEES));
    }


}
