package database;

import java.util.List;
import java.util.Map;

/**
 * Model class for passing search requests to database.
 */
public class SearchPackage {

    /**
    Table to be searched in.
     */
    private Database.Table mainTable;
    /**
     * List of columns to be returned as result.
     */
    private List<Database.Table.Column> selectList;
    /**
     * Map of filters to be applied to columns.
     */
    private Map<Database.Table.Column, String> filterMap;
    /**
     * List of tables to be joined to main table.
     */
    private List<Database.Table> joinTable;

    SearchPackage(Database.Table mainTable, List<Database.Table.Column> selectList, Map<Database.Table.Column, String> filterMap, List<Database.Table> joinTable) {
        this.mainTable = mainTable;
        this.selectList = selectList;
        this.filterMap = filterMap;
        this.joinTable = joinTable;
    }

    Database.Table getMainTable() {
        return mainTable;
    }

    List<Database.Table.Column> getSelectList() {
        return selectList;
    }

    Map<Database.Table.Column, String> getFilterMap() {
        return filterMap;
    }

    List<Database.Table> getJoinTable() {
        return joinTable;
    }

    @Override
    public String toString() {
        return "SearchPackage{" +
                "table=" + mainTable +
                ", selectList=" + selectList +
                ", filterMap=" + filterMap +
                ", joinTable=" + joinTable +
                '}';
    }
}

