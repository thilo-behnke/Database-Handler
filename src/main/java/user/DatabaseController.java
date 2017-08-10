package user;

import database.Database;
import database.DatabaseEntity;
import database.DatabaseHelper;
import database.SearchPackage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static database.Database.Table.*;

public class DatabaseController {

    private DatabaseHelper databaseHelper;

    public DatabaseController(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public static DatabaseEntity getEntityMapping(Database.Table table) {
        return new DatabaseEntity(table, Database.Table.getColumns(table, false));
    }

    public static User createUserByAttributes(Database.Table table, Map<Column, String> attributeMap) {
        List<Database.Table.Column> expectedAttributes = new ArrayList<>();
        expectedAttributes.addAll(getColumns(table, true));
        expectedAttributes.remove(table.getTableKey());
        // TODO: Add better error handling
        if (attributeMap.size() != expectedAttributes.size()) throw new IllegalArgumentException();
        List<String> attributes = new ArrayList<>();
        attributes.add(attributeMap.get(Column.U_ID));
        attributes.add(attributeMap.get(Column.U_NAME));
        if (table.equals(CUSTOMERS)) {
            attributes.add(attributeMap.get(Column.C_RANK));
        } else if (table.equals(EMPLOYEES)) {
            attributes.add(attributeMap.get(Column.E_SALARY));
            attributes.add(attributeMap.get(Column.E_LOCATION));
        }
        return table.getTableEntity(attributes);
    }

    public List<User> searchUserInDB(Database.Table table, Map<Database.Table.Column, String> filterMap) {
        // TODO: It would be better to receive a map from dbhandler just to avoid the exceptions...
        try {
            List<Column> selectList = getColumns(table, true);
            List<Database.Table> joinList = new ArrayList<>();
            if (table.getInheritsFrom() != null) joinList.add(table.getInheritsFrom());
            else joinList = null;
            ResultSet resultSet = databaseHelper.searchInDB(new SearchPackage(table, selectList, filterMap, joinList));
            List<User> resultList = new ArrayList<>();
            while (resultSet.next()) {
                Map<Column, String> attributeMap = new HashMap<>();
                for (Database.Table.Column c : getColumns(table, true)) {
                    attributeMap.put(c, resultSet.getString(c.name()));
                }
                attributeMap.remove(table.getTableKey());
                resultList.add(createUserByAttributes(table, attributeMap));
            }
            return resultList;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Column getColumnByIndex(Database.Table table, int i) {
        List<Column> columns = new ArrayList<>();
        columns.addAll(getColumns(table, true));
        columns.remove(table.getTableKey());
        return columns.get(i);
    }
}
