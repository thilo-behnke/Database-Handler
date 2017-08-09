import database.DatabaseHelper;
import model.user.UserMapper;
import org.junit.Assert;
import org.junit.Test;

import static model.Database.Table.*;

public class DBHandlerTest {

    @Test
    public void createTablesTest() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();
        dbHelper.connectToDatabase("jdbc:postgresql:mydb", "postgres", "admin");

        dbHelper.createTables(
                UserMapper.getEntityMapping(USERS),
                UserMapper.getEntityMapping(CUSTOMERS),
                UserMapper.getEntityMapping(EMPLOYEES));

        Assert.assertTrue(dbHelper.areTablesAvailable(USERS, CUSTOMERS, EMPLOYEES));
    }


}
