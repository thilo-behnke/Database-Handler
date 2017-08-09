package database;

import model.Database;

import java.util.List;

// TODO: Add JDoc
public class DatabaseEntity {

    private Database.Table table;
    private List<Database.Table.Column> columns;
    private List<String> attributes;

    public Database.Table getTable() {
        return table;
    }

    public void setTable(Database.Table table) {
        this.table = table;
    }

    public void setColumns(List<Database.Table.Column> columns) {
        this.columns = columns;
    }

    public List<Database.Table.Column> getColumns() {
        return this.columns;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public static class Column {

        private String name;
        private Types type;

        public Column(String name, Types type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Types getType() {
            return type;
        }

        public enum Types {
            SERIAL, TEXT, INTEGER;
        }
    }

}
