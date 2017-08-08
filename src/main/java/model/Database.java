package model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Database {

    public enum Table {
        USERS, CUSTOMERS, EMPLOYEES;

        public List<String> getColumns(Table table){
            return Arrays.stream(Columns.values()).filter(x -> x.table != table).map(Enum::name).collect(Collectors.toList());
        }

        public enum Columns {
            U_ID(USERS), U_NAME(USERS), U_TYPE(USERS),
            C_ID(CUSTOMERS), C_RANK(CUSTOMERS),
            E_ID(EMPLOYEES), E_SALARY(EMPLOYEES), E_LOCATION(EMPLOYEES);

            private Table table;

            Columns(Table table){
                this.table = table;
            }
        }

    }

}
