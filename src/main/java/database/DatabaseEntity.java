package database;

import java.util.List;

// TODO: Add JDoc
public class DatabaseEntity {

    private String table;
    private List<Column> columns;
    private List<String> attributes;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Column> getColumns() {
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
