package database;

import java.util.List;

// TODO: Add JDoc
public class DatabaseEntity {

    private Database.Table table;
    private List<Database.Table.Column> columns;

    public DatabaseEntity(Database.Table table, List<Database.Table.Column> columns){
        this.table = table;
        this.columns = columns;
    }

    public Database.Table getTable() {
        return table;
    }

    public List<Database.Table.Column> getColumns() {
        return this.columns;
    }
}
