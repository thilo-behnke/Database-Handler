package model.user;

import database.DatabaseEntity;
import model.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.Database.Table.*;

public class UserMapper {

    private static DatabaseEntity userMapping;

    // TODO: Check privacy level
    public static DatabaseEntity getEntityMapping(Database.Table table) {
//        if (table.equals(USERS)) {
//            throw new IllegalArgumentException("USERS are not allowed to be instantiated!");
//        }
        // TODO: Add caching
        userMapping = new DatabaseEntity();
        userMapping.setTable(table);
        userMapping.setColumns(
                // TODO: Refactor into static method
                getColumns(table)
        );
        return userMapping;
    }

    public static User.Employee createEmployeeByAttributes(List<String> attributes) {
        return (User.Employee) createUserByAttributes(EMPLOYEES, attributes);
    }

    public static User.Customer createCustomerByAttributes(List<String> attributes) {
        return (User.Customer) createUserByAttributes(CUSTOMERS, attributes);
    }

    private static User createUserByAttributes(Database.Table table, List<String> attributes) {
        DatabaseEntity entity = UserMapper.getEntityMapping(table);
        if (attributes.size() == entity.getAttributes().size()) {
            return table.getTableEntity(attributes);
        } else {
            throw new IllegalArgumentException("List has wrong format: " + attributes.size() + " Columns");
        }
    }
}
