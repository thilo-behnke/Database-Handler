package model.user;

import database.DatabaseEntity;
import model.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static model.Database.Table.*;

public class UserMapper {

    public static DatabaseEntity getEntityMapping(Database.Table table) {
        return new DatabaseEntity(table, Database.Table.getColumns(table));
    }

    public static User.Employee createEmployeeByAttributes(Map<Column, String> attributes) {
        return (User.Employee) createUserByAttributes(EMPLOYEES, attributes);
    }

    public static User.Customer createCustomerByAttributes(Map<Column, String> attributes) {
        return (User.Customer) createUserByAttributes(CUSTOMERS, attributes);
    }

    public static User createUserByAttributes(Database.Table table, Map<Column, String> attributeMap) {
        List<Database.Table.Column> expectedAttributes = new ArrayList<>();
        expectedAttributes.addAll(getColumns(table));
        // TODO: refactor
        expectedAttributes.addAll(getColumns(USERS));
        expectedAttributes.remove(table.getTableKey());
        // TODO: Add better error handling
        if(attributeMap.size() != expectedAttributes.size()) throw new IllegalArgumentException();
        List<String> attributes = new ArrayList<>();
        attributes.add(attributeMap.get(Column.U_ID));
        attributes.add(attributeMap.get(Column.U_NAME));
        if(table.equals(CUSTOMERS)) {
            attributes.add(attributeMap.get(Column.C_RANK));
        } else if (table.equals(EMPLOYEES)) {
            attributes.add(attributeMap.get(Column.E_SALARY));
            attributes.add(attributeMap.get(Column.E_LOCATION));
        }
        return table.getTableEntity(attributes);
    }

    public static Column getColumnByIndex(Database.Table table, int i) {
        List<Column> columns =  new ArrayList<>();
        columns.addAll(getColumns(USERS));
        columns.addAll(getColumns(table));
        columns.remove(table.getTableKey());
        return columns.get(i);
    }
}
