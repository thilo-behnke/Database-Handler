package user;

import database.DatabaseEntity;
import database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static database.Database.Table.*;

public class UserMapper {

    public static DatabaseEntity getEntityMapping(Database.Table table) {
        return new DatabaseEntity(table, Database.Table.getColumns(table, false));
    }

    public static User createUserByAttributes(Database.Table table, Map<Column, String> attributeMap) {
        List<Database.Table.Column> expectedAttributes = new ArrayList<>();
        expectedAttributes.addAll(getColumns(table, true));
        expectedAttributes.remove(table.getTableKey());
        // TODO: Add better error handling
        if(attributeMap.size() < expectedAttributes.size()) throw new IllegalArgumentException();
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
        columns.addAll(getColumns(table, true));
        columns.remove(table.getTableKey());
        return columns.get(i);
    }
}
